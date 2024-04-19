package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.dto.response.LoginResponse;
import com.luanatamborrino.SwiftHealthPocket.exception.*;
import com.luanatamborrino.SwiftHealthPocket.security.JwtService;
import com.luanatamborrino.SwiftHealthPocket.dto.request.AuthenticationRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.RegisterRequest;
import com.luanatamborrino.SwiftHealthPocket.model._enum.Ruolo;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
     * Metodo per registrare un utente nel database.
     * @param request DTO con i dati della registrazione.
     */
    public void register(RegisterRequest request) {

        //Controllo se il ruolo è valido e poi lo assegno.
        Ruolo ruolo;
        if(request.getRuolo().equals("PAZIENTE")){
             ruolo = Ruolo.PAZIENTE;
        }else if (request.getRuolo().equals("INFERMIERE")) {
            ruolo = Ruolo.INFERMIERE;
        }else{
            throw new BadRequestException("Ruolo non valido.");
        }

        //Controllo se è già registrato un utente con quella email.
        Optional<Utente> utenteGiaRegistrato = repository.findByEmail(request.getEmail());
        if(utenteGiaRegistrato.isPresent()){
            throw new ConflictException("Email già registrata.");
        }

        //Controllo se almeno un campo è vuoto.
        if(request.getNome().isEmpty() || request.getNome().isBlank() ||
                request.getPassword().isEmpty() || request.getPassword().isBlank() ||
                request.getEmail().isEmpty() || request.getEmail().isBlank() ||
                request.getCognome().isEmpty() || request.getCognome().isBlank()){
            throw new BadRequestException("Campo non inserito.");
        }

        //Creo un utente.
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
        if(utenteRegistrato.isEmpty()){
            throw new InternalServerErrorException("Registrazione fallita.");
        }
    }

    /**
     * Metodo per l'autenticazione dell'utente. Dopo aver seguito i controlli viene generato un jwt per l'utente che si è autenticato.
     * @param request DTO con i dati per la login.
     * @return Response contenente il token di autenticazione jwt.
     */
    public LoginResponse authenticate(AuthenticationRequest request) {

        //Effettua l'autenticazione dell'utente utilizzando l'authenticationManager.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        //Cerco l'utente nel database utilizzando l'email fornita nel DTO di autenticazione.
        Utente utente = repository.findByEmail(request.getEmail())
                .orElseThrow();

        //Restituisce una risposta di login contenente il token jwt generato per l'utente.
        return LoginResponse.builder()
                .jwt(jwtService.generateToken(utente))
                .build();
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
