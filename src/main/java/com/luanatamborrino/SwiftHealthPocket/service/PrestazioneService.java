package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.dto.request.EsitoRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.PrenotaPrestazioneRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.PresaInCaricoRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.PrestazioneResponse;
import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;
import com.luanatamborrino.SwiftHealthPocket.exception.InternalServerErrorException;
import com.luanatamborrino.SwiftHealthPocket.exception.NotFoundException;
import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.model.Recensione;
import com.luanatamborrino.SwiftHealthPocket.model.Struttura;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.model._enum.EsitoPrestazione;
import com.luanatamborrino.SwiftHealthPocket.model._enum.Ruolo;
import com.luanatamborrino.SwiftHealthPocket.model._enum.TipoPrestazione;
import com.luanatamborrino.SwiftHealthPocket.observer.publisher.Publisher;
import com.luanatamborrino.SwiftHealthPocket.repository.PrestazioneRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.StrutturaRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import com.luanatamborrino.SwiftHealthPocket.util.Methods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *  Service per gestire tutti i metodi riguardanti la prestazione.
 */
@Service
@RequiredArgsConstructor
public class PrestazioneService {

    private final PrestazioneRepository prestazioneRepository;
    private final UserRepository userRepository;
    private final StrutturaRepository strutturaRepository;

    private final Publisher publisher;

    /**
     * Metodo per prenotare una prestazione sanitaria.
     * @param request DTO con i dati necessari per la prenotazione.
     */
    public void prenotaPrestazione(PrenotaPrestazioneRequest request) {

        //Verifico che gli id di paziente e struttura siano validi e non nulli.
        Methods.getInstance().checkIds(List.of(
                request.getIdPaziente(),
                request.getIdStruttura()
        ));

        //Prendo dal database il paziente con l'id fornito.
        Optional<Utente> paziente = userRepository.findById(request.getIdPaziente());

        //Se non viene trovato alcun paziente con l'id fornito, lancio l'eccezione.
        if(paziente.isEmpty()) {
            throw new NotFoundException("Utente non trovato.");
        }

        //Prendo la stuttura dal database con l'id fornito.
        Optional<Struttura> optionalStruttura = strutturaRepository.findById(request.getIdStruttura());

        //Se non viene trovata alcuna struttura con l'id fornito, lancio l'eccezione.
        if(optionalStruttura.isEmpty()) {
            throw new NotFoundException("Struttura non trovata.");
        }

        //Invoco il metodo checkStringData per verificare che il campo 'tipoPrestazione' non sia vuoto o nullo.
        Methods.getInstance().checkStringData(List.of(
                request.getTipoPrestazione()
        ));

        //Controllo se il tipo di prestazione è valido e poi lo assegno.
        TipoPrestazione tipoPrestazione;
        if(request.getTipoPrestazione().equals("TAMPONE")) {
            tipoPrestazione = TipoPrestazione.TAMPONE;
        }else if(request.getTipoPrestazione().equals("VACCINO")) {
            tipoPrestazione = TipoPrestazione.VACCINO;
        }else {
            throw new BadRequestException("Tipo di prestazione non trovato.");
        }

        //Verifico che la data di inizio non sia nel passato.
        if(request.getDataInizio().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Data nel passato.");
        }

        //Crea l'oggetto Prestazione usando il pattern builder.
        Prestazione prestazione = Prestazione.builder()
                .tipoPrestazione(tipoPrestazione)
                .dataInizio(request.getDataInizio())
                .dataFine(request.getDataInizio().plusMinutes(
                        //Calcola la data di fine basandosi sul tipo di prestazione
                        tipoPrestazione.equals(TipoPrestazione.TAMPONE) ? 10 : 20
                ))
                .paziente(paziente.get())
                .struttura(optionalStruttura.get())
                .recensione(new Recensione())
                .build();

        //Salvo l'oggetto prestazione nel database.
        prestazioneRepository.save(prestazione);
    }

