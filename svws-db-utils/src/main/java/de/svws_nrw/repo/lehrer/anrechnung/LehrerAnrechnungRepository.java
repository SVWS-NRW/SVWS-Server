package de.svws_nrw.repo.lehrer.anrechnung;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Lehrer-Anrechnungsstunden-Tabelle der SVWS-Datenbank
 */
public interface LehrerAnrechnungRepository extends Repository<DTOLehrerAnrechnungsstunde> {

	/**
	 * Bestimmt die Zuordnung der Anrechnungsstunden zu den Lehrer-Abschnitten mit den übergebenen IDs.
	 *
	 * @param idsAbschnitte   die IDs der Lehrer-Abschnitte
	 *
	 * @return die Zuordnung
	 */
	Map<Long, List<DTOLehrerAnrechnungsstunde>> getMapByAbschnitt(Collection<Long> idsAbschnitte);

	/**
	 * Gibt eine Map von Abschnittsdaten-IDs auf die zugehörigen {@link DTOLehrerAnrechnungsstunde}-Einträge zurück.
	 *
	 * @param idsLehrerPersonalabschnittsdaten die IDs der Lehrerabschnittsdaten
	 * @return Map von Abschnittsdaten-ID auf Liste der zugehörigen Anrechnungsstunden
	 */
	Map<Long, List<DTOLehrerAnrechnungsstunde>> getListByIdLehrerAbschnittsdaten(Collection<Long> idsLehrerPersonalabschnittsdaten);

}
