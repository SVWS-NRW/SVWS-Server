package de.svws_nrw.data.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

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
	 * @throws ApiOperationException    falls keine Schule definiert ist
	 */
	public static DTOEigeneSchule get(final DBEntityManager conn) throws ApiOperationException {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw new ApiOperationException(Status.NOT_FOUND, "In der Datenbank wurde keine Schule definiert");
		return schule;
	}


}
