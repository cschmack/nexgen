/**
 * Copyright 2000-2011 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.cxf;

import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.Manifest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

@Path("/version")
public class VersionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionService.class);
    private static final String IMPL_VER = "Implementation-Version";
    
    private static AtomicReference< Class<?>[] > classes = 
            new AtomicReference< Class<?>[] >(new Class<?>[]{});

    public static void setClasses(Class<?>[] classes) {
        VersionService.classes.set(classes);
    }
    
    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public final String getVersionText() {
        StringBuilder versions = new StringBuilder();
        
        for (Entry<String, String> entry : getVersion().entrySet()) {
            versions.append(entry.getKey())
                .append(" = ")
                .append(entry.getValue())
                .append("\n");
        }
        if (versions.length() > 0)
            versions.append("\n");
        return versions.toString();
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public final Map<String, String> getVersion() {
        Map<String, String> jarVersions = Maps.newHashMap();
        
        
        jarVersions.put(getClassName(VersionService.class),
                getClassJarVersion(VersionService.class));
        for (Class<?> clazz : classes.get()) {
            jarVersions.put(getClassName(clazz),
                    getClassJarVersion(clazz));
        }
                
        return jarVersions;
    }
    
    
    protected static String getClassName(Class<?> clazz) {
        return clazz.getSimpleName();
    }
    
    protected static String getClassJarVersion(Class<?> clazz) {
        StringBuilder output = new StringBuilder();
        
        String path = clazz.getResource(clazz.getSimpleName() + ".class").toString();
        LOGGER.debug("Output: {}, path: {}", output, path);
        boolean unknown = true;
        if (path.startsWith("jar")) {
            String manifestPath = path.replaceFirst("\\!.*", "!/META-INF/MANIFEST.MF");
            try {
                LOGGER.debug("manifestPath: {}", manifestPath);
                Manifest manifest = new Manifest(new URL(manifestPath).openStream());
                LOGGER.debug("manifest main attributes: {}", manifest.getMainAttributes().toString());

                String value = manifest.getMainAttributes().getValue(IMPL_VER);
                if (value != null) {
                    output.append(value);
                    unknown = false;
                }
                
                if (LOGGER.isDebugEnabled()) {
                    for (Entry<Object, Object> entry : manifest.getMainAttributes().entrySet()) {
                        LOGGER.debug("entry = {} value = {}", entry.getKey(), entry.getValue());
                    }
                }
                
            } catch (Exception e) { 
                LOGGER.debug("Error Loading Manifest", e);
            }
        }
        if (unknown) {
            output.append("unknown");
        }
        return output.toString();
    }
}
