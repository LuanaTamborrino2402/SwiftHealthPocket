package com.luanatamborrino.SwiftHealthPocket.observer.subscriber;

import com.luanatamborrino.SwiftHealthPocket.dto.request.SendEmailRequest;
import com.luanatamborrino.SwiftHealthPocket.mailing.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementazione dell'interfaccia del subscriber che gestisce le notifiche di dissociazione degli infermieri.

 */
@Component
@RequiredArgsConstructor
public class InfermiereDissociato implements Subscriber {

    private final EmailService emailService;

    //TODO CHIEDERE
    /**
     * Metodo invocato quando un infermiere viene dissociato da una prestazione. Questo metodo prepara e invia una notifica email
     * contenente i dettagli dell'infermiere dissociato.
     * @param nome Nome dell'infermiere dissociato.
     * @param cognome Cognome dell'infermiere dissociato.
     * @param tipoPrestazione Tipo di prestazione, non utilizzato in questo contesto.
     * @param esito Esito della prestazione, non utilizzato in questo contesto.
     * @param emailDestinatario Indirizzo email al quale inviare la notifica di dissociazione.
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
                "Nuovo infermiere dissociato",
                datiDinamici,
                "InfermiereDissociato"));
    }
}
