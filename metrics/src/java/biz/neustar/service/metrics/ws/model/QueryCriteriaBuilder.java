/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import java.util.List;

import biz.neustar.service.metrics.operation.AvgOperation;
import biz.neustar.service.metrics.operation.OperationFactory;
import biz.neustar.service.metrics.operation.RawRecordsOperation;
import biz.neustar.service.metrics.operation.SumOperation;

public final class QueryCriteriaBuilder {
    
    private QueryCriteriaBuilder() {
        // purely utility
    }
    
	public static QueryCriteria buildQueryCriteria(QueryRequest query) {
		QueryCriteria qc = new QueryCriteria( );		
		QueryRequest.StartEndTimeMillis tstePair = query.getStartEndTimeMillis( );
		qc.setTs(tstePair.getStart());
		qc.setTe(tstePair.getEnd());

		List<String> contexts = query.getContexts();
		for (String context : contexts) {
			// TODO: This is another instance where it would be nice to have a
			// programmatic way to
			// retrieve the required metrics context field names.
			ContextKVPair kvPair = new ContextKVPair( context );
			String key = kvPair.getContextKey();
			if (key.equals( Metric.FROM )) {
				qc.setFrom( kvPair.getContextValue());
			} else if (key.equals( Metric.HOST)) {
				qc.setHost( kvPair.getContextValue());
			} else if (key.equals(  Metric.INSTANCE)) {
				qc.setInstance( kvPair.getContextValue());
			} else if (key.equals( Metric.PROCESS)) {
				qc.setProcess( kvPair.getContextValue());
			} else if (key.equals( Metric.RESOURCE)) {
				qc.setResource( kvPair.getContextValue());
		    } else if (key.equals( Metric.SOURCE )) {
				qc.setSource( kvPair.getContextValue());
			} else {
				// This is a non-reserved context, so just stick it in the other/any field.
				qc.addOther( key, kvPair.getContextValue());
			}
		}
		
		for (String metric : query.getMetrics()) {
	        List<OperationFactory> opFactories = 
	                OperationFactory.find(query.getStats());
            for (OperationFactory op : opFactories) {
                qc.addOperation(op.create(metric));
            }
	    }
		
		
		if (query.getRaw()) {
			qc.addOperation(new RawRecordsOperation());
		}
		
		/*
		if( opsCount > 0 )
			qc.addOperation( ops );
		*/

		return qc;
	}
}
