package dianafriptuleac.u5w3d2viaggiupdate.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dipendenti")
@Getter
@Setter
@NoArgsConstructor
public class Dipendente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;
    private String username;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String imgURL;

    @OneToMany(mappedBy = "dipendente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore //evito il ciclo infinito
    private List<Prenotazione> prenotazioni = new ArrayList<>();

    public Dipendente(String username, String nome, String cognome, String email, String password, String imgURL) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.imgURL = imgURL;
    }

    @Override
    public String toString() {
        return "Dipendente{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", imgURL='" + imgURL + '\'' +
                '}';
    }
}
