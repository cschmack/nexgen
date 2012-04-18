/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.util;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public final class ToStringUtil {
    private ToStringUtil() {}

    public static String mapToString(Map<String, ?> map) {
        if (map == null) {
            return "null";
        }
        
        ToStringHelper toStrHelper = Objects.toStringHelper(Map.class);
        for (Entry<String, ?> entry : map.entrySet()) {
            toStrHelper.add(entry.getKey(), entry.getValue());
        }

        return toStrHelper.toString();
    }
}
