package biz.neustar.service.metrics.ws.model.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import biz.neustar.service.metrics.ws.model.ContextConfig;

public final class ContextConfigValidator implements Validator {
    
    private static final ContextConfigValidator INSTANCE = 
            new ContextConfigValidator();
    
    private ContextConfigValidator() {}
    
    public static ContextConfigValidator getDefaultInstance() {
        return INSTANCE;
    }
    
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
