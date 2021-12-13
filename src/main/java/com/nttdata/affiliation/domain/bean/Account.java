package com.nttdata.affiliation.domain.bean;

import lombok.Data;
/**
 * ACCOUNT.
 * La clase contendrá  información de las cuentas Bancarias
 * (AHORRO, CUENTA CORRIENTE y PLAZO FIJO)
 */
@Data
public class Account {
    /**
     * Codigo de la cuenta bancaria.
     */
    private String id;
    /**
     * Tipo de Cuenta: Ahorro, cuenta corriente, Plazo Fijo.
     */
    private AccountType accountType;
    /**
     * Nombre de la cuenta (Ej. Cuenta Sueldo, etc. Opcional).
     */
    private String name;
    /**
     * Comisión de la cuenta.
     */
    private Double commission;
    /**
     * Limite Máximo de movimiento.
     */
    private int limitMovement;
    /**
     * Monto mínimo de promedio diario.
     */
    private Double minimumDailyAverageAmount;
}
