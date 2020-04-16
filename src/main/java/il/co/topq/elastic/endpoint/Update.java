package il.co.topq.elastic.endpoint;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import il.co.topq.elastic.ESRest;

public class Update {

	protected static final ObjectMapper mapper = new ObjectMapper();
	
	private final ESRest client;
	
	private String indexName;
	
	private String documentName;

	public Update(ESRest client, String indexName, String documentName) {
		this.client = client;
		this.indexName = indexName;
		this.documentName = documentName;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> single(String id, Object object) throws IOException{
		return client.put(String.format("/%s/%s/%s", indexName, documentName, id), mapper.writeValueAsString(object), Map.class, true);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> bulk(String[] ids, List<?> objects) throws IOException{
		if (ids.length != objects.size()) {
			throw new IllegalArgumentException("Number of ids have to be equals to number of objects");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0 ; i < ids.length ; i++){
			sb.append(String.format("{ \"update\": {\"_id\":\"%s\"}}\n", ids[i]));
			sb.append("{\"doc\": ");
			sb.append(mapper.writeValueAsString(objects.get(i)));
			sb.append("}").append("\n");
		}
		return client.post(String.format("/%s/%s/_bulk?refresh=wait_for", indexName,documentName),sb.toString(),Map.class,true);
	}
	
}
