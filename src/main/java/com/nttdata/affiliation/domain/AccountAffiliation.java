package com.nttdata.affiliation.domain;

import com.nttdata.affiliation.domain.bean.Account;
import com.nttdata.affiliation.domain.bean.Customer;
import com.nttdata.affiliation.domain.bean.Status;
import lombok.Data;


/**
 * ACCOUNTAFFILIATION.
 * La clase Afiliación de Cuenta Bancarias contendrá  información del
 * registro de una cuenta bancaria de un cliente
 */
@Data
public class AccountAffiliation {
    /**
     * Codigo de la afiliación de cuenta bancaria.
     */
    private String id;
    /**
     * Identificador del cliente.
     */
    private String idCustomer;
    /**
     * Identificador de la cuenta bancaria.
     */
    private String idAccount;
    /**
     * Datos del cliente.
     */
    private Customer customer;
    /**
     * Datos de la cuenta bancaria.
     */
    private Account account;
    /**
     * Numero de cuenta bancaria.
     */
    private String number;
    /**
     * Movimiento (retiro o deposito) en un dia especifico.
     */
    private String movementDay;
    /**
     * Numero de titulares.
     */
    private int numberOfHolder;
    /**
     * Numero de firmantes.
     */
    private int numberOfSigner;
    /**
     * Monto en el momento de la apertura de la cuenta.
     */
    private Double baseAmount;
    /**
     * Saldo disponible.
     */
    private Double balance;
    /**
     * Estado Activo o Inactivo.
     */
    private Status status;

}
