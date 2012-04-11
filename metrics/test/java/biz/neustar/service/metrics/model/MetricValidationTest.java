package biz.neustar.service.metrics.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import biz.neustar.service.metrics.ws.model.ContextProvider;
import biz.neustar.service.metrics.ws.model.Metric;
import biz.neustar.service.metrics.ws.model.MetricValidator;

import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:defaults/metrics-context.xml"})
@DirtiesContext
public class MetricValidationTest {
	
	protected <T> boolean isItemValid(T item, Validator validator, String name) {
    	DataBinder binder = new DataBinder(item, name);
    	binder.setValidator(validator);
    	binder.validate();
    	Errors result = binder.getBindingResult();
    
    	return !result.hasErrors();
    }
	
	
	@Test
	public void testValidBasic() {
        Metric metric = new Metric();
        metric.setFrom("biz.neustar.nis");
        metric.setHost("example.com");
        metric.setResource("http://www.foo.com");
        metric.setRequestor("windstream");
        metric.getValues().put("calls", 12.123456789012);
        
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(new Date());
        metric.setTimestamp(DatatypeConverter.printDateTime(c));
        
        MetricValidator metricValidator = new MetricValidator(null);
        
        assertTrue(isItemValid(metric, metricValidator, "metric"));
	}

	@Test
	public void testInvalidBasic() {
        Metric metric = new Metric();
        // from is required, omit to make invalid -- metric.setFrom("biz.neustar.nis");
        metric.setHost("example.com");
        metric.setResource("http://www.foo.com");
        metric.setRequestor("windstream");
        metric.getValues().put("calls", 1.0);
        
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(new Date());
        metric.setTimestamp(DatatypeConverter.printDateTime(c));
        
        MetricValidator metricValidator = new MetricValidator(null);
        
        assertFalse(isItemValid(metric, metricValidator, "metric"));
	}
	
	@Test
	public void testValidExtraContext() {
        Metric metric = new Metric();
        metric.setFrom("biz.neustar.nis");
        metric.setHost("example.com");
        metric.setResource("http://www.foo.com");
        metric.setRequestor("windstream");
        metric.getValues().put("calls", 1.2);
        metric.set("foo", (Object)"nothing");
        
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(new Date());
        metric.setTimestamp(DatatypeConverter.printDateTime(c));
                
        ContextProvider contextProvider = new ContextProvider() {
			
			@Override
			public Set<String> getContexts(String service) {
				Set<String> contexts;
				if (service.equals("biz.neustar.nis")) {
					contexts = Sets.newHashSet("foo", "boo");
				} else {
					contexts = Sets.newHashSet(); // empty
				}
							
				return contexts;
			}
		};

        MetricValidator metricValidator = new MetricValidator(contextProvider);
        
        assertTrue(isItemValid(metric, metricValidator, "metric"));
	}
	
	@Test
	public void testInvalidExtraContext() {
        Metric metric = new Metric();
        metric.setFrom("biz.neustar.nis");
        metric.setHost("example.com");
        metric.setResource("http://www.foo.com");
        metric.setRequestor("windstream");
        metric.getValues().put("calls", 15.0);
        metric.set("goo", (Object)"nothing");
        
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(new Date());
        metric.setTimestamp(DatatypeConverter.printDateTime(c));
        
        
        
        ContextProvider contextProvider = new ContextProvider() {
			
			@Override
			public Set<String> getContexts(String service) {
				Set<String> contexts;
				if (service.equals("biz.neustar.nis")) {
					contexts = Sets.newHashSet("foo", "boo");
				} else {
					contexts = Sets.newHashSet(); // empty
				}
							
				return contexts;
			}
		};

        MetricValidator metricValidator = new MetricValidator(contextProvider);
        
        assertFalse(isItemValid(metric, metricValidator, "metric"));
	}
	
	@Test
	public void testInvalidExtraContextNoProvider() {
        Metric metric = new Metric();
        metric.setFrom("biz.neustar.nis");
        metric.setHost("example.com");
        metric.setResource("http://www.foo.com");
        metric.setRequestor("windstream");
        metric.getValues().put("calls", 1.2321);
        metric.set("goo", (Object)"nothing");
        
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(new Date());
        metric.setTimestamp(DatatypeConverter.printDateTime(c));
        
        
        
        MetricValidator metricValidator = new MetricValidator(null);
        
        assertFalse(isItemValid(metric, metricValidator, "metric"));
	}
		
}
