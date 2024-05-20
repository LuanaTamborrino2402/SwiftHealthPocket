package com.luanatamborrino.SwiftHealthPocket.dto.response;

import com.luanatamborrino.SwiftHealthPocket.model._enum.EsitoPrestazione;
import com.luanatamborrino.SwiftHealthPocket.model._enum.TipoPrestazione;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PrestazioneResponse {

    private Long idPrestazione;

    private TipoPrestazione tipoPrestazione;

    private EsitoPrestazione esito;

    private LocalDateTime dataInizio;

    private LocalDateTime dataFine;

}
