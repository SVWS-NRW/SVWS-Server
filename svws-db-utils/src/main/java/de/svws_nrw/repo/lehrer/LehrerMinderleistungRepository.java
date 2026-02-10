package de.svws_nrw.repo.lehrer;

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
	Map<Long, List<DTOLehrerEntlastungsstunde>> getMapByAbschnitt(Collection<Long> idsAbschnitte);

}
