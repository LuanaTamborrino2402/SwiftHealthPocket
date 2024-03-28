package com.luanatamborrino.SwiftHealthPocket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDataRequest {

    private String nome;

    private String cognome;

    private String email;

    private String vecchiaPassword;

    private String nuovaPassword;

}
