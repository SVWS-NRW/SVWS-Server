package de.svws_nrw.repo.benutzer;

import de.svws_nrw.db.dto.current.svws.auth.DTOCredentials;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Credentials-Tabelle in der SVWS-Datenbank
 */
public interface CredentialsRepository extends Repository<DTOCredentials> {



}
