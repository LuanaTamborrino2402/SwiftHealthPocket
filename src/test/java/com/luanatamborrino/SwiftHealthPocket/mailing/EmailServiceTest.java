package com.luanatamborrino.SwiftHealthPocket.mailing;

import com.luanatamborrino.SwiftHealthPocket.dto.request.SendEmailRequest;
import com.luanatamborrino.SwiftHealthPocket.exception.InternalServerErrorException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    JavaMailSender emailSender;
    @Mock
    Configuration configuration;
    @Mock
    MimeMessage mimeMessage;
    @Mock
    Template template;
    @InjectMocks
    EmailService emailService;

    @Test
    void inviaEmailThrowsErroreInvioEmail() {

        SendEmailRequest request = new SendEmailRequest(
                "luanatamborrino02@gmail.com",
                "Oggettto della mail",
                new HashMap<>(),
                "eventType"
        );

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);


        try {
            when(configuration.getTemplate(any())).thenThrow(IOException.class);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        assertThrows(InternalServerErrorException.class,
                () -> emailService.sendEmail(request));
    }

    @Test
    void inviaEmailSuccessful() {

        SendEmailRequest request = new SendEmailRequest(
                "luanatamborrino02@gmail.com",
                "Oggettto della mail",
                new HashMap<>(),
                "eventType"
        );

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        try {
            when(configuration.getTemplate(any())).thenReturn(template);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        assertAll(() -> emailService.sendEmail(request));
    }
}
