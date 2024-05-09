package com.luanatamborrino.SwiftHealthPocket.mailing;

import com.luanatamborrino.SwiftHealthPocket.dto.request.SendEmailRequest;
import com.luanatamborrino.SwiftHealthPocket.exception.InternalServerErrorException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Serivce che gestisce tutti i metodi riguardanti il servizio di mailing.
 */
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender emailSender; // Bean che fornisce funzionalità per l'invio di email.

    private final Configuration configuration; // Configurazione utilizzata per la gestione dei template di email.

    /**
     * Metodo per l'invio di email basato su template configurabili.
     * @param request DTO che contiene le informazioni necessarie per l'invio dell'email.
     */
    public void sendEmail(SendEmailRequest request) {

        //Creo un nuovo oggetto MimeMessage per rappresentare un'email.
        MimeMessage message = emailSender.createMimeMessage();

        try{

            //Helper per configurare il messaggio email.
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, //Messaggio MIME.
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());


            //Selezione del template da utilizzare in base al tipo di email.
            Template template = configuration.getTemplate(
                    request.getEventType().equals("InfermiereDissociato") ? "infermiereDissociato.ftl" :
                            request.getEventType().equals("ControlloEsito") ? "controlloEsito.ftl" :
                                    "richiestaCambioStruttura.ftl");

            // Trasforma il template in un testo HTML con i dati forniti.
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, request.getDatiDinamici());

            helper.setTo(request.getEmailDestinatario()); //Imposta il destinatario dell'email.
            helper.setSubject(request.getOggetto()); //Imposta l'oggetto dell'email.
            helper.setText(html, true); //Imposta il contenuto dell'email in formato HTML.

            //Invio l'email.
            emailSender.send(message);

        }catch(MessagingException | IOException | TemplateException e) {

            //Messaggio di errore in caso di eccezioni durante la creazione o l'invio dell'email
            throw new InternalServerErrorException("Errore durante l'invio dell'email");
        }
    }
}
