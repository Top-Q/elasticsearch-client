package il.co.topq.elastic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import il.co.topq.elastic.model.Post;

//@Ignore
public class TestDocument extends AbstractCreateRemoveIndexTestCase {

	
	@Test
	public void testAddDocument() throws IOException {
		Post post0 = new Post();
		post0.setId(555);
		post0.setOp("Itai");
		post0.setPoints(100);
		post0.setSubreddit("all");
		client.index(INDEX).document(DOC).add().single("100", post0);
		sleep(1);
		List<Post> posts = client.index(INDEX).document(DOC).search().byTerm("id", "555").asClass(Post.class);
		Assert.assertNotNull(posts);
		Assert.assertEquals(1, posts.size());
		Post post1 = posts.get(0);
		Assert.assertEquals(post0.getId(), post1.getId());
		Assert.assertEquals(post0.getPoints(), post1.getPoints());
		Assert.assertEquals(post0.getComments(), post1.getComments());
		Assert.assertEquals(post0.getSubreddit(), post1.getSubreddit());
	}
	
	@Test
	public void testDeleteDocument() throws IOException, InterruptedException {
		Post post0 = new Post();
		post0.setId(555);
		post0.setOp("Itai");
		post0.setPoints(100);
		post0.setSubreddit("all");
		client
			.index(INDEX)
			.document(DOC)
			.add()
			.single("100", post0);
		
		Map<String, Object> response = client.index(INDEX).document(DOC).delete().single("100");
		Assert.assertEquals("deleted", response.get("result").toString());
	}
	
	@Test
	public void testUpdateSingleDocument() throws IOException {
		Post post0 = new Post();
		post0.setId(555);
		post0.setOp("Itai");
		post0.setPoints(100);
		post0.setSubreddit("all");
		client.index(INDEX).document(DOC).add().single("100", post0);
		sleep(1);
		post0.setPoints(200);
		client.index(INDEX).document(DOC).update().single("100", post0);
		sleep(1);
		List<Post> posts = client.index(INDEX).document(DOC).search().byTerm("id", "555").asClass(Post.class);
		Post post1 = posts.get(0);
		Assert.assertEquals(200, post1.getPoints());
	}
	
	@Test
	public void testAddBulkDocuments() throws IOException {
		List<Post> posts = new ArrayList<Post>();
		Post post0 = new Post();
		post0.setId(555);
		post0.setOp("bulk");
		post0.setPoints(100);
		post0.setSubreddit("all");
		posts.add(post0);
		
		post0 = new Post();
		post0.setId(666);
		post0.setOp("bulk");
		post0.setPoints(100);
		post0.setSubreddit("all");
		posts.add(post0);
		
		Map<String, Object> response = client.index(INDEX).document(DOC).add().bulk(new String[]{"100","200"}, posts);
		Assert.assertFalse((Boolean)response.get("errors"));
		sleep(1);
		final List<Post> postsResponse = client.index(INDEX).document(DOC).search().byTerm("op", "bulk").asClass(Post.class);
		Post post1 = postsResponse.get(0);
		Assert.assertEquals(100, post1.getPoints());
		post1 = postsResponse.get(1);
		Assert.assertEquals(100, post1.getPoints());

	}

	
	@Test
	public void testUpdateBulkDocuments() throws IOException {
		List<Post> posts = new ArrayList<Post>();
		Post post0 = new Post();
		post0.setId(555);
		post0.setOp("bulk");
		post0.setPoints(100);
		post0.setSubreddit("all");
		posts.add(post0);
		
		post0 = new Post();
		post0.setId(666);
		post0.setOp("bulk");
		post0.setPoints(100);
		post0.setSubreddit("all");
		posts.add(post0);
		
		client.index(INDEX).document(DOC).add().bulk(new String[]{"100","200"}, posts);
		sleep(1);
		posts.get(0).setPoints(200);
		posts.get(1).setPoints(200);
		client.index(INDEX).document(DOC).update().bulk(new String[]{"100","200"}, posts);
		sleep(1);
		
		final List<Post> postsResponse = client.index(INDEX).document(DOC).search().byTerm("op", "bulk").asClass(Post.class);
		Post post1 = postsResponse.get(0);
		Assert.assertEquals(200, post1.getPoints());
		post1 = postsResponse.get(1);
		Assert.assertEquals(200, post1.getPoints());

	}


}
