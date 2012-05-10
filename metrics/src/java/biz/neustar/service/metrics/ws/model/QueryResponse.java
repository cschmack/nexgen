/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import java.util.List;
import java.util.Map;


public class QueryResponse {
	private long rawDataCount;
	private List<Metric> rawData;
	private Map<String,Map<String,Double>> statistics;

	public long getRawDataCount() {
		return this.rawDataCount;
	}
	
	public void setRawDataCount(long rawDataCount) {
		this.rawDataCount = rawDataCount;
	}

	public List<Metric> getRawData() {
		return rawData;
	}

	public void setRawData(List<Metric> metrics) {
		this.rawData = metrics;
	}

	public Map<String, Map<String, Double>> getStatistics() {
		return statistics;
	}

	public void setStatistics(Map<String, Map<String, Double>> statistics) {
		this.statistics = statistics;
	}
}
