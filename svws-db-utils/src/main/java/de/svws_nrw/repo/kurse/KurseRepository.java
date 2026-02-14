package de.svws_nrw.repo.kurse;

import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Kurs-Tabelle der SVWS-Datenbank
 */
public interface KurseRepository extends Repository<DTOKurs> {

	/**
	 * Gibt eine Liste aller Kurse für den angegebenen Schuljahresabschnitt zurück.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return die Liste der Kurse
	 */
	List<DTOKurs> getListBySchuljahresabschnitt(long idSchuljahresabschnitt);

	/**
	 * Gibt eine Map aller Kurse für den angegebenen Schuljahresabschnitt zurück.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return die Map der Kurse
	 */
	Map<Long, DTOKurs> getMapBySchuljahresabschnitt(long idSchuljahresabschnitt);

}
