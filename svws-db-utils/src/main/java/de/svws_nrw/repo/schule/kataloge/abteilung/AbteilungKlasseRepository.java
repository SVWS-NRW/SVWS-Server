package de.svws_nrw.repo.schule.kataloge.abteilung;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungsKlassen;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Klassenzuordnungen-Tabelle für Abteilungen der SVWS-Datenbank
 */
public interface AbteilungKlasseRepository extends Repository<DTOAbteilungsKlassen> {

	/**
	 * Ermittelt eine Liste aller Klassenzuordnungen, welche den Abteilungen mit den übergebenen IDs
	 * zugeordnet sind. Im Falle, dass null oder eine leere Collection übergeben wird, gibt die
	 * Methode eine leere Liste zurück.
	 *
	 * @param idsAbteilungen   die IDs der Abteilungen
	 *
	 * @return die Liste mit den Klassenzuordnung für Abteilungen
	 */
	List<DTOAbteilungsKlassen> findListByAbteilungen(Collection<Long> idsAbteilungen);

}
