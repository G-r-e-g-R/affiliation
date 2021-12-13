package com.nttdata.affiliation.application;

import com.nttdata.affiliation.domain.AccountAffiliation;
import com.nttdata.affiliation.domain.bean.Account;
import com.nttdata.affiliation.domain.bean.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/**
 * ACCOUNTAFFILIATIONREPOSITORY.
 * Define las operaciones en la BD para
 * la afiliacion de una cuenta bancaria y un cliente
 */
public interface AccountAffiliationRepository {
    /**
     * Registro (Afiliación) de un cliente con cuenta bancaria.
     * @param accountAffiliation afiliación de cuenta.
     * @return Mono<AccountAffiliation>
     */
    Mono<AccountAffiliation>
    create(AccountAffiliation accountAffiliation);

    /**
     * Actualización de un cliente con cuenta bancaria.
     * @param id codigo.
     * @param accountAffiliation afiliación de cuenta.
     * @return Mono<AccountAffiliation>
     */
    Mono<AccountAffiliation>
    update(String id, AccountAffiliation accountAffiliation);

    /**
     * Eliminación de un cliente con cuenta bancaria.
     * @param id codigo.
     * @return Mono<AccountAffiliationDao>
     */
    Mono<Void>
    delete(String id);

    /**
     * Busqueda de un cliente con cuenta bancaria por Id.
     * @param id codigo.
     * @return Mono<AccountAffiliation>
     */
    Mono<AccountAffiliation>
    findById(String id);

    /**
     * Busqueda de todos los clientes con cuenta bancaria.
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
    Flux<Customer>
    getCustomerById(final String idCustomer);

    /**
     * Obtenemos los datos del producto: Cuenta Bancaria.
     * @param idAccount codigo de la cuenta bancaria
     * @return Flux<Account>
     */
    Flux<Account>
    getProductAccountById(final String idAccount);

}
