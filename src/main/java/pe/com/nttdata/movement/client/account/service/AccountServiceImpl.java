package pe.com.nttdata.movement.client.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.nttdata.movement.client.account.model.Account;
import pe.com.nttdata.movement.client.account.model.service.AccountService;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private WebClient.Builder client;

    @Override
    public Mono<Account> findById(Long id) {
        return client.build()
                .get()
                .uri("/{id}", Collections.singletonMap("id", id))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Account.class);
    }

    @Override
    public Mono<Account> update(Account account) {
        return client.build()
                .put()
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .retrieve()
                .bodyToMono(Account.class);
    }

}
