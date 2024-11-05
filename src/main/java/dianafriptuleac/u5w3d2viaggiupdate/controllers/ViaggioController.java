package dianafriptuleac.u5w3d2viaggiupdate.controllers;


import dianafriptuleac.u5w3d2viaggiupdate.entities.Viaggio;
import dianafriptuleac.u5w3d2viaggiupdate.exceptions.BadRequestException;
import dianafriptuleac.u5w3d2viaggiupdate.payloads.ViaggioDTO;
import dianafriptuleac.u5w3d2viaggiupdate.services.ViaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/viaggi")
public class ViaggioController {
    @Autowired
    private ViaggioService viaggioService;

    @GetMapping
    public Page<Viaggio> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String sortBy) {
        return this.viaggioService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Viaggio save(@RequestBody @Validated ViaggioDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.viaggioService.saveViaggio(body);
    }

    @GetMapping("/{viaggioId}")
    public Viaggio findById(@PathVariable Long viaggioId) {
        return this.viaggioService.findById(viaggioId);
    }

    @PutMapping("/{viaggioId}")
    public Viaggio findByIdAndUpdate(@PathVariable Long viaggioId, @RequestBody @Validated ViaggioDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        return this.viaggioService.findByIdAndUpdate(viaggioId, body);
    }

    @DeleteMapping("/{viaggioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable Long viaggioId) {
        viaggioService.findByIdAndDelete(viaggioId);
    }

    @PatchMapping("/{viaggioId}/stato")
    public String uploadStato(@PathVariable Long viaggioId, @RequestParam("stato") String newStato) {
        Viaggio upViaggio = this.viaggioService.updateStato(viaggioId, newStato);
        return "Lo stato del viaggio con ID " + viaggioId + " è stato aggiornato correttamente a: " + upViaggio.getStato();
    }

}
