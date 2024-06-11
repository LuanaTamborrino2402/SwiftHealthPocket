package com.luanatamborrino.SwiftHealthPocket.dto;

import com.luanatamborrino.SwiftHealthPocket.dto.response.*;
import com.luanatamborrino.SwiftHealthPocket.model._enum.EsitoPrestazione;
import com.luanatamborrino.SwiftHealthPocket.model._enum.Ruolo;
import com.luanatamborrino.SwiftHealthPocket.model._enum.TipoPrestazione;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class ResponseTest {

    @Test
    void userResponse() {
        UserResponse u = new UserResponse();
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .nome("Luana")
                .cognome("Tamborrino")
                .password("password")
                .email("email")
                .ruolo(Ruolo.PAZIENTE)
                .build();
        Long id = userResponse.getId();
        String nome = userResponse.getNome();
        String cognome = userResponse.getCognome();
        String password = userResponse.getPassword();
        String email = userResponse.getEmail();
        Ruolo ruolo = userResponse.getRuolo();
    }

    @Test
    void strutturaResponse() {
        StrutturaResponse s = new StrutturaResponse();
        StrutturaResponse strutturaResponse = StrutturaResponse.builder()
                .id(1L)
                .nome("Ospedale")
                .indirizzo("Via Roma")
                .cap(12345)
                .build();
        Long id = strutturaResponse.getId();
        String nome = strutturaResponse.getNome();
        String indirizzo = strutturaResponse.getIndirizzo();
        Integer cap = strutturaResponse.getCap();
    }

    @Test
    void recensioneResponse() {
        RecensioneResponse recensioneResponse = new RecensioneResponse("commento", 5, 1L, "Luana", LocalDateTime.now());
        String commento = recensioneResponse.getCommento();
        Integer valutazione = recensioneResponse.getValutazione();
        Long idPrestazione = recensioneResponse.getIdPrestazione();
        String nome = recensioneResponse.getNome();
        LocalDateTime data = recensioneResponse.getData();
    }

    @Test
    void prestazioneResponse() {
        PrestazioneResponse prestazioneResponse = new PrestazioneResponse(1L, TipoPrestazione.TAMPONE, EsitoPrestazione.TAMPONE_NEGATIVO, LocalDateTime.now(), LocalDateTime.now());
        Long idPrestazione = prestazioneResponse.getIdPrestazione();
        TipoPrestazione tipoPrestazione = prestazioneResponse.getTipoPrestazione();
        EsitoPrestazione esito = prestazioneResponse.getEsito();
        LocalDateTime dataInizio = prestazioneResponse.getDataInizio();
        LocalDateTime dataFine = prestazioneResponse.getDataFine();
    }

    @Test
    void messageResponse() {
        MessageResponse m = new MessageResponse();
        MessageResponse messageResponse = MessageResponse.builder().message("ok").build();
        String message = messageResponse.getMessage();
    }

    @Test
    void loginResponse() {
        LoginResponse l = new LoginResponse();
        LoginResponse loginResponse = LoginResponse.builder().message("ok").jwt("jwt").build();
        String message = loginResponse.getMessage();
        String jwt = loginResponse.getJwt();
    }
}
