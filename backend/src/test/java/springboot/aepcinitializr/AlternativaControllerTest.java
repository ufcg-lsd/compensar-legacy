package springboot.aepcinitializr;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import springboot.controller.AlternativaController;

public class AlternativaControllerTest extends AepcApplicationTests {
	private MockMvc mockMvc;
	
	@MockBean
	private AlternativaController alternativaController;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;	
	
	
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(alternativaController).build();
		
		
	}
	
	@Test
	public void testDELETEQuestaoSubj() throws Exception {
	    this.mockMvc.perform(MockMvcRequestBuilders
	            .delete("/api/alternativa/4")
	            .contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(MockMvcResultMatchers.status().isOk());
	}	

	@Test
	public void testGETAllQuestaoSubj() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				.get("/api/alternativa"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@After
	public void tearDown() {
	  JdbcTestUtils.deleteFromTables(jdbcTemplate, "usuario");
	}
	

}
