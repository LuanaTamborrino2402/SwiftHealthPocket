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

@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender emailSender;

    private final Configuration configuration;

    public void sendEmail(SendEmailRequest request){

        MimeMessage message = emailSender.createMimeMessage();

        try{

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );
            Template template = configuration.getTemplate(
                    request.getEventType().equals("InfermiereDissociato") ? "infermiereDissociato.ftl" :
                            request.getEventType().equals("ControlloEsito") ? "controlloEsito.ftl" :
                                    "richiestaCambioStruttura.ftl"
            );
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, request.getDatiDinamici());
            helper.setTo(request.getEmailDestinatario());
            helper.setSubject(request.getOggetto());
            helper.setText(html, true);
            emailSender.send(message);
        }catch(MessagingException | IOException | TemplateException e){
            throw new InternalServerErrorException("Errore durante l'invio dell'email");
        }
    }
}
