package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.dto.request.AssociaDissociaInfermiereRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.CreaModificaStrutturaRequest;
import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;
import com.luanatamborrino.SwiftHealthPocket.exception.ConflictException;
import com.luanatamborrino.SwiftHealthPocket.exception.InternalServerErrorException;
import com.luanatamborrino.SwiftHealthPocket.exception.NotFoundException;
import com.luanatamborrino.SwiftHealthPocket.model.Struttura;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.model._enum.Ruolo;
import com.luanatamborrino.SwiftHealthPocket.observer.publisher.Publisher;
import com.luanatamborrino.SwiftHealthPocket.repository.StrutturaRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StrutturaServiceTest {

    @Mock
    StrutturaRepository strutturaRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    Publisher publisher;
    @InjectMocks
    StrutturaService strutturaService;

    @Test
    void createStrutturaSuccessful() {
        CreaModificaStrutturaRequest request = new CreaModificaStrutturaRequest("nome", "indirizzo", 12345);
        assertAll(() -> strutturaService.creaStruttura(request));
    }

    @Test
    void createStrutturaThrowsCapNonValido() {
        CreaModificaStrutturaRequest request = new CreaModificaStrutturaRequest("nome", "indirizzo", 1234);
        assertThrows(BadRequestException.class, () -> strutturaService.creaStruttura(request));
    }

    @Test
    void getStrutturaDataSuccessful() {
        when(strutturaRepository.findById(1L)).thenReturn(Optional.of(
                Struttura.builder()
                .id(1L)
                .nome("nome")
                .indirizzo("indirizzo")
                .cap(12345)
                .build()));
        assertAll(() -> strutturaService.getStrutturaData(1L));
    }

    @Test
    void getStrutturaDataThrowsStrutturaNonTrovata() {
        when(strutturaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> strutturaService.getStrutturaData(1L));
    }

    @Test
    void getAllStruttureSuccessful() {
        when(strutturaRepository.findAll()).thenReturn(List.of(
                Struttura.builder()
                        .id(1L)
                        .nome("nome")
                        .indirizzo("indirizzo")
                        .cap(12345)
                        .build()));
        assertAll(() -> strutturaService.getAllStrutture());
    }

    @Test
    void getAllStruttureThrowsStruttureNonTrovate() {
        when(strutturaRepository.findAll()).thenReturn(List.of());
        assertThrows(NotFoundException.class, () -> strutturaService.getAllStrutture());
    }

    @Test
    void updateStrutturaSuccessful() {
        CreaModificaStrutturaRequest request = new CreaModificaStrutturaRequest("nome", "indirizzo", 12345);
        when(strutturaRepository.findById(1L)).thenReturn(Optional.of(
                Struttura.builder()
                        .id(1L)
                        .nome("nome")
                        .indirizzo("indirizzo")
                        .cap(12345)
                        .build()));
        assertAll(() -> strutturaService.updateStruttura(1L, request));
    }


    @Test
    void updateStrutturaThrowsStrutturaNonTrovata() {
        CreaModificaStrutturaRequest request = new CreaModificaStrutturaRequest("nome", "indirizzo", 12345);
        when(strutturaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> strutturaService.updateStruttura(1L, request));
    }

    @Test
    void deleteStrutturaByIdThrowsStrutturaNonTrovata() {
        when(strutturaRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> strutturaService.deleteStrutturaById(1L));
    }

    @Test
    void deleteStrutturaByIdThrowsErroreEliminazione() {
        when(strutturaRepository.findById(1L))
                .thenReturn(Optional.of(
                    Struttura.builder()
                        .id(1L)
                        .nome("nome")
                        .indirizzo("indirizzo")
                        .cap(12345)
                        .build()
                    )
                );
        assertThrows(InternalServerErrorException.class, () -> strutturaService.deleteStrutturaById(1L));
    }

    @Test
    void associaInfermiereSuccessful() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(
                Utente.builder()
                        .ruolo(Ruolo.INFERMIERE)
                        .build()));
        when(strutturaRepository.findById(1L)).thenReturn(Optional.of(new Struttura()));
        assertAll(() -> strutturaService.associaInfermiere(new AssociaDissociaInfermiereRequest(1L, 1L)));
    }

    @Test
    void associaInfermiereThrowsUtenteGiaAssociato() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(
                Utente.builder()
                        .ruolo(Ruolo.INFERMIERE)
                        .struttura(new Struttura())
                        .build()));
        when(strutturaRepository.findById(1L)).thenReturn(Optional.of(new Struttura()));
        assertThrows(ConflictException.class, () -> strutturaService.associaInfermiere(new AssociaDissociaInfermiereRequest(1L, 1L)));
    }

    @Test
    void associaInfermiereThrowsUtenteNonInfermiere() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(
                Utente.builder()
                        .ruolo(Ruolo.PAZIENTE)
                        .struttura(new Struttura())
                        .build()));
        when(strutturaRepository.findById(1L)).thenReturn(Optional.of(new Struttura()));
        assertThrows(BadRequestException.class, () -> strutturaService.associaInfermiere(new AssociaDissociaInfermiereRequest(1L, 1L)));
    }

    @Test
    void associaInfermiereThrowsStrutturaNonTrovata() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(
                Utente.builder()
                        .ruolo(Ruolo.PAZIENTE)
                        .struttura(new Struttura())
                        .build()));
        when(strutturaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> strutturaService.associaInfermiere(new AssociaDissociaInfermiereRequest(1L, 1L)));
    }

    @Test
    void associaInfermiereThrowsUtenteNonTrovato() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> strutturaService.associaInfermiere(new AssociaDissociaInfermiereRequest(1L, 1L)));
    }

    @Test
    void dissociaInfermiereSuccessful() {
        AssociaDissociaInfermiereRequest request = new AssociaDissociaInfermiereRequest(1L, 1L);
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.INFERMIERE).nome("nome").cognome("cognome").build()));
        when(strutturaRepository.findById(any())).thenReturn(Optional.of(new Struttura()));
        when(userRepository.findByRuolo(any())).thenReturn(Optional.of(Utente.builder().email("email").build()));
        assertAll(() -> strutturaService.dissociaInfermiere(request));
    }

    @Test
    void dissociaInfermiereThrowsAmministratoreNonTrovato() {
        AssociaDissociaInfermiereRequest request = new AssociaDissociaInfermiereRequest(1L, 1L);
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.INFERMIERE).nome("nome").cognome("cognome").build()));
        when(strutturaRepository.findById(any())).thenReturn(Optional.of(new Struttura()));
        when(userRepository.findByRuolo(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> strutturaService.dissociaInfermiere(request));
    }

    @Test
    void dissociaInfermiereThrowsUtenteNonInfermiere() {
        AssociaDissociaInfermiereRequest request = new AssociaDissociaInfermiereRequest(1L, 1L);
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.PAZIENTE).nome("nome").cognome("cognome").build()));
        when(strutturaRepository.findById(any())).thenReturn(Optional.of(new Struttura()));
        assertThrows(BadRequestException.class, () -> strutturaService.dissociaInfermiere(request));
    }

    @Test
    void dissociaInfermiereThrowsStrutturaNonTrovata() {
        AssociaDissociaInfermiereRequest request = new AssociaDissociaInfermiereRequest(1L, 1L);
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.PAZIENTE).nome("nome").cognome("cognome").build()));
        when(strutturaRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> strutturaService.dissociaInfermiere(request));
    }

    @Test
    void dissociaInfermiereThrowsUtenteNonTrovato() {
        AssociaDissociaInfermiereRequest request = new AssociaDissociaInfermiereRequest(1L, 1L);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> strutturaService.dissociaInfermiere(request));
    }
}
