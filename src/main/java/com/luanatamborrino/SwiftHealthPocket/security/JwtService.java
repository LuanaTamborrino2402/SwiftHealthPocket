package com.luanatamborrino.SwiftHealthPocket.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service per gestire tutti i metodi riguardanti il jwt.
 */
@Service
public class JwtService {

    private static final String SECRET_KEY = "4D30457130736F38466A626A6251534F6D61366E31674F7356394B395031676A";

    /**
     * Metodo usato nel filter, semplicemente estrae l'username codificato nel jwt.
     * @param token Stringa jwt.
     * @return Username dell'utente in sessione.
     */
    public String extractUsername(String token) {

        //getSubject restituisce l'attributo univoco dell'utente.
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * Dato il token e il tipo di dato SINGOLO che si vuole estrarre, viene ritornato proprio quel dato generico.
     * @param token Stringa jwt.
     * @param claimsResolver Funzione che riceve in ingresso i dati estratti dal token e ritorna il singolo dato richiesto.
     * @return Il singolo dato richiesto codificato nel jwt.
     * @param <T> Tipo del dato che voglio estrarre dal jwt.
     */
    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {

        //Estraggo tutti i dati codificati nel token.
         Claims claims = extractAllClaims(token);

        //Estraggo il singolo dato (generico) dall'insieme di tutti i dati codificati nel token.
        return claimsResolver.apply(claims);
    }

    /**
     * Codifica nel token solamente dati dell'utente. Se bisogna codificare anche altri parametri,
     * viene usato il metodo sottostante a questo (overloading).
     * @return Stringa jwt.
     */
    public String generateToken(UserDetails userDetails) {

        //Chiama il metodo sottostante passando un hashmap vuoto per altri parametri facoltativi.
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Date le informazioni in ingresso viene codificato il tutto nel jwt.
     * @param extraClaims Eventuali informazioni aggiuntive da codificare nel token.
     * @param userDetails Dati dell'utente in sessione.
     * @return Stringa jwt.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        return Jwts
            .builder() //Design pattern builder.
            .setClaims(extraClaims) //Aggiungo eventuali informazioni aggiuntive da codificare.
            .setSubject(userDetails.getUsername()) //Aggiungo l'attributo univoco dell'utente.
            .setIssuedAt(new Date(System.currentTimeMillis())) //Data di creazione del token.
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 300)) //Data di scadenza del token.
            .signWith(getSignInKey(), SignatureAlgorithm.HS256) //Codifico con la chiave segreta e specifico l'algoritmo.
            .compact(); //Genero il token.
    }

    /**
     * Viene controllato se l'username estratto dal token è uguale a quello dell'utente in sessione
     * e poi la data di scadenza del token.
     * @param token Stringa jwt.
     * @param userDetails Dati dell'utente in sessione.
     * @return "true" se il token è valido, "false" se il token non è valido.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {

        //Estraggo l'username dal token.
        final String username = extractUsername(token);

        //Controllo se l'username dell'utente loggato è uguale a quello del token e poi controllo la scadenza.
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Controlla se la data di scadenza del token è precedente a quella attuale.
     * @param token Stringa jwt.
     * @return "true" se il token è scaduto, "false" se il token non è scaduto.
     */
    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    /**
     * Dato un token, viene restituita la data di scadenza.
     * @param token Stringa jwt.
     * @return La data di scadenza del token.
     */
    private Date extractExpiration(String token) {

        //getExpiration ritorna la data di scadenza del token.
        return extractClaims(token, Claims::getExpiration);
    }

    /**
     * Utilizza la classe Jwts della dependency io.jsonwebtoken per
     * estrarre i dati che sono stati codificati nel jwt.
     * @param token Stringa jwt.
     * @return Tutti i dati codificati nel jwt.
     */
    private Claims extractAllClaims(String token) {

        return Jwts
                .parserBuilder() //Parser per poter utilizzare il builder.
                .setSigningKey(getSignInKey()) //Setto la chiave segreta per codificare
                .build() //Applico il pattern builder, quindi "costruisco" l'oggetto.
                .parseClaimsJws(token) //Mando il token da decodificare.
                .getBody(); //Prendo tutti i dati codificati nel jwt.
    }

    /**
     * Decodifica la stringa in base 64 e la codifica secondo un algoritmo di hash, così da poter lavorare con il jwt.
     * @return La chiave codificata, con cui gestire il jwt.
     */
    private Key getSignInKey() {

        //Decodifico la nostra chiave segreta
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        /* Viene codificata la chiave secondo un tipo di algoritmo di hash a chiave costruito a partire
        dalla funzione di hash SHA-256 e utilizzato come Hash-based Message Authentication Code (HMAC). */
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
