package dianafriptuleac.u5w3d2viaggiupdate.controllers;


import dianafriptuleac.u5w3d2viaggiupdate.entities.Dipendente;
import dianafriptuleac.u5w3d2viaggiupdate.exceptions.BadRequestException;
import dianafriptuleac.u5w3d2viaggiupdate.payloads.DipendenteDTO;
import dianafriptuleac.u5w3d2viaggiupdate.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/dipendenti")
public class DipendentiController {

    @Autowired
    private DipendenteService dipendenteService;

    //Admin
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") //solo gli admin possono visualizzare tutti i dipendenti
    public Page<Dipendente> findAllDipendenti(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "8") int size,
                                              @RequestParam(defaultValue = "id") String sortBy) {
        return this.dipendenteService.findAll(page, size, sortBy);
    }


    // ************************************************* /ME ENDPOINTS (per profili personali) ***********************************************
    // Se ho gia effettuato SecurityContextHolder.getContext().setAuthentication(authentication) nel Filter (JWTCheckerFilter),
    // allora negli endpoint autenticati
    // posso accedere a chi è il dipendente che sta effettuando la richiesta, tramite @AuthenticationPrincipal
    // Posso implementare endpoints "personali":per leggere il proprio profilo, cambiare i propri dat, cancellare se stessi.

    @GetMapping("/me")
    public Dipendente getDipendenteProfile(@AuthenticationPrincipal Dipendente currentAuthenticateDipendente) {
        return currentAuthenticateDipendente;

    }

    @PutMapping("/me")
    public Dipendente updatemyDipendenteProfile(@AuthenticationPrincipal Dipendente currentAuthenticateDipendente,
                                                @RequestBody @Validated DipendenteDTO body) {
        return this.dipendenteService.findByIdAndUpdate(currentAuthenticateDipendente.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyDipendenteProfile(@AuthenticationPrincipal Dipendente currentAuthenticateDipendente) {
        this.dipendenteService.findByIdAndDelete(currentAuthenticateDipendente.getId());
    }

    @PatchMapping("/me/imgURL")
    public String uploadImg(@AuthenticationPrincipal Dipendente currentAuthenticateDipendente,
                            @RequestParam("imgURL") MultipartFile file) {
        return this.dipendenteService.uploadImg(currentAuthenticateDipendente.getId(), file);
    }

    //************************************************************************************************************

    //All dipendenti
    @GetMapping("/{dipendenteId}")
    public Dipendente findById(@PathVariable Long dipendenteId) {
        return this.dipendenteService.findById(dipendenteId);
    }

    //Admin
    @PutMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Dipendente findByIdAndUpdate(@PathVariable Long dipendenteId, @RequestBody @Validated DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        return this.dipendenteService.findByIdAndUpdate(dipendenteId, body);
    }

    //Admin
    @DeleteMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable Long dipendenteId) {
        dipendenteService.findByIdAndDelete(dipendenteId);
    }


}

