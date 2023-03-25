package de.svws_nrw.data.jahrgaenge;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;

/**
 * Diese Klasse beinhaltet wiederverwendbare Hilfsmethoden
 * zu Jahrgängen in Bezug auf den Datenbank-Zugriff. 
 */
public class DBUtilsJahrgaenge {

	/**
	 * Bestimmt den Jahrgang mit der angegebenen ID
	 * 
	 * @param conn         die Datenbankverbindung
	 * @param idJahrgang   die ID des Jahrgangs
	 * 
	 * @return das DTO zum Jahrgang
	 * 
	 * @throws WebApplicationException   HTTP-Response NOT_FOUND, falls der Jahrgang nicht gefunden werden kann 
	 */
	public static DTOJahrgang get(final DBEntityManager conn, final long idJahrgang) throws WebApplicationException {
		DTOJahrgang jahrgang = conn.queryByKey(DTOJahrgang.class, idJahrgang);
		if (jahrgang == null)
	    	throw OperationError.NOT_FOUND.exception("Konnte den Jahrgang mit der ID " + idJahrgang + " nicht finden.");
		return jahrgang;
	}

}
