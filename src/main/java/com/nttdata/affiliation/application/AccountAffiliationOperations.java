package com.nttdata.affiliation.application;

import com.nttdata.affiliation.domain.AccountAffiliation;
import com.nttdata.affiliation.domain.bean.Account;
import com.nttdata.affiliation.domain.bean.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ACCOUNTAFFILIATIONOPERATIONS.
 * Define las operaciones (CRUD) de la afiliación
 *                               de Cuentas Bancarias de un cliente.
 */
public interface AccountAffiliationOperations {
    /**
     * Creación de una cuenta bancaria para un cliente.
     * @param accountAffiliation afiliación de cuenta.
     * @return Mono<AccountAffiliation>
     */
    Mono<AccountAffiliation>
    create(AccountAffiliation accountAffiliation);

    /**
     * Actualización de una cuenta bancaria para un cliente.
     * @param id codigo.
     * @param accountAffiliation afiliación de cuenta.
     * @return Mono<AccountAffiliation>
     */
    Mono<AccountAffiliation>
    update(String id, AccountAffiliation accountAffiliation);

    /**
     * Eliminación de una cuenta bancaria para un cliente.
     * @param id codigo.
     * @return Mono<AccountAffiliationDao>
     */
    Mono<Void>
    delete(String id);

    /**
     * Busqueda de una cuenta bancaria de un cliente por Id.
     * @param id codigo.
     * @return Mono<AccountAffiliation>
     */
    Mono<AccountAffiliation>
    findById(String id);

    /**
     * Busqueda de todas las cuentas bancarias de los clientes.
     * @return Flux<AccountAffiliation>
     */
    Flux<AccountAffiliation>
    findAll();

    /**
     * Listado de Afiliaciones de cuentas por Cliente.
     * @param idCustomer Codigo del cliente.
     * @return Flux<AccountAffiliation>
     */
    Flux<AccountAffiliation>
    findByIdCustomer(String idCustomer);

    /**
     * Listado de Afiliaciones por cuenta y por cliente.
     * @param idCustomer Codigo de cliente.
     * @param idAccount Codigo de la cuenta.
     * @return Flux<AccountAffiliation>
     */
    Flux<AccountAffiliation>
    findByIdCustomerByIdAccount(String idCustomer, String idAccount);

    /**
     * Obtenemos los datos del cliente.
     * @param idCustomer Codigo del cliente.
     * @return Flux<Customer>
     */
    public
    Flux<Customer>
    getCustomerById(final String idCustomer);

    /**
     * Obtenemos los datos del producto: Cuenta Bancaria.
     * @param idAccount codigo de la cuenta bancaria
     * @return Flux<Account>
     */
    public
    Flux<Account>
    getProductAccountById(final String idAccount);

}
