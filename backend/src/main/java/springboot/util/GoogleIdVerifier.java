package springboot.util;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import springboot.exception.auth.InvalidTokenException;
import springboot.model.Usuario;
import springboot.service.UsuarioService;

import java.util.Collections;

import com.google.api.client.json.gson.GsonFactory;

public class GoogleIdVerifier {

    static final String CLIENT_ID = "1024214029990-d9ppvf55b3sv7ias7b7uhltclj9b1j2g.apps.googleusercontent.com";

    static final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
            .setAudience(Collections.singletonList(CLIENT_ID)).build();

    public static GoogleIdToken.Payload getPayload(String tokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(tokenString);
            if(idToken != null) {
                return idToken.getPayload();
            } else {
                throw new InvalidTokenException();
            }
        } catch (IOException | GeneralSecurityException | InvalidTokenException e) {
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
        } catch (IOException | GeneralSecurityException | InvalidTokenException e) {
            throw new InvalidTokenException();
        }
    }
}
