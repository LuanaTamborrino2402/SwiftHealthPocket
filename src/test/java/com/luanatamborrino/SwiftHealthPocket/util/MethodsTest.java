package com.luanatamborrino.SwiftHealthPocket.util;

import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class MethodsTest {

    @InjectMocks
    Methods methods;

    @Test
    void checkStringDataThrowsInserireTuttiICampi() {
        assertThrows(BadRequestException.class,
                () -> methods.checkStringData(List.of("")));
    }

    @Test
    void checkIdsThrowsInserireTuttiICampi() {
        assertThrows(BadRequestException.class,
                () -> methods.checkIds(List.of(0L)));
    }
}
