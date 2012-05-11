/** Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import java.util.List;

import biz.neustar.service.metrics.operation.Operation;
import biz.neustar.service.metrics.operation.RawRecordsOperation;
import biz.neustar.service.metrics.operation.StatisticalOperation;

public class QueryResponseBuilder
{
	public static QueryResponse buildQueryResponse( List<Operation<?>> operations )
	{
		QueryResponse response = new QueryResponse( );
		
		// TODO: This is basically a hack because I don't know how to annotate the other classes
		// to return the proper JSON response.  We also clearly need a better way to retrieve
		// the results.
		for( Operation<?> op : operations )
		{
			// TODO: Make this something less horrible.
			if( RawRecordsOperation.class.isInstance( op ) )
			{
				RawRecordsOperation rawData = (RawRecordsOperation)op;
				response.setRawData( rawData.getResult( )  );
			}
			else if( StatisticalOperation.class.isInstance( op ))
			{
				// This is some form of statistic.
				StatisticalOperation sop = (StatisticalOperation)op;
				response.addStatistics( sop.getValueName( ), sop.getName( ), sop.getResult( ) );
			}
			else
			{
				// TODO: Figure out what to do with unsupported operation types.
			}
		}
		
		
		return response;
	}
}
