package com.luanatamborrino.SwiftHealthPocket.observer.subscriber;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ControlloEsito implements Subscriber{
    @Override
    public void update() {
        System.out.println("Sono in controllo esito");
    }
}
