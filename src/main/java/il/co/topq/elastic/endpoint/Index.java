package il.co.topq.elastic.endpoint;

import java.io.IOException;
import java.util.Map;

import il.co.topq.elastic.ESRest;
import il.co.topq.elastic.response.generic.GenericResponseHandler;

public class Index {

	private final ESRest client;

	private final String indexName;

	public Index(ESRest client, String name) {
		this.client = client;
		this.indexName = name;
	}

	public boolean isExists() throws IOException {
		return client.head("/" + indexName, true) == 200;
	}

	public Index create(String settings) throws IOException {
		client.put("/" + indexName, settings, Map.class, true);
		return this;
	}

	public Index delete() throws IOException {
		client.delete("/" + indexName, Map.class, true);
		return this;
	}

	public GenericResponseHandler stats() throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, Object> response = client.get("/" + indexName + "/_stats", Map.class, true);
		return new GenericResponseHandler(response);
	}

	public Document document(String documentName) {
		return new Document(client, indexName, documentName);
	}

}
