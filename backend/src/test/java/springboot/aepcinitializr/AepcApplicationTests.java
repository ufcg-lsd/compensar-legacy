package springboot.aepcinitializr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import springboot.AepcApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AepcApplication.class)
@TestPropertySource(locations="classpath:test.properties")
public class AepcApplicationTests {

	@Test
	public void contextLoads() {
	}

}
