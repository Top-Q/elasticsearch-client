package il.co.topq.elastic;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ESRest implements Closeable {

	protected static final ObjectMapper mapper = new ObjectMapper();

	protected List<RestClient> clients;

	public ESRest(List<RestClient> clients) {
		Objects.requireNonNull(clients,"Clients can't be null");
		if (clients.isEmpty()) {
			throw new IllegalArgumentException("Client list can't be empty");
		}
		this.clients = Collections.synchronizedList(clients);
	}

	public <T> T get(String resource, Class<T> responseClass, boolean assertSuccess) throws IOException {
		final Response response = performRequest("GET", resource, Collections.singletonMap("pretty", "true"));
		if (assertSuccess) {
			assertSuccess(response);
		}
		return mapper.readValue(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), responseClass);
	}

	public <T> T post(String resource, String body, Class<T> responseClass, boolean assertSuccess) throws IOException {
		final Response response = performRequest("POST", resource, Collections.singletonMap("pretty", "true"),
				new NStringEntity(body, ContentType.APPLICATION_JSON));
		if (assertSuccess) {
			assertSuccess(response);
		}
		return mapper.readValue(IOUtils.toString(response.getEntity().getContent()), responseClass);
	}

	/**
	 * 
	 * @param resource
	 * @param assertSuccess
	 * @return Status code
	 * @throws IOException
	 */
	public int head(String resource, boolean assertSuccess) throws IOException {
		Response response = performRequest("HEAD", resource, Collections.singletonMap("pretty", "true"));
		return response.getStatusLine().getStatusCode();
	}

	public <T> T put(String resource, String body, Class<T> responseClass, boolean assertSuccess) throws IOException {
		final HttpEntity entity = new NStringEntity(body, ContentType.APPLICATION_JSON);
		final Response response = performRequest("PUT", resource, Collections.<String, String>emptyMap(), entity);
		if (assertSuccess) {
			assertSuccess(response);
		}
		return mapper.readValue(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), responseClass);
	}

	private Response performRequest(String method, String endpoint, Map<String, String> headers) throws IOException {
		return performRequest(method, endpoint, headers, null);
	}

	private Response performRequest(String method, String endpoint, Map<String, String> headers, HttpEntity entity)
			throws IOException {
		IOException savedException = null;
		int clientIndex = 0;
		for (; clientIndex < clients.size(); clientIndex++) {
			try {
				final Response response = clients.get(clientIndex).performRequest(method, endpoint, headers, entity);
				if (clientIndex != 0) {
					synchronized (clients) {
						// If the client is not at the top of the list, we should move
						// it since it is the successful one.
						clients = moveToTop(clients, clients.get(clientIndex));
					}
				}
				return response;
			} catch (IOException e) {
				// TODO: Save all the exceptions and throw them like in TestNG
				// soft assert:
				// https://github.com/cbeust/testng/blob/master/src/main/java/org/testng/asserts/SoftAssert.java
				savedException = e;
			}

		}
		throw savedException;
	}

	private static <T> List<T> moveToTop(List<T> items, T input) {
		int index = items.indexOf(input);
		List<T> copy;
		if (index >= 0) {
				copy = new ArrayList<T>(items.size());
				copy.add(items.get(index));
				copy.addAll(items.subList(0, index));
				copy.addAll(items.subList(index + 1, items.size()));
		} else {
			return items;
		}
		return copy;
	}

	public <T> T delete(String resource, Class<T> responseClass, boolean assertSuccess) throws IOException {
		final Response response = performRequest("DELETE", resource, Collections.<String, String>emptyMap());
		if (assertSuccess) {
			assertSuccess(response);
		}
		return mapper.readValue(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), responseClass);
	}

	private void assertSuccess(Response response) throws IOException {
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new IOException("Return status is " + response.getStatusLine().getStatusCode());
		}
	}

	@Override
	public void close() throws IOException {
		if (null == clients || clients.isEmpty()) {
			return;
		}
		for (RestClient client : clients) {
			if (client != null) {
				client.close();
			}
		}
	}

}
