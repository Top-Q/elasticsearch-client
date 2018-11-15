package il.co.topq.elastic;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import il.co.topq.elastic.model.Post;

public class TestMutipleNodes extends AbstractCreateRemoveIndexTestCase {

	@Override
	protected ESClient initClient() {
		return ESClient.builder().addClient("localhost", 9200).addClient("localhost", 9201).build();
	}

	@Test
	public void testHighAvailiablity() throws IOException, InterruptedException {
		addPost(555);
		System.out.println("Stop the main Elasticsearch node");
		Thread.sleep(10000);
		addPost(666);
		System.out.println("Restart the main Elasticsearch node and take down the secondery");
		Thread.sleep(10000);
		addPost(777);
	}

	private void addPost(int id) throws IOException {
		Post post0 = new Post();
		post0.setId(id);
		post0.setOp("Itai");
		post0.setPoints(100);
		post0.setSubreddit("all");
		client.index(INDEX).document(DOC).add().single("100", post0);
		sleep(1);
	}

}
