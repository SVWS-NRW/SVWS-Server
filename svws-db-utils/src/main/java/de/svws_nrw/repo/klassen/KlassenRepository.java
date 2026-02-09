package de.svws_nrw.repo.klassen;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Klassen-Tabelle der SVWS-Datenbank
 */
public interface KlassenRepository extends Repository<DTOKlassen> {

	/**
	 * Gibt eine Liste aller Klassen für den angegebenen Schuljahresabschnitt zurück.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return die Liste der Klassen
	 */
	List<DTOKlassen> getListBySchuljahresabschnitt(long idSchuljahresabschnitt);

}
