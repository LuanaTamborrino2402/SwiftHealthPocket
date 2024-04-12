package com.luanatamborrino.SwiftHealthPocket.observer.subscriber;

import com.luanatamborrino.SwiftHealthPocket.dto.request.SendEmailRequest;
import com.luanatamborrino.SwiftHealthPocket.mailing.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class RichiestaCambioStruttura implements Subscriber{

    private final EmailService emailService;

    @Override
    public void update(String nome, String cognome, String tipoPrestazione, String esito, String emailDestinatario) {

        Map<String, String> datiDinamici = new HashMap<>();
        datiDinamici.put("nomeInfermiere",nome);
        datiDinamici.put("cognomeInfermiere",cognome);
        datiDinamici.put("tipoPrestazione",tipoPrestazione);
        datiDinamici.put("esito",esito);

        emailService.sendEmail(new SendEmailRequest(emailDestinatario,"Richiesta cambio struttura", datiDinamici,"RichiestaCambioStruttura"));
    }
}
