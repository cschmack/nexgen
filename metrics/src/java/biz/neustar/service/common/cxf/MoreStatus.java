/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.cxf;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

public enum MoreStatus implements Response.StatusType {
    // http://tools.ietf.org/html/draft-nottingham-http-new-status-04
    PRECONDITION_REQUIRED(428, "Precondition Required"),
    TOO_MANY_REQUESTS(429, "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
    NETWORK_AUTH_REQUIRED(511, "Network Authentication Required"),
    ;

    private int code;
    private String reason;
    private MoreStatus(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }
    
    @Override
    public Family getFamily() {
        Response.Status.Family family = Response.Status.Family.OTHER;
        int familyCode = code / 100;
        switch (familyCode) {
        case 1:
            family = Response.Status.Family.INFORMATIONAL;
            break;
        case 2:
            family = Response.Status.Family.SUCCESSFUL;
            break;
        case 3:
            family = Response.Status.Family.REDIRECTION;
            break;
        case 4:
            family = Response.Status.Family.CLIENT_ERROR;
            break;
        case 5:
            family = Response.Status.Family.SERVER_ERROR;
            break;
        }
        return family;
    }

    @Override
    public String getReasonPhrase() {
        return reason;
    }

    @Override
    public int getStatusCode() {
        return code;
    }
}
