package de.svws_nrw.repo.lehrer;

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

}
