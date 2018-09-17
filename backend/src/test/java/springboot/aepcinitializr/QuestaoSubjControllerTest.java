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

import com.fasterxml.jackson.databind.ObjectMapper;

import springboot.controller.QuestaoSubjetivaController;

import springboot.model.QuestaoSubjetiva;

public class QuestaoSubjControllerTest extends AepcApplicationTests{
	private MockMvc mockMvc;
	
	@MockBean
	private QuestaoSubjetivaController questaoSubjController;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;	
	
	private QuestaoSubjetiva questaoSubj;
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(questaoSubjController).build();

		this.questaoSubj =  new QuestaoSubjetiva("subjetiva", "quanto é 2 + 2?", "PISA", null, null, "a resposta é 2 * 2");

	}
	
	@Test
	public void testPUTTQuestaoSubj() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/questaoSubj")
				  .content(asJsonString(this.questaoSubj))
				  .contentType(MediaType.APPLICATION_JSON)
				  .accept(MediaType.APPLICATION_JSON))
				  .andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}  
	
	@Test
	public void testDELETEQuestaoSubj() throws Exception {
	    this.mockMvc.perform(MockMvcRequestBuilders
	            .delete("/api/questaoSubj/4")
	            .contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(MockMvcResultMatchers.status().isOk());
	}	

	@Test
	public void testGETAllQuestaoSubj() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				.get("/api/questaoSubj"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	
	@After
	public void tearDown() {
	  JdbcTestUtils.deleteFromTables(jdbcTemplate, "questao");
	}
	
	
	
}
