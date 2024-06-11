package com.luanatamborrino.SwiftHealthPocket.model;

import com.luanatamborrino.SwiftHealthPocket.model._enum.Ruolo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Model che rappresenta l'utente sul database. Implementa l'interfaccia UserDetails di Spring Security,
 * fornendo metodi essenziali per la gestione della sicurezza.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Utente")
@Table(
        name = "utente",
        uniqueConstraints = @UniqueConstraint(
                name = "email_unique",
                columnNames = "email"
        )
)
@Builder
public class Utente implements UserDetails {

    @Id
    @SequenceGenerator(name = "utente_sequence", sequenceName = "utente_sequence", allocationSize = 1)
    @GeneratedValue(generator = "utente_sequence", strategy = GenerationType.SEQUENCE)
    @Column(name = "id_utente", updatable = false)
    private Long idUtente;

    @Column(name = "nome", nullable = false, columnDefinition = "VARCHAR(255)")
    private String nome;

    @Column(name = "cognome", nullable = false, columnDefinition = "VARCHAR(255)")
    private String cognome;

    @Column(name = "password",nullable = false, columnDefinition = "VARCHAR(255)")
    private String password;

    @Column(name = "email",nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(name = "ruolo", nullable = false, columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    @ManyToOne
    @JoinColumn(name = "id_struttura")
    private Struttura struttura;

    @OneToMany(mappedBy = "paziente")
    private List<Prestazione> prestazioniPaziente; //Lista di prestazioni associate al paziente.

    @OneToMany(mappedBy = "infermiere")
    private List<Prestazione> prestazioniInfermiere; //Lista di prestazioni gestite dall'Infermiere.

    @OneToMany(mappedBy = "paziente")
    private List<Recensione> recensioni; //Lista di recensioni lasciate dal paziente.

    /**
     * Metodo che viene ereditato dall'interfaccia UserDetails. Viene generata la lista delle funzionalità permesse in base al ruolo dell'utente.
     * @return I permessi dell'utente.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ruolo.name()));
    }

    /**
     * Metodo che viene ereditato dall'interfaccia UserDetails.
     * @return La password per l'autenticazione.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Metodo che viene ereditato dall'interfaccia UserDetails.
     * @return Il valore univoco per l'autenticazione.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Metodo che viene ereditato dall'interfaccia UserDetails. Verifica se l'account è scaduto.
     * Un account scaduto non è in grado di autenticarsi.
     * @return "true" se l'account è valido (non scaduto), "false" se non è valido(scaduto).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; //La scadenza del token non è gestita, pertanto restituisce sempre "true".
    }

    /**
     * Metodo che viene ereditato dall'interfaccia UserDetails. Verifica se l'account dell'utente è bloccato.
     * Un account bloccato non può eseguire l'autenticazione.
     * @return "true" se l'account non è bloccato, "false" se l'account è bloccato.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; //Il blocco dell'account non è stato implementato, quindi restituisce sempre "true".
    }
    /**
     * Metodo che viene ereditato dall'interfaccia UserDetails. Verifica se la password dell'utente è scaduta.
     * Una password scaduta impedisce l'autenticazione.
     * @return "true" se la password è valida (non scaduta), "false" se non è più valida (scaduta).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; //La scadenza delle credenziali non viene gestita, pertanto viene sempre restituito "true".
    }

    /**
     * Metodo ereditato dall'interfaccia UserDetails. Verifica lo stato di abilitazione dell'utente.
     * Un utente disabilitato non può effettuare l'autenticazione.
     * @return "true" se l'utente è abilitato, "false" se l'utente non è abilitato.
     */
    @Override
    public boolean isEnabled() {
        return true; //La scadenza dell'abilitazione dell'utente non è implementata, quindi restituisce sempre "true".







    }
}
