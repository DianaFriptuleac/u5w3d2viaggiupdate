package dianafriptuleac.u5w3d2viaggiupdate.tools;

import dianafriptuleac.u5w3d2viaggiupdate.entities.Dipendente;
import dianafriptuleac.u5w3d2viaggiupdate.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWT {
    //leggo il secret da application.properties che pero sara salvato in env.properties per non essere vissibile.
    @Value("${jwt.secret}")
    private String secret;

    //Metodo per creare il token con Jwts.builder()
    public String createToken(Dipendente dipendente) {
        return Jwts.builder().
                //1. data emissione Token - in millesecondi
                        issuedAt(new Date(System.currentTimeMillis()))
                //2. data scadenza Token - in millesecondi (in questo caso e di una settimana)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                //3. Id del dipendente- a chi appartiene il Ttoken(no dati sensibiili)
                .subject(String.valueOf(dipendente.getId()))
                //4. Frimo il Token con algoritmo specifico - HMAC- il secret.
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                //5. Assemblo in una stringa.
                .compact();
    }

    //Metodo per verificare il Token con Jwts.parser()
    public void verifyToken(String accessToken) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build().parse(accessToken); // lancia varie eccezioni: token manipolato, scaduto ect.
        } catch (Exception ex) {
            throw new UnauthorizedException("Problemi con il token! Effettuare un nuovo login!");
        }
    }

}
