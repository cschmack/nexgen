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
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import biz.neustar.service.common.util.JettyServerUtil;
import biz.neustar.service.metrics.config.AppConfig;
import biz.neustar.service.metrics.ws.model.ContextConfig;
import biz.neustar.service.metrics.ws.model.Metric;
import biz.neustar.service.metrics.ws.model.QueryResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class, loader=AnnotationConfigContextLoader.class)
@DirtiesContext
public class MetricsServiceTest {

    @Autowired
    private ApplicationContext appCtx;
    
    
    private String location;
    
    @Before
    public void setup() {
        Server server = appCtx.getBean(Server.class);
        assertNotNull(server);
        location = JettyServerUtil.getURL(server);
    }
    
    
    @Test
    public void testCreation() throws JsonGenerationException, JsonMappingException, IOException {
        WebClient client = WebClient.create(location)
            .path("/metrics/v1/metrics")
            .accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON);

        Metric metric = new Metric();
        metric.setSource("biz.neustar.nis.cnam2nddip.lib.quova.ip2ll");
        metric.setHost("stulhdesec3.ultra.neustar.com");
        metric.setResource("http://www.foo.com");
        metric.setFrom("windstream");
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
        metric.setSource("biz.neustar.nis.cnam2nddip.lib.quova.ip2ll");
        metric.setHost("stulhdesec3.ultra.neustar.com");
        metric.setResource("http://www.foo.com");
        metric.setFrom("windstream");
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
 
    
    @Test
    public void testQuery() throws JsonGenerationException, JsonMappingException, IOException {
        WebClient client = getClient("query")
                .query("contexts", "source{biz.neustar.nis.*}", "host{rcweb1}")
                .query("ts", "-12m")
                .query("stats", "all")
                .query("metrics", "responseTime")
                .query("raw", "true");
        Response resp = client.get();
        assertEquals(200, resp.getStatus());
    }
    

    @Test
    public void testQueryEncoded() throws JsonGenerationException, JsonMappingException, IOException {
        String path = "/metrics/v1/query?contexts=source%7Bbiz.neustar.nis%7D%2Chost%7Bexample.com%7D&ts=2012-04-01T00%3A00%3A00&te=&raw=true&metrics=range&stats=sum";
        URL queryUrl = new URL(location + path);
        
        WebClient client = WebClient.create(location)
                .path(queryUrl)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON);
        
        Response resp = client.get();
        assertEquals(200, resp.getStatus());
        
        
        
        ObjectMapper mapper = new ObjectMapper();
        QueryResponse queryResponse = 
                mapper.readValue((InputStream) resp.getEntity(), QueryResponse.class);
        assertEquals(0, queryResponse.getRawDataCount());
    }
    
    

    @Test
    public void testQueryEncodedWithResults() throws Exception {
        WebClient creationClient = getClient("metrics");
    
        Metric metric1 = new Metric();
        metric1.setSource("biz.neustar.nis.2");
        metric1.setHost("example.com");
        metric1.setResource("http://www.foo.com");
        metric1.setFrom("windstream");
        metric1.getValues().put("range", 5.3);
        metric1.setTimestamp("2012-04-01T01:00:00");
        
        Metric metric2 = new Metric();
        metric2.setSource("biz.neustar.nis.2");
        metric2.setHost("example.com");
        metric2.setResource("http://www.foo.com");
        metric2.setFrom("windstream");
        metric2.getValues().put("range", 4.7);
        
        metric2.setTimestamp("2012-04-01T02:00:00");
        
        
        List<Metric> testData = Lists.newArrayList(metric1, metric2);
        ObjectMapper mapper = new ObjectMapper();
        Response creationResp = creationClient.post(mapper.writeValueAsBytes(testData));
        assertEquals(204, creationResp.getStatus());
    
        
        String path = "/metrics/v1/query?contexts=source%7Bbiz.neustar.nis.2%7D%2Chost%7Bexample.com%7D&ts=2012-04-01T00%3A00%3A00&te=&raw=true&metrics=range&stats=sum";
        URL queryUrl = new URL(location + path);
        
        WebClient client = WebClient.create(location)
                .path(queryUrl)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON);
        
        Response resp = client.get();
        assertEquals(200, resp.getStatus());
        
        QueryResponse queryResponse = 
                mapper.readValue((InputStream) resp.getEntity(), QueryResponse.class);
        assertEquals(2, queryResponse.getRawDataCount());
        // metric values
        List<Metric> rawMetrics = queryResponse.getRawData();
        assertEquals(2, rawMetrics.size());
        assertEquals(metric1, rawMetrics.get(0));
        assertEquals(metric2, rawMetrics.get(1));
        
        // check stats
        Map<String, Map<String, Double>> statsMap = queryResponse.getStatistics();
        assertEquals(1, statsMap.size());
        Map<String, Double> rangeStats = statsMap.get("range");
        assertNotNull(rangeStats);
        assertEquals(10.0, rangeStats.get("sum"), 0.001);
        
    }
    
    private WebClient getClient(String relativePath) {
        return WebClient.create(location)
            .path("/metrics/v1/" + relativePath)
            .accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON);
    }
}
