package com.nttdata.affiliation.infraestructure.repository;

import com.nttdata.affiliation.domain.AccountAffiliation;
import com.nttdata.affiliation.domain.CreditAffiliation;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

/**
 * CUSTOMCREDITAFFILIATIONCRUDREPOSITORY.
 * Implementa las operaciones personalizadas
 * de la afiliación de Creditos.
 */
public class CustomCreditAffiliationCrudRepository
        implements ICustomCreditAffiliationCrudRepository {
    /**
     * Template.
     */
    private final ReactiveMongoTemplate mongoTemplate;

    /**
     * Constructor.
     * @param reactiveMongoTemplate Template.
     */
    public CustomCreditAffiliationCrudRepository(
            final ReactiveMongoTemplate reactiveMongoTemplate) {
        this.mongoTemplate = reactiveMongoTemplate;

    }
    /**
     * Listado de Afiliaciones de Credito por Cliente.
     * @param idCustomer Codigo del cliente.
     * @return Flux<CreditAffiliation>
     */
    @Override
    public Flux<CreditAffiliation> findByIdCustomer(String idCustomer) {
       Query query = new Query(Criteria.where("idCustomer").is(idCustomer));
       return mongoTemplate.find(query, CreditAffiliation.class);
    }

    /**
     * Listado de Afiliaciones de credito y por Cliente.
     * @param idCustomer Codigo del cliente.
     * @param idCredit Codigo de credito.
     * @return Flux<CreditAffiliation>
     */
    @Override
    public
    Flux<CreditAffiliation>
    findByIdCustomerByIdAccount(String idCustomer, String idCredit) {
        Query query = new Query(Criteria.where("idCustomer").is(idCustomer).and("idCredit").is(idCredit));
        return mongoTemplate.find(query, CreditAffiliation.class);
    }
}
