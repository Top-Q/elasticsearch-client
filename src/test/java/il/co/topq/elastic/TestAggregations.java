package il.co.topq.elastic;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import il.co.topq.elastic.model.Post;

public class TestAggregations extends AbstractCreateRemoveIndexTestCase {

	@Test
	public void testMaxAggWithResult() throws IOException {
		Post post = new Post();
		post.setId(555);
		post.setOp("Itai");
		post.setPoints(100);
		post.setSubreddit("all");
		client.index(INDEX).document(DOC).add().single("100", post);
		post = new Post();
		post.setId(777);
		post.setOp("Itai");
		post.setPoints(100);
		post.setSubreddit("all");
		client.index(INDEX).document(DOC).add().single("102", post);
		post = new Post();
		post.setId(666);
		post.setOp("Itai");
		post.setPoints(100);
		post.setSubreddit("all");
		client.index(INDEX).document(DOC).add().single("101", post);

		sleep(1);
		Double id = client.index(INDEX).document(DOC).aggs().max("id");
		Assert.assertEquals(777.0d, id, 0);

	}
	
	@Test
	public void testMaxAggNoneExistField() throws IOException {
		Post post = new Post();
		post.setId(555);
		post.setOp("Itai");
		post.setPoints(100);
		post.setSubreddit("all");
		client.index(INDEX).document(DOC).add().single("100", post);

		sleep(1);
		Double foo = client.index(INDEX).document(DOC).aggs().max("foo");
		Assert.assertNull(foo);

	}


}
