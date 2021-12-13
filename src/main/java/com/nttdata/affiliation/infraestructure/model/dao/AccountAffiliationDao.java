package com.nttdata.affiliation.infraestructure.model.dao;

import com.nttdata.affiliation.domain.bean.Account;
import com.nttdata.affiliation.domain.bean.Customer;
import com.nttdata.affiliation.domain.bean.Status;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * ACCOUNTAFFILIATIONDAO.
 * Contiene los atributos del documento Afiliación de cuentas bancarias
 *                        (accountAffiliation) para la persistencia
 */
@Data
@Document("accountAffiliation")
public class AccountAffiliationDao {
    /**
     * Codigo de la afiliación de cuenta bancaria.
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
    private String idAccount;
    /**
     * Datos del cliente.
     */
    @Transient
    private Customer customer;
    /**
     * Datos de la cuenta bancaria.
     */
    @Transient
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
