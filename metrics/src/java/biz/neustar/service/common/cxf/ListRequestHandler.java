/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.cxf;


import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;

import com.google.common.base.Splitter;

public class ListRequestHandler implements RequestHandler {
    private static Splitter PARAM_SPLITTER = Splitter.on('&');

    @Override
    public Response handleRequest(Message inputMessage, ClassResourceInfo resourceClass) {
        String queryString = (String) inputMessage.get(Message.QUERY_STRING);
        
        if (queryString == null) {
            return null;
        }
        
        StringBuilder fixedParams = new StringBuilder();
        Iterable<String> params = PARAM_SPLITTER.split(queryString);
        for (String param : params) {
            int index = param.indexOf(',');
            
            while (index != -1) {
                if (fixedParams.length() != 0) {
                    fixedParams.append('&');
                }
                
                String name = param.substring(0, param.indexOf('='));
                fixedParams.append(param.substring(0, index));
                param = name + "=" + param.substring(index + 1);
                index = param.indexOf(',');
            }
            
            if (fixedParams.length() != 0) {
                fixedParams.append('&');
            }
            
            fixedParams.append(param);
        }
        String queryStringFixed = fixedParams.toString();
        // should be at least as long otherwise we messed up..
        assert queryStringFixed.length() >= queryString.length();
        inputMessage.put(Message.QUERY_STRING, queryStringFixed);
        
        return null; // null to not block the request
    }

}
