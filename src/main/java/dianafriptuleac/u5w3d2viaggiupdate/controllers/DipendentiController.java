package dianafriptuleac.u5w3d2viaggiupdate.controllers;


import dianafriptuleac.u5w3d2viaggiupdate.entities.Dipendente;
import dianafriptuleac.u5w3d2viaggiupdate.exceptions.BadRequestException;
import dianafriptuleac.u5w3d2viaggiupdate.payloads.DipendenteDTO;
import dianafriptuleac.u5w3d2viaggiupdate.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/dipendenti")
public class DipendentiController {

    @Autowired
    private DipendenteService dipendenteService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") //solo gli admin possono visualizzare tutti i dipendenti
    public Page<Dipendente> findAllDipendenti(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "8") int size,
                                              @RequestParam(defaultValue = "id") String sortBy) {
        return this.dipendenteService.findAll(page, size, sortBy);
    }


    @GetMapping("/{dipendenteId}")
    public Dipendente findById(@PathVariable Long dipendenteId) {
        return this.dipendenteService.findById(dipendenteId);
    }

    @PutMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Dipendente findByIdAndUpdate(@PathVariable Long dipendenteId, @RequestBody @Validated DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        return this.dipendenteService.findByIdAndUpdate(dipendenteId, body);
    }

    @DeleteMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable Long dipendenteId) {
        dipendenteService.findByIdAndDelete(dipendenteId);
    }

    @PatchMapping("/{dipendenteId}/imgURL")
    public String uploadImg(@PathVariable Long dipendenteId, @RequestParam("imgURL") MultipartFile file) {
        return this.dipendenteService.uploadImg(dipendenteId, file);
    }

}

