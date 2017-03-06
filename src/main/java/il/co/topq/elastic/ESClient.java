package il.co.topq.elastic;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import il.co.topq.elastic.endpoint.Index;
import il.co.topq.elastic.response.generic.GenericResponseHandler;

public class ESClient implements Closeable {

	private final ESRest rest;

	public ESClient(String host, int port) {
		rest = new ESRest(RestClient.builder(new HttpHost(host, port, "http")).build());
	}

	@Override
	public void close() throws IOException {
		if (rest != null) {
			rest.close();

		}
	}

	public Index index(String name) {
		return new Index(rest, name);
	}
	
	public GenericResponseHandler nodes() throws IOException{
		@SuppressWarnings("unchecked")
		Map<String,Object> response = rest.get("_nodes", Map.class, true);
		return new GenericResponseHandler(response);
	}
	
	public GenericResponseHandler stats() throws IOException{
		@SuppressWarnings("unchecked")
		Map<String,Object> response = rest.get("_stats", Map.class, true);
		return new GenericResponseHandler(response);
	}
	
}
