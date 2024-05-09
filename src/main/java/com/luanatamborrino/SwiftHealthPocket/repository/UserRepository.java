package com.luanatamborrino.SwiftHealthPocket.repository;

import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.model._enum.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaccia responsabile della comunicazione con il database.
 * Estende l'interfaccia generica JpaRepository che ha come parametri: il tipo della classe e il tipo dell'id.
 */
public interface UserRepository extends JpaRepository<Utente,Long> {

    /**
     * Cerca un'utente nel database basato sull'email fornita come parametro.
     * @param email Email univoca dell'utente.
     * @return L'utente con quell'email.
     */
    Optional<Utente> findByEmail(String email);

    /**
     * Cerca tutti gli utenti nel database basati sul ruolo fornito come parametro.
     * @param ruolo Ruolo dell'utente.
     * @return Lista di utenti con quel ruolo.
     */
    List<Utente> findAllByRuolo(Ruolo ruolo);

    /**
     * Cerca un'utente nel database basato sul ruolo fornito come parametro.
     * @param ruolo Ruolo dell'utente.
     * @return L'utente con quel ruolo.
     */
    Optional<Utente> findByRuolo(Ruolo ruolo);

}
