package il.co.topq.elastic;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import il.co.topq.elastic.model.Post;

@Ignore
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
	@Test
	public void testMinAggWithResult() throws IOException {
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
		Double id = client.index(INDEX).document(DOC).aggs().min("id");
		Assert.assertEquals(555.0d, id, 0);

	}
	
	@Test
	public void testAvgAggWithResult() throws IOException {
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
		Double id = client.index(INDEX).document(DOC).aggs().avg("id");
		Assert.assertEquals(666.0d, id, 0);

	}
	
	@Test
	public void testCardinalityAggWithResult() throws IOException {
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
		Double id = client.index(INDEX).document(DOC).aggs().cardinality("id");
		Assert.assertEquals(3.0d, id, 0);

	}

	
	
}
