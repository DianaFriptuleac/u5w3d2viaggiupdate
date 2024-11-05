package dianafriptuleac.u5w3d2viaggiupdate.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "viaggi")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Viaggio {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;
    private String destinazione;
    private LocalDate dataViaggio;
    private String stato;


    public Viaggio(String destinazione, LocalDate dataViaggio, String stato) {
        this.destinazione = destinazione;
        this.dataViaggio = dataViaggio;
        this.stato = stato;
    }
}
