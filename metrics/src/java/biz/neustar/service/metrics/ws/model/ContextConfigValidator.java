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
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "description", "description.empty");
//		if ((contextConfig.getDescription() == null) || (contextConfig.getDescription().trim().length() == 0) ) {
//			e.rejectValue("description", "description.required", "description field is missing");
//		}
	}

}
