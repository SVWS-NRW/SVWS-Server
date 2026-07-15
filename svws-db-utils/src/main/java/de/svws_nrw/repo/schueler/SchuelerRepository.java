package de.svws_nrw.repo.schueler;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Schüler-Tabelle der SVWS-Datenbank
 */
public interface SchuelerRepository extends Repository<DTOSchueler> {

	/**
	 * Gibt die Liste aller aktiven, nicht gelöschten Schüler zurück, welche in dem übergebenen
	 * Schuljahresabschnitt sind.
	 *
	 * @param idSchuljahresabschnitt   die ID des aktuellen Lernabschnittes
	 *
	 * @return die Liste der Schüler
	 */
	List<DTOSchueler> getListAktiveBySchuljahresabschnitt(long idSchuljahresabschnitt);


	/**
	 * Gibt eine Map aller aktiven, nicht gelöschten Schüler zugeordnet zu ihrer ID zurück,
	 * welche in dem übergebenen Schuljahresabschnitt sind.
	 *
	 * @param idSchuljahresabschnitt   die ID des aktuellen Lernabschnittes
	 *
	 * @return die Map mit den Schülern zugeordnet zu ihrer ID
	 */
	Map<Long, DTOSchueler> getMapAktiveBySchuljahresabschnitt(long idSchuljahresabschnitt);


	/**
	 * Gibt die Liste aller nicht gelöschten Schüler mit einem angegebenen Status zurück,
	 * welche in dem übergebenen Schuljahresabschnitt sind.
	 *
	 * @param idSchuljahresabschnitt   die ID des aktuellen Lernabschnittes
	 * @param status                   eine Menge mit den erlaubten Status-Ids
	 *
	 * @return die Liste der Schüler
	 */
	List<DTOSchueler> getListByStatusAndSchuljahresabschnitt(long idSchuljahresabschnitt, Collection<Long> status);


	/**
	 * Gibt die Map aller nicht gelöschten Schüler mit einem angegebenen Status zugeordnet
	 * zu ihrer ID zurück, welche in dem übergebenen Schuljahresabschnitt sind.
	 *
	 * @param idSchuljahresabschnitt   die ID des aktuellen Lernabschnittes
	 * @param status                   eine Menge mit den erlaubten Status-Ids
	 *
	 * @return die Map mit den Schülern zugeordnet zu ihrer ID
	 */
	Map<Long, DTOSchueler> getMapByStatusAndSchuljahresabschnitt(long idSchuljahresabschnitt, Collection<Long> status);

	/**
	 * Gibt die Liste aller nicht gelöschten Schüler mit einem angegebenen Status zurück,
	 * welche in einem der übergebenen Schuljahresabschnitte sind.
	 *
	 * @param idsSchuljahresabschnitte   die IDs der Lernabschnitte
	 * @param status                     eine Menge mit den erlaubten Status-Ids
	 *
	 * @return die Liste der Schüler
	 */
	List<DTOSchueler> getListByStatusAndSchuljahresabschnitte(Collection<Long> idsSchuljahresabschnitte, Collection<Long> status);


	/**
	 * Gibt die Map aller nicht gelöschten Schüler mit einem angegebenen Status zugeordnet
	 * zu ihrer ID zurück, welche in einem der übergebenen Schuljahresabschnitte sind.
	 *
	 * @param idsSchuljahresabschnitte   die IDs der Lernabschnitte
	 * @param status                     eine Menge mit den erlaubten Status-Ids
	 *
	 * @return die Map mit den Schülern zugeordnet zu ihrer ID
	 */
	Map<Long, DTOSchueler> getMapByStatusAndSchuljahresabschnitte(Collection<Long> idsSchuljahresabschnitte, Collection<Long> status);

}
