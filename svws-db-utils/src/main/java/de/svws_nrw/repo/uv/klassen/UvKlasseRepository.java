package de.svws_nrw.repo.uv.klassen;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvKlasse;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Klassen
 * (UV_Klassen) der SVWS-Datenbank.
 */
public interface UvKlasseRepository extends Repository<DTOUvKlasse> {

	/**
	 * Ermittelt alle Klassen für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Klassen
	 */
	List<DTOUvKlasse> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
