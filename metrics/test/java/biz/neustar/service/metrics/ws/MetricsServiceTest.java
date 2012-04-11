/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.eclipse.jetty.server.Server;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import biz.neustar.service.metrics.ws.model.ContextConfig;
import biz.neustar.service.metrics.ws.model.Metric;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:defaults/metrics-context.xml"})
@DirtiesContext
public class MetricsServiceTest {

    @Autowired
    private ApplicationContext appCtx;
    
    
    private String location;
    
    @Before
    public void setup() {
        Server server = appCtx.getBean(Server.class);
        assertNotNull(server);
        location = String.format("http://localhost:%s",
                server.getConnectors()[0].getPort());
    }
    
    @Test
    public void testCreation() throws JsonGenerationException, JsonMappingException, IOException {
        WebClient client = WebClient.create(location)
            .path("/metrics/v1/metrics")
            .accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON);

        Metric metric = new Metric();
        metric.setFrom("biz.neustar.nis.cnam2nddip.lib.quova.ip2ll");
        metric.setHost("stulhdesec3.ultra.neustar.com");
        metric.setResource("http://www.foo.com");
        metric.setRequestor("windstream");
        metric.getValues().put("calls", 1.1234567890123);
        // YYYY-MM-DDTHH:MM:SS.mmm
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        metric.setTimestamp(dateFormatter.format(new Date()));
        
        List<Metric> testData = Lists.newArrayList(metric);
        ObjectMapper mapper = new ObjectMapper();
        Response resp = client.post(mapper.writeValueAsBytes(testData));
        assertEquals(204, resp.getStatus());
    }
    
    @Test
    public void testInvalidMetricValue() throws Exception {
        WebClient client = WebClient.create(location)
            .path("/metrics/v1/metrics")
            .accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON);

        Metric metric = new Metric();
        metric.setFrom("biz.neustar.nis.cnam2nddip.lib.quova.ip2ll");
        metric.setHost("stulhdesec3.ultra.neustar.com");
        metric.setResource("http://www.foo.com");
        metric.setRequestor("windstream");
        metric.getValues().put("calls", 9.9);
        // YYYY-MM-DDTHH:MM:SS.mmm
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        metric.setTimestamp(dateFormatter.format(new Date()));
        
        List<Metric> testData = Lists.newArrayList(metric);
        ObjectMapper mapper = new ObjectMapper();
        // manually manipulate the json to put in a string value
        String json = mapper.writeValueAsString(testData).replace("9.9", "\"invalid\"");
        
        Response resp = client.post(json.getBytes());
        assertEquals(400, resp.getStatus());
    }
    
    
    @Test
    public void testInvalidCreation() throws JsonGenerationException, JsonMappingException, IOException {
        WebClient client = WebClient.create(location)
            .path("/metrics/v1/metrics")
            .accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON);

        Metric metric = new Metric();        
        List<Metric> testData = Lists.newArrayList(metric);
        ObjectMapper mapper = new ObjectMapper();
        Response resp = client.post(mapper.writeValueAsBytes(testData));
        assertEquals(400, resp.getStatus());
    }
    
    @Test
    public void testConfigCreation() throws JsonGenerationException, JsonMappingException, IOException {
        WebClient client = WebClient.create(location)
            .path("/metrics/v1/config")
            .accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON);

        ContextConfig contextConfig = new ContextConfig();
        contextConfig.setName("default");
        contextConfig.setDescription("default context");
        contextConfig.getContexts().add("doppler");
        contextConfig.getContexts().add("radar");
        
        ObjectMapper mapper = new ObjectMapper();
        Response resp = client.post(mapper.writeValueAsBytes(contextConfig));
        assertEquals(204, resp.getStatus());
    }
    
    @Test
    public void testInvalidConfigCreation() throws JsonGenerationException, JsonMappingException, IOException {
        WebClient client = WebClient.create(location)
            .path("/metrics/v1/config")
            .accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON);

        ContextConfig contextConfig = new ContextConfig();
        //contextConfig.setName("");
        ObjectMapper mapper = new ObjectMapper();
        Response resp = client.post(mapper.writeValueAsBytes(contextConfig));
        assertEquals(400, resp.getStatus());
    }
 
}
