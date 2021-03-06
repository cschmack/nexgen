package biz.neustar.service.metrics.ws.model;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import biz.neustar.service.common.spring.ValidationUtil;
import biz.neustar.service.metrics.ws.model.validation.QueryRequestValidator;

import com.google.common.collect.ImmutableList;

public class QueryRequestTest {
	
	@Test
	public void testRelativeStart() {
		QueryRequest queryRequest = new QueryRequest();
		
		long expected, tsResult;
		
		// I assume any of these might just fail cause can't assure start time are sync'd
		// TODO can Mockito help?
		
		// secs, 
		queryRequest.setTs("-10s");
		expected = new Date().getTime() - 10 * 1000;
		tsResult = queryRequest.getStartEndTimeMillis().getStart();
		
        assertEquals("seconds:", expected/1000, tsResult/1000);
        
		// minutes
		queryRequest.setTs("-10m");
		expected = new Date().getTime() - 10 * 60 * 1000;
		tsResult = queryRequest.getStartEndTimeMillis().getStart();
		
        assertEquals("minutes:", expected/1000, tsResult/1000);
        
		queryRequest.setTs("-10h");
		expected = new Date().getTime() - 10 * 60 * 60 * 1000;
		tsResult = queryRequest.getStartEndTimeMillis().getStart();
		
        assertEquals("hours:", expected/1000, tsResult/1000);
        
		queryRequest.setTs("-10D");
		expected = new Date().getTime() - 10 * 60 * 60 * 24 * 1000;
		tsResult = queryRequest.getStartEndTimeMillis().getStart();
		
        assertEquals("days:", expected/1000, tsResult/1000);

        // month testing 
        queryRequest.setTs("-10M");
        expected = DateTime.now().minusMonths(10).getMillis();
		tsResult = queryRequest.getStartEndTimeMillis().getStart();
		
        assertEquals("months:", expected/1000, tsResult/1000);
	}

	@Test
	public void testRelativeEnd() {
		QueryRequest queryRequest = new QueryRequest();
		
		long expected, teResult;
		
		// I assume any of these might just fail cause can't assure start time are sync'd
		// TODO can Mockito help?
		
		// secs, 
		queryRequest.setTs("-10s");
		queryRequest.setTe("5s");
		
		expected = new Date().getTime() - 5 * 1000;
		teResult = queryRequest.getStartEndTimeMillis().getEnd();
		
        assertEquals("seconds:", expected/1000, teResult/1000);
        
		// mins, 
		queryRequest.setTs("-10m");
		queryRequest.setTe("5m");
		
		expected = new Date().getTime() - 5 * 60 * 1000;
		teResult = queryRequest.getStartEndTimeMillis().getEnd();
		
        assertEquals("minutes:", expected/1000, teResult/1000);

		// hours, 
		queryRequest.setTs("-10h");
		queryRequest.setTe("5h");
		
		expected = new Date().getTime() - 5 * 60 * 60 * 1000;
		teResult = queryRequest.getStartEndTimeMillis().getEnd();
		
        assertEquals("hours:", expected/1000, teResult/1000);

		// days, 
		queryRequest.setTs("-10D");
		queryRequest.setTe("5D");
		
		expected = new Date().getTime() - 5 * 60 * 60 * 24 * 1000;
		teResult = queryRequest.getStartEndTimeMillis().getEnd();
		
        assertEquals("days:", expected/1000, teResult/1000);

		// months, 
		queryRequest.setTs("-10M");
		queryRequest.setTe("5M");
		
        expected = DateTime.now().minusMonths(5).getMillis();
		teResult = queryRequest.getStartEndTimeMillis().getEnd();
		
        assertEquals("months:", expected/1000, teResult/1000);
	}
	
	@Test
	public void testRequestValidation_no_te() {
	    QueryRequest req = new QueryRequest();
	    req.setTs("-10s");
	    req.setTe("");
	    req.setContexts(ImmutableList.of("ctx"));
	    QueryRequestValidator validator = new QueryRequestValidator();
	    
	    ValidationUtil validationUtil = new ValidationUtil();
	    validationUtil.validate(req, validator);
	}

	@Test
	public void testCriteriaBuilder() {
	    QueryRequest query = new QueryRequest();
	    query.setMetrics(ImmutableList.of("foo1", "foo2"));
	    QueryCriteria criteria = QueryCriteriaBuilder.buildQueryCriteria(query);
	    criteria.getOperations();
	}
}
