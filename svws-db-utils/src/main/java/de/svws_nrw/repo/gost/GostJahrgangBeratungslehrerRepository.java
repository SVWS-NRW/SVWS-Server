package de.svws_nrw.repo.gost;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangBeratungslehrer;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangBeratungslehrerPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die GOST-Beratungslehrer-Tabelle für die einzelnen Abiturjahrgänge der SVWS-Datenbank
 */
public interface GostJahrgangBeratungslehrerRepository extends RepositoryBase<DTOGostJahrgangBeratungslehrer, DTOGostJahrgangBeratungslehrerPK> {

	/**
	 * Gibt eine Liste mit den Jahrgangsspezifischen Beratungslehrern zurück.
	 *
	 * @param abiturjahrgaenge   die Abiturjahrgänge deren Beratungslehrer bestimmt werden sollen.
	 *
	 * @return die Liste der Beratungslehrer
	 */
	List<DTOGostJahrgangBeratungslehrer> getListByAbiturjahrgaenge(Collection<Integer> abiturjahrgaenge);

	/**
	 * Gibt eine Map mit den Jahrgangsspezifischen Beratungslehrern zurück. Diese sind in der Map dem Abiturjahrgang und
	 * der ID des Lehrers der Schule zugeordnet.
	 *
	 * @param abiturjahrgaenge   die Abiturjahrgänge deren Beratungslehrer bestimmt werden sollen.
	 *
	 * @return die Map mit der Zuordnung
	 */
	HashMap2D<Integer, Long, DTOGostJahrgangBeratungslehrer> getMap2DByAbiturjahrgaengeAndLehrerID(Collection<Integer> abiturjahrgaenge);

}
