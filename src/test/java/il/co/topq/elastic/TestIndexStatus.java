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
		Map<String,Object> response = client.index(INDEX).stats().asMap();
		Assert.assertNotNull(response);
		Assert.assertEquals(0, ((Map<String,Object>)response.get("_shards")).get("failed"));
	}
	
}
