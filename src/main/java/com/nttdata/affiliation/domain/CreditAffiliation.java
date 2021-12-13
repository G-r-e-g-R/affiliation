package com.nttdata.affiliation.domain;


import com.nttdata.affiliation.domain.bean.Credit;
import com.nttdata.affiliation.domain.bean.Customer;
import lombok.Data;

/**
 * CREDITAFFILIATION.
 * La clase Afiliación de Creditos contendrá  información del
 *                    registro de un credito de un cliente
 */
@Data
public class CreditAffiliation {
    /**
     * Codigo de la afiliación de credito.
     */
    private String id;
    /**
     * Identificador del cliente.
     */
    private String idCustomer;
    /**
     * Identificador del credito.
     */
    private String idCredit;
    /**
     * Datos del cliente.
     */
    private Customer customer;
    /**
     * Datos del credito.
     */
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
