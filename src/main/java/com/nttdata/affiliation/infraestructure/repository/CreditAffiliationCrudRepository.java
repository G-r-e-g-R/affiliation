package com.nttdata.affiliation.infraestructure.repository;

import com.nttdata.affiliation.application.CreditAffiliationRepository;
import com.nttdata.affiliation.domain.CreditAffiliation;
import com.nttdata.affiliation.domain.bean.Credit;
import com.nttdata.affiliation.domain.bean.Customer;
import com.nttdata.affiliation.infraestructure.client.UriService;
import com.nttdata.affiliation.infraestructure.model.dao.CreditAffiliationDao;
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
 * CREDITAFFILIATIONCRUDREPOSITORY.
 * Implementa las operaciones (CRUD) de la afiliación de Credito
 */
@Component
@Slf4j
public class CreditAffiliationCrudRepository
        implements CreditAffiliationRepository {
    /**
     *  Operaciones de afiliación de creditos.
     */
    private final ICreditAffiliationCrudRepository repository;
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
     * @param iCreditAffiliationCrudRepository repositorio.
     */
    public CreditAffiliationCrudRepository(
    final ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory,
    final ICreditAffiliationCrudRepository iCreditAffiliationCrudRepository) {
        this.repository = iCreditAffiliationCrudRepository;
        this.webClient = WebClient
                .builder()
                .baseUrl(UriService.BASE_URI)
                .build();
        this.reactiveCircuitBreaker = circuitBreakerFactory.create("credit");
    }
    /**
     * Regitra las afiliaciones de credito de un cliente.
     * @param creditAffiliation afiliación de credito.
     * @return Mono<CreditAffiliation>
     */
    @Override
    public
    Mono<CreditAffiliation>
    create(
            final CreditAffiliation creditAffiliation) {
        return repository
                .save(
                        mapCreditAffiliationToCreditAffiliationDao(
                                creditAffiliation
                        )
                )
                .map(this::mapCreditAffiliationDaoToCreditAffiliation);
    }
    /**
     * Actualiza las afiliaciones de credito de un cliente.
     * @param id codigo.
     * @param creditAffiliation afiliación de credito.
     * @return Mono<CreditAffiliation>
     */
    @Override
    public
    Mono<CreditAffiliation>
    update(
            final String id, final CreditAffiliation creditAffiliation) {
        return repository
                .findById(id)
                .flatMap(p ->  create(
                        mapCreditAffiliationDaoToCreditAffiliation(
                                p, creditAffiliation
                        ))
                );
    }
    /**
     * Elimina los datos de la afiliacion de Credito de un cliente.
     * @param id codigo.
     * @return Mono<CreditAffiliationDao>
     */
    @Override
    public
    Mono<Void>
    delete(final String id) {
        return repository
                .findById(id)
                .flatMap(p -> repository.deleteById(p.getId()));
    }
    /**
     * Busca por el Id los datos de la afiliacion de credito de un cliente.
     * @param id codigo.
     * @return Mono<CreditAffiliation>
     */
    @Override
    public
    Mono<CreditAffiliation>
    findById(final String id) {
        return repository.findById((id))
                .map(this::mapCreditAffiliationDaoToCreditAffiliation);
    }
    /**
     * Busca  los datos de todas las afiliaciones de credito de un cliente.
     * @return Flux<CreditAffiliation>
     */
    @Override
    public
    Flux<CreditAffiliation>
    findAll() {
        return repository
                .findAll()
                .map(this::mapCreditAffiliationDaoToCreditAffiliation);
    }
    /*
    mapCreditAffiliationToCreditAffiliationDao:

     */

    /**
     * A la clase CreditAffiliation asigna los datos de CreditAffiliationDao.
     * @param creditAffiliation afiliación de credito.
     * @return CreditAffiliationDao
     */
    private CreditAffiliationDao mapCreditAffiliationToCreditAffiliationDao(
            final CreditAffiliation creditAffiliation) {
        CreditAffiliationDao creditAffiliationDao = new CreditAffiliationDao();
        BeanUtils.copyProperties(creditAffiliation, creditAffiliationDao);
        return creditAffiliationDao;
    }
    /**
     * A la clase CreditAffiliation asigna los datos de CreditAffiliationDao.
     * @param creditAffiliationDao afiliación de credito.
     * @return CreditAffiliation
     */
    private CreditAffiliation mapCreditAffiliationDaoToCreditAffiliation(
            final CreditAffiliationDao creditAffiliationDao) {
        log.info("[mapCreditAffiliationDaoToCreditAffiliation] Inicio");
        CreditAffiliation creditAffiliation = new CreditAffiliation();
        BeanUtils.copyProperties(creditAffiliationDao, creditAffiliation);
        Mono<Customer> customers = getCustomerById(creditAffiliationDao.getIdCustomer());
        creditAffiliation.setCustomer(customers.block());
        Mono<Credit> credits = getProductCreditById(creditAffiliationDao.getIdCredit());
        creditAffiliation.setCredit(credits.block());
        log.info("[mapCreditAffiliationDaoToCreditAffiliation] Fin");
        return creditAffiliation;
    }

    /**
     * Asigna el Id de CreditAffiliationDao a CreditAffiliation.
     * @param creditAffiliationDao afiliación de credito Dao.
     * @param creditAffiliation afiliación de credito.
     * @return CreditAffiliation
     */
    private CreditAffiliation mapCreditAffiliationDaoToCreditAffiliation(
            final CreditAffiliationDao creditAffiliationDao,
            final CreditAffiliation creditAffiliation) {
        creditAffiliation.setId(creditAffiliationDao.getId());
        return creditAffiliation;
    }
    /**
     * Listado de Afiliaciones de creditos por Cliente.
     * @param idCustomer Codigo del cliente.
     * @return Flux<CreditAffiliation>
     */
    @Override
    public
    Flux<CreditAffiliation>
    findByIdCustomer(String idCustomer) {
        return repository.findByIdCustomer(idCustomer);
    }

    /**
     * Listado de Afiliacion por cliente y por credito.
     * @param idCustomer Codigo de cliente.
     * @param idCredit Codigo del credito.
     * @return Flux<CreditAffiliation>
     */
    @Override
    public
    Flux<CreditAffiliation>
    findByIdCustomerByIdAccount(String idCustomer, String idCredit) {
        return repository.findByIdCustomerByIdAccount(idCustomer, idCredit);
    }

    /**
     * Obtenemos los datos del cliente.
     * @param idCustomer Codigo del cliente.
     * @return Mono<Customer>
     */
    @Override
    public
    Mono<Customer>
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
                                .bodyToMono(Customer.class),
                        throwable -> {
                            log.info("throwable => {}", throwable.toString());
                            log.info("[getCustomerById] Error en la llamada:"
                                    + UriService.CUSTOMER_GET_BY_ID
                                    + idCustomer);
                            return Mono.just(new Customer());
                        });
    }
    /**
     * Obtenemos los datos del producto: Credito.
     * @param idCredit codigo del credito
     * @return Mono<Credit>
     */
    @Override
    public
    Mono<Credit>
    getProductCreditById(final String idCredit) {
        log.info("[getProductCreditById] Inicio");
        return reactiveCircuitBreaker
                .run(
                    webClient
                            .get()
                            .uri(
                                    UriService.PRODUCT_CREDIT_GET_BY_ID,
                                    idCredit
                            )
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(Credit.class),
                    throwable -> {
                        log.info("throwable => {}", throwable.toString());
                        log.info("[getProductAccountById] Error en la llamada:"
                                + UriService.PRODUCT_CREDIT_GET_BY_ID
                                + idCredit);
                        return Mono.just(new Credit());
                    });
    }
}
