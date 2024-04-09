package com.luanatamborrino.SwiftHealthPocket.observer.publisher;

import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.Subscriber;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Publisher {

    private Map<String, Subscriber> listeners = new HashMap<>();
    public void subscribe(String eventType, Subscriber subscriber){
        listeners.put(eventType, subscriber);
    }

    public void unsubscribe(String eventType){
        listeners.remove(eventType);
    }

    public void notify(String eventType){

        listeners.get(eventType).update();

    }
}
