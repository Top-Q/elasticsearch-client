package il.co.topq.elastic;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import il.co.topq.elastic.endpoint.Index;
import il.co.topq.elastic.response.generic.GenericResponseHandler;

public class ESClient implements Closeable {

	private final ESRest rest;

	public ESClient(String host, int port) {
		final List<RestClient> clients = new ArrayList<RestClient>();
		clients.add(RestClient.builder(new HttpHost(host, port, "http")).build());
		rest = new ESRest(clients);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	private ESClient(List<RestClient> clients) {
		rest = new ESRest(clients);
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
	
	public static class Builder {
		
		private List<RestClient> clients;
		
		private Builder() {
			clients = new ArrayList<RestClient>();
		}
		
		public Builder addClient(String host, int port) {
			RestClient client = RestClient.builder(new HttpHost(host, port, "http")).build();
			clients.add(client);
			return this;
		}
		
		public ESClient build() {
			if (clients.isEmpty()) {
				throw new IllegalArgumentException("Clients can't be null");
			}
			return new ESClient(clients);
		}
		
	}
}
