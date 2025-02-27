package com.luanatamborrino.SwiftHealthPocket.observer.subscriber;

import com.luanatamborrino.SwiftHealthPocket.dto.request.SendEmailRequest;
import com.luanatamborrino.SwiftHealthPocket.mailing.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementazione dell'interfaccia del subscriber che gestisce le notifiche di richieste di cambio struttura.
 */
@Component
@RequiredArgsConstructor
public class RichiestaCambioStruttura implements Subscriber{

    private final EmailService emailService;

    /**
     * Invia un'email al destinatario specificato con informazioni sul personale infermieristico
     * coinvolto e dettagli della richiesta.Questo metodo viene chiamato quando si verifica un evento di cambio struttura.
     * @param nome Nome dell'infermiere coinvolto nella richiesta.
     * @param cognome Cognome dell'infermiere coinvolto nella richiesta.
     * @param tipoPrestazione Tipo di prestazione, non utilizzato in questo contesto.
     * @param esito Esito della prestazione, non utilizzato in questo contesto.
     * @param emailDestinatario Indirizzo email del destinatario a cui inviare i dettagli della richiesta di cambio.
     */
    @Override
    public void update(String nome, String cognome, String tipoPrestazione, String esito, String emailDestinatario) {

        //Creazione di una HashMap per i dati dinamici che saranno inclusi nell'email.
        Map<String, String> datiDinamici = new HashMap<>();

        //Aggiungo i dati e stabilisco una chiave tramite la quale accedo all'interno del template.
        datiDinamici.put("nomeInfermiere",nome);
        datiDinamici.put("cognomeInfermiere",cognome);

        //Invoco il service adatto per l'invio delle mail passandogli il DTO con tutti i dati.
        emailService.sendEmail(new SendEmailRequest(
                emailDestinatario,
                "Richiesta cambio struttura",
                datiDinamici,
                "RichiestaCambioStruttura"
        ));
    }
}
