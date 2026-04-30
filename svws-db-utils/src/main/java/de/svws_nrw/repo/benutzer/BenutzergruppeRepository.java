package de.svws_nrw.repo.benutzer;

import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.svws_nrw.repo.Repository;

/**
 * Repository-Interface für den Zugriff auf {@link DTOBenutzergruppe}-Entitäten.
 * Stellt domänenspezifische Abfragemethoden ergänzend zu den geerbten CRUD-Operationen bereit.
 */
public interface BenutzergruppeRepository extends Repository<DTOBenutzergruppe> {

}
