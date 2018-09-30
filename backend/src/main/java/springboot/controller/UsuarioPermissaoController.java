package springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springboot.model.Usuario;
import springboot.model.UsuarioPermissao;
import springboot.service.UsuarioPermissaoService;


@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
@Api(value = "UsuariosPermissoesControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioPermissaoController {
	
	@Autowired
	UsuarioPermissaoService usuarioPermissaoService;
	
	@ApiOperation("Permite registrar uma permissão a um usuário no sistema. Requer que o corpo do request contenha um objeto com os campos: email e permissao.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = UsuarioPermissao.class) })
	@RequestMapping(value = "/usuario/permissao", method = RequestMethod.POST)
	public UsuarioPermissao savePermissao(@RequestBody UsuarioPermissao usuarioPermissao) {
		return usuarioPermissaoService.savePermissao(usuarioPermissao);
	}
	
	@ApiOperation("Permite atualizar uma permissão a um usuário do sistema. Requer que o corpo do request contenha um objeto com os atributos de um UsuárioPermissão.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
	@RequestMapping(value = "/usuario/permissao/{email}", method = RequestMethod.PUT)
	public ResponseEntity<UsuarioPermissao> update(@PathVariable("email") String email, @RequestBody UsuarioPermissao usuarioPermissao) {
		UsuarioPermissao updatedUsuarioPermissao = usuarioPermissaoService.update(usuarioPermissao, email);
		return new ResponseEntity<UsuarioPermissao>(updatedUsuarioPermissao, HttpStatus.OK);
	}
	
	@ApiOperation("Fornece um array de objetos do tipo de permissao de usuario registrados.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = UsuarioPermissao.class) })
	@RequestMapping(value = "/usuario/permissao", method = RequestMethod.GET)
	public List<UsuarioPermissao> getAll() {
		return usuarioPermissaoService.getAll();
	}

	@ApiOperation("Recupera a permissão de um usuário com específico email.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = UsuarioPermissao.class) })
	@RequestMapping(value = "/usuario/permissao/{email}", method = RequestMethod.GET)
	public UsuarioPermissao getByEmail(@PathVariable("email") String email) {
		return usuarioPermissaoService.getById(email);
	}

}