    /**
     * Metodo per ottenere tutte le prestazioni registrate per un dato paziente.
     * @param idPaziente Id del paziente di cui recuperare le prestazioni.
     * @return Lista di DTO con i dati di ogni prestazione.
     */
    public List<PrestazioneResponse> getAllByPaziente(Long idPaziente) {

        //Verifico che l'id di paziente sia valido e non nullo.
        Methods.getInstance().checkIds(List.of(
                idPaziente
        ));

        //Prendo il paziente dal database con l'id fornito.
        Optional<Utente> paziente = userRepository.findById(idPaziente);

        //Se non viene trovato alcun paziente con l'id fornito, viene lanciata l'eccezione.
        if(paziente.isEmpty()) {
            throw new NotFoundException("Utente non trovato.");
        }

        //Recupero tutte le prestazioni dal database che corrispondono all'id del paziente trovato.
        List<Prestazione> prestazioni = prestazioneRepository.findAllByPaziente(paziente.get());

        //Creo la lista di prestazioni.
        List<PrestazioneResponse> response = new ArrayList<>();

        //Per ogni prestazione trovata, creo un oggetto PrestazioneResponse e la aggiungo alla lista.
        for(Prestazione prestazione: prestazioni) {
            if(prestazione.getInfermiere() != null || prestazione.getDataInizio().isAfter(LocalDateTime.now().plusDays(2))) {
                response.add(new PrestazioneResponse(
                        prestazione.getIdPrestazione(),
                        prestazione.getTipoPrestazione(),
                        prestazione.getEsito(),
                        prestazione.getDataInizio(),
                        prestazione.getDataFine()
                ));
            }
        }

        //Restituisco il DTO.
        return response;
    }

    /**
     * Metodo per eliminare una prenotazione esistente.
     * @param idPrestazione Id della prestazione da eliminare
     */
    public void eliminaPrenotazione(Long idPrestazione) {

        //Verifico che l'id di prestazione sia valido e non nullo.
        Methods.getInstance().checkIds(List.of(
                idPrestazione
        ));

        //Prendo la prestazione dal database con l'id fornito.
        Optional<Prestazione> prestazione = prestazioneRepository.findById(idPrestazione);

        //Se non viene trovato alcuna prestazione con l'id fornito, viene lanciata l'eccezione.
        if(prestazione.isEmpty()) {
            throw new NotFoundException("Prestazione non trovata.");
        }

        //Verifico che la prestazione non sia troppo vicina nel tempo per essere cancellata (meno di 2 ore prima dell'inizio).
        if(prestazione.get().getDataInizio().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Impossibile eliminare la prestazione.");
        }

        //Elimino la prestazione dal database.
        prestazioneRepository.deleteById(prestazione.get().getIdPrestazione());


        //Verifico dopo l'eliminazione che la prestazione sia stata effettivamente rimossa.
        if(prestazioneRepository.existsById(idPrestazione)) {
            throw new InternalServerErrorException("Errore nell'eliminazione.");
        }
    }

    /**
     * Metodo per ottenere tutte le prenotazioni, senza infermiere a carico, associate ad una specifica struttura.
     * @param idStruttura Id della struttura per la quale recuperare le prestazioni.
     * @return Lista di DTO con i dati di ogni prestazione.
     */
    public List<PrestazioneResponse> getAllPrenotazioni(Long idStruttura) {

        //Verifico che l'id di struttura sia valido e non nullo.
        Methods.getInstance().checkIds(List.of(
                idStruttura
        ));

        //Prendo la stuttura dal database con l'id fornito.
        Optional<Struttura> struttura = strutturaRepository.findById(idStruttura);

        //Se non viene trovata alcuna struttura con l'id fornito, lancio l'eccezione.
        if(struttura.isEmpty()){
            throw new NotFoundException("Struttura non trovata.");
        }

        //Recupero tutte le prestazioni associate a quella struttura dal database.
        List<Prestazione> prestazioni = prestazioneRepository.findAllByStruttura(struttura.get());

        //Creo la lista di prestazioni.
        List<PrestazioneResponse> prestazioneResponse = new ArrayList<>();

        //Creo e aggiungo alla lista un oggetto PrestazioneResponse per ogni prestazione futura senza infermiere assegnato.
        for(Prestazione prestazione: prestazioni) {
            if((prestazione.getDataInizio().isAfter(LocalDateTime.now()) && prestazione.getInfermiere() != null )
                    || (prestazione.getInfermiere() == null && prestazione.getDataInizio().isAfter(LocalDateTime.now().plusDays(2)))) {
                prestazioneResponse.add(new PrestazioneResponse(
                        prestazione.getIdPrestazione(),
                        prestazione.getTipoPrestazione(),
                        prestazione.getEsito(),
                        prestazione.getDataInizio(),
                        prestazione.getDataFine()
                ));
            }
        }

        //Restituisco il DTO.
        return prestazioneResponse;
    }


