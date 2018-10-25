package springboot.aepcinitializr;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import springboot.controller.AlternativaController;
import springboot.model.Alternativa;

public class AlternativaControllerTest extends AepcApplicationTests {
	private MockMvc mockMvc;
	
	@MockBean
	private AlternativaController alternativaController;
	

	private Alternativa alternativa;
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(alternativaController).build();
		
		this.alternativa = new Alternativa("nova", true);
	}
	
	@Test
	public void testDELETEQuestaoSubj() throws Exception {
	    this.mockMvc.perform(MockMvcRequestBuilders
	            .delete("/api/alternativa/1")
	            .contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(MockMvcResultMatchers.status().isOk());
	}	

	@Test
	public void testGETAllQuestaoSubj() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				.get("/api/alternativa"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void testGETAlternativaById() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/alternativa/1")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testPUTALternativa() throws Exception {
		String body = (new ObjectMapper()).valueToTree(alternativa).toString();
		this.mockMvc.perform(
				MockMvcRequestBuilders.put("/api/alternativa/1").content(body).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	

}
