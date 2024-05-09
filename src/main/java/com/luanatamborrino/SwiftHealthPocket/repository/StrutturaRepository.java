package com.luanatamborrino.SwiftHealthPocket.repository;

import com.luanatamborrino.SwiftHealthPocket.model.Struttura;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaccia responsabile della comunicazione con il database.
 * Estende l'interfaccia generica JpaRepository che ha come parametri: il tipo della classe e il tipo dell'id.
 */
public interface StrutturaRepository extends JpaRepository<Struttura, Long> {
}
