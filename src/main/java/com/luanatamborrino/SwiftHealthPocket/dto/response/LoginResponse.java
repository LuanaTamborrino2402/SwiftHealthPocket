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
public class LoginResponse {

    private String message;

    private String jwt;
}