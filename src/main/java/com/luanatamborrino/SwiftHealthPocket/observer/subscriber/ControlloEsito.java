package com.luanatamborrino.SwiftHealthPocket.observer.subscriber;

import com.luanatamborrino.SwiftHealthPocket.dto.request.SendEmailRequest;
import com.luanatamborrino.SwiftHealthPocket.mailing.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementazione dell'interfaccia del subscriber che gestisce le notifiche degli esiti delle prestazioni,
 * inviando email dettagliate ai destinatari appropriati.
 */
@Component
@RequiredArgsConstructor
public class ControlloEsito implements Subscriber {

    private final EmailService emailService;

    /**
     * Invia un'email al paziente per notificare dell'esito della prestazione effetutata.
     * Questo metodo viene chiamato quando si verifica un evento a cui questo subscriber è iscritto.
     * @param nome Nome del paziente.
     * @param cognome Cognome del paziente.
     * @param tipoPrestazione Tipo della prestazione effettuata.
     * @param esito Risultato dell'esito della prestazione.
     * @param emailDestinatario Indirizzo email del paziente a cui inviare i dettagli dell'esito.
     */
    @Override
    public void update(String nome, String cognome, String tipoPrestazione, String esito, String emailDestinatario) {

        //Creazione di una HashMap per i dati dinamici che saranno inclusi nell'email.
        Map<String, String> datiDinamici = new HashMap<>();

        //Aggiungo i dati e stabilisco una chiave tramite la quale accedo all'interno del template.
        datiDinamici.put("tipoPrestazione",tipoPrestazione);
        datiDinamici.put("esito",esito);

        //Invoco il service adatto per l'invio delle mail passandogli il DTO con tutti i dati.
        emailService.sendEmail(new SendEmailRequest(
                emailDestinatario,
                "Esito prestazione",
                datiDinamici,
                "ControlloEsito"
        ));
    }
}
