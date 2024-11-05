package dianafriptuleac.u5w3d2viaggiupdate.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dianafriptuleac.u5w3d2viaggiupdate.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
//Dipendente completo

@Entity
@Table(name = "dipendenti")
@Getter
@Setter
@NoArgsConstructor
//Campi che non voglio visualizzare
@JsonIgnoreProperties({"password", "role", "accountNonLocked",
        "credentialsNonExpired", "accountNonExpired", "authorities", "enabled"})
public class Dipendente implements UserDetails {
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
    @Enumerated(EnumType.STRING)
    private Role role;

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
        this.role = Role.DIPENDENTE;
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
                ", role='" + role + '\'' +
                '}';
    }

    //Metodo per ottenere la lista i ruoli
    //<? extends GrantedAuthority>  --> qualsiasi cosa basta che estenda GrantedAuthority
    //lista di oggetti he implementa GrantedAuthority come fa SimpleGrantedAuthority(rapresenta ruoli utenti in Spring).
    //non torniamo enum
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

}
