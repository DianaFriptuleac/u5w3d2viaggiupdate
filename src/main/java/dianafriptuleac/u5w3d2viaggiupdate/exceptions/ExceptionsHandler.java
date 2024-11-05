package dianafriptuleac.u5w3d2viaggiupdate.exceptions;


import dianafriptuleac.u5w3d2viaggiupdate.payloads.ErrorsResponceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public ErrorsResponceDTO handleBadrequestEx(BadRequestException ex) {
        return new ErrorsResponceDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) //401
    public ErrorsResponceDTO handleUnauthorized(UnauthorizedException ex) {
        return new ErrorsResponceDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorsResponceDTO handleNotFoundEx(NotFoundException ex) {
        return new ErrorsResponceDTO(ex.getMessage(), LocalDateTime.now());

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)//500
    public ErrorsResponceDTO handleGeneriEx(Exception ex) {
        ex.printStackTrace();
        return new ErrorsResponceDTO("Problema lato server!", LocalDateTime.now());
    }
}
