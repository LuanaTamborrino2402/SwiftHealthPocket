package com.luanatamborrino.SwiftHealthPocket.dto.response;

import com.luanatamborrino.SwiftHealthPocket.model._enum.Ruolo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;

    private String nome;

    private String cognome;

    private String password;

    private String email;

    private Ruolo ruolo;
}
