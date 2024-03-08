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
 * Model che rappresenta l'utente sul db. Implementa UserDetails, un'interfaccia
 * di Spring Security che espone una serie di metodi utili per la sicurezza.
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
     * Metodo ereditato dall'interfaccia UserDetails. In base al ruolo, viene generata una lista delle funzionalità concesse.
     * @return I permessi dell'utente.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ruolo.name()));
    }

    /**
     * Metodo ereditato dall'interfaccia UserDetails.
     * @return La password per l'autenticazione.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Metodo ereditato dall'interfaccia UserDetails.
     * @return Il valore univoco per l'autenticazione.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Metodo ereditato dall'interfaccia UserDetails. Indica se l'account è scaduto. Un account scaduto non può autenticarsi.
     * @return "true" se l'account è valido (non scaduto), "false" se non è valido(scaduto).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Metodo ereditato dall'interfaccia UserDetails. Indica se l'account dell'utente è bloccato o meno. Un account bloccato non può essere autenticato.
     * @return "true" se l'account non è bloccato, "false" se l'account è bloccato.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /**
     * Metodo ereditato dall'interfaccia UserDetails. Indica se la password dell'utente è scaduta. La password scaduta non permette l'autenticazione.
     * @return "true" se la password è valida (non scaduta), "false" se non è più valida (scaduta).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Metodo ereditato dall'interfaccia UserDetails. Indica se l'utente è abilitato o disabilitato. Un utente disabilitato non può essere autenticato.
     * @return "true" se l'utente è abilitato, "false" se l'utente non è abilitato.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
