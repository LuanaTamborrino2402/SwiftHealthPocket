package com.luanatamborrino.SwiftHealthPocket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecensioneRequest {

    private Integer valutazione;

    private String commento;

    private Long idPaziente;
}
