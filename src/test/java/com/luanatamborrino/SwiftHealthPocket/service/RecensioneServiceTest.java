package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.dto.request.RecensioneRequest;
import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;
import com.luanatamborrino.SwiftHealthPocket.exception.NotFoundException;
import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.model.Recensione;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.model._enum.EsitoPrestazione;
import com.luanatamborrino.SwiftHealthPocket.model._enum.Ruolo;
import com.luanatamborrino.SwiftHealthPocket.repository.PrestazioneRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.RecensioneRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecensioneServiceTest {

    @Mock
    RecensioneRepository recensioneRepository;
    @Mock
    PrestazioneRepository prestazioneRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    RecensioneService recensioneService;

    @Test
    void salvaSuccessful() {
        RecensioneRequest request = new RecensioneRequest(4, "commento", 1L);
        when(prestazioneRepository.findById(1L)).thenReturn(Optional.of(
                Prestazione.builder()
                        .esito(EsitoPrestazione.TAMPONE_NEGATIVO)
                        .recensione(new Recensione())
                        .build()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.PAZIENTE).build()));
        assertAll(() -> recensioneService.salva(1L, request));
    }

    @Test
    void salvaThrowsEsitoNonPresente() {
        RecensioneRequest request = new RecensioneRequest(4, "commento", 1L);
        when(prestazioneRepository.findById(1L)).thenReturn(Optional.of(Prestazione.builder().recensione(new Recensione()).build()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.PAZIENTE).build()));
        assertThrows(BadRequestException.class, () -> recensioneService.salva(1L, request));
    }

    @Test
    void salvaThrowsValutazioneNonValida() {
        RecensioneRequest request = new RecensioneRequest(6, "commento", 1L);
        when(prestazioneRepository.findById(1L)).thenReturn(Optional.of(Prestazione.builder().recensione(new Recensione()).build()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.PAZIENTE).build()));
        assertThrows(BadRequestException.class, () -> recensioneService.salva(1L, request));
    }

    @Test
    void salvaThrowsCommentoNonValido() {
        String commento = "";
        for(int i=0; i<1002; i++) {
            commento += "a";
        }
        RecensioneRequest request = new RecensioneRequest(6, commento, 1L);
        when(prestazioneRepository.findById(1L)).thenReturn(Optional.of(Prestazione.builder().recensione(new Recensione()).build()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.PAZIENTE).build()));
        assertThrows(BadRequestException.class, () -> recensioneService.salva(1L, request));
    }

    @Test
    void salvaThrowsRuoloNonCorretto() {
        RecensioneRequest request = new RecensioneRequest(6, "commento", 1L);
        when(prestazioneRepository.findById(1L)).thenReturn(Optional.of(Prestazione.builder().recensione(new Recensione()).build()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.INFERMIERE).build()));
        assertThrows(BadRequestException.class, () -> recensioneService.salva(1L, request));
    }

    @Test
    void salvaThrowsPazienteNonTrovato() {
        RecensioneRequest request = new RecensioneRequest(6, "commento", 1L);
        when(prestazioneRepository.findById(1L)).thenReturn(Optional.of(Prestazione.builder().recensione(new Recensione()).build()));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> recensioneService.salva(1L, request));
    }

    @Test
    void salvaThrowsPrestazioneNonTrovata() {
        RecensioneRequest request = new RecensioneRequest(6, "commento", 1L);
        when(prestazioneRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> recensioneService.salva(1L, request));
    }

    @Test
    void getAllByPazienteSuccessful() {
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.PAZIENTE).build()));
        when(recensioneRepository.findAllByPaziente(any())).thenReturn(List.of(
                Recensione.builder()
                        .commento("commento")
                        .valutazione(3)
                        .prestazione(new Prestazione())
                        .paziente(Utente.builder().nome("nome").build())
                        .data(LocalDateTime.now())
                        .build()));
        assertAll(() -> recensioneService.getAllByPaziente(1L));
    }

    @Test
    void getAllByPazienteThrowsRuoloNonCorretto() {
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.INFERMIERE).build()));
        assertThrows(BadRequestException.class, () -> recensioneService.getAllByPaziente(1L));
    }

    @Test
    void getAllByPazienteThrowsPazienteNonTrovato() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> recensioneService.getAllByPaziente(1L));
    }

    @Test
    void getByPrestazioneIdSuccessful() {
        when(prestazioneRepository.findById(any())).thenReturn(Optional.of(new Prestazione()));
        when(recensioneRepository.findByPrestazione(any())).thenReturn(
                Recensione.builder()
                        .commento("commento")
                        .valutazione(3)
                        .prestazione(new Prestazione())
                        .paziente(Utente.builder().nome("nome").build())
                        .data(LocalDateTime.now())
                        .build());
        assertAll(() -> recensioneService.getByPrestazioneId(1L));
    }

    @Test
    void getByPrestazioneIdThrowsPrestazioneNonTrovata() {
        when(prestazioneRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> recensioneService.getByPrestazioneId(1L));
    }
}
