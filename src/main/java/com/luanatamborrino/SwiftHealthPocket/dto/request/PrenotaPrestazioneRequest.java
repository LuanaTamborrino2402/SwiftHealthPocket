package com.luanatamborrino.SwiftHealthPocket.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrenotaPrestazioneRequest {

    private String tipoPrestazione;

    private LocalDateTime dataInizio;

    private Long idPaziente;

    private Long idStruttura;
}
