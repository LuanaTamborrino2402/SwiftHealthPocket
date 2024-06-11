package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.dto.request.EsitoRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.PrenotaPrestazioneRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.PresaInCaricoRequest;
import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;
import com.luanatamborrino.SwiftHealthPocket.exception.InternalServerErrorException;
import com.luanatamborrino.SwiftHealthPocket.exception.NotFoundException;
import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.model.Struttura;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.model._enum.EsitoPrestazione;
import com.luanatamborrino.SwiftHealthPocket.model._enum.Ruolo;
import com.luanatamborrino.SwiftHealthPocket.model._enum.TipoPrestazione;
import com.luanatamborrino.SwiftHealthPocket.observer.publisher.Publisher;
import com.luanatamborrino.SwiftHealthPocket.repository.PrestazioneRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.StrutturaRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
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
public class PrestazioneServiceTest {

    @Mock
    PrestazioneRepository prestazioneRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    StrutturaRepository strutturaRepository;
    @Mock
    Publisher publisher;
    @InjectMocks
    PrestazioneService prestazioneService;

    @Test
    void prenotaPrestazioneSuccessful() {
        PrenotaPrestazioneRequest request = new PrenotaPrestazioneRequest(
                "TAMPONE",
                LocalDateTime.now().plusDays(5),
                1L,
                1L
        );
        when(userRepository.findById(any())).thenReturn(Optional.of(new Utente()));
        when(strutturaRepository.findById(any())).thenReturn(Optional.of(new Struttura()));
        assertAll(() -> prestazioneService.prenotaPrestazione(request));
    }

    @Test
    void prenotaPrestazioneThrowsDataNelPassato() {
        PrenotaPrestazioneRequest request = new PrenotaPrestazioneRequest(
                "VACCINO",
                LocalDateTime.now().minusDays(3),
                1L,
                1L
        );
        when(userRepository.findById(any())).thenReturn(Optional.of(new Utente()));
        when(strutturaRepository.findById(any())).thenReturn(Optional.of(new Struttura()));
        assertThrows(BadRequestException.class, () -> prestazioneService.prenotaPrestazione(request));
    }

    @Test
    void prenotaPrestazioneThrowsTipoPrestazioneNonTrovato() {
        PrenotaPrestazioneRequest request = new PrenotaPrestazioneRequest(
                "TAMPONE",
                LocalDateTime.now().minusDays(3),
                1L,
                1L
        );
        when(userRepository.findById(any())).thenReturn(Optional.of(new Utente()));
        when(strutturaRepository.findById(any())).thenReturn(Optional.of(new Struttura()));
        assertThrows(BadRequestException.class, () -> prestazioneService.prenotaPrestazione(request));
    }

    @Test
    void prenotaPrestazioneThrowsStrutturaNonTrovata() {
        PrenotaPrestazioneRequest request = new PrenotaPrestazioneRequest(
                "TAMPONE",
                LocalDateTime.now().minusDays(3),
                1L,
                1L
        );
        when(userRepository.findById(any())).thenReturn(Optional.of(new Utente()));
        when(strutturaRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> prestazioneService.prenotaPrestazione(request));
    }

