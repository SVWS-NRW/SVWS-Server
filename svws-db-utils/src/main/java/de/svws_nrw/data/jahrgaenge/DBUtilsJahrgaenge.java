package de.svws_nrw.data.jahrgaenge;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse beinhaltet wiederverwendbare Hilfsmethoden
 * zu Jahrgängen in Bezug auf den Datenbank-Zugriff.
 */
public final class DBUtilsJahrgaenge {

	private DBUtilsJahrgaenge() {
		throw new IllegalStateException("Instantiation of " + DBUtilsJahrgaenge.class.getName() + " not allowed");
	}

	/**
	 * Bestimmt den Jahrgang mit der angegebenen ID
	 *
	 * @param conn         die Datenbankverbindung
	 * @param idJahrgang   die ID des Jahrgangs
	 *
	 * @return das DTO zum Jahrgang
	 *
	 * @throws ApiOperationException   HTTP-Response NOT_FOUND, falls der Jahrgang nicht gefunden werden kann
	 */
	public static DTOJahrgang get(final DBEntityManager conn, final long idJahrgang) throws ApiOperationException {
		final DTOJahrgang jahrgang = conn.queryByKey(DTOJahrgang.class, idJahrgang);
		if (jahrgang == null)
	    	throw new ApiOperationException(Status.NOT_FOUND, "Konnte den Jahrgang mit der ID " + idJahrgang + " nicht finden.");
		return jahrgang;
	}

}
