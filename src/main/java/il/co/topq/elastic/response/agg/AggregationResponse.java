package il.co.topq.elastic.response.agg;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import il.co.topq.elastic.response.generic.Hits;
import il.co.topq.elastic.response.generic.Shards;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "_scroll_id", "took", "timed_out", "_shards", "hits", "aggregations" })
public class AggregationResponse {

	@JsonProperty("_scroll_id")
	private String scrollId;
	@JsonProperty("took")
	private Integer took;
	@JsonProperty("timed_out")
	private Boolean timedOut;
	@JsonProperty("_shards")
	private Shards shards;
	@JsonProperty("hits")
	private Hits hits;
	@JsonProperty("aggregations")
	private Map<String, Object> aggregations;

	@JsonProperty("took")
	public Integer getTook() {
		return took;
	}

	@JsonProperty("took")
	public void setTook(Integer took) {
		this.took = took;
	}

	@JsonProperty("timed_out")
	public Boolean getTimedOut() {
		return timedOut;
	}

	@JsonProperty("timed_out")
	public void setTimedOut(Boolean timedOut) {
		this.timedOut = timedOut;
	}

	@JsonProperty("_shards")
	public Shards getShards() {
		return shards;
	}

	@JsonProperty("_shards")
	public void setShards(Shards shards) {
		this.shards = shards;
	}

	
	
	@JsonProperty("hits")
	public Hits getHits() {
		return hits;
	}

	@JsonProperty("hits")
	public void setHits(Hits hits) {
		this.hits = hits;
	}

	@JsonProperty("aggregations")
	public Map<String, Object> getAggregations() {
		return aggregations;
	}

	@JsonProperty("aggregations")
	public void setAggregations(Map<String, Object> aggregations) {
		this.aggregations = aggregations;
	}

	@JsonProperty("_scroll_id")
	public String getScrollId() {
		return scrollId;
	}

	@JsonProperty("_scroll_id")
	public void setScrollId(String scrollId) {
		this.scrollId = scrollId;
	}

}
