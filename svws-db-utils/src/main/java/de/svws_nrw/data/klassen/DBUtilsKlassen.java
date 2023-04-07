package de.svws_nrw.data.klassen;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;

/**
 * Diese Klasse beinhaltet wiederverwendbare Hilfsmethoden
 * zu Klassen in Bezug auf den Datenbank-Zugriff.
 */
public final class DBUtilsKlassen {

	private DBUtilsKlassen() {
		throw new IllegalStateException("Instantiation of " + DBUtilsKlassen.class.getName() + " not allowed");
	}

	/**
	 * Bestimmt die Klasse mit der angegebenen ID
	 *
	 * @param conn       die Datenbankverbindung
	 * @param idKlasse   die ID der Klasse
	 *
	 * @return das DTO zur Klasse
	 *
	 * @throws WebApplicationException   HTTP-Response NOT_FOUND, falls die Klasse nicht gefunden werden kann
	 */
	public static DTOKlassen get(final DBEntityManager conn, final Long idKlasse) throws WebApplicationException {
		if (idKlasse == null)
	    	throw OperationError.NOT_FOUND.exception("Die ID einer Klasse darf nicht null sein.");
		final DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, idKlasse);
		if (klasse == null)
	    	throw OperationError.NOT_FOUND.exception("Konnte die Klasse mit der ID " + idKlasse + " nicht finden.");
		return klasse;
	}


	/**
	 * Bestimmt zu der übergebenen Klasse, die zugehörige Klasse in dem angebenen Schuljahresabschnitt
	 *
	 * @param conn                     die Datenbankverbindung
	 * @param klasse                   die Klasse, für welche die zugehörige Klasse bestimmt werden soll
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return die zugehörige Klasse
	 *
	 * @throws WebApplicationException HTTP-Response BAD_REQUEST, falls die Klasse oder die ID null sind,
	 *                                          oder NOT_FOUND, falls die zugehörige Klasse nicht ermittelt werden kann
	 */
	public static DTOKlassen getKlasseInAbschnitt(final DBEntityManager conn, final DTOKlassen klasse, final Long idSchuljahresabschnitt) throws WebApplicationException {
		if (idSchuljahresabschnitt == null)
			throw OperationError.BAD_REQUEST.exception("Die ID des Schuljahresabschnittes darf nicht null sein.");
		if (klasse == null)
			throw OperationError.BAD_REQUEST.exception("Die Klasse darf nicht null sein.");
		if (klasse.Schuljahresabschnitts_ID == idSchuljahresabschnitt)
			return klasse;
		List<DTOKlassen> klassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.Klasse = ?1 AND e.Schuljahresabschnitts_ID = ?2", DTOKlassen.class, klasse.Klasse, idSchuljahresabschnitt);
		if (klassen.size() == 0) {
			klassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.ASDKlasse = ?1 AND e.Schuljahresabschnitts_ID = ?2", DTOKlassen.class, klasse.ASDKlasse, idSchuljahresabschnitt);
			if (klassen.size() == 0)
	        	throw OperationError.NOT_FOUND.exception("Konnte die Klasse " + klasse.Klasse + " des vorigen Abschnitts für den Schuljahresabschnitts mit der ID " + idSchuljahresabschnitt + " nicht finden.");
		}
		return klassen.get(0);
	}


	/**
	 * Bestimmt die Folgeklasse zu der übergebenen Klasse in dem selben Schuljahresabschnitt.
	 *
	 * @param conn                     die Datenbankverbindung
	 * @param klasse                   die Klasse, für welche die Folge-Klasse bestimmt werden soll
	 *
	 * @return die Folge-Klasse
	 *
	 * @throws WebApplicationException HTTP-Response BAD_REQUEST, falls die Klasse ist oder keine Folgeklasse zugewiesen wurde,
	 *                                          oder NOT_FOUND, falls die zugehörige Folge-Klasse nicht ermittelt werden kann
	 */
	public static DTOKlassen getFolgeKlasse(final DBEntityManager conn, final DTOKlassen klasse) {
		if (klasse == null)
			throw OperationError.BAD_REQUEST.exception("Die Klasse darf nicht null sein.");
		if (klasse.FKlasse == null)
			throw OperationError.BAD_REQUEST.exception("Die Klasse " + klasse.Klasse + " hat keine Folge-Klasse zugewiesen.");
		final List<DTOKlassen> klassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.Klasse = ?1 AND e.Schuljahresabschnitts_ID = ?2", DTOKlassen.class, klasse.FKlasse, klasse.Schuljahresabschnitts_ID);
		if (klassen.size() == 0)
        	throw OperationError.NOT_FOUND.exception("Konnte die Folge-Klasse " + klasse.FKlasse + " in dem Schuljahresabschnitts mit der ID " + klasse.Schuljahresabschnitts_ID + " nicht finden.");
		return klassen.get(0);
	}

}
