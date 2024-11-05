package dianafriptuleac.u5w3d2viaggiupdate.services;

import dianafriptuleac.u5w3d2viaggiupdate.entities.Dipendente;
import dianafriptuleac.u5w3d2viaggiupdate.exceptions.UnauthorizedException;
import dianafriptuleac.u5w3d2viaggiupdate.payloads.DipendenteLoginDTO;
import dianafriptuleac.u5w3d2viaggiupdate.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private JWT jwt;

    public String checkCredentialsAndGenerateToken(DipendenteLoginDTO body) {
        //1. Controllo credenziali
        //1.1 Controllo email
        Dipendente dipendenteFound = this.dipendenteService.findByEmailAndUsername(body.email(), body.username());
        //1.2 Verifico che il dipendente esista  ela password corrisponda
        if (dipendenteFound != null && dipendenteFound.getPassword().equals(body.password())) {
            //2. se e tutto ok - genero il token
            String accessToken = jwt.createToken(dipendenteFound);
            //3. ritorna il token
            return accessToken;
        } else {
            //4. altrimenti- 401 (Unauthorized)
            throw new UnauthorizedException("Credenziali errate!");
        }

    }
}
