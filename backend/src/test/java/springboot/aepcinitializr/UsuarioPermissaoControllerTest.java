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

import springboot.controller.UsuarioPermissaoController;
import springboot.enums.PermissaoType;
import springboot.model.UsuarioPermissao;

public class UsuarioPermissaoControllerTest extends AepcApplicationTests {

	private MockMvc mockMvc;

	@MockBean
	private UsuarioPermissaoController usuarioPermissaoController;


	private UsuarioPermissao usuarioPermissao;
	private UsuarioPermissao usuarioPermissaoUpdated;
	private PermissaoType permissaoType;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(usuarioPermissaoController).build();

		this.usuarioPermissao = new UsuarioPermissao("prof@gmail.com", this.permissaoType.CRIA_QUESTOES);
		usuarioPermissaoUpdated = new UsuarioPermissao("prof@gmail.com", this.permissaoType.ALL);

	}

	@Test
	public void testPOSTUsuarioPermissao() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/api/usuario/permissao").content(asJsonString(this.usuarioPermissao))
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
	public void testGETAllUsuarioPermissao() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/usuario/permissao"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGETQuestaoUsuarioPermissaoById() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/usuario/permissao/" + this.usuarioPermissao.getEmail())
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testPUTUsuario() throws Exception {
		String body = (new ObjectMapper()).valueToTree(usuarioPermissaoUpdated).toString();
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/api/usuario/permissao/" + this.usuarioPermissao.getEmail())
						.content(body).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}


}
