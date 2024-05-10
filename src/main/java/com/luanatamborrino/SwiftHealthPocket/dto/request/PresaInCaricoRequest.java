package com.luanatamborrino.SwiftHealthPocket.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PresaInCaricoRequest {

    private Long idInfermiere;

    private Long idPrestazione;
}
