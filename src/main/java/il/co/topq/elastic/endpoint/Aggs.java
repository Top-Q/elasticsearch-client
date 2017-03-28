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

	@SuppressWarnings("unchecked")
	public Double max(String field) throws IOException {
		final String aggFileldName = "max_agg";
		String requestBody = String
				.format("{\"size\": 1,\"aggs\" : {\"%s\" : { \"max\" : { \"field\" : \"%s\" } }}}",aggFileldName, field);
		AggregationResponse response = client.post("/" + indexName + "/" + documentName + "/_search?", requestBody, AggregationResponse.class, true);
		final Object result = ((Map<String,String>)response.getAggregations().get(aggFileldName)).get("value");
		if (null == result) {
			return null;
		}
		return Double.parseDouble(String.valueOf((result)));
	}

	public String min(String field) {
		return null;
	}

	public int cardinality(String field) {
		return 0;
	}

	public List<Map<String, Object>> extendedStats(String field) {
		return null;
	}

	public List<Map<String, Object>> percentiles(String field) {
		return null;
	}

}
