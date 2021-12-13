package com.nttdata.affiliation.infraestructure.model.dao;

import com.nttdata.affiliation.domain.bean.Credit;
import com.nttdata.affiliation.domain.bean.Customer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * CREDITAFFILIATIONDAO.
 * Contiene los atributos del documento Afiliación de Creditos
 *                        (creditAffiliation) para la persistencia
 */
@Data
@Document("creditAffiliation")
public class CreditAffiliationDao {
    /**
     * Codigo de la Afiliación de Crédito.
     */
    @Id
    private String id;
    /**
     * Identificador del cliente.
     */
    private String idCustomer;
    /**
     * Identificador de la cuenta bancaria.
     */
    private String idCredit;
    /**
     * Datos del cliente.
     */
    @Transient
    private Customer customer;
    /**
     * Datos del credito.
     */
    @Transient
    private Credit credit;
    /**
     * Numero del prestamo.
     */
    private String loanNumber;
    /**
     * Numero de Tarjeta de credito.
     */
    private String cardNumber;
    /**
     * Monto Base al momento del registro del Credito.
     */
    private Double baseAmount;
    /**
     * Saldo disponible.
     */
    private Double balance;
    /**
     * Limite de Credito.
     */
    private Double creditLimit;
}
