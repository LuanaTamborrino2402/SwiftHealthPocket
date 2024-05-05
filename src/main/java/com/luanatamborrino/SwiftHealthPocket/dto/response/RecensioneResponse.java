package com.luanatamborrino.SwiftHealthPocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RecensioneResponse {

    private String commento;

    private Integer valutazione;

    private Long idPrestazione;

    private String nome;

    private LocalDateTime data;
}
