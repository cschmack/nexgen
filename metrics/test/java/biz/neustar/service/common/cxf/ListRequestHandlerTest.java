/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.cxf;

import static org.junit.Assert.*;

import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.junit.Test;
import org.mockito.Mockito;

public class ListRequestHandlerTest {

    @Test
    public void testContextList() {
        ListRequestHandler handler = new ListRequestHandler();
        
        Message inputMessage = Mockito.mock(Message.class);
        
        Mockito.when(inputMessage.get(Message.QUERY_STRING))
            .thenReturn("contexts=source{biz.neustar.nis},host{example.com}&ts=2012-04-01T00:00:00&metrics=range&metrics=responseTime&raw=true&stats=sum");
        
        String expected = 
                "contexts=source{biz.neustar.nis}&contexts=host{example.com}&ts=2012-04-01T00:00:00&metrics=range&metrics=responseTime&raw=true&stats=sum";
        
        ClassResourceInfo resourceClass = Mockito.mock(ClassResourceInfo.class);
        
        assertNull(handler.handleRequest(inputMessage, resourceClass));
        Mockito.verify(inputMessage).put(Message.QUERY_STRING, expected);
    }

    @Test
    public void testContextListMultiples() {
        ListRequestHandler handler = new ListRequestHandler();
        Message inputMessage = Mockito.mock(Message.class);
        
        Mockito.when(inputMessage.get(Message.QUERY_STRING))
            .thenReturn("contexts=source{biz.neustar.nis},host{example.com},from{foo}");
        
        String expected = 
                "contexts=source{biz.neustar.nis}&contexts=host{example.com}&contexts=from{foo}";
        
        ClassResourceInfo resourceClass = Mockito.mock(ClassResourceInfo.class);
        
        assertNull(handler.handleRequest(inputMessage, resourceClass));
        Mockito.verify(inputMessage).put(Message.QUERY_STRING, expected);
    }
    
    
    @Test
    public void testNullQuery() {
        ListRequestHandler handler = new ListRequestHandler();
        Message inputMessage = Mockito.mock(Message.class);
        
        Mockito.when(inputMessage.get(Message.QUERY_STRING))
            .thenReturn(null);
                
        ClassResourceInfo resourceClass = Mockito.mock(ClassResourceInfo.class);
        
        assertNull(handler.handleRequest(inputMessage, resourceClass));
        
        Mockito.verify(inputMessage, Mockito.never()).put(
                Mockito.anyString(), Mockito.anyString());
    }
    
    @Test
    public void testEmptyQuery() {
        ListRequestHandler handler = new ListRequestHandler();
        Message inputMessage = Mockito.mock(Message.class);
        
        Mockito.when(inputMessage.get(Message.QUERY_STRING))
            .thenReturn("");
                
        ClassResourceInfo resourceClass = Mockito.mock(ClassResourceInfo.class);
        
        assertNull(handler.handleRequest(inputMessage, resourceClass));
        
        Mockito.verify(inputMessage).put(Message.QUERY_STRING, "");
    }
    
    //
    
    @Test
    public void testUrlEncoded() {
        ListRequestHandler handler = new ListRequestHandler();
        Message inputMessage = Mockito.mock(Message.class);
        
        Mockito.when(inputMessage.get(Message.QUERY_STRING))
            .thenReturn("contexts=source%7Bbiz.neustar.nis%7D%2Chost%7Bexample.com%7D%2chost%7Bblah.org%7D");
                
        ClassResourceInfo resourceClass = Mockito.mock(ClassResourceInfo.class);
        
        assertNull(handler.handleRequest(inputMessage, resourceClass));
        
        Mockito.verify(inputMessage).put(Message.QUERY_STRING, 
                "contexts=source%7Bbiz.neustar.nis%7D&contexts=host%7Bexample.com%7D&contexts=host%7Bblah.org%7D");
    }
    
    @Test
    public void testEmptyValue() {
        ListRequestHandler handler = new ListRequestHandler();
        Message inputMessage = Mockito.mock(Message.class);
        
        Mockito.when(inputMessage.get(Message.QUERY_STRING))
            .thenReturn("contexts&raw,stuff");
                
        ClassResourceInfo resourceClass = Mockito.mock(ClassResourceInfo.class);
        
        assertNull(handler.handleRequest(inputMessage, resourceClass));
        
        Mockito.verify(inputMessage).put(Message.QUERY_STRING, "contexts&raw,stuff");
    }
    

    @Test
    public void testCommaInName() {
        ListRequestHandler handler = new ListRequestHandler();
        Message inputMessage = Mockito.mock(Message.class);
        
        Mockito.when(inputMessage.get(Message.QUERY_STRING))
            .thenReturn("raw,stuff=blah");
                
        ClassResourceInfo resourceClass = Mockito.mock(ClassResourceInfo.class);
        
        assertNull(handler.handleRequest(inputMessage, resourceClass));
        
        Mockito.verify(inputMessage).put(Message.QUERY_STRING, "raw,stuff=blah");
    }
}
