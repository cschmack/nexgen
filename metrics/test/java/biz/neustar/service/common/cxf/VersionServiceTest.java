/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.cxf;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.VersionTestor;

public class VersionServiceTest {

    @Before
    @After
    public void beforeOrAfter() {
        VersionService.setClasses(new Class<?>[]{});
    }
    
    @Test
    public void testVersionedJar() {
        VersionService.setClasses(new Class<?>[] { VersionTestor.class});
        VersionService verSrvc = new VersionService();
        
        Map<String, String> verMap = verSrvc.getVersion();
        assertEquals(2, verMap.size());
        assertEquals("unknown", verMap.get("VersionService"));
        assertTrue(verMap.get("VersionTestor").matches("\\d+\\.\\d+.\\d+.\\d+"));
        assertEquals(
                "VersionTestor = " + verMap.get("VersionTestor") + "\n" +
                "VersionService = unknown\n" + // unknown since VersionService isn't in a jar
                "\n", // for clarity, we append a CRLF at the end
                verSrvc.getVersionText());
    }
    
    @Test
    public void testVersionNoJar() {
        VersionService verSrvc = new VersionService();
        
        Map<String, String> verMap = verSrvc.getVersion();
        assertEquals(1, verMap.size());
        
        assertEquals(
                "VersionService = unknown\n" + // unknown since VersionService isn't in a jar
                "\n", // for clarity, we append a CRLF at the end
                verSrvc.getVersionText());
    }
}
