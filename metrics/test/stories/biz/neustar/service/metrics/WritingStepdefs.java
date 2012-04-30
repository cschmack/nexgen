/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics;

import static org.junit.Assert.*;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

public class WritingStepdefs {
    private Logger LOG = LoggerFactory.getLogger(WritingStepdefs.class);
    @Autowired
    private Server metricsServer;

    @Given("a metric interface")
    public void a_metric_interface() {
        LOG.info("Setup the metrics service");
        assertNotNull(metricsServer);
    }
    
    @When("I send key value pair metric data that is numeric")
    public void send_numeric_metric_data() {
        LOG.info("Send numeric metric data");
    }
    
    @Then("the metric is stored with a timestamp for later retrieval")
    public void store_metric_with_timestamp() {
        LOG.info("metric stored with timestamp");
    }
    
    // they don't all need to go in here, it's just a sample..
    
    @When("I send key value pair metric data that is non-numeric")
    public void send_non_numeric_metric_data() {
        LOG.info("Send non-numeric metric data");
    }
    
    @Then("^the metric is rejected as invalid$")
    public void the_metric_is_rejected_as_invalid() {
        LOG.info("Reject non-numeric metric data");
    }
}
