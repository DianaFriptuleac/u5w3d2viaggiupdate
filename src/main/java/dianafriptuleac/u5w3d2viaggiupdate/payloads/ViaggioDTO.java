package dianafriptuleac.u5w3d2viaggiupdate.payloads;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ViaggioDTO(
        @NotEmpty(message = "La destinazione è un campo obbligatorio!")
        @Size(min = 4, max = 50, message = "La destinazione deve essere compresa tra 4 e 50 caratteri!")
        String destinazione,

        @FutureOrPresent(message = "Inserire una data viaggio valida!")
        LocalDate dataViaggio,

        String stato) {
}