    @Test
    void prenotaPrestazioneThrowsUtenteNonTrovato() {
        PrenotaPrestazioneRequest request = new PrenotaPrestazioneRequest(
                "TAMPONE",
                LocalDateTime.now().minusDays(3),
                1L,
                1L
        );
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> prestazioneService.prenotaPrestazione(request));
    }

    @Test
    void getAllByPazienteSuccessful() {
        when(userRepository.findById(any())).thenReturn(Optional.of(new Utente()));
        when(prestazioneRepository.findAllByPaziente(any())).thenReturn(List.of(Prestazione.builder().dataInizio(LocalDateTime.now().plusDays(3)).build()));
        assertAll(() -> prestazioneService.getAllByPaziente(1L));
    }

    @Test
    void getAllByPazienteUtenteNonTrovato() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> prestazioneService.getAllByPaziente(1L));
    }

    @Test
    void eliminaPrenotazioneSuccessful() {
        when(prestazioneRepository.findById(any())).thenReturn(Optional.of(Prestazione.builder().dataInizio(LocalDateTime.now().plusDays(3)).build()));
        when(prestazioneRepository.existsById(any())).thenReturn(false);
        assertAll(() -> prestazioneService.eliminaPrenotazione(1L));
    }

    @Test
    void eliminaPrenotazioneThrowsErroreNellEliminazione() {
        when(prestazioneRepository.findById(any())).thenReturn(Optional.of(Prestazione.builder().dataInizio(LocalDateTime.now().plusDays(3)).build()));
        when(prestazioneRepository.existsById(any())).thenReturn(true);
        assertThrows(InternalServerErrorException.class, () -> prestazioneService.eliminaPrenotazione(1L));
    }

    @Test
    void eliminaPrenotazioneThrowsDataNonValida() {
        when(prestazioneRepository.findById(any())).thenReturn(Optional.of(Prestazione.builder().dataInizio(LocalDateTime.now()).build()));
        assertThrows(BadRequestException.class, () -> prestazioneService.eliminaPrenotazione(1L));
    }

    @Test
    void eliminaPrenotazioneThrowsPrestazioneNonTrovata() {
        when(prestazioneRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> prestazioneService.eliminaPrenotazione(1L));
    }

    @Test
    void getAllPrenotazioniSuccessful() {
        when(strutturaRepository.findById(any())).thenReturn(Optional.of(new Struttura()));
        when(prestazioneRepository.findAllByStruttura(any())).thenReturn(List.of(Prestazione.builder().dataInizio(LocalDateTime.now().plusDays(3)).build()));
        assertAll(() -> prestazioneService.getAllPrenotazioni(1L));
    }

    @Test
    void getAllPrenotazioniThrowsStrutturaNonTrovata() {
        when(strutturaRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> prestazioneService.getAllPrenotazioni(1L));
    }

    @Test
    void presaInCaricoSuccessful() {
        PresaInCaricoRequest request = new PresaInCaricoRequest(1L, 1L);
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.INFERMIERE).build()));
        when(prestazioneRepository.findById(any())).thenReturn(Optional.of(new Prestazione()));
        assertAll(() -> prestazioneService.presaInCarico(request));
    }

    @Test
    void presaInCaricoThrowsPrestazioneNonTrovata() {
        PresaInCaricoRequest request = new PresaInCaricoRequest(1L, 1L);
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.INFERMIERE).build()));
        when(prestazioneRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> prestazioneService.presaInCarico(request));
    }

    @Test
    void presaInCaricoThrowsRuoloNonValido() {
        PresaInCaricoRequest request = new PresaInCaricoRequest(1L, 1L);
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.PAZIENTE).build()));
        assertThrows(BadRequestException.class, () -> prestazioneService.presaInCarico(request));
    }

    @Test
    void presaInCaricoThrowsInfermiereNonTrovato() {
        PresaInCaricoRequest request = new PresaInCaricoRequest(1L, 1L);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> prestazioneService.presaInCarico(request));
    }

    @Test
    void esitoSuccessfulTamponePositivo() {
        EsitoRequest request = new EsitoRequest("TAMPONE_POSITIVO");
        when(prestazioneRepository.findById(any())).thenReturn(Optional.of(Prestazione.builder()
                .paziente(Utente.builder().email("email").build())
                .infermiere(new Utente())
                .tipoPrestazione(TipoPrestazione.TAMPONE)
                .dataFine(LocalDateTime.now().minusDays(2))
                .build()));
        assertAll(() -> prestazioneService.esito(1L, request));
    }

    @Test
    void esitoSuccessfulTamponeNegativo() {
        EsitoRequest request = new EsitoRequest("TAMPONE_NEGATIVO");
        when(prestazioneRepository.findById(any())).thenReturn(Optional.of(Prestazione.builder()
                .paziente(Utente.builder().email("email").build())
                .infermiere(new Utente())
                .tipoPrestazione(TipoPrestazione.TAMPONE)
                .dataFine(LocalDateTime.now().minusDays(2))
                .build()));
        assertAll(() -> prestazioneService.esito(1L, request));
    }

    @Test
    void esitoSuccessfulVaccinoEffettuato() {
        EsitoRequest request = new EsitoRequest("VACCINO_EFFETTUATO");
        when(prestazioneRepository.findById(any())).thenReturn(Optional.of(Prestazione.builder()
                .paziente(Utente.builder().email("email").build())
                .infermiere(new Utente())
                .tipoPrestazione(TipoPrestazione.VACCINO)
                .dataFine(LocalDateTime.now().minusDays(2))
                .build()));
        assertAll(() -> prestazioneService.esito(1L, request));
    }

    @Test
    void esitoSuccessfulThrowsEsitoNonValido() {
        EsitoRequest request = new EsitoRequest("okk");
        when(prestazioneRepository.findById(any())).thenReturn(Optional.of(Prestazione.builder()
                .paziente(Utente.builder().email("email").build())
                .infermiere(new Utente())
                .tipoPrestazione(TipoPrestazione.VACCINO)
                .dataFine(LocalDateTime.now().minusDays(2))
                .build()));
        assertThrows(BadRequestException.class, () -> prestazioneService.esito(1L, request));
    }

    @Test
    void esitoSuccessfulThrowsPrestazioneNonCorrispondente() {
        EsitoRequest request = new EsitoRequest("VACCINO_EFFETTUATO");
        when(prestazioneRepository.findById(any())).thenReturn(Optional.of(Prestazione.builder()
                .paziente(Utente.builder().email("email").build())
                .infermiere(new Utente())
                .tipoPrestazione(TipoPrestazione.TAMPONE)
                .dataFine(LocalDateTime.now().minusDays(2))
                .build()));
        assertThrows(BadRequestException.class, () -> prestazioneService.esito(1L, request));
    }

    @Test
    void esitoSuccessfulThrowsInfermiereNonAssociato() {
        EsitoRequest request = new EsitoRequest("VACCINO_EFFETTUATO");
        when(prestazioneRepository.findById(any())).thenReturn(Optional.of(Prestazione.builder()
                .paziente(Utente.builder().email("email").build())
                .tipoPrestazione(TipoPrestazione.TAMPONE)
                .dataFine(LocalDateTime.now().minusDays(2))
                .build()));
        assertThrows(BadRequestException.class, () -> prestazioneService.esito(1L, request));
    }

    @Test
    void esitoSuccessfulThrowsDataNonValida() {
        EsitoRequest request = new EsitoRequest("VACCINO_EFFETTUATO");
        when(prestazioneRepository.findById(any())).thenReturn(Optional.of(Prestazione.builder()
                .paziente(Utente.builder().email("email").build())
                .tipoPrestazione(TipoPrestazione.TAMPONE)
                .dataFine(LocalDateTime.now().plusDays(2))
                .build()));
        assertThrows(BadRequestException.class, () -> prestazioneService.esito(1L, request));
    }

    @Test
    void esitoSuccessfulThrowsPrestazioneNonTrovata() {
        EsitoRequest request = new EsitoRequest("VACCINO_EFFETTUATO");
        when(prestazioneRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> prestazioneService.esito(1L, request));
    }

    @Test
    void getPrenotazioniByPazienteSuccessful() {
        when(userRepository.findById(any())).thenReturn(Optional.of(new Utente()));
        when(prestazioneRepository.findAllByPaziente(any())).thenReturn(List.of(Prestazione.builder().dataInizio(LocalDateTime.now().plusDays(3)).build()));
        assertAll(() -> prestazioneService.getPrenotazioniByPaziente(1L));
    }

    @Test
    void getPrenotazioniByPazienteThrowsPazienteNonTrovato() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> prestazioneService.getPrenotazioniByPaziente(1L));
    }

    @Test
    void getPrenotazioniByInfermiereSuccessful() {
        when(userRepository.findById(any())).thenReturn(Optional.of(new Utente()));
        when(prestazioneRepository.findAllByInfermiere(any())).thenReturn(List.of(Prestazione.builder().dataInizio(LocalDateTime.now().plusDays(3)).build()));
        assertAll(() -> prestazioneService.getPrenotazioniByInfermiere(1L));
    }

    @Test
    void getPrenotazioniByInfermiereThrowsInfermiereNonTrovato() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> prestazioneService.getPrenotazioniByInfermiere(1L));
    }

    @Test
    void storicoPrestazioniByPazienteSuccessful() {
        when(userRepository.findById(any())).thenReturn(Optional.of(new Utente()));
        when(prestazioneRepository.findAllByPaziente(any())).thenReturn(List.of(Prestazione.builder().esito(EsitoPrestazione.TAMPONE_NEGATIVO).dataFine(LocalDateTime.now().minusDays(3)).build()));
        assertAll(() -> prestazioneService.storicoPrestazioniByPaziente(1L));
    }

    @Test
    void storicoPrestazioniByPazienteThrowsPazienteNonTrovato() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> prestazioneService.storicoPrestazioniByPaziente(1L));
    }

    @Test
    void storicoPrestazioniSuccessful() {
        when(prestazioneRepository.findAll()).thenReturn(List.of(Prestazione.builder().esito(EsitoPrestazione.TAMPONE_NEGATIVO).dataFine(LocalDateTime.now().minusDays(3)).build()));
        assertAll(() -> prestazioneService.storicoPrestazioni());
    }

    @Test
    void cercaTamponeSuccessful() {
        when(prestazioneRepository.findAllByTipoPrestazione(any())).thenReturn(List.of(new Prestazione()));
        assertAll(() -> prestazioneService.cercaTampone());
    }

    @Test
    void cercaVaccinoSuccessful() {
        when(prestazioneRepository.findAllByTipoPrestazione(any())).thenReturn(List.of(new Prestazione()));
        assertAll(() -> prestazioneService.cercaVaccino());
    }
}
