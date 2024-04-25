package com.luanatamborrino.SwiftHealthPocket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresaInCaricoRequest {

    private Long idInfermiere;

    private Long idPrestazione;
}
