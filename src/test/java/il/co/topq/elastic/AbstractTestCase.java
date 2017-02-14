package il.co.topq.elastic;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;

public class AbstractTestCase {
	protected static final String SETTINGS = "{\"settings\": { \"index\": { \"number_of_shards\": 3, \"number_of_replicas\": 1 	}}}";

	protected static final String INDEX = "reddit";

	protected static final String DOC = "post";

	protected ESClient client;

	@Before
	public void setup() {
		client = new ESClient("localhost", 9200);
	}

	@After
	public void teardown() throws IOException {
		client.close();
	}

}
