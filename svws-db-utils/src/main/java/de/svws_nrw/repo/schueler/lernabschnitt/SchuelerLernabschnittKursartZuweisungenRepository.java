package de.svws_nrw.repo.schueler.lernabschnitt;

import java.util.Collection;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOSchuelerZuweisung;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOSchuelerZuweisungPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die neuen Schüler-Kursart-Zuweisungen für Lernabschnitte
 */
public interface SchuelerLernabschnittKursartZuweisungenRepository extends RepositoryBase<DTOSchuelerZuweisung, DTOSchuelerZuweisungPK> {

	/**
	 * Bestimmt die Zuordnung der Kursart für die übergebenen Schüler-Lernabschnitte.
	 *
	 * @param idsLernabschnitte   die IDs der Lernabschnitte
	 *
	 * @return die Kursart-Zuordnungen
	 */
	HashMap2D<Long, Long, DTOSchuelerZuweisung> getMapByLernabschnitte(Collection<Long> idsLernabschnitte);

}
