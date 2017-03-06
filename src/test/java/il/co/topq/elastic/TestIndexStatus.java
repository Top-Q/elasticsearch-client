package il.co.topq.elastic;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TestIndexStatus extends AbstractCreateRemoveIndexTestCase {
	
	@Test
	@SuppressWarnings("unchecked")
	public void testEsStats() throws IOException{
		sleep(1);
		Map<String,Object> response = client.index(INDEX).stats().asMap();
		Assert.assertNotNull(response);
		Assert.assertEquals(0, ((Map<String,Object>)response.get("_shards")).get("failed"));
		int totalShards = (Integer) ((Map<String, Object>) response.get("_shards")).get("total");
		Assert.assertTrue(totalShards > 0);
		int successfulShards = (Integer) ((Map<String, Object>) response.get("_shards")).get("successful");
		Assert.assertEquals(totalShards, successfulShards);

	}
	
}
