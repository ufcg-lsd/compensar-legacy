package springboot.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import springboot.exception.auth.InvalidTokenException;
import springboot.model.Usuario;
import springboot.service.UsuarioService;

import java.util.Collections;

public class GoogleIdVerifier {

    static final String CLIENT_ID = "363497084086-sj4dhuvvkmcivpbl0h2fgrrvnm0229og.apps.googleusercontent.com";

    static final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
            .setAudience(Collections.singletonList(CLIENT_ID)).build();

    public static GoogleIdToken.Payload getPayload(String tokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(tokenString);
            if(idToken != null) {
                return idToken.getPayload();
            } else {
                throw new InvalidTokenException();
            }
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public static Usuario getUsuario(String tokenString, UsuarioService service) {
        try {
            GoogleIdToken idToken = verifier.verify(tokenString);
            if(idToken != null) {
                Usuario usuario = service.getById(idToken.getPayload().getEmail());
                if(usuario != null) {
                    return usuario;
                } else {
                    throw new InvalidTokenException();
                }
            } else {
                throw new InvalidTokenException();
            }
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }
}
