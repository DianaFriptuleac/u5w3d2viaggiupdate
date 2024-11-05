package dianafriptuleac.u5w3d2viaggiupdate.exceptions;

public class NotFoundException extends RuntimeException {
    //per un id
    public NotFoundException(Long id) {

        super("II record con l'id: " + id + " non è stato trovato!");
    }

    //Per una stringa
    public NotFoundException(String message) {
        super(message);
    }
}
