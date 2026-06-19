package de.svws_nrw.repo.gost;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachkombinationen;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die GOST-Jahrgangs-Tabelle mit den Fachkombinationen der SVWS-Datenbank
 */
public interface GostJahrgangFachkombinationenRepository extends Repository<DTOGostJahrgangFachkombinationen> {

	/**
	 * Gibt eine Liste mit den Fachkombinationen der übergebenen Abiturjahrgänge zurück.
	 *
	 * @param abiturjahrgaenge   die Abiturjahrgänge
	 *
	 * @return die Liste
	 */
	List<DTOGostJahrgangFachkombinationen> getListByAbiturjahrgaenge(Collection<Integer> abiturjahrgaenge);


	/**
	 * Gibt eine Map mit den Fachkombinationen der übergebenen Abiturjahrgänge zugeordnet zu den Abiturjahrgängen zurück.
	 *
	 * @param abiturjahrgaenge   die Abiturjahrgänge
	 *
	 * @return die Map
	 */
	Map<Integer, List<DTOGostJahrgangFachkombinationen>> getMapByAbiturjahrgaenge(Collection<Integer> abiturjahrgaenge);

}
