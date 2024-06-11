package com.luanatamborrino.SwiftHealthPocket.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Classe che implementa metodi eseguiti ad ogni richiesta al server.
 * Estende OncePerRequestFilter per configurare il comportamento specificato.
 * (Alternativamente, si può implementare l'interfaccia Filter).
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Questo metodo opera in modo analogo al doFilter tradizionale,
     * garantendo però l'esecuzione una sola volta per richiesta.
     * @param request Effettiva richiesta del client dalla quale possiamo estrarre i dati.
     * @param response Effettiva risposta del server che può contenere eventuali dati.
     * @param filterChain Contiene la lista dei filtri da eseguire. Si tratta del Chain of Responsibility pattern.
     * @throws ServletException Generata quando vi è un errore generico su una servlet.
     * @throws IOException Generata quando si va incontro a problemi di operazioni I/O.
     */
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization"); //Prendo il jwt dall'header della richiesta.
        final String jwt;
        final String userEmail;

        // Se l'header di autorizzazione è assente o un altro tipo di errore, continua con la catena di filtri e esci dal metodo.
        if(authHeader == null ||!authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response); //Passo la request e la response al prossimo filtro nella catena.
            return;
        }

        jwt = authHeader.substring(7); //Estraggo il JWT dall'header.
        userEmail = jwtService.extractUsername(jwt); //Estraggo l'username codificato nel jwt.

         /* Se l'email dell'utente esiste e non è autenticato,
            verifico se esiste un utente nel database con questo indirizzo email. */
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //Prendo dal db i dati dell'utente con quell'inidirzzo email.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            //Verifico se il token è valido.
            if(jwtService.isTokenValid(jwt, userDetails)) {
                //Aggiorno il token con i dati dell'utente.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                //Imposto i dati da codificare.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Aggiorno il token in sessione.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //Continuo con la catena di filtri da eseguire.
        filterChain.doFilter(request, response);
    }
}
