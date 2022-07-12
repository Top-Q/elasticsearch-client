package il.co.topq.elastic.endpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import il.co.topq.elastic.ESRest;

public class Add {

	protected static final ObjectMapper mapper = new ObjectMapper();
	
	private final ESRest client;
	
	private String indexName;
	
	private String documentName;

	public Add(ESRest client, String indexName, String documentName) {
		this.client = client;
		this.indexName = indexName;
		this.documentName = documentName;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> bulk(String[] ids, List<?> objects) throws IOException{
		if (ids.length != objects.size()) {
			throw new IllegalArgumentException("Number of ids have to be equals to number of objects");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0 ; i < ids.length ; i++){
			sb.append(String.format("{ \"create\": {\"_id\":\"%s\"}\n", ids[i]));
			sb.append(mapper.writeValueAsString(objects.get(i))).append("\n");
		}
		return client.post(String.format("/%s/_bulk", indexName),sb.toString(),Map.class,true);
	}
	
	public Map<String,Object> single(String id, Object object) throws IOException{
		List<Object> objects = new ArrayList<>();
		objects.add(object);
		return bulk(new String[]{id}, objects);
	}

}
