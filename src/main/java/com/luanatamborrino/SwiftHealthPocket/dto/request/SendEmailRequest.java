package com.luanatamborrino.SwiftHealthPocket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class SendEmailRequest {

    private String emailDestinatario;

    private String oggetto;

    private Map<String, String> datiDinamici;

    private String eventType;
}
