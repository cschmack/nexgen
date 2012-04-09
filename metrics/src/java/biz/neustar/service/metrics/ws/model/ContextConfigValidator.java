package biz.neustar.service.metrics.ws.model;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ContextConfigValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ContextConfig.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors e) {
		//ContextConfig contextConfig = (ContextConfig)obj;
		
		ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
	}

}
