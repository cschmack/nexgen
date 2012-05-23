/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.cxf;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JsonMappingExceptionMapper;
import com.fasterxml.jackson.jaxrs.json.JsonParseExceptionMapper;

public final class JacksonProviders {

    private JacksonProviders() {
        // utility
    }
 
    /**
     * register the default configured JacksonJsonProvider with exception mappers as well.
     * 
     * @param jaxrsServlet the servlet to add the providers
     */
    public static void registerJsonProvider(SpringJaxrsServlet jaxrsServlet) {
        // add the jackson json providers
        JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
        jsonProvider.configure(
                SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jaxrsServlet.addProvider(jsonProvider);
        jaxrsServlet.addProvider(new JsonMappingExceptionMapper());
        jaxrsServlet.addProvider(new JsonParseExceptionMapper());
    }
    
    // TODO: xml version
}
