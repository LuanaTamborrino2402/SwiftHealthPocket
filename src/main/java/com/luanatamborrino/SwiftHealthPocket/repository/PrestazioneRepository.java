package com.luanatamborrino.SwiftHealthPocket.repository;

import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.model.Struttura;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestazioneRepository extends JpaRepository<Prestazione, Long> {

    List<Prestazione> findAllByPaziente(Utente paziente);

    List<Prestazione> findAllByStruttura(Struttura struttura);

    List<Prestazione> findAllByInfermiere(Utente infemiere);


}
