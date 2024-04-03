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
        if(utenteRegistrato.isEmpty()){
            throw new InternalServerErrorException("Registrazione fallita.");
        }
    }

    public LoginResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Utente utente = repository.findByEmail(request.getEmail())
                .orElseThrow();

        return LoginResponse.builder()
                .jwt(jwtService.generateToken(utente))
                .build();
    }

    public HttpHeaders putJwtInHttpHeaders(String jwt) {

        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer: " + jwt);

        return headers;
    }
}
