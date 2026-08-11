package de.svws_nrw.repo.schueler.lernabschnitt;

import java.util.Collection;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository der SVWS-Datenbank zum Zugriff auf die Lernabschnitts-bezogene Bemerkungen zu Schülern
 */
public interface SchuelerLernabschnittBemerkungRepository extends Repository<DTOSchuelerPSFachBemerkungen> {

	/**
	 * Gibt eine Map der Bemerkungen zugeordnet zu den Lernabschnitts-IDs zurück.
	 *
	 * @param idsAbschnitte   die IDs der angefragten Lernabschnitte
	 *
	 * @return die Map mit der Zuordnung der Bemerkungen zu den jeweiligen Lernabschnitt-IDs
	 */
	Map<Long, DTOSchuelerPSFachBemerkungen> findMapByLernabschnittID(Collection<Long> idsAbschnitte);

}
