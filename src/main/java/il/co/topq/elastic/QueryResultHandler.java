package il.co.topq.elastic;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import il.co.topq.elastic.response.QueryResponse;


public class QueryResultHandler {

	private static final ObjectMapper mapper = new ObjectMapper();

	private final List<QueryResponse> queryResponseList;

	public QueryResultHandler(List<QueryResponse> response) {
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

}
