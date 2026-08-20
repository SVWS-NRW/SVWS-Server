package de.svws_nrw.repo.schule.kataloge.teilleistungsart;

import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
import de.svws_nrw.repo.ReferencedBulkDeletionRepository;

/**
 * Das Interface für ein Repository der SVWS-Datenbank zum Zugriff auf die Arten von Teilleistungen
 */
public interface TeilleistungsartRepository extends ReferencedBulkDeletionRepository<DTOTeilleistungsarten> {


	/**
	 * Gibt zurück, ob ein Entity mit der übergebenen Bezeichnung bereits existiert.
	 * @param bezeichnung die Bezeichnung der Leistungsart.
	 * @return true falls ein Entity existiert, ansonsten false
	 */
	boolean existsBy(String bezeichnung);

}
