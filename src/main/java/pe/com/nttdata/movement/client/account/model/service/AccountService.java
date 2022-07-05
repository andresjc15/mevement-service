package pe.com.nttdata.movement.client.account.model.service;

import pe.com.nttdata.movement.client.account.model.Account;
import reactor.core.publisher.Mono;

public interface AccountService {

    public Mono<Account> findById(Long id);
    public Mono<Account> update(Account account);
}
