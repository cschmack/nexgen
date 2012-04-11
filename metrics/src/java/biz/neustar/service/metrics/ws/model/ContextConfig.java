package biz.neustar.service.metrics.ws.model;

import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

public class ContextConfig {

	private String name;    
    private String description;
	private Set<String> contexts = Sets.newHashSet();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<String> getContexts() {
		return contexts;
	}

	public void setContexts(Set<String> contexts) {
		this.contexts = contexts;
	}

    public String toString() {
        return Objects.toStringHelper(this)
            .add("name", name)
            .add("description", description)
            .add("values", contexts.toString())
            .toString();
    }	
}
