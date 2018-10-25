package springboot.aepcinitializr;

import java.util.ArrayList;
import java.util.List;

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

import springboot.controller.QuestaoObjetivaController;
import springboot.model.Alternativa;
import springboot.model.QuestaoObjetiva;

public class QuestaoObjControllerTest extends AepcApplicationTests {
	private MockMvc mockMvc;

	@MockBean
	private QuestaoObjetivaController questaoObjController;



	private QuestaoObjetiva questaoObj;
	private QuestaoObjetiva updatedQuestaoObj;
	private List<Alternativa> alternativas;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(questaoObjController).build();

		this.alternativas = new ArrayList<>();
		this.alternativas.add(new Alternativa("2", true));
		this.alternativas.add(new Alternativa("3", false));

		this.questaoObj = new QuestaoObjetiva((long) 1,"objetiva", "o quadrado de PI elevado ao cubo é:", "ENEM", null, null,
				alternativas);
		this.updatedQuestaoObj = new QuestaoObjetiva((long) 2,"objetiva", "o quadrado de PI elevado ao quadrado é:", "ENEM",
				null, null, alternativas);
	}

	@Test
	public void testPOSTQuestaoObj() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/api/questaoObj").content(asJsonString(this.questaoObj))
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
	public void testDELETEQuestaoObj() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/questaoObj/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGETAllQuestaoObj() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/questaoObj"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGETQuestaoObjById() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/questaoObj/1")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testPUTQuestaoObj() throws Exception {
		String body = (new ObjectMapper()).valueToTree(updatedQuestaoObj).toString();
		this.mockMvc.perform(
				MockMvcRequestBuilders.put("/api/questaoObj/1").content(body).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}



}
