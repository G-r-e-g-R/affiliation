package com.nttdata.affiliation.application;

import com.nttdata.affiliation.domain.AccountAffiliation;
import com.nttdata.affiliation.domain.bean.Account;
import com.nttdata.affiliation.domain.bean.AccountType;
import com.nttdata.affiliation.domain.bean.Customer;
import com.nttdata.affiliation.domain.bean.CustomerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * CUSTOMEROPERATIONSIMPL.
 * Implementa las operaciones (CRUD) de la afiliación
 *                         de Cuentas Bancarias de un cliente
 */
@Service
@Slf4j
public class AccountAffiliationOperationsImpl
        implements  AccountAffiliationOperations {

    /**
     * Repositorio de los registros (Afiliación) de cuentas bancarias.
     */
    private final AccountAffiliationRepository repository;

    /**
     * Constructor.
     * @param accountAffiliationRepository repositorio.
     */
    public
    AccountAffiliationOperationsImpl(
            final AccountAffiliationRepository accountAffiliationRepository) {
      this.repository = accountAffiliationRepository;
    }

    /**
     * Registro (Afiliación) de un cliente con cuenta bancaria.
     * @param accountAffiliation afiliación de cuenta.
     * @return Mono<AccountAffiliation>
     */
    @Override
    public
    Mono<AccountAffiliation>
    create(final AccountAffiliation accountAffiliation) {
        log.info("[create] Inicio");
      Flux<Customer>
      customerList = getCustomerById(accountAffiliation.getIdCustomer());
      Flux<Account>
      accountList = getProductAccountById(accountAffiliation.getIdAccount());

        return customerList.flatMap(
                customer -> {

                    if (customer.getCustomerType().name()
                        .equals(
                        CustomerType.EMPRESARIAL.name())
                    ){
                        return createEnterprise(accountAffiliation,accountList);
                    }else if (customer.getCustomerType().name()
                              .equals(
                              CustomerType.PERSONAL.name())
                    ){
                        return accountList
                                .flatMap(
                                        account ->
                                                createPersonal(
                                                        accountAffiliation,
                                                        account)
                                );
                    }else {
                        return Mono.just(new AccountAffiliation());
                    }
                }
        ).next();

    }

    /**
     * Afiliación de un cliente empresarial.
     * @param accountAffiliation datos de afiliacion.
     * @param accountList cuenta bancaria.
     * @return Mono<AccountAffiliation>
     */
    private
    Mono<AccountAffiliation>
    createEnterprise(
      final AccountAffiliation accountAffiliation,
      final Flux<Account> accountList) {
        log.info("[createEnterprise] Inicio");
        return accountList.flatMap(
                account -> {
                    if (account.getAccountType().name()
                        .equals(
                        AccountType.CUENTA_CORRIENTE.name())
                            &&
                        accountAffiliation.getNumberOfHolder() > 0   //Numero de titulares 1 o más
                    ){
                        return repository.create(accountAffiliation);
                    }else{
                        return Mono.just(new AccountAffiliation());
                    }
                }
        ).next();

    }
    /**
     * Afiliación de un cliente Personal.
     * @param accountAffiliation datos de afiliacion.
     * @param account cuenta bancaria.
     * @return Mono<AccountAffiliation>
     */
    private
    Mono<AccountAffiliation>
    createPersonal(
      final AccountAffiliation accountAffiliation,
      final Account account) {
        log.info("[createPersonal] Inicio");
        return findByIdCustomer(accountAffiliation.getIdCustomer())
                .filter(accAffiliation ->
                      getProductAccountById(accAffiliation.getIdAccount())
                              .blockFirst().getAccountType().name()
                              .equals(
                                      account.getAccountType().name()
                              )
                )
                .map( __ -> new AccountAffiliation())
                .switchIfEmpty(
                        Mono.defer(
                                () -> repository.create(accountAffiliation)
                        ))
                .next();
    }

    /**
     * Actualización de un cliente con cuenta bancaria.
     * @param id codigo.
     * @param accountAffiliation afiliación de cuenta.
     * @return Mono<AccountAffiliation>
     */
    @Override
    public
    Mono<AccountAffiliation>
    update(final String id, final AccountAffiliation accountAffiliation) {
        Mono<AccountAffiliation> accountAffiliationBD = repository.findById(id);
        return accountAffiliationBD
                .switchIfEmpty(
                        Mono.just(new AccountAffiliation()))
                .flatMap(a ->{
                    a.setBalance(accountAffiliation.getBalance());
                    a.setMovementDay(accountAffiliation.getMovementDay());
                    a.setNumber(accountAffiliation.getNumber());
                    a.setNumberOfHolder(accountAffiliation.getNumberOfHolder());
                    a.setNumberOfSigner(accountAffiliation.getNumberOfSigner());
                    a.setStatus(accountAffiliation.getStatus());
                    return repository.update(id, a);
                });
    }

    /**
     * Eliminación de un cliente con cuenta bancaria.
     * @param id codigo.
     * @return Mono<AccountAffiliationDao>
     */
    @Override
    public
    Mono<Void>
    delete(final String id) {
      return repository.delete(id);
    }

    /**
     * Busqueda de un cliente con cuenta bancaria por Id.
     * @param id codigo.
     * @return Mono<AccountAffiliation>
     */
    @Override
    public
    Mono<AccountAffiliation>
    findById(final String id) {
      return repository.findById(id);
    }

    /**
     * Busqueda de todos los clientes con cuenta bancaria.
     * @return Flux<AccountAffiliation>
     */
    @Override
    public
    Flux<AccountAffiliation>
    findAll() {
      return repository.findAll();
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
     * Listado de Afiliaciones por cuenta y por Cliente.
     * @param idCustomer Codigo de cliente.
     * @param idAccount Codigo de la cuenta.
     * @return Flux<AccountAffiliation>
     */
    @Override
    public
    Flux<AccountAffiliation>
    findByIdCustomerByIdAccount(String idCustomer, String idAccount) {
        return repository.findByIdCustomer(idCustomer);
    }

    /**
     * Obtenemos los datos del cliente.
     * @param idCustomer Codigo del cliente.
     * @return Flux<Customer>
     */
    @Override
    public Flux<Customer> getCustomerById(String idCustomer) {
        log.info("[getCustomerById] idAccount:"+idCustomer);
        return repository.getCustomerById(idCustomer);
    }

    /**
     * Obtenemos los datos del producto: Cuenta Bancaria.
     * @param idAccount codigo de la cuenta bancaria
     * @return Flux<Account>
     */
    @Override
    public Flux<Account> getProductAccountById(String idAccount) {
        log.info("[getProductAccountById] idAccount:"+idAccount);
        return repository.getProductAccountById(idAccount);
    }


}
