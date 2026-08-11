package de.svws_nrw.repo.lehrer.mehrleistung;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerMehrleistung;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Lehrer-Mehrleistungs-Tabelle der SVWS-Datenbank
 */
public interface LehrerMehrleistungRepository extends Repository<DTOLehrerMehrleistung> {

	/**
	 * Bestimmt die Zuordnung der Mehrleistungen zu den Lehrer-Abschnitten mit den übergebenen IDs.
	 *
	 * @param idsAbschnitte   die IDs der Lehrer-Abschnitte
	 *
	 * @return die Zuordnung
	 */
	Map<Long, List<DTOLehrerMehrleistung>> getMapByIdsLehrerAbschnittsdaten(Collection<Long> idsAbschnitte);

	/**
	 * Gibt eine Map von Abschnittsdaten-IDs auf die zugehörigen {@link DTOLehrerMehrleistung}-Einträge zurück.
	 *
	 * @param idsLehrerPersonalabschnittsdaten die IDs der Lehrerabschnittsdaten
	 * @return Map von Abschnittsdaten-ID auf Liste der zugehörigen Mehrleistungen
	 */
	Map<Long, List<DTOLehrerMehrleistung>> getListByIdLehrerAbschnittsdaten(Collection<Long> idsLehrerPersonalabschnittsdaten);


}
