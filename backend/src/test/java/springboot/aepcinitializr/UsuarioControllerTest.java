package springboot.aepcinitializr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import springboot.controller.UsuarioController;
import springboot.model.Usuario;

public class UsuarioControllerTest extends AepcApplicationTests {

	private MockMvc mockMvc;

	@MockBean
	private UsuarioController usuarioController;

	private Usuario usuario;
	private Usuario usuarioUpdated;

	@BeforeEach
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
		this.usuario = new Usuario("marcelo", 19, "UFCG", "Aluno", "Remígio", "marcelo@gmail.com", true);
		this.usuarioUpdated = new Usuario("marcelo Gabriel Vitorino", 19, "UFCG", "Aluno", "Remígio",
				"marcelo@gmail.com", true);
	}

	@Test
	public void testGETUsuario() throws Exception {
	    this.mockMvc.perform(MockMvcRequestBuilders.get("/api/usuario")
	            .requestAttr("usuario", this.usuario)  // Simulando o usuário logado
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGETAllUsuario() throws Exception {
	    this.mockMvc.perform(MockMvcRequestBuilders.get("/api/usuario/all")
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(MockMvcResultMatchers.status().isOk());
	}

	// TODO: Comentado pois essa função não existe
	// @Test
	// public void testPOSTUsuario() throws Exception {
	//     this.mockMvc.perform(MockMvcRequestBuilders.post("/api/usuario")
	//             .content(asJsonString(this.usuario)) 
	//             .contentType(MediaType.APPLICATION_JSON)
	//             .accept(MediaType.APPLICATION_JSON)
	//             .requestAttr("usuario", this.usuario)) // Simulando o usuário logado
	//             .andExpect(MockMvcResultMatchers.status().isOk());
	// }


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
	 public void testDELETEUsuario() throws Exception {
	 	this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/usuario/" + this.usuario.getEmail())
	 			.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
	 }

	@Test
	public void testGETQuestaoUsuarioById() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/usuario/" + this.usuario.getEmail())
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testPUTUsuario() throws Exception {
		String body = asJsonString(usuarioUpdated);
		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/usuario")
				.requestAttr("usuario", this.usuario)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
