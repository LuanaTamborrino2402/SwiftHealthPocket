package com.luanatamborrino.SwiftHealthPocket.observer.subscriber;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InfermiereDissociato implements Subscriber{

    @Override
    public void update() {
        System.out.println("Sono in infermiere dissociato");
    }
}
