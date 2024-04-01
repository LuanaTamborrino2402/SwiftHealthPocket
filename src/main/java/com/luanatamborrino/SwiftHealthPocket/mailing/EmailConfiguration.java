package com.luanatamborrino.SwiftHealthPocket.mailing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.util.Properties;

@Configuration
public class EmailConfiguration {

    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("swifthealthpocket@gmail.com");
        mailSender.setPassword("yxhn bgjr pajb rhsz");

        Properties props = mailSender.getJavaMailProperties();

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    @Primary
    public FreeMarkerConfigurationFactoryBean factoryBean() {

        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();

        bean.setTemplateLoaderPath("classpath:/templates");

        return bean;
    }
}
