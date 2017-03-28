package il.co.topq.elastic.endpoint;

import il.co.topq.elastic.ESRest;

public class Document {

	private final ESRest client;

	private final String indexName;

	private final String documentName;

	public Document(ESRest client, String indexName, String documentName) {
		super();
		this.client = client;
		this.indexName = indexName;
		this.documentName = documentName;
	}

	public Query query() {
		return new Query(client, indexName, documentName, 100, true);
	}

	public Add add() {
		return new Add(client, indexName, documentName);
	}
	
	public Update update(){
		return new Update(client, indexName, documentName);
	}

	public Delete delete() {
		return new Delete(client, indexName, documentName);
	}
	
	public Aggs aggs(){
		return new Aggs(client, indexName, documentName);
	}

}
