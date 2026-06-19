package de.svws_nrw.repo.gost;

import java.util.Collection;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachbelegungen;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachbelegungenPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Vorlagen-Fachbelegungen von Abiturjahrgängen in der gymnasialen Oberstufe in der SVWS-Datenbank
 */
public interface GostJahrgangFachbelegungenRepository extends RepositoryBase<DTOGostJahrgangFachbelegungen, DTOGostJahrgangFachbelegungenPK> {

	/**
	 * Gibt eine Map mit den Vorlage-Fachbelegungen von Abiturjahrgängen zurück. Diese sind in der Map dem Abiturjahrgang und
	 * der Fach-ID der Belegung zugeordnet.
	 *
	 * @param abiturjahrgaenge   die IDs der Abiturjahrgänge, deren Vorlage-Fachbelegungen bestimmt werden sollen.
	 *
	 * @return die Map mit der Zuordnung
	 */
	HashMap2D<Integer, Long, DTOGostJahrgangFachbelegungen> getMap2DByAbiturjahrgangAndFachID(Collection<Integer> abiturjahrgaenge);

	/**
	 * Löscht alle Vorlage-Fachbelegungen der übergebenen Abiturjahrgänge.
	 *
	 * @param abiturjahrgaenge   die Abiturjahrgänge, deren Fachbelegungen entfernt werden sollen
	 */
	void deleteMultipleByAbiturjahrgang(Collection<Long> abiturjahrgaenge);

}
