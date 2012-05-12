/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.cxf;

import java.util.List;

import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;


public class ServletHolderFactory {

    /**
     * Create a Jetty Servlet that is wired to support the supplied service.
     * The servlet is setup to support extensions on the path 
     * and strip then and appropriately set the accepts header.
     * It also enables GZip on requests and responses.
     * @param jaxrsServlet the JAX-RS servlet to use for the service
     * @param serviceClasses the service classes to expose on the servlet
     * @return the Servlet that is ready to be added to Jetty
     */
    public ServletHolder getServletHolder(SpringJaxrsServlet jaxrsServlet, 
            Class<?>... serviceClasses) {

        StringBuilder serviceClassNames = new StringBuilder();
        
        // now append the service classes
        for (Class<?> serviceClass : serviceClasses) {
            serviceClassNames.append(serviceClass.getName())
                .append(' ');
        }
        // setup versioning
        VersionService.setClasses(serviceClasses);
        serviceClassNames.append(VersionService.class.getName());
        
        ServletHolder holder = new ServletHolder(jaxrsServlet);
        holder.setInitParameter("jaxrs.serviceClasses", 
                serviceClassNames.toString());

        // Register our providers
        final String providers = ListRequestHandler.class.getName();
        if (!providers.isEmpty()) {
            holder.setInitParameter("jaxrs.providers", providers);
        }
        
        final String inInterceptors = 
                createList(GZIPInInterceptor.class);
        
        holder.setInitParameter("jaxrs.inInterceptors", 
                inInterceptors);
        
        holder.setInitParameter("jaxrs.outInterceptors",
                createList(GZIPOutInterceptor.class));
        
        return holder;
    }
    
    protected String createList(Class<?>...interceptors) {
        List<String> names = Lists.newArrayList();
        for (Class<?> interceptor : interceptors) {
            names.add(interceptor.getName());
        }
        return Joiner.on(' ').join(names);
    }
}
