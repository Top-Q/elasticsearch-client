package il.co.topq.elastic.endpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import il.co.topq.elastic.ESRest;
import il.co.topq.elastic.response.query.SearchResponse;
import il.co.topq.elastic.response.query.SearchResponseHandler;

public class Search {

	private final static String SERCH_BY_TERM = "{\"size\":%d,\"query\": {\"term\" : { \"%s\" : \"%s\" }  } }";

	private final static String SERCH_BY_WILDCARD = "{\"size\":%d,\"query\": {\"wildcard\" : { \"%s\" : \"%s\" }  } }";
	
	private final static String SERCH_BY_QUERY = "{\"size\":%d,\"query\": { \"bool\" : { \"must\" : { \"query_string\" : { \"query\" : \"%s\" } }} }}";

	private final static String SERCH_BY_RANGE = "{\"size\":%d,\"query\": { \"range\" : { \"%s\" :  %s  } }}";

	private final ESRest client;

	private final String indexName;

	private final String documentName;

	private final int size;

	private final boolean scroll;

	public Search(ESRest client, String indexName, String documentName, int size, boolean scroll) {
		this.client = client;
		this.indexName = indexName;
		this.documentName = documentName;
		this.size = size;
		this.scroll = scroll;
	}

	private SearchResponseHandler search(String requestBody) throws IOException {
		SearchResponse response = client.post("/" + indexName + "/_search?scroll=1m", requestBody,
				SearchResponse.class, true);

		final List<SearchResponse> responses = new ArrayList<SearchResponse>();
		while (response.getHits().getHits().size() > 0) {
			responses.add(response);
			response = scroll(response.getScrollId());
		}
		return new SearchResponseHandler(responses);
	}

	public SearchResponseHandler byQuery(String query) throws IOException {
		String requestBody = String.format(SERCH_BY_QUERY, size, query);
		return search(requestBody);
	}

	public SearchResponseHandler byTerm(String filterTermKey, String filterTermValue) throws IOException {
		String requestBody = String.format(SERCH_BY_TERM, size, filterTermKey, filterTermValue);
		return search(requestBody);
	}
	
	public SearchResponseHandler byWildcard(String filterWildCardKey, String filterWildCardValue) throws IOException {
		String requestBody = String.format(SERCH_BY_WILDCARD, size, filterWildCardKey, filterWildCardValue);
		return search(requestBody);
	}

	public SearchResponseHandler byRange(String term, Map<String, Object> rangeParameters)
			throws IOException, JsonProcessingException {
		String requestBody = String.format(SERCH_BY_RANGE, size, term, new ObjectMapper().writeValueAsString(rangeParameters));
		return search(requestBody);
	}

	private SearchResponse scroll(String scrollId) throws IOException {
		return client.post("/_search/scroll", String.format("{\"scroll\":\"1m\",\"scroll_id\":\"%s\"}", scrollId),
				SearchResponse.class, true);
	}

	public Search query(int size) {
		return new Search(client, indexName, documentName, size, scroll);
	}

	public Search query(boolean scroll) {
		return new Search(client, indexName, documentName, size, scroll);
	}

	public static void main(String[] args) throws JsonProcessingException {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("foo", "bar");
		m.put("foo1", 12);
		final ObjectMapper mapper = new ObjectMapper();
		String result = mapper.writeValueAsString(m);
		System.out.println(result);

	}

}