    /**
     * Meotodo che prende in carico la prestazione assegnando un infermiere.
     * @param request DTO contente l'id dell'infermiere e della prestazione da prendere in carico.
     */
    public void presaInCarico(PresaInCaricoRequest request) {

        //Verifico che l'id di infermiere e di prestazione siano validi e non nulli.
        Methods.getInstance().checkIds(List.of(
                request.getIdInfermiere(),
                request.getIdPrestazione()
        ));

        //Recupero l'infermiere dal database con l'id fornito.
        Optional<Utente> infermiere = userRepository.findById(request.getIdInfermiere());

        //Se non viene trovato alcun infermiere con l'id fornito, lancio l'eccezione.
        if(infermiere.isEmpty()){
            throw new NotFoundException("Infermiere non trovato.");
        }

        //Controllo se il ruolo è quello di infermiere, altrimenti lancio l'eccezione.
        if(!infermiere.get().getRuolo().equals(Ruolo.INFERMIERE)) {
            throw new BadRequestException("Ruolo non valido.");
        }

        //Recupero la prestazione dal database con l'id fornito.
        Optional<Prestazione> prestazione = prestazioneRepository.findById(request.getIdPrestazione());

        //Se non viene trovata alcuna prestazione con l'id fornito, lancio l'eccezione.
        if(prestazione.isEmpty()) {
            throw new NotFoundException("Prestazione non trovata.");
        }

        //Assegno l'infermiere alla prestazione.
        prestazione.get().setInfermiere(infermiere.get());

        //Salvo la prestazione aggiornata nel database.
        prestazioneRepository.save(prestazione.get());
    }

    /**
     * Metodo che registra l'esito di una prestazione.
     * @param idPrestazione Id della prestazione per la quale registrare l'esito.
     * @param request DTO contenente l'esito della prestazione da registrare.
     */
    public void esito(Long idPrestazione, EsitoRequest request) {

        ///Verifico che l'id di prestazione sia valido e non nullo.
        Methods.getInstance().checkIds(List.of(
                idPrestazione
        ));

        //Recupero la prestazione dal database con l'id fornito.
        Optional<Prestazione> prestazione = prestazioneRepository.findById(idPrestazione);

        //Se non viene trovata alcuna prestazione con l'id fornito, lancio l'eccezione.
        if(prestazione.isEmpty()) {
            throw new NotFoundException("Prestazione non trovata.");
        }

        //Invoco il metodo checkStringData per verificare che il campo 'esito' non sia vuoto o nullo.
        Methods.getInstance().checkStringData(List.of(
                request.getEsito()
        ));

        //Verifico che la data di fine della prestazione non sia nel futuro.
        if(prestazione.get().getDataFine().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Data non valida.");
        }

        if(prestazione.get().getInfermiere() == null){
            throw new BadRequestException("Infermiere non associato");
        }
        if(request.getEsito().equals("VACCINO_EFFETTUATO") && prestazione.get().getTipoPrestazione().equals(TipoPrestazione.TAMPONE)
                || request.getEsito().equals("TAMPONE_POSITIVO") && prestazione.get().getTipoPrestazione().equals(TipoPrestazione.VACCINO)
                || request.getEsito().equals("TAMPONE_NEGATIVO") && prestazione.get().getTipoPrestazione().equals(TipoPrestazione.VACCINO)
        ){
            throw new BadRequestException("Prestazione non corrispondente.");
        }

        //Assegno un valore di EsitoPrestazione basato sull'esito fornito nella richiesta e lancio l'eccezione per esiti non validi.
        EsitoPrestazione esitoPrestazione = switch (request.getEsito()) {
            case "TAMPONE_POSITIVO" -> EsitoPrestazione.TAMPONE_POSITIVO;
            case "TAMPONE_NEGATIVO" -> EsitoPrestazione.TAMPONE_NEGATIVO;
            case "VACCINO_EFFETTUATO" -> EsitoPrestazione.VACCINO_EFFETTUATO;
            default -> throw new BadRequestException("Esito non valido.");
        };

        //Imposto l'esito della prestazione recuperata dal database.
        prestazione.get().setEsito(esitoPrestazione);

        //Salvo la prestazione aggiornata nel database.
        prestazioneRepository.save(prestazione.get());

        //Notifico via email l'esito della prestazione al paziente.
        publisher.notify("ControlloEsito",
                "",
                "",
                switch (prestazione.get().getTipoPrestazione()) {
                    case TAMPONE -> "Tampone";
                    case VACCINO -> "Vaccino";
                },
                switch (prestazione.get().getEsito()) {
                    case TAMPONE_POSITIVO -> "Positivo";
                    case TAMPONE_NEGATIVO -> "Negativo";
                    case VACCINO_EFFETTUATO -> "Effettuato";
                },

                //Recupero l'indirizzo email del paziente associato alla prestazione corrente.
                prestazione.get().getPaziente().getEmail()
        );
    }

