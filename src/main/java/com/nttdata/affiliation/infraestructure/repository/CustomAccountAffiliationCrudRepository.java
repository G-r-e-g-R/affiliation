package com.nttdata.affiliation.infraestructure.repository;

import com.nttdata.affiliation.domain.AccountAffiliation;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
/**
 * CUSTOMACCOUNTAFFILIATIONCRUDREPOSITORY.
 * Implementa las operaciones personalizadas
 * de la afiliación de cuentas bancarias.
 */
public class CustomAccountAffiliationCrudRepository
        implements ICustomAccountAffiliationCrudRepository {
    /**
     * Template.
     */
    private final ReactiveMongoTemplate mongoTemplate;

    /**
     * Constructor.
     * @param reactiveMongoTemplate Template.
     */
    public  CustomAccountAffiliationCrudRepository(
            final ReactiveMongoTemplate reactiveMongoTemplate) {
        this.mongoTemplate = reactiveMongoTemplate;

    }
    /**
     * Listado de Afiliaciones de cuentas por Cliente.
     * @param idCustomer Codigo del cliente.
     * @return Flux<AccountAffiliation>
     */
    @Override
    public Flux<AccountAffiliation> findByIdCustomer(String idCustomer) {
       Query query = new Query(Criteria.where("idCustomer").is(idCustomer));
       return mongoTemplate.find(query, AccountAffiliation.class);
    }

    /**
     * Listado de Afiliaciones de cuentas por Cliente.
     * @param idCustomer Codigo del cliente.
     * @param idAccount Codigo de la cuenta bancaria
     * @return  Flux<AccountAffiliation>
     */
    @Override
    public
    Flux<AccountAffiliation>
    findByIdCustomerInAndIdAccount(String idCustomer, String idAccount) {
        Query query = new Query(Criteria.where("idCustomer").is(idCustomer).and("idAccount").is(idAccount));
        return mongoTemplate.find(query, AccountAffiliation.class);
    }
}
