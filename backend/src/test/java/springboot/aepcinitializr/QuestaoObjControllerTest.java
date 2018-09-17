package springboot.aepcinitializr;

import java.util.ArrayList;
import java.util.List;

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

import springboot.controller.QuestaoObjetivaController;
import springboot.model.Alternativa;
import springboot.model.QuestaoObjetiva;

public class QuestaoObjControllerTest extends AepcApplicationTests{
	private MockMvc mockMvc;
	
	@MockBean
	private QuestaoObjetivaController questaoObjController;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;	
	
	private QuestaoObjetiva questaoObj;
	private List<Alternativa> alternativas;
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(questaoObjController).build();
		
		this.alternativas = new ArrayList<>();
		this.alternativas.add(new Alternativa("2",true));
		this.alternativas.add(new Alternativa("3",false));
		
		this.questaoObj = new QuestaoObjetiva("objetiva", "o quadrado de PI elevado ao cubo é:", "ENEM", null, null,alternativas);     

	}
	

	@Test
	public void testPOSTQuestaoObj() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/questaoObj")
				  .content(asJsonString(this.questaoObj))
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
	public void testDELETEQuestaoObj() throws Exception {
	    this.mockMvc.perform(MockMvcRequestBuilders
	            .delete("/api/questaoObj/1")
	            .contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(MockMvcResultMatchers.status().isOk());
	}	

	@Test
	public void testGETAllQuestaoObj() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				.get("/api/questaoObj"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	
	@After
	public void tearDown() {
	  JdbcTestUtils.deleteFromTables(jdbcTemplate, "questao");
	}
	
	
}
