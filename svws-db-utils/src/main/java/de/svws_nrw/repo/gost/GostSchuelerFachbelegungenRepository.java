package de.svws_nrw.repo.gost;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungenPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Schüler-Fachbelegungen in der gymnasialen Oberstufe in der SVWS-Datenbank
 */
public interface GostSchuelerFachbelegungenRepository extends RepositoryBase<DTOGostSchuelerFachbelegungen, DTOGostSchuelerFachbelegungenPK> {

	/**
	 * Gibt eine Map mit den Fachbelegungen von Schülern zurück. Diese sind in der Map der Schüler-ID und
	 * Fach-ID der Belegung zugeordnet.
	 *
	 * @param idsSchueler   die IDs der Schüler, deren Fachbelegungen bestimmt werden sollen.
	 *
	 * @return die Map mit der Zuordnung
	 */
	HashMap2D<Long, Long, DTOGostSchuelerFachbelegungen> getMap2DBySchuelerIDAndFachID(Collection<Long> idsSchueler);

	/**
	 * Gibt eine Map mit den Fachbelegungen von Schülern zurück. Diese sind in der Map der Schüler-ID in
	 * Form einer Liste zugeordnet.
	 *
	 * @param idsSchueler   die IDs der Schüler, deren Fachbelegungen bestimmt werden sollen.
	 *
	 * @return die Map mit der Zuordnung
	 */
	Map<Long, List<DTOGostSchuelerFachbelegungen>> getMapBySchuelerID(Collection<Long> idsSchueler);

	/**
	 * Löscht alle Fachbelegungen von Schülern mit den übergebenen IDs.
	 *
	 * @param idsSchueler   die IDs der Schüler, deren Fachbelegungen entfernt werden sollen
	 */
	void deleteMultipleBySchuelerID(Collection<Long> idsSchueler);

}
