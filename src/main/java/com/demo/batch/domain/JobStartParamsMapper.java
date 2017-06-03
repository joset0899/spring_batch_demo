package com.demo.batch.domain;


import org.springframework.batch.core.JobParameters;

public class JobStartParamsMapper {

    public JobStartParams map(JobParameters jobParameters) {
        return new JobStartParams(jobParameters.getLong("year").intValue(), jobParameters.getLong("month").intValue());
    }
}
