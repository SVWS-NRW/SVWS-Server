package de.svws_nrw.repo.schueler.teilleistung;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerTeilleistung;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository der SVWS-Datenbank zum Zugriff auf die Schüler-Teilleistungen
 */
public interface SchuelerTeilleistungRepository extends Repository<DTOSchuelerTeilleistung> {

	/**
	 * Ermittelt eine Liste aller Teilleistungen, welche den Leistungsdaten mit den übergebenen IDs
	 * zugeordnet sind.
	 *
	 * @param idsLeistungen   die IDs der Leistungsdaten
	 *
	 * @return die Liste mit den Teilleistungen
	 */
	List<DTOSchuelerTeilleistung> findListByLeistungsdaten(Collection<Long> idsLeistungen);

}
