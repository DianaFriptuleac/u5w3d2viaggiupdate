package dianafriptuleac.u5w3d2viaggiupdate.repositories;


import dianafriptuleac.u5w3d2viaggiupdate.entities.Dipendente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DipendenteRepository extends JpaRepository<Dipendente, Long> {
    Optional<Dipendente> findByEmail(String email);

    Optional<Dipendente> findByUsername(String username);

    Optional<Dipendente> findByEmailAndUsername(String email, String username);

}
