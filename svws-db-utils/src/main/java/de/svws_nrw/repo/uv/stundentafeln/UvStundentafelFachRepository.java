package de.svws_nrw.repo.uv.stundentafeln;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvStundentafelFach;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Stundentafel-Fächer
 * (UV_StundentafelFaecher) der SVWS-Datenbank.
 */
public interface UvStundentafelFachRepository extends Repository<DTOUvStundentafelFach> {

	/**
	 * Ermittelt die UV-Stundentafel-Fächer zu den übergebenen Stundentafel-IDs.
	 *
	 * @param idsStundentafel   die IDs der Stundentafeln
	 *
	 * @return die Liste der UV-Stundentafel-Fächer
	 */
	List<DTOUvStundentafelFach> getListByStundentafelIds(Collection<Long> idsStundentafel);

}
