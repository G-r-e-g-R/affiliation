package com.nttdata.affiliation.infraestructure.repository;

import com.nttdata.affiliation.domain.AccountAffiliation;
import reactor.core.publisher.Flux;
/**
 * ICUSTOMACCOUNTAFFILIATIONCRUDREPOSITORY.
 * Define las operaciones personalizadas de la Afiliación
 * de Cuentas Bancarias de un cliente el cual extiende del Reactive CRUD.
 */
public interface ICustomAccountAffiliationCrudRepository {
    /**
     * Listado de Afiliaciones de cuentas por Cliente.
     * @param idCustomer Codigo del cliente.
     * @return Flux<AccountAffiliation>
     */
    Flux<AccountAffiliation>
    findByIdCustomer(String idCustomer);
    /**
     * Listado de Afiliaciones por Cliente y por cuenta bancaria.
     * @param idCustomer Codigo del cliente.
     * @param idAccount Codigo de la cuenta bancaria
     * @return Flux<AccountAffiliation>
     */
    Flux<AccountAffiliation>
    findByIdCustomerByIdAccount(String idCustomer, String idAccount);
}
