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

	/**
	 * Receives two parameters: <br>
	 * <ul>
	 * <li>aggFileldName</li>
	 * <li>Aggregation type</li>
	 * <li>field</li>
	 * </ul>
	 */
	private final static String AGG_NO_QUERY = "{\"size\": 1,\"aggs\" : {\"%s\" : { \"%s\" : { \"field\" : \"%s\" } }}}";

	/**
	 * Receives three parameters: <br>
	 * <ul>
	 * <li>String query</li>
	 * <li>aggFileldName</li>
	 * <li>Aggregation type</li>
	 * <li>field</li>
	 * </ul>
	 */
	private final static String AGG_WITH_QUERY = "{\"size\": 1,\"query\": { \"bool\" : { \"must\" : { \"query_string\" : { \"query\" : \"%s\" } }} },\"aggs\" : {\"%s\" : { \"%s\" : { \"field\" : \"%s\" } }}}";

	public Aggs(ESRest client, String indexName, String documentName) {
		super();
		this.client = client;
		this.indexName = indexName;
		this.documentName = documentName;
	}

	private Double singleResultAgg(String aggregation, String field) throws IOException {
		return singleResultAgg(aggregation, field, null);
	}

	private Double singleResultAgg(String aggregation, String field, String queryString) throws IOException {
		final String aggFileldName = "my_agg";
		String requestBody;
		if (queryString == null || queryString.isEmpty()) {
			requestBody = String.format(AGG_NO_QUERY, aggFileldName, aggregation, field);
		} else {
			requestBody = String.format(AGG_WITH_QUERY, queryString, aggFileldName, aggregation, field);
		}
		AggregationResponse response = client.post("/" + indexName + "/" + documentName + "/_search?", requestBody,
				AggregationResponse.class, true);
		@SuppressWarnings("unchecked")
		final Object result = ((Map<String, String>) response.getAggregations().get(aggFileldName)).get("value");
		if (null == result) {
			return null;
		}
		return Double.parseDouble(String.valueOf((result)));

	}

	public Double max(String field, String queryString) throws IOException {
		return singleResultAgg("max", field, queryString);
	}

	public Double max(String field) throws IOException {
		return singleResultAgg("max", field);
	}

	public Double min(String field, String queryString) throws IOException {
		return singleResultAgg("min", field, queryString);
	}

	public Double min(String field) throws IOException {
		return singleResultAgg("min", field);
	}

	public Double avg(String field, String queryString) throws IOException {
		return singleResultAgg("avg", field);
	}

	public Double avg(String field) throws IOException {
		return singleResultAgg("avg", field);
	}

	public Double cardinality(String field, String queryString) throws IOException {
		return singleResultAgg("cardinality", field);
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
