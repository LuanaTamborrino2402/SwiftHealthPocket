package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.dto.request.UpdateUserDataRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.UserResponse;
import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;
import com.luanatamborrino.SwiftHealthPocket.exception.ConflictException;
import com.luanatamborrino.SwiftHealthPocket.exception.InternalServerErrorException;
import com.luanatamborrino.SwiftHealthPocket.exception.NotFoundException;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.model._enum.Ruolo;
import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service per gestire tutti i metodi riguardanti l'utente.
 */
@Service
@RequiredArgsConstructor
public class UtenteService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Metodo che, dato un id, prende l'utente dal database tramite la repository e lo ritorna al controller.
     * @param userId Id dell'utente.
     * @return DTO con i dati dell'utente.
     */
    public UserResponse getUserData(Long userId){

        //Controllo che l'id parta da 1.
        if(userId < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Controllo se è presente un utente con quell'id.
        Optional<Utente> user = userRepository.findById(userId);

        //Se non viene trovato alcun utente con l'ID fornito, viene lanciata l'eccezione.
        if(user.isEmpty()){
            throw new NotFoundException("Utente non trovato.");
        }

        //Ritorno il DTO con i dati dell'utente richiesto.
        return new UserResponse(
                user.get().getIdUtente(),
                user.get().getNome(),
                user.get().getCognome(),
                user.get().getPassword(),
                user.get().getEmail(),
                user.get().getRuolo()
        );
    }

    /**
     * Metodo che, dato l'id, viene eliminato l'utente dal database.
     * @param userId Id dell'utente da eliminare.
     */
    public void deleteUserById(Long userId){

        //controllo che l'id parta da 1.
        if(userId < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Controllo se è presente un utente con quell'id.
        Optional<Utente> user = userRepository.findById(userId);

        //Se non viene trovato alcun utente con l'ID fornito, lancio l'eccezione.
        if(user.isEmpty()){
            throw new NotFoundException("Utente non trovato.");
        }

        //Elimino l'utente trovato dal database.
        userRepository.deleteById(user.get().getIdUtente());

        //Verifico se l'utente è stato effettivamente eliminato.
        Optional<Utente> userDeleted = userRepository.findById(user.get().getIdUtente());

        //Se l'utente è ancora presente dopo la cancellazione, lancio l'eccezione.
        if(userDeleted.isPresent()){
            throw new InternalServerErrorException("Errore nell'eliminazione.");
        }
    }

    /**
     * Metodo che, dato l'indirizzo email, l'utente viene eliminato dal database.
     * @param email Email dell'utente da eliminare.
     */
    public void deleteUserByEmail(String email){

        //Ricerca dell'utente corrispondente all'indirizzo email fornito.
        Optional<Utente> user = userRepository.findByEmail(email);

        //Se non viene trovato alcun utente con l'indirizzo email fornito, lancio l'eccezione.
        if(user.isEmpty()){
            throw new NotFoundException("Utente non trovato.");
        }

        //Elimino l'utente dal database.
        userRepository.delete(user.get());

        //Verifico se l'utente è stato effettivamente eliminato.
        Optional<Utente> userDeleted = userRepository.findByEmail(user.get().getEmail());

        //Se l'utente è ancora presente dopo la cancellazione, lancio l'eccezione.
        if(userDeleted.isPresent()){
            throw new InternalServerErrorException("Errore nell'eliminazione");
        }
    }

    /**
     * Metodo che restituisce la lista degli utenti presenti sul database.
     * @return Lista di DTO con i dati di ogni utente.
     */
    public List<UserResponse> getAllUsers(){

        //Prendo tutti gli utenti dal database.
        List<Utente> userList = userRepository.findAll();

        //Se non vè presente nessun utetne, lancio l'eccezione.
        if(userList.isEmpty()){
            throw new NotFoundException("Utenti non trovati.");
        }

        //Creo la lists di utenti.
        List<UserResponse> response = new ArrayList<>();

        //Per ogni utente trovato, creo un oggetto UserResponse e lo aggiungo alla lista.
        for (Utente user : userList ){
            response.add(new UserResponse(
                    user.getIdUtente(),
                    user.getNome(),
                    user.getCognome(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getRuolo()
            ));
        }

        //Restituisco il DTO.
        return response;
    }

    /**
     * Metodo che restituisce la lista degli utenti in base al ruolo.
     * @param ruolo Ruolo dell'utente.
     * @return Lista di DTO con i dati di ogni utente.
     */
    public List<UserResponse> getAllUsersByRole(String ruolo){

        //Controllo se il ruolo è valido e poi lo assegno.
        Ruolo ruoloDaCercare;
        if(ruolo.equals("PAZIENTE")){
            ruoloDaCercare = Ruolo.PAZIENTE;
        }else if (ruolo.equals("INFERMIERE")) {
            ruoloDaCercare = Ruolo.INFERMIERE;
        }else{
            throw new BadRequestException("Ruolo non valido.");
        }

        //Recupero tutti gli utenti con il ruolo specificato.
        List<Utente> userList = userRepository.findAllByRuolo(ruoloDaCercare);

        //Se non ci sono utenti nel database con il ruolo specificato, lancio l'eccezione.
        if(userList.isEmpty()){
            throw new NotFoundException("Utenti non trovati.");
        }

        //Creo la lists di utenti.
        List<UserResponse> response = new ArrayList<>();

        //Per ogni utente trovato, creo un oggetto UserResponse e lo aggiungo alla lista.
        for(Utente user : userList ){
            response.add(new UserResponse(
                    user.getIdUtente(),
                    user.getNome(),
                    user.getCognome(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getRuolo()
            ));
        }

        //Restituisco IL DTO.
        return response;
    }

    /**
     * Metodo utilizzato per modificare i dati di un utente.
     * @param userId Id dell'utente da modificare.
     * @param request DTO con i nuovi dati.
     * @return DTO con tutti i dati dell'utente modificati.
     */
    public UserResponse updateUserData(Long userId, UpdateUserDataRequest request){

        //controllo che l'id parta da 1.
        if(userId < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Controllo se è presente un utente con quell'id.
        Optional<Utente> optionalUser = userRepository.findById(userId);

        //Se non viene trovato alcun utente con l'ID fornito, lancio l'eccezione.
        if(optionalUser.isEmpty()){
            throw new NotFoundException("Utente non trovato.");
        }

        //Se esiste l'utente, lo assegno ad una variabile.
        Utente user = optionalUser.get();

        //Controllo se il campo del nome non è vuoto e non contiene solo spazi bianchi, aggiorno il nome dell'utente.
        if(!request.getNome().isBlank() && !request.getNome().isEmpty()){
            user.setNome(request.getNome());
        }

        //Controllo se il campo del cognome non è vuoto e non contiene solo spazi bianchi, aggiorno il cognome dell'utente.
        if(!request.getCognome().isBlank() && !request.getCognome().isEmpty()){
            user.setCognome(request.getCognome());
        }

        //Controllo se il campo dell'indirizzo email non è vuoto, non contiene solo spazi bianchi e non coincide con quello attuale.
        if(!request.getEmail().isBlank() &&
                !request.getEmail().isEmpty() &&
                !request.getEmail().equals(user.getEmail())){

            //Controllo se esiste già un utente con il nuovo indirizzo email.
            Optional<Utente> userWithEmail = userRepository.findByEmail(request.getEmail());

            //Se esiste, lancio l'eccezione.
            if(userWithEmail.isPresent()){
                throw new ConflictException("Utente già presente con quell'email.");
            }else{
                //Se non esiste, assegno all'utente il nuovo indirizzo email.
                user.setEmail(request.getEmail());
            }
        }

        //Controllo se i campi "vecchia password" e "nuova password" non sono vuoti e non contengono spazi bianchi.
        if(!request.getVecchiaPassword().isBlank() &&
                !request.getVecchiaPassword().isEmpty() &&
                !request.getNuovaPassword().isBlank() &&
                !request.getNuovaPassword().isEmpty()){

            //Controllo se utente inserisce correttamente la sua vecchia password, se non è corretta lancio l'eccezione.
            if(!passwordEncoder.matches(request.getVecchiaPassword(), user.getPassword())){
                throw new BadRequestException("Password non corretta.");
            }

            //Aggiorno la password dell'utente con una nuova password fornita, criptandola prima di salvarla nel database.
            user.setPassword(passwordEncoder.encode(request.getNuovaPassword()));
        }

        //Salvo le modifiche apportate.
        userRepository.save(user);

        //Ritorno il DTO che conterrà i dettagli aggiornati dell'utente.
        return new UserResponse(
                user.getIdUtente(),
                user.getNome(),
                user.getCognome(),
                user.getPassword(),
                user.getEmail(),
                user.getRuolo()
        );
    }
}
