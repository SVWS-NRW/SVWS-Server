package de.svws_nrw.repo.gost;

import java.util.Collection;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFaecher;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFaecherPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die GOST-Fächer-Tabelle für die einzelnen Abiturjahrgänge der SVWS-Datenbank
 */
public interface GostJahrgangFaecherRepository extends RepositoryBase<DTOGostJahrgangFaecher, DTOGostJahrgangFaecherPK> {

	/**
	 * Gibt eine Map mit den Jahrgangsspezifischen Fachinformationen zurück. Diese sind in der Map dem Abiturjahrgang und
	 * der ID des zugrundeliegenden Faches der Schule zugeordnet.
	 *
	 * @param abiturjahrgaenge   die Abiturjahrgänge deren Fachinformationen bestimmt werden sollen.
	 *
	 * @return die Map mit der Zuordnung
	 */
	HashMap2D<Integer, Long, DTOGostJahrgangFaecher> getMap2DByAbiturjahrgangAndFachID(Collection<Integer> abiturjahrgaenge);

}
