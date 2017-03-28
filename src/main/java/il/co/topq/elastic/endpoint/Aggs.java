package il.co.topq.elastic.endpoint;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import il.co.topq.elastic.ESRest;
import il.co.topq.elastic.response.agg.AggregationResponse;

public class Aggs {

	private final ESRest client;

	private final String indexName;

	private final String documentName;

	public Aggs(ESRest client, String indexName, String documentName) {
		super();
		this.client = client;
		this.indexName = indexName;
		this.documentName = documentName;
	}

	private Double singleResultAgg(String aggregation,String field) throws IOException {
		final String aggFileldName = "my_agg";
		String requestBody = String
				.format("{\"size\": 1,\"aggs\" : {\"%s\" : { \"%s\" : { \"field\" : \"%s\" } }}}",aggFileldName,aggregation, field);
		AggregationResponse response = client.post("/" + indexName + "/" + documentName + "/_search?", requestBody, AggregationResponse.class, true);		@SuppressWarnings("unchecked")
		final Object result = ((Map<String,String>)response.getAggregations().get(aggFileldName)).get("value");
		if (null == result) {
			return null;
		}
		return Double.parseDouble(String.valueOf((result)));
		
	}
	
	public Double max(String field) throws IOException {
		return singleResultAgg("max", field);
	}

	public Double min(String field) throws IOException {
		return singleResultAgg("min", field);
	}
	
	public Double avg(String field) throws IOException {
		return singleResultAgg("avg", field);
	}

	public Double cardinality(String field) throws IOException {
		return singleResultAgg("cardinality", field);
	}

	public List<Map<String, Object>> extendedStats(String field) {
		return null;
	}

	public List<Map<String, Object>> percentiles(String field) {
		return null;
	}

}
