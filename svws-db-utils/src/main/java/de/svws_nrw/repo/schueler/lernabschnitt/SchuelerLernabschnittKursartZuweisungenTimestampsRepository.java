package de.svws_nrw.repo.schueler.lernabschnitt;

import java.util.Collection;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerZuweisungen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerZuweisungenPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Zeitstempel der neuen Schüler-Kursart-Zuweisungen für Lernabschnitte
 */
public interface SchuelerLernabschnittKursartZuweisungenTimestampsRepository
		extends RepositoryBase<DTOTimestampsSchuelerZuweisungen, DTOTimestampsSchuelerZuweisungenPK> {

	/**
	 * Bestimmt der Zeitstempel der Kursart-Zuordnungen für die übergebenen Schüler-Lernabschnitte.
	 *
	 * @param idsLernabschnitte   die IDs der Lernabschnitte
	 *
	 * @return die Map mit den Zeitstempeln
	 */
	HashMap2D<Long, Long, DTOTimestampsSchuelerZuweisungen> getMapByLernabschnitte(Collection<Long> idsLernabschnitte);

}
