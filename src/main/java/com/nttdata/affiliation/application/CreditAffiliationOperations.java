package com.nttdata.affiliation.application;

import com.nttdata.affiliation.domain.AccountAffiliation;
import com.nttdata.affiliation.domain.CreditAffiliation;
import com.nttdata.affiliation.domain.bean.Credit;
import com.nttdata.affiliation.domain.bean.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * CREDITAFFILIATIONOPERATIONS.
 * Define las operaciones (CRUD) de la afiliación
 *                               de Creditos de un cliente
 */
public interface CreditAffiliationOperations {
    /**
     * Creación de credito para un cliente.
     * @param creditAffiliation afiliación de credito.
     * @return Mono<CreditAffiliation>
     */
    Mono<CreditAffiliation>
    create(CreditAffiliation creditAffiliation);

    /**
     * Actualización de un credito para un cliente.
     * @param id codigo.
     * @param creditAffiliation afiliación de credito.
     * @return Mono<CreditAffiliation>
     */
    Mono<CreditAffiliation>
    update(String id, CreditAffiliation creditAffiliation);

    /**
     * Eliminación de un credito para un cliente.
     * @param id codigo.
     * @return Mono<CreditAffiliationDao>
     */
    Mono<Void>
    delete(String id);

    /**
     * Busqueda de un credito de un cliente por Id.
     * @param id codigo.
     * @return Mono<CreditAffiliation>
     */
    Mono<CreditAffiliation>
    findById(String id);

    /**
     * Busqueda de todas las creditos de los clientes.
     * @return Flux<CreditAffiliation>
     */
    Flux<CreditAffiliation>
    findAll();

    /**
     * Listado de Afiliaciones de creditos por Cliente.
     * @param idCustomer Codigo del cliente.
     * @return Flux<CreditAffiliation>
     */
    Flux<CreditAffiliation>
    findByIdCustomer(String idCustomer);

    /**
     * Listado de Afiliaciones por credito y por cliente.
     * @param idCustomer Codigo de cliente.
     * @param idCredit Codigo del credito.
     * @return Flux<CreditAffiliation>
     */
    Flux<CreditAffiliation>
    findByIdCustomerByIdAccount(String idCustomer, String idCredit);

    /**
     * Obtenemos los datos del cliente.
     * @param idCustomer Codigo del cliente.
     * @return Mono<Customer>
     */
    Mono<Customer>
    getCustomerById(final String idCustomer);

    /**
     * Obtenemos los datos del producto: Credito.
     * @param idAccount codigo del credito.
     * @return Mono<Credit>
     */
    Mono<Credit>
    getProductCreditById(final String idAccount);
}
