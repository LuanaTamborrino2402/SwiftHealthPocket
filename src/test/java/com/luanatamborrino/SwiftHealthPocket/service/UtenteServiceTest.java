package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.dto.request.AssociaDissociaInfermiereRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.UpdateUserDataRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UtenteServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    StrutturaRepository strutturaRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    Publisher publisher;
    @InjectMocks
    UtenteService utenteService;

    @Test
    void getUserDataSuccessful() {
        when(userRepository.findById(any())).thenReturn(Optional.of(
                Utente.builder()
                .idUtente(1L)
                .nome("nome")
                .cognome("cognome")
                .password("password")
                .email("email")
                .ruolo(Ruolo.INFERMIERE)
                .build()
        ));
        assertAll(() -> utenteService.getUserData(1L));
    }

    @Test
    void getUserDataThrowsUtenteNonTrovato() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> utenteService.getUserData(1L));
    }

    @Test
    void deleteUserByIdThrowsErroreEliminazione() {
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().idUtente(1L).build()));
        assertThrows(InternalServerErrorException.class, () -> utenteService.deleteUserById(1L));
    }

    @Test
    void deleteUserByIdThrowsUtenteNonTrovato() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> utenteService.deleteUserById(1L));
    }

    @Test
    void deleteUserByEmailThrowsErroreEliminazione() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(Utente.builder().email("email").build()));
        assertThrows(InternalServerErrorException.class, () -> utenteService.deleteUserByEmail("email"));
    }

    @Test
    void deleteUserByEmailThrowsUtenteNonTrovato() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> utenteService.deleteUserByEmail("email"));
    }

    @Test
    void getAllUsersSuccessful() {
        when(userRepository.findAll()).thenReturn(List.of(Utente.builder()
                        .idUtente(1L)
                        .nome("nome")
                        .cognome("cognome")
                        .password("password")
                        .email("email")
                        .ruolo(Ruolo.INFERMIERE)
                .build()));
        assertAll(() -> utenteService.getAllUsers());
    }

    @Test
    void getAllUsersThrowsUtentiNonTrovati() {
        when(userRepository.findAll()).thenReturn(List.of());
        assertThrows(NotFoundException.class, () -> utenteService.getAllUsers());
    }

    @Test
    void getAllUsersByRoleSuccessful() {
        when(userRepository.findAllByRuolo(any())).thenReturn(List.of(
                Utente.builder()
                        .idUtente(1L)
                        .nome("nome")
                        .cognome("cognome")
                        .password("password")
                        .email("email")
                        .ruolo(Ruolo.PAZIENTE)
                        .build()
        ));
        assertAll(() -> utenteService.getAllUsersByRole("INFERMIERE"));
    }

    @Test
    void getAllUsersByRoleThrowsUtentiNonTrovati() {
        when(userRepository.findAllByRuolo(any())).thenReturn(List.of());
        assertThrows(NotFoundException.class, () -> utenteService.getAllUsersByRole("PAZIENTE"));
    }

    @Test
    void getAllUsersByRoleThrowsRuoloNonValido() {
        assertThrows(BadRequestException.class, () -> utenteService.getAllUsersByRole("AMMINISTRATORE"));
    }

    @Test
    void updateUserDataSuccessful() {
        Optional<Utente> utente = Optional.of(
                Utente.builder()
                        .idUtente(1L)
                        .nome("nome")
                        .cognome("cognome")
                        .password("password")
                        .email("email1")
                        .ruolo(Ruolo.PAZIENTE)
                        .build()
        );

        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "nome", "cognome", "email", "vecchiaPassword", "nuovaPassword"
        );
        when(userRepository.findById(any())).thenReturn(utente);
        when(passwordEncoder.matches(request.getVecchiaPassword(), utente.get().getPassword())).thenReturn(true);
        assertAll(() -> utenteService.updateUserData(1L, request));
    }

    @Test
    void updateUserDataThrowsPasswordNonCorretta() {
        Optional<Utente> utente = Optional.of(
                Utente.builder()
                        .idUtente(1L)
                        .nome("nome")
                        .cognome("cognome")
                        .password("password")
                        .email("email")
                        .ruolo(Ruolo.PAZIENTE)
                        .build()
        );

        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "nome", "cognome", "email", "vecchiaPassword", "nuovaPassword"
        );
        when(userRepository.findById(any())).thenReturn(utente);
        when(passwordEncoder.matches(request.getVecchiaPassword(), utente.get().getPassword())).thenReturn(false);
        assertThrows(BadRequestException.class, () -> utenteService.updateUserData(1L, request));
    }

    @Test
    void updateUserDataThrowsUtenteGiaPresenteConEmail() {
        Optional<Utente> utente = Optional.of(
                Utente.builder()
                        .idUtente(1L)
                        .nome("nome")
                        .cognome("cognome")
                        .password("password")
                        .email("email1")
                        .ruolo(Ruolo.PAZIENTE)
                        .build()
        );

        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "nome", "cognome", "email", "vecchiaPassword", "nuovaPassword"
        );
        when(userRepository.findById(any())).thenReturn(utente);
        when(userRepository.findByEmail(eq("email"))).thenReturn(Optional.of(new Utente()));
        assertThrows(ConflictException.class, () -> utenteService.updateUserData(1L, request));
    }

    @Test
    void updateUserDataThrowsUtenteNonTrovato() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> utenteService.updateUserData(1L, new UpdateUserDataRequest()));
    }

    @Test
    void getDisponibilitaInfermiereThrowsInfermiereNonDisponibile() {
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.INFERMIERE).struttura(new Struttura()).build()));
        assertThrows(BadRequestException.class, () -> utenteService.getDisponibilitaInfermiere(1L));
    }

    @Test
    void getDisponibilitaInfermiereThrowsRuoloNonCorretto() {
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.PAZIENTE).struttura(new Struttura()).build()));
        assertThrows(BadRequestException.class, () -> utenteService.getDisponibilitaInfermiere(1L));
    }

    @Test
    void getDisponibilitaInfermiereThrowsUtenteNonTrovato() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> utenteService.getDisponibilitaInfermiere(1L));
    }

    @Test
    void richiestaCambioStrutturaSuccessful() {
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().struttura(new Struttura()).nome("nome").cognome("cognome").ruolo(Ruolo.INFERMIERE).build()));
        when(userRepository.findByRuolo(any())).thenReturn(Optional.of(Utente.builder().email("email").build()));
        assertAll(() -> utenteService.richiestaCambioStruttura(1L));
    }

    @Test
    void richiestaCambioStrutturaThrowsUtenteNonAncoraAssociato() {
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().nome("nome").cognome("cognome").ruolo(Ruolo.INFERMIERE).build()));
        when(userRepository.findByRuolo(any())).thenReturn(Optional.of(Utente.builder().email("email").build()));
        assertThrows(ConflictException.class, () -> utenteService.richiestaCambioStruttura(1L));
    }

    @Test
    void richiestaCambioStrutturaThrowsAmministratoreNonTrovato() {
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().nome("nome").cognome("cognome").ruolo(Ruolo.INFERMIERE).build()));
        when(userRepository.findByRuolo(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> utenteService.richiestaCambioStruttura(1L));
    }

    @Test
    void richiestaCambioStrutturaThrowsRuoloNonCorretto() {
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().nome("nome").cognome("cognome").ruolo(Ruolo.PAZIENTE).build()));
        assertThrows(BadRequestException.class, () -> utenteService.richiestaCambioStruttura(1L));
    }

    @Test
    void richiestaCambioStrutturaThrowsUtenteNonTrovato() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> utenteService.richiestaCambioStruttura(1L));
    }

    @Test
    void cambioForzatoStrutturaSuccessful() {
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().struttura(new Struttura()).ruolo(Ruolo.INFERMIERE).build()));
        when(strutturaRepository.findById(any())).thenReturn(Optional.of(new Struttura()));
        assertAll(() -> utenteService.cambioForzatoStruttura(new AssociaDissociaInfermiereRequest(1L, 1L)));
    }

    @Test
    void cambioForzatoStrutturaThrowsUtenteNonAncoraAssociato() {
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.INFERMIERE).build()));
        when(strutturaRepository.findById(any())).thenReturn(Optional.of(new Struttura()));
        assertThrows(ConflictException.class, () -> utenteService.cambioForzatoStruttura(new AssociaDissociaInfermiereRequest(1L, 1L)));
    }

    @Test
    void cambioForzatoStrutturaThrowsUtenteNonInfermiere() {
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.PAZIENTE).build()));
        when(strutturaRepository.findById(any())).thenReturn(Optional.of(new Struttura()));
        assertThrows(BadRequestException.class, () -> utenteService.cambioForzatoStruttura(new AssociaDissociaInfermiereRequest(1L, 1L)));
    }

    @Test
    void cambioForzatoStrutturaThrowsStrutturaNonTrovata() {
        when(userRepository.findById(any())).thenReturn(Optional.of(Utente.builder().ruolo(Ruolo.PAZIENTE).build()));
        when(strutturaRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> utenteService.cambioForzatoStruttura(new AssociaDissociaInfermiereRequest(1L, 1L)));
    }

    @Test
    void cambioForzatoStrutturaThrowsUtenteNonTrovato() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> utenteService.cambioForzatoStruttura(new AssociaDissociaInfermiereRequest(1L, 1L)));
    }
}
