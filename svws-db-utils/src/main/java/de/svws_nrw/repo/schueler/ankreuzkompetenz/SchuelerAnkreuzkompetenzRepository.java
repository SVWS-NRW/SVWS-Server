package de.svws_nrw.repo.schueler.ankreuzkompetenz;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository der SVWS-Datenbank zum Zugriff auf die Schüler-Ankreuzkompetenzen
 */
public interface SchuelerAnkreuzkompetenzRepository extends Repository<DTOSchuelerAnkreuzfloskeln> {

	/**
	 * Ermittelt eine Liste aller Ankreuzkompetenzen, welche den Lernabschnitten mit den übergebenen IDs
	 * zugeordnet sind.
	 *
	 * @param idsLernabschnitte   die IDs der Lernabschnitte
	 *
	 * @return die Liste mit den Ankreuzkompetenzen
	 */
	List<DTOSchuelerAnkreuzfloskeln> findListByLernabschnitt(Collection<Long> idsLernabschnitte);

}
