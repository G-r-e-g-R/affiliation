package com.nttdata.affiliation.application;

import com.nttdata.affiliation.domain.AccountAffiliation;
import com.nttdata.affiliation.domain.CreditAffiliation;
import com.nttdata.affiliation.domain.bean.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/**
 * CREDITAFFILIATIONOPERATIONSIMPL.
 * Implementa las operaciones (CRUD) de la afiliación
 *                         de Credito de un cliente
 */
@Service
@Slf4j
public class CreditAffiliationOperationsImpl
        implements   CreditAffiliationOperations {
    /**
     * Repositorio de los registros (Afiliación) de creditos.
     */
    private final CreditAffiliationRepository creditAffiliationRepository;

    /**
     * Constructor.
     * @param repository repositorio.
     */
    public
    CreditAffiliationOperationsImpl(
            final CreditAffiliationRepository repository) {
      this.creditAffiliationRepository = repository;
    }

    /**
     * Creación de credito para un cliente.
     * @param creditAffiliation afiliación de credito.
     * @return Mono<CreditAffiliation>
     */
    @Override
    public
    Mono<CreditAffiliation>
    create(final CreditAffiliation creditAffiliation) {
        log.info("[Greg] Inicio");
        Mono<Customer>
                customers = getCustomerById(creditAffiliation.getIdCustomer());
        Mono<Credit>
                credits = getProductCreditById(creditAffiliation.getIdCredit());

        Mono<Customer> customersEnterprise
                = customers.filter(a -> a.getCustomerType() !=null && a.getCustomerType().equals(CustomerType.EMPRESARIAL));
        Mono<Customer> customersPersonal
                = customers.filter(a -> a.getCustomerType() !=null && a.getCustomerType().equals(CustomerType.PERSONAL));

        Mono<Credit> creditsEnterprise
                = credits.filter(a -> a.getCreditType() !=null && (a.getCreditType().equals(CreditType.CREDITO_EMPRESARIAL) ||
                                 a.getCreditType().equals(CreditType.TARJETA_DE_CREDITO_EMPRESARIAL)));

        Mono<Credit> creditsPersonal
                = credits.filter(a -> a.getCreditType() !=null && (a.getCreditType().equals(CreditType.CREDITO_PERSONAL) ||
                                 a.getCreditType().equals(CreditType.TARJETA_DE_CREDITO_PERSONAL)));

        /*Registro de Empresa si cumple condición:*/
        log.info("[Greg] Analizando los requisitos de la empresa ");
        Mono<CreditAffiliation> credAffEnterprise = Mono
           .zip(customersEnterprise, creditsEnterprise, (customer,credit) -> {
                CreditAffiliation affiliation = new CreditAffiliation();
                affiliation.setCustomer(customer);
                affiliation.setCredit(credit);
            return affiliation;
        });
        Mono<CreditAffiliation> creditEnterprise = credAffEnterprise.filter( c -> c.getCustomer() != null && c.getCredit() != null)
                    .flatMap(a -> creditAffiliationRepository.create(creditAffiliation));

        /*Regitro de Persona si cumple condición*/
        log.info("[Greg] Analizando los requisitos de la persona ");
        Mono<CreditAffiliation> credAffPersonal = Mono.zip(customersPersonal, creditsPersonal, (customer,credit) -> {
            CreditAffiliation affiliation = new CreditAffiliation();
            affiliation.setCustomer(customer);
            affiliation.setCredit(credit);
            return affiliation;
        });
        Mono<CreditAffiliation> creditPersonal = credAffPersonal.filter( c -> c.getCustomer() != null && c.getCredit() != null)
                    .flatMap(a ->creditAffiliationRepository.create(creditAffiliation));//subscribe(a -> log.info("[Greg] grabar Persona"));

        log.info("[Greg] Realizando el registro o descartando los datos de afilicación por no cumplir requisitos");

        return creditEnterprise
                .switchIfEmpty(creditPersonal
                        .switchIfEmpty(Mono
                                .just(new CreditAffiliation())
                                .map(personalAffiliation-> personalAffiliation))
                        .map(enterpriseAffiliation->enterpriseAffiliation)
                );
    }

    /**
     * Actualización de un credito para un cliente.
     * @param id codigo.
     * @param creditAffiliation afiliación de credito.
     * @return Mono<CreditAffiliation>
     */
    @Override
    public
    Mono<CreditAffiliation>
    update(final String id, final CreditAffiliation creditAffiliation) {
        Mono<CreditAffiliation> creditAffiliationBD = creditAffiliationRepository.findById(id);
        return creditAffiliationBD
                .switchIfEmpty(Mono
                        .just(new CreditAffiliation())
                )
                .flatMap(a -> {
                    a.setBalance(creditAffiliation.getBalance());
                    a.setCardNumber(creditAffiliation.getCardNumber());
                    a.setCreditLimit(creditAffiliation.getCreditLimit());
                    a.setLoanNumber(creditAffiliation.getLoanNumber());
                    return creditAffiliationRepository.update(id, a);
                });
    }

    /**
     * Eliminación de un credito para un cliente.
     * @param id codigo.
     * @return Mono<CreditAffiliationDao>
     */
    @Override
    public
    Mono<Void>
    delete(final String id) {
        return creditAffiliationRepository.delete(id);
    }

    /**
     * Busqueda de un credito de un cliente por Id.
     * @param id codigo.
     * @return Mono<CreditAffiliation>
     */
    @Override
    public
    Mono<CreditAffiliation>
    findById(final String id) {
        return creditAffiliationRepository.findById(id);
    }

    /**
     * Busqueda de todas las creditos de los clientes.
     * @return Flux<CreditAffiliation>
     */
    @Override
    public
    Flux<CreditAffiliation>
    findAll() {
        return creditAffiliationRepository.findAll();
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
        return creditAffiliationRepository.findByIdCustomer(idCustomer);
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
        return creditAffiliationRepository.findByIdCustomerByIdAccount(idCustomer,idCredit);
    }

    /**
     * Obtenemos los datos del cliente.
     * @param idCustomer Codigo del cliente.
     * @return Mono<Customer>
     */
    @Override
    public Mono<Customer> getCustomerById(String idCustomer) {
        log.info("[getCustomerById] idAccount:"+idCustomer);
        return creditAffiliationRepository.getCustomerById(idCustomer);
    }

    /**
     * Obtenemos los datos del producto: Creditos.
     * @param idCredit codigo del credito.
     * @return Mono<Credit>
     */
    @Override
    public Mono<Credit> getProductCreditById(String idCredit) {
        log.info("[getProductCreditById] idCredit:"+idCredit);
        return creditAffiliationRepository.getProductCreditById(idCredit);
    }
}
