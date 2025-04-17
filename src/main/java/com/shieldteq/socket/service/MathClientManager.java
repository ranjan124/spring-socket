package com.shieldteq.socket.service;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class MathClientManager {
    private final Set<RSocketRequester> clients = Collections.synchronizedSet(new HashSet<>());

    public void addClient(RSocketRequester client) {
        client.rsocketClient().onClose().doFirst(() -> clients.add(client))
                .doFinally(s -> {
                    System.out.println("Removing client: " + client);
                    clients.remove(client);
                }).subscribe();
    }

//    @Scheduled(fixedRate = 1000)
//    public void print() {
//        System.out.println("Clients: " + clients);
//    }

    public void notify(int i) {
        Flux.fromIterable(clients).flatMap(c -> c.route("math.updates").data(i).send()).subscribe();
    }
}
