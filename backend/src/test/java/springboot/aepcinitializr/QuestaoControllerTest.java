package springboot.aepcinitializr;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import springboot.controller.QuestaoController;
import springboot.enums.CompetenciaType;
import springboot.model.Alternativa;
import springboot.model.EnunciadoCompetencia;
import springboot.model.Questao;

public class QuestaoControllerTest extends AepcApplicationTests{
	private MockMvc mockMvc;
	
	@MockBean
	private QuestaoController questaoController;
	

	
	private Questao questao;
	
	private Questao updatedQuestao;

	private List<Alternativa> alternativas;
	private Set<CompetenciaType> competencias;
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(questaoController).build();
		
		this.alternativas = new ArrayList<>();
		this.competencias = new HashSet<>();
		
		this.alternativas.add(new Alternativa("2", true));
		this.alternativas.add(new Alternativa("3", false));
		this.competencias.add(CompetenciaType.COMP_COLETA);
		this.competencias.add(CompetenciaType.COMP_PARALELIZAÇÃO);
		

		this.questao =  new Questao("Subjetiva", "Álgebra", "quanto é 2 + 2?", "PISA", null, "a resposta é 2 * 2", alternativas, competencias);
		this.updatedQuestao = new Questao("Subjetiva",  "Álgebra", "quanto é 2 + 2?", "PISA", null, "a resposta é 2 + 2", alternativas, competencias);

	}
	
	@Test
	public void testPUTTQuestao() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/questao")
				  .content(asJsonString(this.questao))
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
	public void testDELETEQuestao() throws Exception {
	    this.mockMvc.perform(MockMvcRequestBuilders
	            .delete("/api/questao/1")
	            .contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(MockMvcResultMatchers.status().isOk());
	}	

	@Test
	public void testGETAllQuestao() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				.get("/api/questao"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void testGETQuestaoById() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/questao/1")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testPUTQuestao() throws Exception {
		String body = (new ObjectMapper()).valueToTree(updatedQuestao).toString();
		this.mockMvc.perform(
				MockMvcRequestBuilders.put("/api/questao/1").content(body).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	
}
