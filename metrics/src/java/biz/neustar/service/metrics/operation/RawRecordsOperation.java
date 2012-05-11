/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;

import java.util.ArrayList;
import java.util.List;

import biz.neustar.service.metrics.ws.model.Metric;

public class RawRecordsOperation extends Operation<List<Metric>>
{
	private List<Metric> metrics;

	public RawRecordsOperation( )
	{
		this.name = "rawData";
		this.metrics = new ArrayList<Metric>( );
	}

	@Override
	public List<Metric> getResult( )
	{
		return metrics;
	}

	@Override
	public void process( Metric metric )
	{
		if( !metrics.contains( metric ) )
		{
			metrics.add( metric );
		}
	}
}
