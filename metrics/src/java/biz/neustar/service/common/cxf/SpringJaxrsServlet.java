/**
 * Copyright 2000-2011 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.cxf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.apache.cxf.jaxrs.spring.SpringResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.Lists;

/**
 * 
 * Enhanced SpringJaxrsServlet to support spring based service
 * wiring of spring beans to an existing jetty (or other) servlet container.
 *
 */

public class SpringJaxrsServlet extends CXFNonSpringJaxrsServlet {
	private static final long serialVersionUID = 1L;
    
	@Autowired
	private ApplicationContext ac;
	
	private final List<Object> providers = Lists.newArrayList();
	
	public void addProvider(Object provider) {
	    providers.add(provider);
	}
	
	@Override
	protected List<?> getProviders(ServletConfig servletConfig) throws ServletException {
	    List<Object> providers = Lists.newArrayList();
	    // merge the provider lists
	    providers.addAll(super.getProviders(servletConfig));
	    providers.addAll(this.providers);
	    return providers;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected Map<Class, ResourceProvider> getResourceProviders(ServletConfig servletConfig,
	        Map<Class, Map<String, String>> resourceClasses) throws ServletException {
		
		Map<Class, ResourceProvider> map = new HashMap<Class, ResourceProvider>();
        for (Map.Entry<Class, Map<String, String>> entry : resourceClasses.entrySet()) {
            Class<?> c = entry.getKey();
        	// TODO: add an init option to specify bean names instead of classes?
        	String[] beanNames = ac.getBeanNamesForType(c, false, true);
        	for (String beanName : beanNames) {
        		// use the spring resource factory since we are using javaconfig
        		SpringResourceFactory factory = new SpringResourceFactory(beanName);
        		factory.setApplicationContext(ac);
        		map.put(c, factory);
        	}
        }
        return map;
	}
}
