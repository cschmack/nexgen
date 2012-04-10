package biz.neustar.service.metrics.ws.model;

import java.util.Set;

public interface ContextProvider {
	
	// get set of contexts for service
	public Set<String> getContexts(String service);

}
