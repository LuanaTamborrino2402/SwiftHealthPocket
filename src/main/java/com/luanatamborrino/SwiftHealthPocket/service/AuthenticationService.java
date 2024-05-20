package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.dto.response.LoginResponse;
import com.luanatamborrino.SwiftHealthPocket.exception.*;
import com.luanatamborrino.SwiftHealthPocket.security.JwtService;
import com.luanatamborrino.SwiftHealthPocket.dto.request.AuthenticationRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.RegisterRequest;
import com.luanatamborrino.SwiftHealthPocket.model._enum.Ruolo;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import com.luanatamborrino.SwiftHealthPocket.util.Methods;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service per gestire tutti i metodi riguardanti l'autenticazione.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Metodo per registrare un utente sul database.
     * @param request DTO con i dati della registrazione.
     */
    public void register(RegisterRequest request) {

        //Controllo se il ruolo è valido e poi lo assegno.
        Ruolo ruolo;
        if(request.getRuolo().equals("PAZIENTE")) {
             ruolo = Ruolo.PAZIENTE;
        }else if (request.getRuolo().equals("INFERMIERE")) {
            ruolo = Ruolo.INFERMIERE;
        }else{
            throw new BadRequestException("Ruolo non valido.");
        }

        //Invoco checkStringData per verificare la validità dei campi essenziali dell'utente.
        //Assicuro che i valori per nome, cognome, email e password non siano vuoti o nulli.
        Methods.getInstance().checkStringData(List.of(
                request.getNome(),
                request.getCognome(),
                request.getEmail(),
                request.getPassword()
        ));

        //Controllo se è già registrato un utente con quell'indirizzo email.
        Optional<Utente> utenteGiaRegistrato = repository.findByEmail(request.getEmail());

        //Se esiste, lancio un eccezione.
        if(utenteGiaRegistrato.isPresent()) {
            throw new ConflictException("Email già registrata.");
        }

        //Costruisco l'oggetto utente con il pattern builder
        Utente utente = Utente.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .ruolo(ruolo)
                .build();

        //Salvo l'utente creato nel database.
        repository.save(utente);

        //Controllo se l'utente appena registrato è presente effettivamente nel database.
        Optional<Utente> utenteRegistrato = repository.findByEmail(request.getEmail());

        //Se l'utente registrato non esite, lancio l'eccezione.
        if(utenteRegistrato.isEmpty()) {
            throw new InternalServerErrorException("Registrazione fallita.");
        }
    }

    /**
     * Metodo per l'autenticazione dell'utente. Dopo aver seguito i controlli viene generato un jwt per l'utente che si è autenticato.
     * @param request DTO con i dati per la login.
     * @return Response contenente il token di autenticazione jwt.
     */
    public LoginResponse authenticate(AuthenticationRequest request) {

        //Effettuo l'autenticazione dell'utente utilizzando l'authenticationManager.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        //Cerco l'utente nel database utilizzando l'indirizzo email fornito nel DTO di autenticazione.
        Utente utente = repository.findByEmail(request.getEmail())
                .orElseThrow();

        //Restituisco una risposta di login contenente il token jwt generato per l'utente.
        return new LoginResponse(
                "Il tuo accesso è stato verificato con successo e sei ora connesso al sistema.",
                jwtService.generateToken(utente)
        );
    }

    /**
     * Metodo aggiunge il token JWT all'header HTTP.
     * @param jwt Il token JWT da inserire nell'header.
     * @return L'oggetto HttpHeaders con il token al suo interno.
     */
    public HttpHeaders putJwtInHttpHeaders(String jwt) {

        //Creo un nuovo oggetto HttpHeaders per impostare l'header HTTP.
        HttpHeaders headers = new HttpHeaders();

        //Aggiungo il token JWT all'header Authorization con lo schema "Bearer".
        headers.add("Authorization", "Bearer: " + jwt);

        //Restituisco gli HttpHeaders contenenti l'header Authorization con il token JWT.
        return headers;
    }
}
