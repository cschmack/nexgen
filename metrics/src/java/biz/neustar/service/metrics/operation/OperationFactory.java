/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;


public enum OperationFactory {
    SUM() {
        @Override
        public StatisticalOperation create(String valueName) {
            return new SumOperation(valueName);
        }
    },
    AVG() {
        @Override
        public StatisticalOperation create(String valueName) {
            return new AvgOperation(valueName);
        }
    }
    
    ;
        
    public abstract StatisticalOperation create(String valueName);
    
    /**
     * Find the operation for the given name
     * @param name the name of the operation
     * @return the operation that matches the name if found, or else null.
     */
    public static OperationFactory find(String name) {
        //this.values()[0].name()
        for (OperationFactory op : values()) {
            if (op.name().equalsIgnoreCase(name)) {
                return op;
            }
        }
        return null;
    }
}
