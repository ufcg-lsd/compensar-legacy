package springboot.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springboot.dto.input.UsuarioInput;
import springboot.dto.output.CustomRestOutput;
import springboot.exception.auth.InvalidTokenException;
import springboot.exception.auth.UserAlreadyExistException;
import springboot.model.Usuario;
import springboot.service.UsuarioService;
import springboot.util.GoogleIdVerifier;

import java.util.Date;

@Controller
@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
@Api(value = "AuthControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final int HOUR = 60 * 60 * 1000;

    @Autowired
    UsuarioService usuarioService;

    @ApiOperation("Permite autenticar um usuário no sistema.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = CustomRestOutput.class) })
    @RequestMapping(value = "/auth/authenticate", method = RequestMethod.GET)
    public ResponseEntity<CustomRestOutput> authenticate() {
        return new ResponseEntity<>(new CustomRestOutput(new Date(), "Usuário autenticado com sucesso!"), HttpStatus.OK);
    }


    @ApiOperation("Permite cadastrar um usuário no sistema.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = CustomRestOutput.class) })
    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST)
    public ResponseEntity<CustomRestOutput> signup(@RequestBody UsuarioInput usuario, @RequestHeader("Authorization") String token) {
        if(token == null || !token.startsWith("Bearer ")) {
            throw new InvalidTokenException();
        }

        GoogleIdToken.Payload payload = GoogleIdVerifier.getPayload(token.substring(7));

        String email = payload.getEmail();
        String nome = (String) payload.get("name");

        try {
            usuarioService.getById(email);
            throw new UserAlreadyExistException();
        } catch (Exception e) {
            usuarioService.save(new Usuario(nome, usuario.getIdade(), usuario.getNomeInstituicao(), usuario.getCargo(), usuario.getCidade(), email, true));
        }
        return new ResponseEntity<>(new CustomRestOutput(new Date(), "Usuário autenticado com sucesso!"), HttpStatus.OK);
    }
}
