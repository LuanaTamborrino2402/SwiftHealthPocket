package com.luanatamborrino.SwiftHealthPocket.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

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
    private List<Prestazione> prestazioniPaziente;

    @OneToMany(mappedBy = "infermiere")
    private List<Prestazione> prestazioniInfermiere;

    @OneToMany(mappedBy = "paziente")
    private List<Recensione> recensioni;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ruolo.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
