package biz.neustar.service.metrics.ws.model;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

@Component
public class ContextProvider {
    private static final Set<String> EMPTY_SET = Sets.newHashSet();
    
    private ConcurrentSkipListMap<String, ContextConfig> contextMap = 
            new ConcurrentSkipListMap<String, ContextConfig>();
	
    
    public void setContexts(ContextConfig contextConfig) {
        Preconditions.checkNotNull(contextConfig);
        Preconditions.checkArgument(Strings.emptyToNull(contextConfig.getName()) != null);
        
        contextMap.put(contextConfig.getName(), contextConfig);
    }
    
	// get set of contexts for service
	public Set<String> getContexts(String service) {
        Set<String> contexts;
        // TODO find the greatest match of service name from the stored contexts
        if (contextMap.containsKey(service)) {
            contexts = contextMap.get(service).getContexts();
        } else {
            contexts = EMPTY_SET; // empty
        }
        
        return contexts;
    }
}
