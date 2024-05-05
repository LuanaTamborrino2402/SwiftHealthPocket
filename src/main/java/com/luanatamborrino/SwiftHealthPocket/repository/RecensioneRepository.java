package com.luanatamborrino.SwiftHealthPocket.repository;

import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.model.Recensione;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecensioneRepository extends JpaRepository<Recensione, Long> {

    List<Recensione> findAllByPaziente(Utente paziente);

    Recensione findByPrestazione(Prestazione prestazione);

}
