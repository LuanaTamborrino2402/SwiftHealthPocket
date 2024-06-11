package com.luanatamborrino.SwiftHealthPocket.mailing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class EmailConfigurationTest {

    @InjectMocks
    EmailConfiguration emailConfiguration;

    @Test
    void getJavaMailSender() {
        assertAll(() -> emailConfiguration.getJavaMailSender());
    }

    @Test
    void factoryBean() {
        assertAll(() -> emailConfiguration.factoryBean());
    }
}
