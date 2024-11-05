package dianafriptuleac.u5w3d2viaggiupdate.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "prenotazioni")
@Getter
@Setter
@NoArgsConstructor
public class Prenotazione {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;
    private LocalDate dataRichiesta;
    private String note;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dipendente_id", nullable = false)
    private Dipendente dipendente;

    @ManyToOne
    @JoinColumn(name = "viaggio_id", nullable = false)
    private Viaggio viaggio;

    public Prenotazione(String note, Dipendente dipendente, Viaggio viaggio) {
        this.dataRichiesta = LocalDate.now();
        this.note = note;
        this.dipendente = dipendente;
        this.viaggio = viaggio;
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "id=" + id +
                ", dataRichiesta=" + dataRichiesta +
                ", note='" + note + '\'' +
                '}';
    }
}
