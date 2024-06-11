package com.luanatamborrino.SwiftHealthPocket.dto;

import com.luanatamborrino.SwiftHealthPocket.dto.request.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class RequestTest {

    @Test
    void updateUserDataRequest() {
        UpdateUserDataRequest updateUserDataRequest = UpdateUserDataRequest.builder()
                .nome("Luana")
                .cognome("Tamborrino")
                .email("email")
                .vecchiaPassword("vecchiaPassword")
                .nuovaPassword("nuovaPassword")
                .build();
        String nome = updateUserDataRequest.getNome();
        String cognome = updateUserDataRequest.getCognome();
        String email = updateUserDataRequest.getEmail();
        String vecchiaPassword = updateUserDataRequest.getVecchiaPassword();
        String nuovaPassword = updateUserDataRequest.getNuovaPassword();
    }

    @Test
    void sendEmailRequest() {
        SendEmailRequest s = new SendEmailRequest();
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .emailDestinatario("email")
                .oggetto("subject")
                .datiDinamici(new HashMap<>())
                .eventType("eventType")
                .build();
        String emailDestinatario = sendEmailRequest.getEmailDestinatario();
        String oggetto = sendEmailRequest.getOggetto();
        Map<String, String> datiDinamici = sendEmailRequest.getDatiDinamici();
        String eventType = sendEmailRequest.getEventType();
    }

    @Test
    void registerRequest() {
        RegisterRequest r = new RegisterRequest();
        RegisterRequest registerRequest = RegisterRequest.builder()
                .nome("Luana")
                .cognome("Tamborrino")
                .email("email")
                .password("password")
                .ruolo("ruolo")
                .build();
        String nome = registerRequest.getNome();
        String cognome = registerRequest.getCognome();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        String ruolo = registerRequest.getRuolo();
    }

    @Test
    void recensioneRequest() {
        RecensioneRequest r = new RecensioneRequest();
        RecensioneRequest recensioneRequest = RecensioneRequest.builder()
                .valutazione(5)
                .commento("commento")
                .idPaziente(1L)
                .build();
        Integer valutazione = recensioneRequest.getValutazione();
        String commento = recensioneRequest.getCommento();
        Long idPaziente = recensioneRequest.getIdPaziente();
    }

    @Test
    void preseInCaricoRequest() {
        PresaInCaricoRequest p = new PresaInCaricoRequest();
        PresaInCaricoRequest preseInCaricoRequest = PresaInCaricoRequest.builder()
                .idPrestazione(1L)
                .idInfermiere(1L)
                .build();
        Long idPrestazione = preseInCaricoRequest.getIdPrestazione();
        Long idInfermiere = preseInCaricoRequest.getIdInfermiere();
    }

    @Test
    void prenotaPrestazioneRequest() {
        PrenotaPrestazioneRequest p = new PrenotaPrestazioneRequest();
        PrenotaPrestazioneRequest prenotaPrestazioneRequest = PrenotaPrestazioneRequest.builder()
                .tipoPrestazione("tipoPrestazione")
                .dataInizio(null)
                .idPaziente(1L)
                .idStruttura(1L)
                .build();
        String tipoPrestazione = prenotaPrestazioneRequest.getTipoPrestazione();
        Long idPaziente = prenotaPrestazioneRequest.getIdPaziente();
        Long idStruttura = prenotaPrestazioneRequest.getIdStruttura();
    }

    @Test
    void esitoRequest() {
        EsitoRequest e = new EsitoRequest();
        EsitoRequest esitoRequest = EsitoRequest.builder()
                .esito("esito")
                .build();
        String esito = esitoRequest.getEsito();
    }

    @Test
    void authenticationRequest() {
        AuthenticationRequest a = new AuthenticationRequest();
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .email("email")
                .password("password")
                .build();
        String email = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();
    }

    @Test
    void creaModificaStrutturaRequest() {
        CreaModificaStrutturaRequest c = new CreaModificaStrutturaRequest();
        CreaModificaStrutturaRequest creaModificaStrutturaRequest = CreaModificaStrutturaRequest.builder()
                .nome("nome")
                .indirizzo("indirizzo")
                .cap(1)
                .build();
        String nome = creaModificaStrutturaRequest.getNome();
        String indirizzo = creaModificaStrutturaRequest.getIndirizzo();
        Integer cap = creaModificaStrutturaRequest.getCap();
    }
}
