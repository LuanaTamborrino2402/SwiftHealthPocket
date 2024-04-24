package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.dto.request.PrenotaPrestazioneRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.PrestazioneResponse;
import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;
import com.luanatamborrino.SwiftHealthPocket.exception.InternalServerErrorException;
import com.luanatamborrino.SwiftHealthPocket.exception.NotFoundException;
import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.model.Struttura;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.model._enum.TipoPrestazione;
import com.luanatamborrino.SwiftHealthPocket.repository.PrestazioneRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.StrutturaRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrestazioneService {

    private final PrestazioneRepository prestazioneRepository;
    private final UserRepository userRepository;
    private final StrutturaRepository strutturaRepository;



    public void prenotaPrestazione(PrenotaPrestazioneRequest request){

        //Controllo che gli id di infermiere e sturttura partano da 1.
        if(request.getIdPaziente() < 1 || request.getIdStruttura() < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Prendo dal database l'infermiere con l'id fornito.
        Optional<Utente> optionalUser = userRepository.findById(request.getIdPaziente());

        //Se non viene trovato alcun infermiere con l'id fornito, lancio l'eccezione.
        if(optionalUser.isEmpty()){
            throw new NotFoundException("Utente non trovato.");
        }

        //Prendo la stuttura dal database con l'id fornito.
        Optional<Struttura> optionalStruttura = strutturaRepository.findById(request.getIdStruttura());

        //Se non viene trovata alcuna struttura con l'id fornito, lancio l'eccezione.
        if(optionalStruttura.isEmpty()){
            throw new NotFoundException("Struttura non trovata.");
        }

        if(request.getTipoPrestazione().isBlank() && request.getTipoPrestazione().isEmpty()){
            throw new BadRequestException("Inserire il tipo di prestazione.");
        }

        TipoPrestazione tipoPrestazione;
        if(request.getTipoPrestazione().equals("TAMPONE")){
            tipoPrestazione = TipoPrestazione.TAMPONE;
        }else if(request.getTipoPrestazione().equals("VACCINO")){
            tipoPrestazione = TipoPrestazione.VACCINO;
        }else {
            throw new BadRequestException("Tipo di prestazione non trovato.");
        }

        if(request.getDataInizio().isBefore(LocalDateTime.now())){
            throw new BadRequestException("Data nel passato.");
        }

        //TODO ci sarebbe da controllare se la data è fattibile o se tutti gli infermieri sono già occupati,però non lo facciamo e se 48h prima della prestazione, nessun infermiere ha accettato, allora la prenotazione viene annullata
        Prestazione prestazione = Prestazione.builder()
                .tipoPrestazione(tipoPrestazione)
                .dataInizio(request.getDataInizio())
                .dataFine(request.getDataInizio().plusMinutes(
                        tipoPrestazione.equals(TipoPrestazione.TAMPONE) ? 10 : 20
                ))
                .paziente(optionalUser.get())
                .struttura(optionalStruttura.get())
                .build();

        prestazioneRepository.save(prestazione);
    }

    public List<PrestazioneResponse> getAllByPaziente(Long idPaziente){

        //Controllo che l'id parta da 1.
        if(idPaziente < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Prendo l'utente dal database con l'id fornito.
        Optional<Utente> paziente = userRepository.findById(idPaziente);

        //Se non viene trovato alcun utente con l'id fornito, viene lanciata l'eccezione.
        if(paziente.isEmpty()){
            throw new NotFoundException("Utente non trovato.");
        }

        List<Prestazione> prestazioni = prestazioneRepository.findAllByPaziente(paziente.get());

        List<PrestazioneResponse> response = new ArrayList<>();

        for(Prestazione prestazione: prestazioni){
            response.add(new PrestazioneResponse(
                    prestazione.getIdPrestazione(),
                    prestazione.getTipoPrestazione(),
                    prestazione.getEsito(),
                    prestazione.getDataInizio(),
                    prestazione.getDataFine()
            ));
        }

         return response;

    }

    public void eliminaPrenotazione(Long idPrestazione){

        //Controllo che l'id parta da 1.
        if(idPrestazione < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Prendo l'utente dal database con l'id fornito.
        Optional<Prestazione> prestazione = prestazioneRepository.findById(idPrestazione);

        //Se non viene trovato alcun utente con l'id fornito, viene lanciata l'eccezione.
        if(prestazione.isEmpty()){
            throw new NotFoundException("Prestazione non trovata.");
        }

        if(prestazione.get().getDataInizio().isBefore(LocalDateTime.now().plusHours(2))){
            throw new BadRequestException("Impossibile eliminare la prestazione.");
        }

        prestazioneRepository.deleteById(prestazione.get().getIdPrestazione());

        if(prestazioneRepository.existsById(idPrestazione)){
            throw new InternalServerErrorException("Errore nell'eliminazione.");
        }

    }
}
