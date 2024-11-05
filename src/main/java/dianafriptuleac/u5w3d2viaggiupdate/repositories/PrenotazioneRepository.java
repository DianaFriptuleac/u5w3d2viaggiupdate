package dianafriptuleac.u5w3d2viaggiupdate.repositories;


import dianafriptuleac.u5w3d2viaggiupdate.entities.Dipendente;
import dianafriptuleac.u5w3d2viaggiupdate.entities.Prenotazione;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    @Query("SELECT p FROM Prenotazione p WHERE p.dipendente = :dipendente AND p.viaggio.dataViaggio = :dataViaggio")
    Optional<Prenotazione> findByDipendenteAndDataViaggio(Dipendente dipendente, LocalDate dataViaggio);

    //@Modifying- Query di modifica e non di lettura
    @Query("DELETE FROM Prenotazione p WHERE p.viaggio.id = :viaggioId")
    @Modifying
    @Transactional
    void deleteByViaggioId(Long viaggioId);

    @Query("DELETE FROM Prenotazione p WHERE p.dipendente.id = :dipendenteId")
    @Modifying
    @Transactional
    void deleteByDipendenteId(Long dipendenteId);
}

