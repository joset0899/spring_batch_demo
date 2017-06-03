package com.demo.batch;

import javax.sql.DataSource;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.amqp.AmqpItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.demo.batch.message.Person;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	final static String queueName = "spring-boot";

	@Autowired
	private ConnectionFactory rabbitConnectionFactory;
	
	@Autowired
	private JobProgressListener jobProgressListener;
	
    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("spring-boot-exchange");
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }
    
    @Bean
  
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    public Object getjobProgressListener(){
    	return jobProgressListener;
    }
    
    @Bean
    public RabbitTemplate batchExchangeTemplate() {
     RabbitTemplate r = new RabbitTemplate(rabbitConnectionFactory);
     r.setExchange("spring-boot-exchange");
     r.setQueue(queueName);
     r.setConnectionFactory(rabbitConnectionFactory);
     r.setMessageConverter(producerJackson2MessageConverter());
     return r;
    }
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	@Autowired
	public DataSource dataSource;
	
	@Bean
	public FlatFileItemReader<Person> reader(){
		
		FlatFileItemReader<Person> reader = new  FlatFileItemReader<Person>();
		reader.setResource(new ClassPathResource("sample-data.csv"));
		reader.setLineMapper(new DefaultLineMapper<Person>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "firstName", "lastName" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                setTargetType(Person.class);
            }});
        }});
		
		return reader;
	}
	
	@Bean
	public AmqpItemReader<Person> reader(RabbitTemplate rabbitTemplate){
		
		AmqpItemReader<Person> reader = new AmqpItemReader<>(rabbitTemplate);
		return reader;
	}
	
	@Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

	@Bean
	public JdbcBatchItemWriter<Person> writer() {
		JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
		writer.setSql("DELETE From people WHERE first_name=:firstName AND last_name=:lastName");
		writer.setDataSource(dataSource);
		return writer;
	}
	
	 @Bean
	    public Job importUserJob(JobCompletionNotificationListener listener) {
	        return jobBuilderFactory.get("importUserJob")
	                .incrementer(new RunIdIncrementer())
	                .listener(listener)
	                .flow(step1())
	                .end()
	                .build();
	    }

	    @Bean
	    public Step step1() {
	        return stepBuilderFactory.get("step1")
	                .<Person, Person> chunk(10)
	                .reader(reader(batchExchangeTemplate()))
	                .processor(processor())
	                .writer(writer())
	                .listener(getjobProgressListener())
	                .build();
	    }
}
