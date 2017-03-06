package il.co.topq.elastic.response.generic;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericResponseHandler {

	private static final ObjectMapper mapper = new ObjectMapper();

	private final Map<String, Object> response;

	public GenericResponseHandler(Map<String, Object> response) {
		this.response = response;
	}

	public Map<String, Object> asMap() {
		return response;
	}

	public String asString() {
		try {
			return mapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {

		}
		return "";
	}

	@Override
	public String toString() {
		return asString();
	}

}
