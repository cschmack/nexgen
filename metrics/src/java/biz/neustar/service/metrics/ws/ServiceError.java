/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws;

import com.google.common.base.Objects;

public enum ServiceError {

    TOO_MANY_REQUESTS_PER_SEC(100, "Too many requests per second" ),
    TOO_MANY_POINTS(101, "Too many metric points" ),
    TOO_LARGE(102, "Request message too large"),
    INSERT_ERROR(103, "Insertion error occurred"),
    // add more here..
    ;
    
    
    private int code;
    private String text;
    private ServiceError(int code, String text) {
        this.code = code;
        this.text = text;
    }
    
    public String getText() {
        return text;
    }
    
    public int getCode() {
        return code;
    }
    
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", this.name())
                .add("code", code)
                .add("text", text)
                .toString();
    }
}
