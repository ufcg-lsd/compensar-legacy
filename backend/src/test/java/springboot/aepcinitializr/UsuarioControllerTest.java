package springboot.aepcinitializr;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import springboot.controller.UsuarioController;
import springboot.model.Usuario;
import springboot.service.UsuarioService;
import springfox.documentation.spring.web.json.Json;



public class UsuarioControllerTest extends AepcApplicationTests{
	

	private MockMvc mockMvc;
	
	@MockBean
	private UsuarioController usuarioController;
	
	@Autowired
	private UsuarioService usuarioService;
	
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
	public void testGETbyEmail() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/usuario/marcelo.vitorino@gmail.com"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("string"))	
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void testDELETEUsuario() throws Exception {
	    this.mockMvc.perform(MockMvcRequestBuilders
	            .delete("/api/usuario/marcelo.vitorino@gmail.com")
	            .contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(MockMvcResultMatchers.status().isOk());
	}	
	
		/**
	@Test
	public void nao_deve_permitir_salvar_cliente_pf_com_nome_cpf_duplicado() throws Exception {

	    this.mockMvc.perform(MockMvcRequestBuilders.post("/api/cliente/pessoafisica/post")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("teste"))
	            .andDo(print())
	            .andExpect(status().is2xxSuccessful());
	}


	@Test
	public void testPUTUsuario() throws Exception {
		Usuario usuario = (Usuario) usuarioService.save(new Usuario("Marcelo G D S Q Vitorino", "UFCG","marcelo.vitorino@gmail.com"));
		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/usuario/" + usuario.getEmail())).andExpect(MockMvcResultMatchers.redirectedUrl("/usuario"));
	}

	*/
	
	@After
	public void tearDown() {
	  JdbcTestUtils.deleteFromTables(jdbcTemplate, "usuario");
	}
	
	
	
}
