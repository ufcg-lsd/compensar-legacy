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


import springboot.controller.UsuarioController;
import springboot.model.Usuario;



public class UsuarioControllerTest extends AepcApplicationTests{
	

	private MockMvc mockMvc;
	
	@MockBean
	private UsuarioController usuarioController;
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;	
	
	private Usuario usuario;
	private Usuario usuarioUpdated;


	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
		this.usuario = new Usuario("Marcelo G D S Q Vitorino", "UFCG","marcelo.vitorino@gmail.com");
		this.usuarioUpdated = new Usuario("Marcelo G D S Q Vitorino", "USP","marcelo.vitorino@usp.com");
	}
	
	@Test
	public void testGETAllUsuario() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				.get("/api/usuario"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	
	@Test
	public void testPOSTUsuario() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/usuario")
				  .content(asJsonString(this.usuario))
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
	public void testDELETEUsuario() throws Exception {
	    this.mockMvc.perform(MockMvcRequestBuilders
	            .delete("/api/usuario/"+ this.usuario.getEmail())
	            .contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(MockMvcResultMatchers.status().isOk());
	}	

	@Test
	public void testGETQuestaoUsuarioById() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/usuario/"+ this.usuario.getEmail())
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testPUTUsuario() throws Exception {
		String body = (new ObjectMapper()).valueToTree(usuarioUpdated).toString();
		this.mockMvc.perform(
				MockMvcRequestBuilders.put("/api/usuario/"+ this.usuario.getEmail()).content(body).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@After
	public void tearDown() {
	  JdbcTestUtils.deleteFromTables(jdbcTemplate, "usuario");
	}
	

	
	
}