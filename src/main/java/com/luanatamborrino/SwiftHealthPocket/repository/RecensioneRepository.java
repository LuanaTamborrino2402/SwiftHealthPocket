package com.luanatamborrino.SwiftHealthPocket.repository;

import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.model.Recensione;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interfaccia responsabile della comunicazione con il database.
 * Estende l'interfaccia generica JpaRepository che ha come parametri: il tipo della classe e il tipo dell'id.
 */
public interface RecensioneRepository extends JpaRepository<Recensione, Long> {

    /**
     * Trova tutte le recensioni associate a un paziente specifico.
     * @param paziente L'oggetto paziente per cui recuperare le recensioni.
     * @return Una lista di recensioni trovate per il paziente specificato.
     */
    List<Recensione> findAllByPaziente(Utente paziente);

    /**
     * Trova la recensione associata a una specifica prestazione.
     * @param prestazione L'oggetto prestazione per cui recuperare la recensione.
     * @return La recensione trovata per la prestazione specificata o null se non trovata.
     */
    Recensione findByPrestazione(Prestazione prestazione);

}
