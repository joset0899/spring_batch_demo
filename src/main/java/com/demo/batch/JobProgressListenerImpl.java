package com.demo.batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.demo.batch.domain.JobStartParams;
import com.demo.batch.domain.JobStartParamsMapper;
import com.demo.batch.message.Person;
import com.demo.events.JobProgressEvent;


@Component
@StepScope
public class JobProgressListenerImpl implements JobProgressListener {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private int totalItemCount;
	private JobStartParams jobStartParams;
    private String stepName;
	
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
    private Jackson2JsonMessageConverter jackson2JsonMessageConverter;
	
	@Autowired
	public JobProgressListenerImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public ExitStatus afterStep(StepExecution arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		
		log.info("!!! JOB INICIO! Time to verify the results");

		jobStartParams = new JobStartParamsMapper().map(stepExecution.getJobParameters());
        stepName = stepExecution.getStepName();
        
		List<Person> results = jdbcTemplate.query("SELECT first_name, last_name FROM people", new RowMapper<Person>() {
			@Override
			public Person mapRow(ResultSet rs, int row) throws SQLException {
				return new Person(rs.getString(1), rs.getString(2));
			}
		});
		
		totalItemCount = results.size();
		
		rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        rabbitTemplate.convertAndSend("notifi-event", new JobProgressEvent(jobStartParams, stepName, 0));
		
	}

	@Override
	public void afterWrite(List arg0) {
		
		log.info("!!! JOB read! Time to verify the results");

		List<Person> results = jdbcTemplate.query("SELECT first_name, last_name FROM people", new RowMapper<Person>() {
			@Override
			public Person mapRow(ResultSet rs, int row) throws SQLException {
				return new Person(rs.getString(1), rs.getString(2));
			}
		});
		
		int valor =  (totalItemCount -results.size())*100/totalItemCount;
		
		rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        rabbitTemplate.convertAndSend("notifi-event", new JobProgressEvent(jobStartParams, stepName, valor));
	}

	@Override
	public void beforeWrite(List arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWriteError(Exception arg0, List arg1) {
		// TODO Auto-generated method stub
		
	}

}
