package de.svws_nrw.repo.uv.klassen;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvKlassenLehrer;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Klassen-Lehrer-Zuordnungen
 * (UV_KlassenLehrer) der SVWS-Datenbank.
 */
public interface UvKlassenLehrerRepository extends Repository<DTOUvKlassenLehrer> {

	/**
	 * Ermittelt alle Klassen-Lehrer-Zuordnungen für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Klassen-Lehrer-Zuordnungen
	 */
	List<DTOUvKlassenLehrer> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
