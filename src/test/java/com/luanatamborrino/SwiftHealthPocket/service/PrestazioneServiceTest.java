package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.observer.publisher.Publisher;
import com.luanatamborrino.SwiftHealthPocket.repository.PrestazioneRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.StrutturaRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PrestazioneServiceTest {
    @Mock
    PrestazioneRepository prestazioneRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    StrutturaRepository strutturaRepository;

    @Mock
    Publisher publisher;

    @InjectMocks
    PrestazioneService prestazioneService;




}
