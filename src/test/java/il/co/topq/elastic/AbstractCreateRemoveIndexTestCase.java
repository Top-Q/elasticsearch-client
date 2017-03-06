package il.co.topq.elastic;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;

public abstract class AbstractCreateRemoveIndexTestCase extends AbstractTestCase{
	
	@Before
	public void setUp() throws IOException{
		if (!client.index(INDEX).isExists()){
			client.index(INDEX).create(SETTINGS);
		}
	}
	
	@After
	public void tearDown() throws IOException{
		if (client.index(INDEX).isExists()){
			client.index(INDEX).delete();
		}
	}
	
}
