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

import springboot.controller.QuestaoCompetenciaController;
import springboot.enums.CompetenciaType;
import springboot.model.QuestaoCompetencia;

public class QuestaoCompetenciaControllerTest extends AepcApplicationTests {
	private MockMvc mockMvc;

	@MockBean
	private QuestaoCompetenciaController questaoCompetenciaController;

	private QuestaoCompetencia questaoCompetencia;
	
	private CompetenciaType competenciaType;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(questaoCompetenciaController).build();

		this.questaoCompetencia = new QuestaoCompetencia("1", this.competenciaType.COLETA);

	}

	@Test
	public void testPOSTQuestaoCompetencia() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/api/questaoCompetencia").content(asJsonString(this.questaoCompetencia))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
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
	public void testDELETEQuestaoCompetencia() throws Exception {
		this.mockMvc
				.perform(
						MockMvcRequestBuilders
								.delete("/api/questaoCompetencia/" + this.questaoCompetencia.getId() + "/"
										+ this.questaoCompetencia.getCompetencia())
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGETAllQuestaoCompetencia() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/questaoCompetencia"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
