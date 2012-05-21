/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


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
    },
    ALL() {
        @Override
        public StatisticalOperation create(String valueName) {
            CompositeOperation compositeOp = new CompositeOperation();
            // TODO: use values[] ?
            compositeOp.add(SUM.create(valueName));
            compositeOp.add(AVG.create(valueName));
            return null; // TODO:
            //return compositeOp;
        }
    },
    MIN() {
    	@Override
        public StatisticalOperation create(String valueName) {
            return new MinOperation(valueName);
    	}
    },
    MAX() {
    	@Override
        public StatisticalOperation create(String valueName) {
            return new MaxOperation(valueName);
    	}
    },
    ;
    
    public abstract StatisticalOperation create(String valueName);
    
    /**
     * Find the operation for the given name
     * @param name the name of the operation
     * @return the operation that matches the name if found, or else null.
     */
    public static OperationFactory find(String name) {
        for (OperationFactory op : values()) {
            if (op.name().equalsIgnoreCase(name)) {
                return op;
            }
        }
        return null;
    }
    
    public static List<OperationFactory> find(List<String> names) {
        Set<OperationFactory> opFactories = Sets.newHashSet();
        for (OperationFactory op : values()) {
            for (String name : names) {
                if (name.equalsIgnoreCase("all")) {
                    opFactories.addAll(all());
                    break; // add everything, can stop here
                } else if (op.name().equalsIgnoreCase(name)) {
                    opFactories.add(op);
                }
            }
        }
        return Lists.newArrayList(opFactories);
    }
    
    public static List<OperationFactory> all() {
        return Lists.newArrayList(OperationFactory.values());
    }
}
