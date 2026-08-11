package de.svws_nrw.repo.schule.kataloge.abteilung;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungen;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Abteilungen-Tabelle der SVWS-Datenbank
 */
public interface AbteilungenRepository extends Repository<DTOAbteilungen> {

	/**
	 * Gibt eine Liste aller Abteilungen für den angegebenen Schuljahresabschnitt zurück.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return die Liste der Abteilungen
	 */
	List<DTOAbteilungen> getListBySchuljahresabschnitt(long idSchuljahresabschnitt);

}
