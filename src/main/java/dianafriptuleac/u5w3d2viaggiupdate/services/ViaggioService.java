package dianafriptuleac.u5w3d2viaggiupdate.services;


import dianafriptuleac.u5w3d2viaggiupdate.entities.Viaggio;
import dianafriptuleac.u5w3d2viaggiupdate.exceptions.BadRequestException;
import dianafriptuleac.u5w3d2viaggiupdate.exceptions.NotFoundException;
import dianafriptuleac.u5w3d2viaggiupdate.payloads.ViaggioDTO;
import dianafriptuleac.u5w3d2viaggiupdate.repositories.PrenotazioneRepository;
import dianafriptuleac.u5w3d2viaggiupdate.repositories.ViaggioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ViaggioService {

    @Autowired
    private ViaggioRepository viaggioRepository;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;


    public Viaggio saveViaggio(ViaggioDTO body) {
        this.viaggioRepository.findByDestinazioneAndDataViaggio(body.destinazione(), body.dataViaggio()).ifPresent(viaggio -> {
            throw new BadRequestException("Un viaggio per " + body.destinazione() + " in data " + body.dataViaggio() + " esiste già! " +
                    "Cambia data o destinazione!");
        });

        Viaggio newViaggio = new Viaggio(body.destinazione(), body.dataViaggio(), "in programma");
        return this.viaggioRepository.save(newViaggio);
    }

    public Page<Viaggio> findAll(int page, int size, String sortBy) {
        if (size > 100)
            size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.viaggioRepository.findAll(pageable);
    }

    public Viaggio findById(long viaggioId) {
        return this.viaggioRepository.findById(viaggioId).orElseThrow(() -> new NotFoundException(viaggioId));

    }

    public Viaggio findByIdAndUpdate(long viaggioId, ViaggioDTO body) {
        return this.viaggioRepository.findById(viaggioId).map(viaggio -> {
            viaggio.setDestinazione(body.destinazione());
            viaggio.setDataViaggio(body.dataViaggio());
            viaggio.setStato(body.stato());
            return this.viaggioRepository.save(viaggio);
        }).orElseThrow(() -> new NotFoundException(viaggioId));
    }

    @Transactional
    public void findByIdAndDelete(long viaggioId) {
        prenotazioneRepository.deleteByViaggioId(viaggioId); //cancello anche le prenotazioni del viaggio
        Viaggio viaggio = viaggioRepository.findById(viaggioId)
                .orElseThrow(() -> new NotFoundException(viaggioId));

        viaggioRepository.delete(viaggio);
    }

    public Viaggio updateStato(long viaggiId, String newStato) {
        Viaggio foudViaggio = this.findById(viaggiId);
        foudViaggio.setStato(newStato);
        return this.viaggioRepository.save(foudViaggio);
    }
}



