package il.co.topq.elastic.response.generic;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Total {

    @JsonProperty("value")
    private Long value;

    @JsonProperty("relation")
    private String relation;

    @JsonProperty("value")
    public Long getValue() {
        return value;
    }
    @JsonProperty("value")
    public void setValue(Long value) {
        this.value = value;
    }

    @JsonProperty("relation")
    public String getRelation() {
        return relation;
    }

    @JsonProperty("relation")
    public void setRelation(String relation) {
        this.relation = relation;
    }
}
