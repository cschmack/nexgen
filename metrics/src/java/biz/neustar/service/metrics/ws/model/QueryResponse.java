/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;


public class QueryResponse {
	private long rawDataCount;

	public long getRawDataCount() {
		return this.rawDataCount;
	}
	
	public void setRawDataCount(long rawDataCount) {
		this.rawDataCount = rawDataCount;
	}
}
