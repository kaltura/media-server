package configurations;

import validator.Validator;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataValidator {
    @JsonProperty("type")
    private Class<Validator> type;

	public Class<Validator> getType() {
		return type;
	}

	@SuppressWarnings("unchecked")
	public void setType(String type) throws ClassNotFoundException {
		this.type = (Class<Validator>) Class.forName(type);
	}
    
    
}
