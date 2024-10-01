package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import springboot.model.Email;
import springboot.service.EmailService;

/**
 * Controlador responsável pelo envio de emails no sistema.
 * Este controlador permite o envio de um email simples de um usuário para o administrador.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Validated
public class EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * Envia um email simples de um usuário para o administrador do sistema.
     * O email deve ser passado no corpo da requisição, e deve ser validado antes de ser enviado.
     * 
     * @param email objeto contendo as informações do email a ser enviado, validado automaticamente.
     * @return uma resposta HTTP indicando o sucesso ou falha do envio.
     *         - 200 OK se o email foi enviado com sucesso.
     *         - 500 Internal Server Error se houver um erro ao tentar enviar o email.
     */
    @Operation(summary = "Permite registrar um email de um user para o admin.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Email enviado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação de entrada"),
        @ApiResponse(responseCode = "500", description = "Erro ao enviar o email")
    })
    @PostMapping("/email")
    public ResponseEntity<String> sendSimpleEmail(@Valid @RequestBody Email email) {
        try {
            emailService.sendEmail(email);
            return ResponseEntity.ok("Email enviado com sucesso!");
        } catch (MailException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar o email");
        }
    }
}
