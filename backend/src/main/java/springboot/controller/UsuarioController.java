package springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import springboot.model.Usuario;
import springboot.service.UsuarioService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
public class UsuarioController {
	
	@Autowired
	UsuarioService usuarioService;
	
	@RequestMapping(value = "/usuario", method = RequestMethod.POST)
	public Usuario save(@RequestBody Usuario usuario) {
		return usuarioService.save(usuario);
	}
	
	@RequestMapping(value = "/usuario/{email}", method = RequestMethod.DELETE)
	public ResponseEntity<Usuario> delete(@PathVariable("email") String email) {
		Usuario usuario = usuarioService.delete(email);
		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/usuario/{email}", method = RequestMethod.PUT)
	public ResponseEntity<Usuario> update(@PathVariable("email") String email, @RequestBody Usuario usuario) {
		Usuario updatedUsuario = usuarioService.update(usuario, email);
		return new ResponseEntity<Usuario>(updatedUsuario, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/usuario", method = RequestMethod.GET)
	public List<Usuario> getAll() {
		return usuarioService.getAll();
	}

	/*
	@RequestMapping(value = "/usuario/search/{nome}", method = RequestMethod.GET)
	public Usuario searchByNome(@PathVariable("nome") String nome) {
		return usuarioService.pesquisarPorNome(nome);
	}
	*/
	
}
