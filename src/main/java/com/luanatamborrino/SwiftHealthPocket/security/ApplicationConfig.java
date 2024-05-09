package com.luanatamborrino.SwiftHealthPocket.security;

import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean utili per la configurazione generale dell'applicazione. Grazie all'annotazione "@Configuration",
 * il contenuto di questa classe viene eseguito appena il server viene avviato
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository repository;

    /**
     * Dato un username, viene preso l'utente dal database.
     * @return Dati dell'utente in sessione.
     */
    @Bean
    public UserDetailsService userDetailsService() {

        return username -> repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
    }

    /**
     * Bean con configurazioni di base per l'autenticazione
     * @return Un oggetto con le configurazioni settate.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {

        //Creo un'istanza di DaoAuthenticationProvider
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        //Imposto il servizio per ottenere i dettagli degli utenti, utilizzato per caricare dati degli utenti durante l'autenticazione.
        authProvider.setUserDetailsService(userDetailsService());

        //Specifico il metodo per codificare la password.
        authProvider.setPasswordEncoder(passwordEncoder());

        //Restituisco l'istanza configurata del provider di autenticazione.
        return authProvider;
    }

    /**
     * Bean che specifica come gestire l'autenticazione.
     * @param config Configurazione dell'authenticationManager.
     * @return Un'istanza dell'authenticationManager.
     * @throws Exception Eccezione generale che può essere lanciata durante la configurazione.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return  config.getAuthenticationManager();
    }

    /**
     * Bean che specifica quale metodo utilizzare per codificare la password.
     * @return Un'istanza dell'encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
