package il.co.topq.elastic;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;


public class TestIndex extends AbstractTestCase{
	
	@Test
	public void testCreateAndDeleteIndex() throws IOException{
		Assert.assertFalse(client.index(INDEX).isExists());
		client.index(INDEX).create(SETTINGS);
		Assert.assertTrue(client.index(INDEX).isExists());
		client.index(INDEX).delete();
		Assert.assertFalse(client.index(INDEX).isExists());
	}
	
	
}
