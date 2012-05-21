/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class QueryRequest {
	
	@JsonIgnoreProperties
	class StartEndTimeMillis {
		private long start;
		private long end;
		
		public StartEndTimeMillis(long start, long end) {
			this.start = start;
			this.end = end;
		}
		
		public long getStart() {
			return start;
		}
		
		public long getEnd() {
			return end;
		}
	}
 
	public static final String TS_REGEX = "^(-)(\\d+)([smhDM])$";
	public static final String TE_REGEX = "^(\\d+)([smhDM])$";

    private List<String> contexts = Lists.newArrayList();
    private String ts;
    private String te;
    private List<String> metrics = Lists.newArrayList();
    private List<String> stats = Lists.newArrayList();
    private boolean raw = false;
    
    @JsonIgnore
    public StartEndTimeMillis getStartEndTimeMillis() {
    	DateTime dt = getStartDateTime();
    	
    	return new StartEndTimeMillis(dt.getMillis(), getEndDateTime(dt).getMillis());
    }
    
    @JsonIgnore
    public DateTime getStartDateTime() {
		DateTime startTime = DateTime.now();
		// try and calculate
		if (!Strings.isNullOrEmpty(ts)) {
			Matcher matcher = Pattern.compile(QueryRequest.TS_REGEX).matcher(ts);
			if (matcher.matches()) {
				boolean neg = matcher.group(1).equals("-");
				int val = Integer.parseInt(matcher.group(2));
				char suffix = matcher.group(3).charAt(0);
				// smhDM
				if (suffix == 's') {
					startTime = neg ? startTime.minusSeconds(val) : startTime.plusSeconds(val);
				} else if (suffix == 'm') {
					startTime = neg ? startTime.minusMinutes(val) : startTime.plusMinutes(val);
				} else if (suffix == 'h') {
					startTime = neg ? startTime.minusHours(val) : startTime.plusHours(val);
				} else if (suffix == 'D') {
					startTime = neg ? startTime.minusDays(val) : startTime.plusDays(val);
				} else if (suffix == 'M') {
					startTime = neg ? startTime.minusMonths(val) : startTime.plusMonths(val);
				}
			} else {
				startTime = DateTime.parse(ts, ISODateTimeFormat.localDateOptionalTimeParser());
			}
		}
		return startTime;
    }
    
    @JsonIgnore
    public DateTime getEndDateTime(DateTime startDateTime) {
    	DateTime endTime = DateTime.now();
    
		if (!Strings.isNullOrEmpty(te)) {
			Matcher matcher = Pattern.compile(QueryRequest.TE_REGEX).matcher(te);
			if (matcher.matches()) {
				int val = Integer.parseInt(matcher.group(1));
				char suffix = matcher.group(2).charAt(0);
				// smhDM
				if (suffix == 's') {
					endTime = startDateTime.plusSeconds(val);
				} else if (suffix == 'm') {
					endTime = startDateTime.plusMinutes(val);
				} else if (suffix == 'h') {
					endTime = startDateTime.plusHours(val);
				} else if (suffix == 'D') {
					endTime = startDateTime.plusDays(val);
				} else if (suffix == 'M') {
					endTime = startDateTime.plusMonths(val);
				}
			} else {
				endTime = DateTime.parse(te, ISODateTimeFormat.localDateOptionalTimeParser());
			}
		}
    	return endTime;
    }
    
    public List<String> getContexts() {
        return contexts;
    }
    public void setContexts(List<String> contexts) {
        this.contexts = contexts;
    }
    public String getTs() {
        return ts;
    }
    public void setTs(String ts) {
        this.ts = ts;
        
    }
    public String getTe() {
        return te;
    }
    public void setTe(String te) {
        this.te = te;
    }
    public List<String> getMetrics() {
        return metrics;
    }
    public void setMetrics(List<String> metrics) {
        this.metrics = metrics;
    }
    public List<String> getStats() {
        return stats;
    }
    public void setStats(List<String> stats) {
        this.stats = stats;
    }
    public boolean getRaw( ) {
        return raw;
    }
    public void setRaw(boolean raw) {
        this.raw = raw;
    }
    
    public String toString() {
        return Objects.toStringHelper(this)
                .add("ts", ts)
                .add("te", te)
                .add("contexts", contexts)
                .add("metrics", metrics)
                .add("stats", stats)
                .add("raw", raw)
                .toString();
    }
}
