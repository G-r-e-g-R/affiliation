package com.nttdata.affiliation.infraestructure.repository;

import com.nttdata.affiliation.infraestructure.model.dao.AccountAffiliationDao;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
/**
 * IACCOUNTAFFILIATIONCRUDREPOSITORY.
 * Define las operaciones (CRUD) de la Afiliación
 * de Cuentas Bancarias de un cliente el cual extiende del Reactive CRUD.
 * Ademas se extiende el repositorio con uno personalizado.
 */
public interface IAccountAffiliationCrudRepository
        extends ReactiveCrudRepository<AccountAffiliationDao, String>,
                ICustomAccountAffiliationCrudRepository {
}
