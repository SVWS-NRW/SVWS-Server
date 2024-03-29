package de.svws_nrw.data.klassen;

import java.util.List;
import java.util.Objects;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

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
	 * @throws ApiOperationException   HTTP-Response NOT_FOUND, falls die Klasse nicht gefunden werden kann
	 */
	public static DTOKlassen get(final DBEntityManager conn, final Long idKlasse) throws ApiOperationException {
		if (idKlasse == null)
	    	throw new ApiOperationException(Status.NOT_FOUND, "Die ID einer Klasse darf nicht null sein.");
		final DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, idKlasse);
		if (klasse == null)
	    	throw new ApiOperationException(Status.NOT_FOUND, "Konnte die Klasse mit der ID %d nicht finden.".formatted(idKlasse));
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
	 * @throws ApiOperationException   HTTP-Response BAD_REQUEST, falls die Klasse oder die ID null sind,
	 *                                 oder NOT_FOUND, falls die zugehörige Klasse nicht ermittelt werden kann
	 */
	public static DTOKlassen getKlasseInAbschnitt(final DBEntityManager conn, final DTOKlassen klasse, final Long idSchuljahresabschnitt) throws ApiOperationException {
		if (idSchuljahresabschnitt == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Schuljahresabschnittes darf nicht null sein.");
		if (klasse == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Klasse darf nicht null sein.");
		if (Objects.equals(klasse.Schuljahresabschnitts_ID, idSchuljahresabschnitt))
			return klasse;
		List<DTOKlassen> klassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.Klasse = ?1 AND e.Schuljahresabschnitts_ID = ?2", DTOKlassen.class, klasse.Klasse, idSchuljahresabschnitt);
		if (klassen.isEmpty()) {
			klassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.ASDKlasse = ?1 AND e.Schuljahresabschnitts_ID = ?2", DTOKlassen.class, klasse.ASDKlasse, idSchuljahresabschnitt);
			if (klassen.isEmpty())
	        	throw new ApiOperationException(Status.NOT_FOUND, "Konnte die Klasse " + klasse.Klasse + " des vorigen Abschnitts für den Schuljahresabschnitts mit der ID " + idSchuljahresabschnitt + " nicht finden.");
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
	 * @throws ApiOperationException   HTTP-Response BAD_REQUEST, falls die Klasse ist oder keine Folgeklasse zugewiesen wurde,
	 *                                 oder NOT_FOUND, falls die zugehörige Folge-Klasse nicht ermittelt werden kann
	 */
	public static DTOKlassen getFolgeKlasse(final DBEntityManager conn, final DTOKlassen klasse) throws ApiOperationException {
		if (klasse == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Klasse darf nicht null sein.");
		if (klasse.FKlasse == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Klasse " + klasse.Klasse + " hat keine Folge-Klasse zugewiesen.");
		final List<DTOKlassen> klassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.Klasse = ?1 AND e.Schuljahresabschnitts_ID = ?2", DTOKlassen.class, klasse.FKlasse, klasse.Schuljahresabschnitts_ID);
		if (klassen.isEmpty())
        	throw new ApiOperationException(Status.NOT_FOUND, "Konnte die Folge-Klasse " + klasse.FKlasse + " in dem Schuljahresabschnitts mit der ID " + klasse.Schuljahresabschnitts_ID + " nicht finden.");
		return klassen.get(0);
	}

}
