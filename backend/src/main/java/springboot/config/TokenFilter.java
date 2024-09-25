package springboot.config;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import springboot.service.UsuarioService;
import springboot.util.GoogleIdVerifier;

/**
 * Filtro de autenticação que intercepta requisições HTTP e verifica a
 * validade de um token JWT fornecido no cabeçalho da requisição.
 * Se o token for válido e o usuário estiver registrado no sistema,
 * a requisição é encaminhada para o próximo filtro da cadeia. Caso contrário,
 * uma resposta de erro é enviada.
 */
public class TokenFilter extends GenericFilterBean {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Intercepta requisições HTTP, validando o token JWT no cabeçalho da
     * requisição.
     * Se o token for válido e o usuário registrado, a requisição é encaminhada.
     * 
     * @param request  a requisição HTTP
     * @param response a resposta HTTP
     * @param chain    a cadeia de filtros
     * @throws IOException      se ocorrer um erro durante o processamento do token
     *                          ou da requisição
     * @throws ServletException se ocorrer um erro durante o processamento do filtro
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (usuarioService == null) {
            ApplicationContext ctx = WebApplicationContextUtils
                    .getRequiredWebApplicationContext(this.getServletContext());
            usuarioService = ctx.getBean(UsuarioService.class);
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String token = extractToken(httpRequest);
        if (token == null) {
            sendErrorResponse(httpResponse, HttpStatus.BAD_REQUEST, "Token inválido ou expirado!");
            return;
        }

        GoogleIdToken.Payload payload = validateToken(token, httpResponse);
        if (payload == null) {
            return;
        }

        if (!isUserRegistered(payload.getEmail(), httpResponse)) {
            return;
        }

        request.setAttribute("usuario", usuarioService.getById(payload.getEmail()));
        chain.doFilter(request, response);
    }

    /**
     * Extrai o token JWT do cabeçalho "Authorization".
     *
     * @param request a requisição HTTP
     * @return o token JWT, ou null se o cabeçalho não estiver presente ou mal
     *         formatado
     */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    /**
     * Valida o token JWT usando o GoogleIdVerifier.
     *
     * @param token    o token JWT a ser validado
     * @param response a resposta HTTP para enviar erros, se necessário
     * @return o payload do token se for válido, ou null caso contrário
     * @throws IOException se ocorrer um erro durante a validação do token
     */
    private GoogleIdToken.Payload validateToken(String token, HttpServletResponse response) throws IOException {
        try {
            Payload payload = GoogleIdVerifier.getPayload(token);
            if (isTokenExpired(payload)) {
                sendErrorResponse(response, HttpStatus.BAD_REQUEST, "Token inválido ou expirado!");
                return null;
            }
            return payload;
        } catch (IOException e) {
            sendErrorResponse(response, HttpStatus.BAD_REQUEST, "Token inválido ou expirado!");
            return null;
        }
    }

    /**
     * Verifica se o token JWT expirou.
     *
     * @param payload o payload do token JWT
     * @return true se o token expirou, false caso contrário
     */
    private boolean isTokenExpired(GoogleIdToken.Payload payload) {
        return new Date().getTime() > payload.getExpirationTimeSeconds() * 1000;
    }

    /**
     * Verifica se o usuário está registrado no sistema.
     *
     * @param email    o e-mail do usuário
     * @param response a resposta HTTP para enviar erros, se necessário
     * @return true se o usuário estiver registrado, false caso contrário
     * @throws IOException se ocorrer um erro durante a verificação do usuário
     */
    private boolean isUserRegistered(String email, HttpServletResponse response) throws IOException {
        try {
            usuarioService.getById(email);
            return true;
        } catch (Exception e) {
            sendErrorResponse(response, HttpStatus.NOT_FOUND, "Usuário ainda não registrado no sistema!");
            return false;
        }
    }

    /**
     * Envia uma resposta de erro HTTP com o status e a mensagem fornecidos.
     *
     * @param response a resposta HTTP
     * @param status   o status HTTP a ser enviado
     * @param message  a mensagem de erro
     * @throws IOException se ocorrer um erro ao enviar a resposta
     */
    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.sendError(status.value(), message);
    }
}
