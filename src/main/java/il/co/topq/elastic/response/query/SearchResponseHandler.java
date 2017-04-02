package il.co.topq.elastic.response.query;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SearchResponseHandler {

	private static final ObjectMapper mapper = new ObjectMapper();

	private final List<SearchResponse> queryResponseList;

	public SearchResponseHandler(List<SearchResponse> response) {
		super();
		this.queryResponseList = response;
	}

	public List<Map<String, Object>> asMap() {
		List<Map<String, Object>> asMapResponse = new ArrayList<Map<String, Object>>();
//		@formatter:off
		queryResponseList.stream()
			.map(response -> response.getHits().getHits())
			.flatMap(List::stream)
			.map(hit -> hit.getDataSource())
			.forEach(asMapResponse::add);
//		@formatter:on
		return asMapResponse;
	}

	public <T> List<T> asClass(Class<T> clazz) {
//		@formatter:off
		return asMap()
				.stream()
				.map(hit -> mapper.convertValue(hit, clazz))
				.collect(toList());
//		@formatter:on
	}

	/**
	 * Returns the result as JSON string or empty string if fails to parse it
	 * 
	 * @return The result as JSon string
	 */
	public String asString() {
		try {
			return mapper.writeValueAsString(queryResponseList);
		} catch (JsonProcessingException e) {

		}
		return "";
	}
	
	@Override
	public String toString(){
		return asString();
	}

}
