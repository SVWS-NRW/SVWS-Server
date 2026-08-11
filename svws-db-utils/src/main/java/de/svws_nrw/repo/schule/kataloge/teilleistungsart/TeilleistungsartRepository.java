package de.svws_nrw.repo.schule.kataloge.teilleistungsart;

import java.util.List;
import java.util.Set;

import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository der SVWS-Datenbank zum Zugriff auf die Arten von Teilleistungen
 */
public interface TeilleistungsartRepository extends Repository<DTOTeilleistungsarten> {


	/**
	 * Gibt zurück, ob ein Entity mit der übergebenen Bezeichnung bereits existiert.
	 * @param bezeichnung die Bezeichnung der Leistungsart.
	 * @return true falls ein Entity existiert, ansonsten false
	 */
	boolean existsBy(String bezeichnung);

	/**
	 *  Liefert Ids die in anderen Tabellen referenziert werden.
	 * @param idsToDelete zu überprüfende Ids
	 * @return Liste referenzierter ids
	 */
	Set<Long> getReferencedIds(List<Long> idsToDelete);

}
