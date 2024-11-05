package dianafriptuleac.u5w3d2viaggiupdate.security;

import dianafriptuleac.u5w3d2viaggiupdate.entities.Dipendente;
import dianafriptuleac.u5w3d2viaggiupdate.exceptions.UnauthorizedException;
import dianafriptuleac.u5w3d2viaggiupdate.services.DipendenteService;
import dianafriptuleac.u5w3d2viaggiupdate.tools.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Component obbligatorio- x poter utilizzare la classe nella cattena filtri
@Component
public class JWTCheckerFilter extends OncePerRequestFilter {

    @Autowired
    private JWT jwt;

    @Autowired
    private DipendenteService dipendenteService;

    //Metodo richiamato ad ogni richiesta - controlla il token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //----------------------------------- Autenticazione -----------------------------------------------

        // 1. Verifico se nella richiesta è presente l'Authorization Header,
        // e se è ben formato ("Bearer josdjojosdj..."), altimenti - 401
        String authHeader = request.getHeader("Authorization"); //reccupero l'header
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire token nell'Authorization Header nel formato corretto!");

        //2.Estrago il token dal header
        String accessToken = authHeader.substring(7);// 7-nr. caratteri prima del token (Bearer )

        //3.Verifico il token con il metodo del jwt.verifyToken(accessToken) che si trova nel tools.
        jwt.verifyToken(accessToken);

        //----------------------------------- Autorizzazione -----------------------------------------------
        //1. Cerco utente tramite id
        String dipendenteId = jwt.getIdFromToken(accessToken);
        Dipendente currentDipendente = this.dipendenteService.findById(Long.parseLong(dipendenteId));

        //2. Associo Security Context al dipendente
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentDipendente, null, currentDipendente.getAuthorities());
        // Il terzo parametro serve per  utilizzare @PreAuthorize, così il SecurityContext saprà quali sono i ruoli dell dipendente
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Aggiorno SecurityContext associandogli il dipendente autenticato


        //3. Se e tutto ok - passo la richiesta al prossiimo filtro con filterChain.
        //con doFilter(request, response) riichiamo il prossimo filtro o controller della catena
        //filterChain- catena di filtri di sicurezza
        filterChain.doFilter(request, response);

        // 5. Se qualcosa non va con il token --> 401
    }


    //*************************************************************************************************************
    //Metodo richiamato ad ogni richiesta - controlla il token. Prova con try e catch
   /* @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Verifico se nella richiesta è presente l'Authorization Header,
        // e se è ben formato ("Bearer josdjojosdj..."), altimenti - 401
        String authorizedHeader = request.getHeader("Authorization"); //reccupero l'header
        if (authorizedHeader == null || !authorizedHeader.startsWith("Bearer")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //nel caso di errore - 401
            //messaggio di errore al client
            response.getWriter().write("Inserire token nell'Authorization Header e nel formato corretto!");
            return; //interrompo il metodo nel caso di errore (blocco gli altri passaggi)
        }
        //2.Estrago il token dal header
        String accessToken = authorizedHeader.substring(7); // 7-nr. caratteri prima del token (Bearer )

        //3.Verifico il token con il metodo del jwt.verifyToken(accessToken) che si trova nel tools.
        try {
            jwt.verifyToken(accessToken);

            //4. Se e tutto ok - passo la richiesta al prossiimo filtro con filterChain.
            //con doFilter(request, response) riichiamo il prossimo filtro o controller della catena
            //filterChain- catena di filtri di sicurezza
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            //5. se c'e qualcosa che non va con il token ci da un 401.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
            response.getWriter().write("Token non valido o scaduto. Effettuare un nuovo login.");

        }
    }*/
    //*************************************************************************************************************


    // Disabilito il filtro per tutte le richieste al controller Auth, quindi tutte le richieste che avranno come URL /auth/** non dovranno
    // avere il controllo del token
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}


