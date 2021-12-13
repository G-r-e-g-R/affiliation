package com.nttdata.affiliation.domain.bean;

import lombok.Data;
/**
 * CREDIT.
 * La clase contendrá  información de los Creditos
 * (PERSONAL, EMPRESARIAL y TARJETAS DE CREDITO)
 */
@Data
public class Credit {
    /**
     * Código del producto de crédito.
     */
    private String id;
    /**
     * Tipo de Credito: Personal, Empresarial, etc.
     */
    private CreditType creditType;
    /**
     * Numero maximo de creditos 0: Sin limites.
     */
    private int maximumNumberCredit;


}