    /**
     * Metodo che recupera tutte le prestazioni future per uno specifico paziente dato il sui id.
     * @param idPaziente Id del paziente di cui recuperare le prenotazioni.
     * @return Lista di DTO che contiene i dati delle prestazioni future.
     */
    public List<PrestazioneResponse> getPrenotazioniByPaziente(Long idPaziente) {

        //Verifico che l'id di paziente sia valido e non nullo.
        Methods.getInstance().checkIds(List.of(
                idPaziente
        ));

        //Prendo il paziente dal database con l'id fornito.
        Optional<Utente> paziente = userRepository.findById(idPaziente);

        //Se non viene trovato alcun paziente con l'id fornito, viene lanciata l'eccezione.
        if(paziente.isEmpty()) {
            throw new NotFoundException("Paziente non trovato.");
        }

        //Recupero tutte le prestazioni associate al paziente specificato.
        List<Prestazione> prestazioni = prestazioneRepository.findAllByPaziente(paziente.get());

        //Creo la lista di prestazioni.
        List<PrestazioneResponse> prestazioneResponse = new ArrayList<>();

        //Creo e aggiungo alla lista un oggetto PrestazioneResponse per ogni prestazione futura.
        for(Prestazione prestazione: prestazioni) {
            if((prestazione.getDataInizio().isAfter(LocalDateTime.now()) && prestazione.getInfermiere() != null )
                    || (prestazione.getInfermiere() == null && prestazione.getDataInizio().isAfter(LocalDateTime.now().plusDays(2)))){
                prestazioneResponse.add(new PrestazioneResponse(
                        prestazione.getIdPrestazione(),
                        prestazione.getTipoPrestazione(),
                        prestazione.getEsito(),
                        prestazione.getDataInizio(),
                        prestazione.getDataFine()
                ));
            }
        }

        //Restituisco il DTO.
        return prestazioneResponse;
    }

    /**
     * Metodo che recupera tutte le prestazioni future gestite da un specifico infermiere.
     * @param idInfermiere Id dell'infermiere di cui si vogliono ottenere le prenotazioni.
     * @return Lista di DTO con i dettagli delle prenotazioni.
     */
    public List<PrestazioneResponse> getPrenotazioniByInfermiere(Long idInfermiere) {

        //Verifico che l'id di infermiere sia valido e non nullo.
        Methods.getInstance().checkIds(List.of(
                idInfermiere
        ));

        //Prendo l'infermiere dal database con l'id fornito.
        Optional<Utente> infermiere = userRepository.findById(idInfermiere);

        //Se non viene trovato alcun infermiere con l'id fornito, viene lanciata l'eccezione.
        if(infermiere.isEmpty()) {
            throw new NotFoundException("Infermiere non trovato.");
        }

        //Recupero tutte le prestazioni assegnate all'infermiere specificato.
        List<Prestazione> prestazioni = prestazioneRepository.findAllByInfermiere(infermiere.get());

        //Creo una lista di prestazioni.
        List<PrestazioneResponse> prestazioneResponse = new ArrayList<>();

        //Creo e aggiungo alla lista un oggetto PrestazioneResponse per ogni prestazione futura.
        for(Prestazione prestazione: prestazioni) {
            if(prestazione.getDataInizio().isAfter(LocalDateTime.now())){
                prestazioneResponse.add(new PrestazioneResponse(
                        prestazione.getIdPrestazione(),
                        prestazione.getTipoPrestazione(),
                        prestazione.getEsito(),
                        prestazione.getDataInizio(),
                        prestazione.getDataFine()
                ));
            }
        }

        //Restituisco il DTO.
        return prestazioneResponse;
    }

