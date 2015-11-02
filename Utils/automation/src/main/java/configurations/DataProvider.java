package configurations;

import provider.Provider;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataProvider {
    @JsonProperty("type")
    private Class<Provider> type;

	public Class<Provider> getType() {
		return type;
	}

	@SuppressWarnings("unchecked")
	public void setType(String type) throws ClassNotFoundException {
		this.type = (Class<Provider>) Class.forName(type);
	}
    
    
}
