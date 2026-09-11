package de.svws_nrw.repo.uv.lerngruppen;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppe;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Lerngruppen
 * (UV_Lerngruppen) der SVWS-Datenbank.
 */
public interface UvLerngruppeRepository extends Repository<DTOUvLerngruppe> {

	/**
	 * Ermittelt alle Lerngruppen für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Lerngruppen
	 */
	List<DTOUvLerngruppe> getListByPlanungsabschnitt(long idPlanungsabschnitt);

	/**
	 * Ermittelt alle Lerngruppen für die angegebenen Klassen.
	 *
	 * @param idsKlassen   die IDs der Klassen
	 *
	 * @return die Liste der Lerngruppen
	 */
	List<DTOUvLerngruppe> getListByKlassenIds(Collection<Long> idsKlassen);

	/**
	 * Ermittelt alle Lerngruppen für die angegebenen Kurse.
	 *
	 * @param idsKurse die IDs der Kurse
	 * @return die zugehörigen Lerngruppen
	 */
	List<DTOUvLerngruppe> getListByKursIds(Collection<Long> idsKurse);

}
