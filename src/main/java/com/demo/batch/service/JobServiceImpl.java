package com.demo.batch.service;

import org.fluttercode.datafactory.impl.DataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.demo.batch.domain.JobStartParams;

@Service
public class JobServiceImpl implements JobService{

	private static final Logger LOG = LoggerFactory.getLogger(JobServiceImpl.class); 
			
	@Autowired
	private Job importUserJob;
	
	@Autowired
    private JobLauncher jobLauncher;
	
	@Autowired
	private  JdbcTemplate jdbcTemplate;

	
	@Override
	public void runFirstJob(JobStartParams jobStartParams) {
		
		JobParameters jobParameter = new JobParametersBuilder()
				.addLong("month",new Long(jobStartParams.getMonth()))
                .addLong("year", new Long(jobStartParams.getYear()))
                .toJobParameters();
		
		LOG.info("Running job in jobservice");
		
		try {
			jobLauncher.run(importUserJob, jobParameter);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			LOG.error("Job running failed", e);
			e.printStackTrace();
		}
		
	}

	@Override
	public void poblarPerson() {
		
		String sql = "INSERT INTO people " +
				"(first_name, last_name) VALUES (?, ?)";
		
		DataFactory data = new DataFactory();
		for (int i = 0; i <100000; i++) {
			LOG.info("Running poblar"+data.getFirstName()+" - "+data.getLastName());
			jdbcTemplate.update(sql, new Object[] { data.getFirstName(),data.getLastName()
			});
		}
		
		
		
	}

}
