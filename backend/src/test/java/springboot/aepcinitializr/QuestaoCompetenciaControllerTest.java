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

import springboot.controller.QuestaoCompetenciaController;
import springboot.model.QuestaoCompetencia;

public class QuestaoCompetenciaControllerTest extends AepcApplicationTests {
	private MockMvc mockMvc;

	@MockBean
	private QuestaoCompetenciaController questaoCompetenciaController;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private QuestaoCompetencia questaoCompetencia;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(questaoCompetenciaController).build();

		this.questaoCompetencia = new QuestaoCompetencia((long) 1, "coleta");

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

	@After
	public void tearDown() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "questao_competencia");
	}
}
