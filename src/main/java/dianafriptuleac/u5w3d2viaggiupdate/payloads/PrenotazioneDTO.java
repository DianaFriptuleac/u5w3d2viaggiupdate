package dianafriptuleac.u5w3d2viaggiupdate.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PrenotazioneDTO(
        @NotEmpty(message = "Inserire note e preferenze per il viaggio!")
        @Size(min = 4, max = 250, message = "Le note devono essere comprese tra 4 e 250 caratteri!")
        String note,
        @NotNull(message = "Inserire l'ID del dipendente!")
        Long dipendenteId,
        @NotNull(message = "Inserire l'ID del viaggio!")
        Long viaggioId) {
}
