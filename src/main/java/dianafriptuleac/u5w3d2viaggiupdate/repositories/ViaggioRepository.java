package dianafriptuleac.u5w3d2viaggiupdate.repositories;


import dianafriptuleac.u5w3d2viaggiupdate.entities.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ViaggioRepository extends JpaRepository<Viaggio, Long> {
    Optional<Viaggio> findByDestinazioneAndDataViaggio(String destinazione, LocalDate dataViaggio);
}
