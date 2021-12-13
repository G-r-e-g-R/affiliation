package com.nttdata.affiliation.infraestructure.rest;

import com.nttdata.affiliation.application.CreditAffiliationOperations;
import com.nttdata.affiliation.domain.AccountAffiliation;
import com.nttdata.affiliation.domain.CreditAffiliation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * CREDITAFFILIATIONCONTROLLER.
 * Realiza las afiliaciones de los creditos con clientes
 */
@Slf4j
@RestController
@RequestMapping("/affiliations/credits")
@RequiredArgsConstructor
public class CreditAffiliationController {
    /**
     * Operaciones de Afiliación de creditos.
     */
    private final CreditAffiliationOperations creditAffiliationOperations;

    /**
     * Busca  los datos de todas las afiliaciones de credito de un cliente.
     * @return Flux<CreditAffiliation>
     */
    @GetMapping
    public
    Mono<ResponseEntity<Flux<CreditAffiliation>>>
    getAll() {
        return Mono.just(
                ResponseEntity
                        .ok()
                        .body(creditAffiliationOperations.findAll()));
    }

    /**
     * Busca por el Id los datos de la afiliacion de credito de un cliente.
     * @param id codigo.
     * @return Mono<CreditAffiliation>
     */
    @GetMapping("/{id}")
    public
    Mono<ResponseEntity<CreditAffiliation>>
    getById(@PathVariable final String id) {
        return creditAffiliationOperations.findById(id)
                .map(a -> ResponseEntity
                        .ok()
                        .body(a))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    /**
     * Busca por Id del cliente los datos de la afiliacion de cuentas bancarias.
     * @param id codigo del cliente.
     * @return Mono<CreditAffiliation>
     */
    @GetMapping("/customer/{id}")
    public
    Flux<ResponseEntity<CreditAffiliation>>
    getByIdCustomer(@PathVariable final String id) {

        return creditAffiliationOperations.findByIdCustomer(id)
                .map(a -> ResponseEntity
                        .ok()
                        .body(a))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    /**
     * Busca por Id del cliente y Id del Producto la afiliacion credito.
     * @param idCustomer Codigo del cliente.
     * @param idCredit Codigo del credito.
     * @return Mono<CreditAffiliation>
     */
    @GetMapping("/{idCustomer}/{idCredit}")
    public
    Flux<ResponseEntity<CreditAffiliation>>
    getByIdCustomerByIdAccount(@PathVariable("idCustomer") final String idCustomer,
                               @PathVariable("idCredit") final String idCredit) {

        return creditAffiliationOperations
                .findByIdCustomerByIdAccount(idCustomer, idCredit)
                .map(a -> ResponseEntity
                        .ok()
                        .body(a))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    /**
     *  Regitra las afiliaciones de credito de un cliente.
     * @param creditAffiliation afiliación de credito.
     * @return Mono<CreditAffiliation>
     */
    @PostMapping
    public
    Mono<ResponseEntity<CreditAffiliation>>
    post(@RequestBody final CreditAffiliation creditAffiliation) {
        return creditAffiliationOperations.create(creditAffiliation)
                .filter(
                        creditAffiliationResponse
                        -> creditAffiliationResponse.getId() != null
                )
                .map(
                        creditAffiliationResponse
                        -> ResponseEntity
                                .ok()
                                .body(creditAffiliationResponse))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Actualiza las afiliaciones de credito de un cliente.
     * @param id codigo.
     * @param credittAffiliation afiliación de credito.
     * @return Mono<CreditAffiliation>
     */
    @PutMapping("/{id}")
    public
    Mono<ResponseEntity<CreditAffiliation>>
    put(@PathVariable final String id,
        @RequestBody final CreditAffiliation credittAffiliation) {
        return creditAffiliationOperations.update(id, credittAffiliation)
                .map(a -> ResponseEntity
                        .ok()
                        .body(a))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Elimina los datos de la afiliacion de Credito de un cliente.
     * @param id codigo.
     * @return Mono<CreditAffiliationDao>
     */
    @DeleteMapping("/{id}")
    public
    Mono<ResponseEntity<Void>>
    delete(@PathVariable final String id) {
        return creditAffiliationOperations.delete(id)
                .map(c -> ResponseEntity
                        .noContent()
                        .<Void>build())
                .defaultIfEmpty(ResponseEntity
                        .notFound()
                        .build());
    }
}
