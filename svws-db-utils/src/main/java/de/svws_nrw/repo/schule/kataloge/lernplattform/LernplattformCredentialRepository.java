package de.svws_nrw.repo.schule.kataloge.lernplattform;

import de.svws_nrw.db.dto.current.svws.auth.DTOCredentialsLernplattformen;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Zugangsdaten zu Lernplattformen in der SVWS-Datenbank.
 * Mehrere Zugangsdaten können über {@link #findMapByIds(java.util.Collection)} anhand ihrer IDs bestimmt werden.
 */
public interface LernplattformCredentialRepository extends Repository<DTOCredentialsLernplattformen> {

}
