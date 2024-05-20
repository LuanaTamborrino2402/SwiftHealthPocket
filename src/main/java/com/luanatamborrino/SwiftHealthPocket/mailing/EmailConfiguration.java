package com.luanatamborrino.SwiftHealthPocket.mailing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.util.Properties;

/**
 * Bean per la configurazione del servizio di mailing.
 */
@Configuration
public class EmailConfiguration {

    /**
     * Bean che ritorna un'istanza di JavaMailSenders configurata in base
     * ai protocolli e alle credianziali adatte all'applicazione.
     * @return Istanza di JavaMailSender.
     */
    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        //Impostazione del server SMTP di Gmail come host di invio.
        mailSender.setHost("smtp.gmail.com");
        //Utilizzo della porta SMTP standard per le connessioni sicure.
        mailSender.setPort(587);

        //Credenziali di accesso specifiche per questa applicazione.
        mailSender.setUsername("swifthealthpocket@gmail.com");
        //Password generata appositamente per l'uso in sviluppo.
        mailSender.setPassword("yxhn bgjr pajb rhsz");

        Properties props = mailSender.getJavaMailProperties();

        //Configurazione del protocollo di trasporto
        props.put("mail.transport.protocol", "smtp");
        //Attivazione dell'autenticazione per l'accesso al server SMTP.
        props.put("mail.smtp.auth", "true");
        /* Implementazione di STARTTLS per garantire una trasmissione dei dati
            criptata e sicura tra client e server. */
        props.put("mail.smtp.starttls.enable", "true");
        //Attivazione del debug per monitorare il flusso di operazioni SMTP tramite log.
        props.put("mail.debug", "true");

        return mailSender;
    }

    /**
     * Bean di configurazione per FreeMarker, utilizzato per gestire i template email.
     * Questo bean è prioritario se presenti più configurazioni di FreeMarker.
     * @return Configura e restituisce un bean di FreeMarkerConfigurationFactoryBean.
     */
    @Bean
    @Primary //Per risolvere delle ambiguità sulla scelta tra bean dello stesso tipo.
    public FreeMarkerConfigurationFactoryBean factoryBean() {

        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();

        //Definizione del percorso dei template utilizzati da FreeMarker.
        bean.setTemplateLoaderPath("classpath:/templates");

        return bean;
    }
}
