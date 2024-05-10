package com.luanatamborrino.SwiftHealthPocket.dto.request;

import lombok.*;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {

    private String emailDestinatario;

    private String oggetto;

    private Map<String, String> datiDinamici;

    private String eventType;
}
