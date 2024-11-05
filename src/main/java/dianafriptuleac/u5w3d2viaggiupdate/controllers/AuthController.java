package dianafriptuleac.u5w3d2viaggiupdate.controllers;

import dianafriptuleac.u5w3d2viaggiupdate.entities.Dipendente;
import dianafriptuleac.u5w3d2viaggiupdate.exceptions.BadRequestException;
import dianafriptuleac.u5w3d2viaggiupdate.payloads.DipendenteDTO;
import dianafriptuleac.u5w3d2viaggiupdate.payloads.DipendenteLoginDTO;
import dianafriptuleac.u5w3d2viaggiupdate.payloads.DipendenteLoginResponseDTO;
import dianafriptuleac.u5w3d2viaggiupdate.services.AuthService;
import dianafriptuleac.u5w3d2viaggiupdate.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private DipendenteService dipendenteService;

    @PostMapping("/login")
    public DipendenteLoginResponseDTO login(@RequestBody DipendenteLoginDTO body) {
        return new DipendenteLoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente saveDipendente(@RequestBody @Validated DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }
        return this.dipendenteService.saveDipendente(body);

    }
}
