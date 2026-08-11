package de.svws_nrw.repo.schueler.leistungsdaten;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.map.HashMap2D;
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
	 * Ermittelt eine Liste aller Leistungsdaten, welche dem übergebenen Kurs zugeordnet sind.
	 *
	 * @param idKurs die ID des Kurses
	 *
	 * @return die Liste mit den Leistungsdaten
	 */
	List<DTOSchuelerLeistungsdaten> findListByKurs(long idKurs);


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

	/**
	 * Zu der übergebenen Menge von SchuelerLernabschnittsdaten-IDs werden alle Schülerleistungsdaten dieser Abschnitte als Liste
	 * zugeordnet zu der ID des Lernabschnittes zurückgegeben.
	 *
	 * @param idsAbschnitte   die IDs der Schueler-Lernabschnittsdaten, für welche die Schülerleistungsdaten zurückgegeben werden sollen
	 *
	 * @return die Map mit der Zuordnung der Liste der Leistungsdaten zu den Lernabschnitts-IDs
	 */
	Map<Long, List<DTOSchuelerLeistungsdaten>> getMapListByLernabschnittsIds(Collection<Long> idsAbschnitte);

	/**
	 * Zu der übergebenen Menge von SchuelerLernabschnittsdaten-IDs werden alle Schülerleistungsdaten dieser Abschnitte in einer HashMap2D zurückgegeben,
	 * wobei die erste Schlüssel-Ebene die SchuljahresabschnittID und die zweite Schlüssel-Ebene die Fach-IDs enthält. Die Werte der Map sind die entsprechenden
	 * Schülerleistungsdaten-Objekte. Es werden nur die Schülerleistungsdaten zurückgegeben, welche den übergebenen Lernabschnitts-IDs zugeordnet sind.
	 *
	 * @param idsAbschnitte   die IDs der SchuelerLernabschnittsdaten-Objekte, für welche die Schülerleistungsdaten zurückgegeben werden sollen
	 *
	 * @return die Map mit den Lernabschnitts-IDs pro Schüler nach Abschnitts-IDs und Fach-IDs
	 */
	HashMap2D<Long, Long, DTOSchuelerLeistungsdaten> getMapByLernabschnittsIds(Collection<Long> idsAbschnitte);

}
