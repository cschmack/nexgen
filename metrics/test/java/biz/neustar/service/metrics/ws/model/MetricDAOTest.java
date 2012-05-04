/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import biz.neustar.service.metrics.config.AppConfig;
import biz.neustar.service.metrics.operation.Operation;
import biz.neustar.service.metrics.operation.SumOperation;

import com.google.common.collect.Maps;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class, loader=AnnotationConfigContextLoader.class)
public class MetricDAOTest {

    @Autowired
    private MetricsDAO metricsDAO;
    
    @Before
    public void before() {
        metricsDAO.getMetrics().clear();
    }
    
    
    @Test
    public void testPut() {
        Metric metric = new Metric();
        metric.setFrom("tests");
        metric.setTimestamp(
                DatatypeConverter.printDateTime(Calendar.getInstance()));
        metric.getValues().put("foo", 1.1);
        metricsDAO.put(metric);
        assertEquals(1, metricsDAO.getMetrics().size());
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testOperation() {
        Metric metric = new Metric();
        metric.setFrom("tests");
        metric.setTimestamp(
                DatatypeConverter.printDateTime(Calendar.getInstance()));
        metric.getValues().put("foo", 1.1);
        metricsDAO.put(metric);
        
        Operation op = Mockito.mock(Operation.class);
        
        metricsDAO.apply(op, 
                0, (new Date()).getTime(), Pattern.compile("tests\\.foo"));
        Mockito.verify(op, Mockito.times(1)).apply(metric);
        
        // try regex
        Operation op2 = Mockito.mock(Operation.class);
        metricsDAO.apply(op2, 
                0, (new Date()).getTime(), Pattern.compile("tests.*"));
        Mockito.verify(op2, Mockito.times(1)).apply(metric);
    }

    @Test
    public void testSumOp() {
        {
            Metric metric = new Metric();
            metric.setFrom("tests");
            metric.setTimestamp(
                    DatatypeConverter.printDateTime(Calendar.getInstance()));
            metric.getValues().put("foo", 1.1);
            metricsDAO.put(metric);
        }
        {
            Metric metric = new Metric();
            metric.setFrom("tests");
            metric.setTimestamp(
                    DatatypeConverter.printDateTime(Calendar.getInstance()));
            metric.getValues().put("foo", 2.1);
            metricsDAO.put(metric);
        }

        SumOperation op = new SumOperation("foo");
        metricsDAO.apply(op, 0, (new Date()).getTime(),Pattern.compile("tests.*"));
        assertEquals(3.2, op.getResult(), 0.01);
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testOperationContextFilter() {
        Metric metric1 = new Metric();
        metric1.setFrom("tests");
        metric1.setTimestamp(
                DatatypeConverter.printDateTime(Calendar.getInstance()));
        metric1.getValues().put("foo", 1.1);
        metric1.any().put("something", "else");
        metricsDAO.put(metric1);
        
        Metric metric2 = new Metric();
        metric2.setFrom("tests");
        metric2.setTimestamp(
                DatatypeConverter.printDateTime(Calendar.getInstance()));
        metric2.getValues().put("foo", 1.1);
        metric2.any().put("something", "todo");
        metricsDAO.put(metric2);
        
        // try regex
        Operation op2 = Mockito.mock(Operation.class);
        Map<String, Pattern> contextMatch = Maps.newHashMap();
        contextMatch.put("something", Pattern.compile("else"));
        metricsDAO.apply(op2, 
                0, (new Date()).getTime(), Pattern.compile("tests.*"),
                contextMatch);
        Mockito.verify(op2, Mockito.times(1)).apply(metric1);
    }
}
