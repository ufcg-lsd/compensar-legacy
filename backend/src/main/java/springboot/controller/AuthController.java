package springboot.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import springboot.dto.input.UsuarioInput;
import springboot.dto.output.CustomRestOutput;
import springboot.exception.auth.InvalidTokenException;
import springboot.exception.auth.UserAlreadyExistException;
import springboot.model.Usuario;
import springboot.service.UsuarioService;
import springboot.util.GoogleIdVerifier;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "AuthController", description = "API para autenticação e cadastro de usuários")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Permite autenticar um usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso!")
    })
    @GetMapping("/auth/authenticate")
    public ResponseEntity<CustomRestOutput> authenticate() {
        return ResponseEntity.ok(new CustomRestOutput(new Date(), "Usuário autenticado com sucesso!"));
    }

    @Operation(summary = "Permite cadastrar um usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Token inválido"),
            @ApiResponse(responseCode = "409", description = "Usuário já existe")
    })
    @PostMapping("/auth/signup")
    public ResponseEntity<CustomRestOutput> signup(@RequestBody UsuarioInput usuario,
            @RequestHeader("Authorization") String token) {
        validateToken(token);

        GoogleIdToken.Payload payload = GoogleIdVerifier.getPayload(extractToken(token));
        String email = payload.getEmail();
        String nome = (String) payload.get("name");
        try {
            usuarioService.getById(email);
            throw new UserAlreadyExistException();
        } catch (Exception e) {
            usuarioService.save(new Usuario(nome, usuario.getIdade(), usuario.getNomeInstituicao(), usuario.getCargo(),
                    usuario.getCidade(), email, true));
        }
        
        return ResponseEntity.ok(new CustomRestOutput(new Date(), "Usuário cadastrado com sucesso!"));
    }

    private String extractToken(String token) {
        return token.substring(7); // Remove "Bearer "
    }

    private void validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new InvalidTokenException();
        }
    }
}
