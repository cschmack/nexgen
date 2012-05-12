/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;

import static org.junit.Assert.*;

import org.junit.Test;

public class OperationFactoryTest {

    @Test
    public void testKnownOps() {
        assertNotNull(OperationFactory.find("sum"));
        assertNotNull(OperationFactory.find("SuM"));
        assertNotNull(OperationFactory.find("sum").create("blah"));
        
        assertNotNull(OperationFactory.find("aVg"));
        assertNotNull(OperationFactory.find("avg").create("blah"));
    }

    @Test
    public void testUnknownOps() {
        assertNull(OperationFactory.find("nothing"));
    }
}
