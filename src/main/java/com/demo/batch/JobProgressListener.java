package com.demo.batch;

import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.ItemWriteListener;


public interface JobProgressListener extends StepExecutionListener,ItemWriteListener {

}
