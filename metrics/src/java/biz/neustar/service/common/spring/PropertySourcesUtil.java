/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.spring;

import java.io.IOException;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

import com.google.common.base.Strings;

public final class PropertySourcesUtil {
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(PropertySourcesUtil.class);
    
    public static final String CONFIG_ENV_PROP = "configEnv";
    
    private PropertySourcesUtil() {
        // utility class
    }

    public static PropertySources reorderPropertySources(StandardEnvironment env) {
        MutablePropertySources sources = env.getPropertySources();        
        // Now we're going to mess with the order of the property sources a bit
        // any ResourcePropertySources should be first and then any configEnv specific 
        // versions should be before them
        MutablePropertySources orderedSources = new MutablePropertySources();
        org.springframework.core.env.PropertySource<?> firstNonRes = null;
        Iterator<org.springframework.core.env.PropertySource<?>> iter = null;
        // TODO: in-place reordering..
        for (iter = sources.iterator(); iter.hasNext(); ) {
            org.springframework.core.env.PropertySource<?> source = iter.next();
            final boolean isResSrc = 
                    ResourcePropertySource.class.isAssignableFrom(source.getClass());
            if (isResSrc && firstNonRes != null) {
                orderedSources.addBefore(firstNonRes.getName(), source);
            } else {
                if (!isResSrc && firstNonRes == null) {
                    firstNonRes = source;
                }
                orderedSources.addLast(source);
            }
        }
        
        String configEnv = env.getProperty(CONFIG_ENV_PROP);
        if (!Strings.isNullOrEmpty(configEnv)) {
            try {
                orderedSources.addFirst(new ResourcePropertySource("classpath:"+ configEnv + "/app.properties"));
            } catch (IOException ex) {
                // actually going to just log & ignore
                LOGGER.error("Environment Specific config not found for: {}", configEnv);
            }
        }
        
        return orderedSources;
    }
    
}
