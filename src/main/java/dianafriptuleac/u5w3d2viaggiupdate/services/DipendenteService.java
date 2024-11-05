package dianafriptuleac.u5w3d2viaggiupdate.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dianafriptuleac.u5w3d2viaggiupdate.entities.Dipendente;
import dianafriptuleac.u5w3d2viaggiupdate.exceptions.BadRequestException;
import dianafriptuleac.u5w3d2viaggiupdate.exceptions.NotFoundException;
import dianafriptuleac.u5w3d2viaggiupdate.payloads.DipendenteDTO;
import dianafriptuleac.u5w3d2viaggiupdate.repositories.DipendenteRepository;
import dianafriptuleac.u5w3d2viaggiupdate.repositories.PrenotazioneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DipendenteService {

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private Cloudinary cloudinaryUploader;

    //bcrypt x password
    @Autowired
    private PasswordEncoder bdrypt;

    public Dipendente saveDipendente(DipendenteDTO body) {
        this.dipendenteRepository.findByEmail(body.email()).ifPresent(dipendente -> {
            throw new BadRequestException("Email " + body.email() + " già in uso!");
        });

        this.dipendenteRepository.findByUsername(body.username()).ifPresent(dipendente -> {
            throw new BadRequestException("Username " + body.username() + " già in uso!");
        });

        Dipendente newDipendente = new Dipendente(body.username(), body.nome(), body.cognome(), body.email(), bdrypt.encode(body.password()),
                "https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());
        return this.dipendenteRepository.save(newDipendente);
    }

    public Page<Dipendente> findAll(int page, int size, String sortBy) {
        if (size > 100)
            size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.dipendenteRepository.findAll(pageable);
    }

    public Dipendente findById(long dipendenteId) {
        return this.dipendenteRepository.findById(dipendenteId).orElseThrow(() -> new NotFoundException(dipendenteId));

    }

    public Dipendente findByIdAndUpdate(Long dipendenteId, DipendenteDTO body) {
        Dipendente foundDipendente = this.findById(dipendenteId);
        if (!foundDipendente.getEmail().equals(body.email())) {
            this.dipendenteRepository.findByEmail(body.email()).ifPresent(
                    dipendente -> {
                        throw new BadRequestException("Email " + body.email() + " già in uso!");
                    });
        }
        if (!foundDipendente.getUsername().equals(body.username())) {
            this.dipendenteRepository.findByUsername(body.username()).ifPresent(
                    dipendente -> {
                        throw new BadRequestException("Username " + body.username() + " già in uso!");
                    }
            );
        }
        foundDipendente.setUsername(body.username());
        foundDipendente.setNome(body.nome());
        foundDipendente.setCognome(body.cognome());
        foundDipendente.setEmail(body.email());
        foundDipendente.setPassword(bdrypt.encode(body.password()));
        foundDipendente.setImgURL("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());
        return this.dipendenteRepository.save(foundDipendente);
    }

    @Transactional
    public void findByIdAndDelete(long dipendenteId) {
        prenotazioneRepository.deleteByDipendenteId(dipendenteId); //cancello anche la prenotazione
        Dipendente foundDipendente = this.findById(dipendenteId);
        this.dipendenteRepository.delete(foundDipendente);
    }

    public String uploadImg(Long dipendenteId, MultipartFile file) {
        String url = null;
        try {
            url = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        } catch (IOException e) {
            throw new BadRequestException("Ci sono stati problemi con l'upload del file!");
        }
        Dipendente dipendente = dipendenteRepository.findById(dipendenteId).orElseThrow(() -> new NotFoundException(dipendenteId));
        dipendente.setImgURL(url);
        dipendenteRepository.save(dipendente);
        return url;
    }

    //find by email and username
    public Dipendente findByEmailAndUsername(String email, String username) {
        return this.dipendenteRepository.findByEmailAndUsername(email, username).orElseThrow(() ->
                new NotFoundException("Il dipendente non trovato!"));

    }

}
