package de.svws_nrw.repo.schueler;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository der SVWS-Datenbank zum Zugriff auf die Schüler-Leistungsdaten
 */
public interface SchuelerLeistungsdatenRepository extends Repository<DTOSchuelerLeistungsdaten> {

	/**
	 * Ermittelt eine Liste aller Leistungsdaten, welche den Lernabschnitten mit den übergebenen IDs
	 * zugeordnet sind.
	 *
	 * @param idsLernabschnitte   die IDs der Lernabschnitte
	 *
	 * @return die Liste mit den Leistungsdaten
	 */
	List<DTOSchuelerLeistungsdaten> findListByLernabschnitt(Collection<Long> idsLernabschnitte);


	/**
	 * Ermittelt eine Liste aller Leistungsdaten, welche den Lernabschnitten mit den übergebenen IDs
	 * zugeordnet sind und wo einer der übergeben Lehrer der Fachlehrer ist.
	 *
	 * @param idsLernabschnitte   die IDs der Lernabschnitte
	 * @param idsFachlehrer       die IDs der Fachlehrer
	 *
	 * @return die Liste mit den Leistungsdaten
	 */
	List<DTOSchuelerLeistungsdaten> findListByLernabschnittAndFachlehrer(Collection<Long> idsLernabschnitte, Collection<Long> idsFachlehrer);

	/**
	 * Ermittelt eine Liste aller Leistungsdaten, welche den Lernabschnitten mit den übergebenen IDs
	 * zugeordnet sind und welche einem der übergeben Faecher zugeordnet ist.
	 *
	 * @param idsLernabschnitte   die IDs der Lernabschnitte
	 * @param idsFaecher          die IDs der Faecher
	 *
	 * @return die Liste mit den Leistungsdaten
	 */
	List<DTOSchuelerLeistungsdaten> findListByLernabschnittAndFach(Collection<Long> idsLernabschnitte, Collection<Long> idsFaecher);

}