    /**
     * Metodo che recupera lo storico delle prestazioni concluse di un specifico paziente.
     * @param idPaziente Id del paziente di cui si desidera recuperare lo storico delle prestazioni.
     * @return Lista di DTO i dettagli delle prestazioni concluse.
     */
    public List<PrestazioneResponse> storicoPrestazioniByPaziente(Long idPaziente) {

        //Verifico che l'id di paziente sia valido e non nullo.
        Methods.getInstance().checkIds(List.of(
                idPaziente
        ));

        //Prendo il paziente dal database con l'id fornito.
        Optional<Utente> paziente = userRepository.findById(idPaziente);

        //Se non viene trovato alcun paziente con l'id fornito, viene lanciata l'eccezione.
        if(paziente.isEmpty()) {
            throw new NotFoundException("Paziente non trovato.");
        }

        //Recupero tutte le prestazioni concluse per il paziente.
        List<Prestazione> prestazioni = prestazioneRepository.findAllByPaziente(paziente.get());

        //Creo una lista di prestazioni.
        List<PrestazioneResponse> prestazioneResponse = new ArrayList<>();

        //Creo e aggiungo alla lista un oggetto PrestazioneResponse per ogni prestazione conclusa con esito definito.
        for(Prestazione prestazione: prestazioni) {
            if(prestazione.getDataFine().isBefore(LocalDateTime.now()) && prestazione.getEsito() != null ) {
                prestazioneResponse.add(new PrestazioneResponse(
                        prestazione.getIdPrestazione(),
                        prestazione.getTipoPrestazione(),
                        prestazione.getEsito(),
                        prestazione.getDataInizio(),
                        prestazione.getDataFine()
                ));
            }
        }

        //Restituisco il DTO.
        return prestazioneResponse;
    }

    /**
     * Metodo che recupera l'elenco storico di tutte le prestazioni completate fino ad oggi.
     * @return Lista di DTO contenente i dati di ciascuna prestazione completata.
     */
    public List<PrestazioneResponse> storicoPrestazioni() {

        //Recupero tutte le prestazioni dal database.
        List<Prestazione> prestazioni = prestazioneRepository.findAll();

        //Creo una lista di prestazioni.
        List<PrestazioneResponse> prestazioneResponse = new ArrayList<>();

        //Creo e aggiungo alla lista un oggetto PrestazioneResponse per ogni prestazione futura.
        for(Prestazione prestazione: prestazioni) {
            if(prestazione.getDataFine().isBefore(LocalDateTime.now()) && prestazione.getEsito() != null ) {
                prestazioneResponse.add(new PrestazioneResponse(
                        prestazione.getIdPrestazione(),
                        prestazione.getTipoPrestazione(),
                        prestazione.getEsito(),
                        prestazione.getDataInizio(),
                        prestazione.getDataFine()
                ));
            }
        }

        //Restituisco il DTO.
        return prestazioneResponse;
    }

    /**
     * Metodo che recupera l'elenco di tutte le prestazioni che sono classificate come tamponi.
     * @return Lista di oggetti Prestazione che rappresentano i tamponi trovati.
     */
    public List<Prestazione> cercaTampone() {

        //Recupero tutte le prestazioni del tipo tampone.
        //Chiamo il repository per trovare tutte le prestazioni del tipo specificato "TAMPONE".
        List<Prestazione> listaTamponi = prestazioneRepository.findAllByTipoPrestazione(TipoPrestazione.TAMPONE);

        //Ritorna la lista di prestazioni trovate.
        return listaTamponi;
    }

    /**
     * Metodo che recupera un elenco di tutte le prestazioni che sono classificate come vaccini.
     * @return Lista di oggetti Prestazione che rappresentano i vaccini trovati.
     */
    public List<Prestazione> cercaVaccino() {

        //Chiamo il repository per trovare tutte le prestazioni del tipo specificato "VACCINO".
        List<Prestazione> listaVaccini = prestazioneRepository.findAllByTipoPrestazione(TipoPrestazione.VACCINO);

        //Ritorna la lista di prestazioni trovate.
        return listaVaccini;
    }
}
