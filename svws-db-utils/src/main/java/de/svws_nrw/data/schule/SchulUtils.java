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
public class SchulUtils {

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
	public static DTOSchuljahresabschnitte getSchuljahreabschnitt(final DBEntityManager conn, long id)  throws WebApplicationException {
		DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, id);
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
	public static DTOSchuljahresabschnitte getSchuljahreabschnitt(final DBEntityManager conn, long schuljahr, int abschnitt)  throws WebApplicationException {
		List<DTOSchuljahresabschnitte> dtos = conn.queryList("SELECT e FROM DTOSchuljahresabschnitte e WHERE e.Jahr = ?1 and e.Abschnitt = ?2", DTOSchuljahresabschnitte.class, schuljahr, abschnitt);
		if (dtos.size() != 1)
			throw OperationError.NOT_FOUND.exception();
		DTOSchuljahresabschnitte dto = dtos.get(0);
		return dto;
	}
	
}
