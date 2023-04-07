package de.svws_nrw.data.schule;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;

/**
 * Dies Klassen stellt Hilfmethoden für den Datenbankzugriff
 * zur Verfügung, welche in den Data-Klassen an mehreren Stellen
 * verwendet werden.
 */
public final class SchulUtils {

	private SchulUtils() {
		throw new IllegalStateException("Instantiation of " + SchulUtils.class.getName() + " not allowed");
	}

	/**
	 * Ermittelt den DB-Schuljahresabschnitt anhand der ID.
	 *
	 * @param conn   die aktuelle Datenbankverbindung
	 * @param id     die ID des Schuljahresabschnitts
	 *
	 * @return der Schuljahresabschnitt
	 *
	 * @throws WebApplicationException    falls der Schuljahresabschnitt nicht existiert (NOT_FOUND)
	 */
	public static DTOSchuljahresabschnitte getSchuljahreabschnitt(final DBEntityManager conn, final long id)  throws WebApplicationException {
		final DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, id);
		if (abschnitt == null)
			throw OperationError.NOT_FOUND.exception();
		return abschnitt;
	}


	/**
	 * Ermittelt den DB-Schuljahresabschnitt anhand des Schuljahres und des Abschnittes.
	 *
	 * @param conn        die aktuelle Datenbankverbindung
	 * @param schuljahr   das Schuljahr
	 * @param abschnitt   der Abschnitt im Schuljahr
	 *
	 * @return der Schuljahresabschnitt
	 *
	 * @throws WebApplicationException    falls der Schuljahresabschnitt nicht existiert (NOT_FOUND)
	 */
	public static DTOSchuljahresabschnitte getSchuljahreabschnitt(final DBEntityManager conn, final long schuljahr, final int abschnitt)  throws WebApplicationException {
		final List<DTOSchuljahresabschnitte> dtos = conn.queryList("SELECT e FROM DTOSchuljahresabschnitte e WHERE e.Jahr = ?1 and e.Abschnitt = ?2", DTOSchuljahresabschnitte.class, schuljahr, abschnitt);
		if (dtos.size() != 1)
			throw OperationError.NOT_FOUND.exception();
		final DTOSchuljahresabschnitte dto = dtos.get(0);
		return dto;
	}

}
