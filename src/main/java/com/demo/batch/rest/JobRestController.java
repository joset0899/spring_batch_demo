package com.demo.batch.rest;


import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.batch.domain.JobStartParams;
import com.demo.batch.service.JobService;


@Controller
public class JobRestController {

	private static final Logger logger = LoggerFactory.getLogger(JobRestController.class);
	
	@Autowired
	private JobService jobService;
	
	@RequestMapping(value = "runJob/{year}/{month}", method = RequestMethod.GET)
    @ResponseBody
    public void runJob(@PathVariable("year") Long year, @PathVariable("month") Long month) {
		
		logger.debug("Running job in rest controller");
        StopWatch stopwatch =new StopWatch();
        stopwatch.start();
		jobService.runFirstJob(new JobStartParams(year, month));
		stopwatch.stop();
		logger.info("=======================================");
		logger.info("Time needed: " + stopwatch.getTotalTimeMillis());
		logger.info("=======================================");
	}
	
	@RequestMapping(value = "poblar", method = RequestMethod.GET)
    @ResponseBody
    public void poblar() {
    	jobService.poblarPerson();
    }
}
