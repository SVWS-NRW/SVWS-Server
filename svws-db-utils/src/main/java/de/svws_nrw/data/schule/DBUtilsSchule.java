package de.svws_nrw.data.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;

/**
 * Diese Klasse beinhaltet wiederverwendbare Hilfsmethoden
 * zu der Schule in Bezug auf den Datenbank-Zugriff.
 */
public final class DBUtilsSchule {

	private DBUtilsSchule() {
		throw new IllegalStateException("Instantiation of " + DBUtilsSchule.class.getName() + " not allowed");
	}

	/**
	 * Ermittelt die Daten der eigenen Schule.
	 *
	 * @param conn   die aktuelle Datenbankverbindung
	 *
	 * @return das Datenbank-DTO der Schule
	 *
	 * @throws WebApplicationException    falls keine Schule definiert ist
	 */
	public static DTOEigeneSchule get(final DBEntityManager conn) throws WebApplicationException {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw OperationError.NOT_FOUND.exception("In der Datenbank wurde keine Schule definiert");
		return schule;
	}


}
