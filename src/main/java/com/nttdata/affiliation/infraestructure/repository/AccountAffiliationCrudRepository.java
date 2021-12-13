package com.nttdata.affiliation.infraestructure.repository;

import com.nttdata.affiliation.application.AccountAffiliationRepository;
import com.nttdata.affiliation.domain.AccountAffiliation;
import com.nttdata.affiliation.domain.bean.Account;
import com.nttdata.affiliation.domain.bean.Customer;
import com.nttdata.affiliation.infraestructure.client.UriService;
import com.nttdata.affiliation.infraestructure.model.dao.AccountAffiliationDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ACCOUNTAFFILIATIONCRUDREPOSITORY.
 * Implementa las operaciones (CRUD) de la afiliación de cuentas bancarias
 */
@Component
@Slf4j
public class AccountAffiliationCrudRepository
        implements AccountAffiliationRepository {
    /**
     * Operaciones de afiliación de cuentas bancarias.
     */
    private final IAccountAffiliationCrudRepository repository;
    /**
     * Servicio web cliente.
     */
    private final WebClient webClient;
    /**
     * Circuit Breaker.
     */
    private final ReactiveCircuitBreaker reactiveCircuitBreaker;

    /**
     * Constructor.
     * @param circuitBreakerFactory corto circuito.
     * @param iAccountAffiliationCrudRepository respositorio.
     */
    public
    AccountAffiliationCrudRepository(
    final ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory,
    final IAccountAffiliationCrudRepository iAccountAffiliationCrudRepository) {
        this.repository = iAccountAffiliationCrudRepository;
        this.webClient = WebClient.builder()
                .baseUrl(UriService.BASE_URI)
                .build();
        this.reactiveCircuitBreaker = circuitBreakerFactory.create("account");
    }
    /**
     * Regitra las afiliaciones de cuentas bancarias de un cliente.
     * @param accountAffiliation afiliación de cuenta.
     * @return Mono<AccountAffiliation>
     */
    @Override
    public
    Mono<AccountAffiliation>
    create(final AccountAffiliation accountAffiliation) {
        log.info("[create] Inicio");
        return repository
                .save(
                        mapAccountAffiliationToAccountAffiliationDao(
                                accountAffiliation
                        )
                )
                .map(this::mapAccountAffiliationDaoToAccountAffiliation);

    }
    /**
     * Actualiza las afiliaciones de cuentas bancarias de un cliente.
     * @param id codigo.
     * @param accountAffiliation afiliación de cuenta.
     * @return Mono<AccountAffiliation>
     */
    @Override
    public
    Mono<AccountAffiliation>
    update(final String id, final AccountAffiliation accountAffiliation) {
        return repository
                .findById(id)
                .flatMap(p ->
                        create(
                                mapAccountAffiliationDaoToAccountAffiliation(
                                        p, accountAffiliation)
                        )
                );
    }
    /**
     * Elimina los datos de la afiliacion de cuentas bancarias de un cliente.
     * @param id Codigo de la cuenta.
     * @return Mono<AccountAffiliationDao>
     */
    @Override
    public
    Mono<Void>
    delete(final String id) {
        return repository.findById(id)
                .flatMap(p -> repository.deleteById(p.getId()));
    }
    /**
     * Busca por Id los datos de la afiliacion de cuentas bancarias.
     * @param id codigo.
     * @return Mono<AccountAffiliation>
     */
    @Override
    public
    Mono<AccountAffiliation>
    findById(final String id) {
        return repository.findById((id))
                .map(this::mapAccountAffiliationDaoToAccountAffiliation);
    }
    /**
     * Busca todas las afiliaciones de cuentas bancarias de un cliente.
     * @return Flux<AccountAffiliation>
     */
    @Override
    public
    Flux<AccountAffiliation>
    findAll() {
        return repository.findAll()
                .map(this::mapAccountAffiliationDaoToAccountAffiliation);
    }

    /**
     * Listado de Afiliaciones de cuentas por Cliente.
     * @param idCustomer Codigo del cliente.
     * @return Flux<AccountAffiliation>
     */
    @Override
    public
    Flux<AccountAffiliation>
    findByIdCustomer(String idCustomer) {
        return repository.findByIdCustomer(idCustomer);
    }

    /**
     * Listado de Afiliación por cliente y por cuenta.
     * @param idCustomer Codigo de cliente.
     * @param idAccount Codigo de la cuenta.
     * @return Flux<AccountAffiliation>
     */
    @Override
    public
    Flux<AccountAffiliation>
    findByIdCustomerByIdAccount(String idCustomer, String idAccount) {
        return repository.findByIdCustomerByIdAccount(idCustomer, idAccount);
    }

    /**
     * Crea AccountAffiliation y asigna los datos de AccountAffiliationDao.
     * @param accountAffiliation afiliación de cuenta.
     * @return AccountAffiliationDao
     */
    private
    AccountAffiliationDao
    mapAccountAffiliationToAccountAffiliationDao(
      final AccountAffiliation accountAffiliation) {

        AccountAffiliationDao accountAffiliationDao
                = new AccountAffiliationDao();
        BeanUtils.copyProperties(accountAffiliation, accountAffiliationDao);
        return accountAffiliationDao;
    }
    /**
     * Crea AccountAffiliation y asigna los datos de AccountAffiliationDao.
     * @param accountAffiliationDao afiliación de cuenta.
     * @return AccountAffiliation
     */
    private
    AccountAffiliation
    mapAccountAffiliationDaoToAccountAffiliation(
      final AccountAffiliationDao accountAffiliationDao) {

        log.info("[mapAccountAffiliationDaoToAccountAffiliation] Inicio");
        AccountAffiliation accountAffiliation = new AccountAffiliation();
        BeanUtils.copyProperties(accountAffiliationDao, accountAffiliation);
        //Complementamos los datos faltantes
        Flux<Customer> customers = getCustomerById(accountAffiliationDao.getIdCustomer());
        accountAffiliation.setCustomer(customers.blockFirst());
        Flux<Account> accounts = getProductAccountById(accountAffiliationDao.getIdAccount());
        accountAffiliation.setAccount(accounts.blockFirst());
        log.info("[mapAccountAffiliationDaoToAccountAffiliation] Fin");
        return accountAffiliation;
    }
    /**
     * Asigna el Id de AccountAffiliationDao a AccountAffiliation.
     * @param accountAffiliationDao afiliación de cuenta Dao.
     * @param accountAffiliation afiliación de cuenta.
     * @return AccountAffiliation
     */
    private
    AccountAffiliation
    mapAccountAffiliationDaoToAccountAffiliation(
      final AccountAffiliationDao accountAffiliationDao,
       final AccountAffiliation accountAffiliation) {

        accountAffiliation.setId(accountAffiliationDao.getId());
        return accountAffiliation;
    }
    /**
     * Obtenemos los datos del cliente.
     * @param idCustomer Codigo del cliente.
     * @return Flux<Customer>
     */
    @Override
    public
    Flux<Customer>
    getCustomerById(final String idCustomer) {
        log.info("[getCustomerById] Inicio:"+idCustomer);
        return reactiveCircuitBreaker
                .run(
                        webClient
                                .get()
                                .uri(
                                        UriService.CUSTOMER_GET_BY_ID,
                                        idCustomer
                                )
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToFlux(Customer.class),
                throwable -> {
                    log.info("throwable => {}", throwable.toString());
                    log.info("[getCustomerById] Error en la llamada:"
                            + UriService.CUSTOMER_GET_BY_ID
                            + idCustomer);
                    return Flux.just(new Customer());
                });
    }
    /**
     * Obtenemos los datos del producto: Cuenta Bancaria.
     * @param idAccount codigo de la cuenta bancaria
     * @return Flux<Account>
     */
    @Override
    public
    Flux<Account>
    getProductAccountById(final String idAccount) {
        log.info("[getProductAccountById] Inicio");
        return reactiveCircuitBreaker
                .run(
                        webClient
                                .get()
                                .uri(
                                        UriService.PRODUCT_ACCOUNT_GET_BY_ID,
                                        idAccount
                                )
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToFlux(Account.class),
                throwable -> {
                    log.info("throwable => {}", throwable.toString());
                    log.info("[getProductAccountById] Error en la llamada:"
                            + UriService.PRODUCT_ACCOUNT_GET_BY_ID
                            + idAccount);
                    return Flux.just(new Account());
                });
    }
}
