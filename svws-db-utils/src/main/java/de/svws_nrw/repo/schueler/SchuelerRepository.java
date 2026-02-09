package de.svws_nrw.repo.schueler;

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

}
