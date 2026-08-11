package de.svws_nrw.repo.lehrer.minderleistung;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerEntlastungsstunde;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Lehrer-Minderleistungs-Tabelle der SVWS-Datenbank
 */
public interface LehrerMinderleistungRepository extends Repository<DTOLehrerEntlastungsstunde> {

	/**
	 * Bestimmt die Zuordnung der Minderleistungen zu den Lehrer-Abschnitten mit den übergebenen IDs.
	 *
	 * @param idsAbschnitte   die IDs der Lehrer-Abschnitte
	 *
	 * @return die Zuordnung
	 */
	Map<Long, List<DTOLehrerEntlastungsstunde>> getMapByAbschnittIds(Collection<Long> idsAbschnitte);

	/**
	 * Ermittelt Entlastungsstunden nach Abschnitts ID
	 * @param idAbschnitt die ID des Abschnitts
	 *
	 * @return Liste von {@link DTOLehrerEntlastungsstunde}
	 */
	List<DTOLehrerEntlastungsstunde> getAllByLehrerAbschnittId(long idAbschnitt);

	/**
	 * Gibt eine Map von Abschnittsdaten-IDs auf die zugehörigen {@link DTOLehrerEntlastungsstunde}-Einträge zurück.
	 *
	 * @param idsLehrerPersonalabschnittsdaten die IDs der Lehrerabschnittsdaten
	 * @return Map von Abschnittsdaten-ID auf Liste der zugehörigen Minderleistungen
	 */
	Map<Long, List<DTOLehrerEntlastungsstunde>> getListByIdLehrerAbschnittsdaten(Collection<Long> idsLehrerPersonalabschnittsdaten);

}
