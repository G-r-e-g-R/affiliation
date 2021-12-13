package com.nttdata.affiliation.infraestructure.repository;

import com.nttdata.affiliation.domain.AccountAffiliation;
import com.nttdata.affiliation.domain.CreditAffiliation;
import reactor.core.publisher.Flux;

/**
 * ICUSTOMCREDITAFFILIATIONCRUDREPOSITORY.
 * Define las operaciones personalizadas de la Afiliación
 * de Credit de un cliente el cual extiende del Reactive CRUD.
 */
public interface ICustomCreditAffiliationCrudRepository {
    /**
     * Listado de Afiliaciones de creditos por Cliente.
     * @param idCustomer Codigo del cliente.
     * @return Flux<CreditAffiliation>
     */
    Flux<CreditAffiliation> findByIdCustomer(String idCustomer);

    /**
     * Listado de Afiliaciones por Cliente y por credito.
     * @param idCustomer Codigo del cliente.
     * @param idCredit Codigo del credito
     * @return Flux<CreditAffiliation>
     */
    Flux<CreditAffiliation>
    findByIdCustomerByIdAccount(String idCustomer, String idCredit);
}
