package com.luanatamborrino.SwiftHealthPocket.repository;

import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.model.Struttura;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.model._enum.TipoPrestazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interfaccia responsabile della comunicazione con il database.
 * Estende l'interfaccia generica JpaRepository che ha come parametri: il tipo della classe e il tipo dell'id.
 */
public interface PrestazioneRepository extends JpaRepository<Prestazione, Long> {

    /**
     * Recupera tutte le prestazioni associate a un dato paziente.
     * @param paziente L'oggetto Utente per cui recuperare le prestazioni.
     * @return Una lista di prestazioni associate al paziente specificato.
     */
    List<Prestazione> findAllByPaziente(Utente paziente);

    /**
     * Recupera tutte le prestazioni effettuate in una specifica struttura.
     * @param struttura L'oggetto Struttura per cui recuperare le prestazioni.
     * @return Una lista di prestazioni svolte nella struttura specificata.
     */
    List<Prestazione> findAllByStruttura(Struttura struttura);

    /**
     * Recupera tutte le prestazioni gestite da un specifico infermiere.
     * @param infemiere L'oggetto Utente che rappresenta l'infermiere.
     * @return Una lista di prestazioni gestite dall'infermiere specificato.
     */
    List<Prestazione> findAllByInfermiere(Utente infemiere);

    List<Prestazione> findAllByTipoPrestazione(TipoPrestazione tipoPrestazione);
}
