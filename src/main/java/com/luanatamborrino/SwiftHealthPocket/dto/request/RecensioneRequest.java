package com.luanatamborrino.SwiftHealthPocket.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecensioneRequest {

    private Integer valutazione;

    private String commento;

    private Long idPaziente;
}
