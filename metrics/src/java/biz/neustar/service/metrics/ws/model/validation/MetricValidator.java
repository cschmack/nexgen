package biz.neustar.service.metrics.ws.model.validation;

import java.util.Map;
import java.util.Set;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import biz.neustar.service.metrics.ws.model.ContextProvider;
import biz.neustar.service.metrics.ws.model.Metric;

public class MetricValidator implements Validator {

	private ContextProvider contextProvider;
	private TimeValidator timeValidator = new TimeValidator();
	
	public MetricValidator(ContextProvider contextProvider) {
		this.contextProvider = contextProvider;
	}
	
	public void setTimeValidator(TimeValidator timeValidator) {
	    this.timeValidator = timeValidator;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Metric.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors e) {
		Metric metric = (Metric)obj;

		ValidationUtils.invokeValidator(timeValidator, metric.getTimestamp(), e);

		// required context
		ValidationUtils.rejectIfEmptyOrWhitespace(e, Metric.FROM, "from.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(e, Metric.SOURCE, "source.empty");
		
		Map<String, Object> any = metric.any();
		// check for extra 
		if ((any != null) && (!any.isEmpty())) {
			if (contextProvider == null) {
				e.reject("context.unknown", "no Context Provider");
			} else {
				Set<String> contexts = contextProvider.getContexts(metric.getSource());
				
				for (String s: any.keySet()) {
					if (!contexts.contains(s)) {
						// since not an actual field, just record an error with specific message
						e.reject("context.unknown", "unknown context: " + s);
					}
				}
			}
		}
	}
}
