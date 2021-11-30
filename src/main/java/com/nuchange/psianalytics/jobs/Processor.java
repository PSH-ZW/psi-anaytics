package com.nuchange.psianalytics.jobs;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class Processor implements ItemProcessor<Object[], Object[]> {

    @Override
    public Object[] process(Object[] data) {
        return data;
    }
}
