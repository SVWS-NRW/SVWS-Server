package de.svws_nrw.repo.kataloge;

import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
import de.svws_nrw.repo.ReferenceRepository;

/**
 * Das Interface für ein Repository der SVWS-Datenbank zum Zugriff auf die Arten von Teilleistungen
 */
public interface TeilleistungsartRepository extends ReferenceRepository<DTOTeilleistungsarten> {


	/**
	 * Gibt zurück, ob ein Entity mit der übergebenen Bezeichnung bereits existiert.
	 * @param bezeichnung die Bezeichnung der Leistungsart.
	 * @return true falls ein Entity existiert, ansonsten false
	 */
	boolean existsBy(String bezeichnung);

}
