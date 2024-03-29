package de.svws_nrw.data.schule;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;

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
	 * Bestimmt das DB-DTO für die eigene Schule.
	 *
	 * @param conn   die Datenbankverbindung für die Abfrage des Schul-DTOs
	 *
	 * @return das DTO für die eigene Schule.
	 *
	 * @throws ApiOperationException mit dem Response Code 404 NOT_FOUND falls kein Schul-Eintrag in der DB vorhanden ist
	 */
	public static @NotNull DTOEigeneSchule getDTOSchule(@NotNull final DBEntityManager conn) throws ApiOperationException {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
    		throw new ApiOperationException(Status.NOT_FOUND, "Kein Eintrag für die eigene Schule in der Datenbank vorhanden.");
		return schule;
	}

	/**
	 * Ermittelt den DB-Schuljahresabschnitt anhand der ID.
	 *
	 * @param conn   die aktuelle Datenbankverbindung
	 * @param id     die ID des Schuljahresabschnitts
	 *
	 * @return der Schuljahresabschnitt
	 *
	 * @throws ApiOperationException    falls der Schuljahresabschnitt nicht existiert (NOT_FOUND)
	 */
	public static @NotNull DTOSchuljahresabschnitte getSchuljahreabschnitt(final DBEntityManager conn, final long id) throws ApiOperationException {
		final DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, id);
		if (abschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND);
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
	 * @throws ApiOperationException    falls der Schuljahresabschnitt nicht existiert (NOT_FOUND)
	 */
	public static DTOSchuljahresabschnitte getSchuljahreabschnitt(final DBEntityManager conn, final long schuljahr, final int abschnitt) throws ApiOperationException {
		final List<DTOSchuljahresabschnitte> dtos = conn.queryList("SELECT e FROM DTOSchuljahresabschnitte e WHERE e.Jahr = ?1 and e.Abschnitt = ?2", DTOSchuljahresabschnitte.class, schuljahr, abschnitt);
		if (dtos.size() != 1)
			throw new ApiOperationException(Status.NOT_FOUND);
		return dtos.get(0);
	}

}
