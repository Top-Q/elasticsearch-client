package il.co.topq.elastic;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import il.co.topq.elastic.model.Post;

@Ignore
public class TestMutipleNodes extends AbstractCreateRemoveIndexTestCase {

	@Override
	protected ESClient initClient() {
		return ESClient.builder().addClient("localhost", 9200).addClient("localhost", 9201).build();
	}

	@Test
	public void testHighAvailiablity() throws IOException, InterruptedException {
		addPostAndVerify(555);
		System.out.println("Stop the main Elasticsearch node");
		// sleep(10);
		addPostAndVerify(666);
		System.out.println("Restart the main Elasticsearch node and take down the secondery");
		// sleep(10);
		addPostAndVerify(777);
	}

	private void addPostAndVerify(int id) throws IOException {
		if (!client.index(INDEX).isExists()) {
			client.index(INDEX).create(SETTINGS);
		}
		Post post0 = new Post();
		post0.setId(id);
		post0.setOp("Itai");
		post0.setPoints(100);
		post0.setSubreddit("all");
		client.index(INDEX).document(DOC).add().single(id + "", post0);
		sleep(1);
		List<Post> posts = client.index(INDEX).document(DOC).search().byTerm("id", id + "").asClass(Post.class);
		Assert.assertNotNull(posts);
		Assert.assertEquals(1, posts.size());
	}

}
