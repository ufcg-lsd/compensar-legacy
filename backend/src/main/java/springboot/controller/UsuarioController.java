package springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import org.springframework.http.MediaType;
import springboot.model.Usuario;
import springboot.service.UsuarioService;

@Controller
@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
@Api(value = "UsuariosControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;

	@ApiOperation("Permite apagar um usuário do sistema.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
	@RequestMapping(value = "/usuario/{email}", method = RequestMethod.DELETE)
	public ResponseEntity<Usuario> delete(@PathVariable("email") String email) {
		Usuario usuario = usuarioService.delete(email);
		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}

	@ApiOperation("Permite atualizar um usuário do sistema. Requer que o corpo do request contenha um objeto com os atributos de um usuário.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
	@RequestMapping(value = "/usuario/", method = RequestMethod.PUT)
	public ResponseEntity<Usuario> update(@RequestAttribute(name="usuario") Usuario oldUsuario, @RequestBody Usuario novoUsuario) {
		Usuario updatedUsuario = usuarioService.update(new Usuario(oldUsuario.getNome(), novoUsuario.getIdade(), novoUsuario.getNomeInstituicao(), novoUsuario.getCargo(), novoUsuario.getCidade(), oldUsuario.getEmail(),true), oldUsuario.getEmail());
		return new ResponseEntity<Usuario>(updatedUsuario, HttpStatus.OK);
	}

	@ApiOperation("Fornece um array de objetos do tipo usuario registrados.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
	@RequestMapping(value = "/usuario", method = RequestMethod.GET)
	public List<Usuario> getAll() {
		return usuarioService.getAll();
	}

	@ApiOperation("Recupera um usuario com específico email.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
	@RequestMapping(value = "/usuario/{email}", method = RequestMethod.GET)
	public Usuario getByEmail(@PathVariable("email") String email) {
		return usuarioService.getById(email);
	}

	@ApiOperation("Recupera informaçoes do usuario atual.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
	@RequestMapping(value = "/usuario/", method = RequestMethod.GET)
	public Usuario getActualUser(@RequestAttribute(name="usuario") Usuario usuario) {
		return usuarioService.save(new Usuario(usuario.getNome(), usuario.getIdade(), usuario.getNomeInstituicao(), usuario.getCargo(), usuario.getCidade(), usuario.getEmail(), true));
	}
	/*
	 * @RequestMapping(value = "/usuario/search/{nome}", method = RequestMethod.GET)
	 * public Usuario searchByNome(@PathVariable("nome") String nome) { return
	 * usuarioService.pesquisarPorNome(nome); }
	 */

}
