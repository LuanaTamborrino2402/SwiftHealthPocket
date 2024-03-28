package com.luanatamborrino.SwiftHealthPocket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreaStrutturaRequest {

    private String nome;

    private String indirizzo;

    private Integer cap;
}
