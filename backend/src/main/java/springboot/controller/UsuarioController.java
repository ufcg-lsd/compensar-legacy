package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import springboot.model.Usuario;
import springboot.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Atualiza as informações do usuário logado.
     *
     * @param oldUsuario  O usuário logado atualmente (preenchido automaticamente
     *                    pelo Spring).
     * @param novoUsuario As novas informações do usuário, exceto o email.
     * @return O usuário atualizado.
     */
    @Operation(summary = "Atualiza as informações do usuário logado. Requer que o corpo do request contenha um objeto com os atributos do usuário, exceto o e-mail.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "OK", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
    })
    @PutMapping(value = "/usuario")
    public ResponseEntity<Usuario> update(@RequestAttribute(name = "usuario") Usuario oldUsuario,
                                          @RequestBody Usuario novoUsuario) {
        Usuario updatedUsuario = usuarioService.update(
            new Usuario(oldUsuario.getNome(), novoUsuario.getIdade(), novoUsuario.getNomeInstituicao(),
                        novoUsuario.getCargo(), novoUsuario.getCidade(), oldUsuario.getEmail(), true),
            oldUsuario.getEmail());
        return new ResponseEntity<>(updatedUsuario, HttpStatus.OK);
    }

    /**
     * Recupera as informações do usuário atual logado.
     *
     * @param usuario O usuário logado atualmente (preenchido automaticamente pelo
     *                Spring).
     * @return O usuário logado.
     */
    @Operation(summary = "Recupera as informações do usuário atual logado.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "OK", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @GetMapping(value = "/usuario")
    public ResponseEntity<Usuario> getActualUser(@RequestAttribute(name = "usuario") Usuario usuario) {
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    /**
     * Apaga um usuário do sistema pelo email.
     *
     * @param email O email do usuário a ser deletado.
     * @return O usuário deletado.
     */
    @Operation(summary = "Permite apagar um usuário do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE) })
    })
    @DeleteMapping(value = "/usuario/{email}")
    public ResponseEntity<Usuario> delete(@PathVariable("email") String email) {
        Usuario usuario = usuarioService.delete(email);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    /**
     * Retorna todos os usuários registrados.
     *
     * @return Lista de usuários.
     */
    @Operation(summary = "Fornece um array de objetos do tipo usuario registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE) })
    })
    @GetMapping(value = "/usuario/all")
    public List<Usuario> getAll() {
        return usuarioService.getAll();
    }

    /**
     * Recupera um usuário pelo email.
     *
     * @param email O email do usuário.
     * @return O usuário encontrado.
     */
    @Operation(summary = "Recupera um usuario com específico email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE) })
    })
    @GetMapping(value = "/usuario/{email}")
    public ResponseEntity<Usuario> getByEmail(@PathVariable("email") String email) {
        Usuario usuario = usuarioService.getById(email);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    /**
     * Pesquisa um usuário pelo nome.
     *
     * @param nome O nome do usuário.
     * @return O usuário encontrado.
     */
    // @GetMapping(value = "/usuario/search/{nome}")
    // public ResponseEntity<Usuario> searchByNome(@PathVariable("nome") String
    // nome) {
    // Usuario usuario = usuarioService.pesquisarPorNome(nome);
    // return new ResponseEntity<>(usuario, HttpStatus.OK);
    // }
}
