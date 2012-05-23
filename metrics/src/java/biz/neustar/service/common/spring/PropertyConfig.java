/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.StandardEnvironment;




/*
This class when loaded will reorder the property sources such that the ResourcePropertySources
will be prioritized before the system or environment sources.  Also properties in the directory
specified by -DconfigEnv= will be used IF they match the names specified in @PropertySource annotation
on the configuration class. 

@Configuration("someServiceConfig")
@PropertySource({"classpath:defaults/app.properties"}) // by convention this is the default
@ComponentScan("biz.neustar.service")
public class AppConfig {
    .....
}

In this case if '-DconfigEnv=blah', then blah/app.properties (if it exists) would be prioritized before 
defaults/app.properties for property lookups.


/////
//This 'should' work according to the docs, however it only works if the configEnv properties file is 1st
//@PropertySource({"classpath:defaults/app.properties","classpath:${configEnv:defaults}/app.properties"})
/////
 */

@Configuration("propertyConfig")
public class PropertyConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer pspc(StandardEnvironment env) {
        PropertySourcesPlaceholderConfigurer propertyConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertyConfigurer.setPropertySources(PropertySourcesUtil.reorderPropertySources(env));
        return propertyConfigurer;
    }
}
