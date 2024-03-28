package com.luanatamborrino.SwiftHealthPocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrutturaResponse {

    private Long id;

    private String nome;

    private String indirizzo;

    private Integer cap;
}
