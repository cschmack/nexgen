/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.util;

import org.eclipse.jetty.server.Server;

import com.google.common.base.Preconditions;

public class JettyServerUtil {
    private JettyServerUtil() {
        // utility class
    }
    
    /**
     * Get the URL for the given Server for the first connector
     * over http.
     * @return an URL of the form:  http://127.0.0.1:9090
     */
    public static String getURL(Server server) {
        Preconditions.checkArgument(server.getConnectors() != null);
        Preconditions.checkArgument(server.getConnectors().length > 0);
        return "http://" + server.getConnectors()[0].getName();
    }
}
