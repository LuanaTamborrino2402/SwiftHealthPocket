package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.dto.response.UserResponse;
import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;
import com.luanatamborrino.SwiftHealthPocket.exception.NotFoundException;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtenteService {

    private final UserRepository repository;

    public UserResponse getUserData(Long id){

        //controllo che l'id parta da 1.
        if(id < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Controllo se è presente un utente con quell'id.
        Optional<Utente> user = repository.findById(id);

        //Se non esiste lancio l'eccezione.
        if(user.isEmpty()){
            throw new NotFoundException("Utente non trovato.");
        }

        return new UserResponse(user.get().getIdUtente(),
                user.get().getNome(),
                user.get().getCognome(),
                user.get().getPassword(),
                user.get().getEmail(),
                user.get().getRuolo());
    }

}
