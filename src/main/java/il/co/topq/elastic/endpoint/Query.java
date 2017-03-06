package il.co.topq.elastic.endpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import il.co.topq.elastic.ESRest;
import il.co.topq.elastic.response.query.QueryResponse;
import il.co.topq.elastic.response.query.QueryResponseHandler;


public class Query {

	private final ESRest client;
	
	private final String indexName;

	private final String documentName;

	private final int size;

	private final boolean scroll;

	public Query(ESRest client, String indexName, String documentName, int size, boolean scroll) {
		this.client = client;
		this.indexName = indexName;
		this.documentName = documentName;
		this.size = size;
		this.scroll = scroll;
	}
	
	public QueryResponseHandler byQuery(String query) throws IOException{
		return null;
	}

	public QueryResponseHandler byTerm(String filterTermKey, String filterTermValue) throws IOException {
		String requestBody = String.format("{\"size\":%d,\"query\": {\"term\" : { \"%s\" : \"%s\" }  } }", size,
				filterTermKey, filterTermValue);
		
		QueryResponse response = client.post("/" + indexName + "/" + documentName + "/_search?scroll=1m", requestBody,
				QueryResponse.class, true);

		final List<QueryResponse> responses = new ArrayList<QueryResponse>();
		while (response.getHits().getHits().size() > 0) {
			responses.add(response);
			response = scroll(response.getScrollId());
		}
		return new QueryResponseHandler(responses);
	}

	private QueryResponse scroll(String scrollId) throws IOException {
		return client.post("/_search/scroll", String.format("{\"scroll\":\"1m\",\"scroll_id\":\"%s\"}", scrollId),
				QueryResponse.class, true);
	}

	public Query query(int size) {
		return new Query(client, indexName, documentName, size, scroll);
	}

	public Query query(boolean scroll) {
		return new Query(client, indexName, documentName, size, scroll);
	}

}
