package com.demo.batch.service;

import com.demo.batch.domain.JobStartParams;

public interface JobService {

	 void runFirstJob(JobStartParams jobStartParams);
	 
	 void poblarPerson();
}
