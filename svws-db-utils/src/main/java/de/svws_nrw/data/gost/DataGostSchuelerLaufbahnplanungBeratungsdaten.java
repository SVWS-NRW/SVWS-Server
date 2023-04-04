package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostLaufbahnplanungBeratungsdaten}.
 */
public final class DataGostSchuelerLaufbahnplanungBeratungsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostLaufbahnplanungBeratungsdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostSchuelerLaufbahnplanungBeratungsdaten(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long schueler_id) {
		if (schueler_id == null)
	    	return OperationError.NOT_FOUND.getResponse();
		GostUtils.pruefeSchuleMitGOSt(conn);
		final DTOGostSchueler gostSchueler = conn.queryByKey(DTOGostSchueler.class, schueler_id);
    	if (gostSchueler == null)
    		return OperationError.NOT_FOUND.getResponse();
    	final GostLaufbahnplanungBeratungsdaten daten = new GostLaufbahnplanungBeratungsdaten();
    	daten.beratungslehrerID = gostSchueler.Beratungslehrer_ID;
    	daten.beratungsdatum = gostSchueler.DatumBeratung;
    	daten.kommentar = gostSchueler.Kommentar;
    	daten.ruecklaufdatum = gostSchueler.DatumRuecklauf;
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long schueler_id, final InputStream is) {
    	final Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() > 0) {
    		try {
    			conn.transactionBegin();
	    		final DTOGostSchueler gostSchueler = conn.queryByKey(DTOGostSchueler.class, schueler_id);
		    	if (gostSchueler == null)
		    		throw OperationError.NOT_FOUND.exception();
		    	for (final Entry<String, Object> entry : map.entrySet()) {
		    		final String key = entry.getKey();
		    		final Object value = entry.getValue();
		    		switch (key) {
		    			case "beratungslehrerID" -> {
		    				final Long beratungslehrerID = JSONMapper.convertToLong(value, true);
		    				if (beratungslehrerID != null) {
		    					final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, beratungslehrerID);
		    					if (lehrer == null)
		    						throw OperationError.CONFLICT.exception();
		    				}
		    				gostSchueler.Beratungslehrer_ID = beratungslehrerID;
		    			}
		    			case "beratungsdatum" -> gostSchueler.DatumBeratung = JSONMapper.convertToString(value, true, false);
		    			case "kommentar" -> gostSchueler.Kommentar = JSONMapper.convertToString(value, true, true);
		    			case "ruecklaufdatum" -> gostSchueler.DatumRuecklauf = JSONMapper.convertToString(value, true, false);
		    			default -> throw OperationError.BAD_REQUEST.exception();
		    		}
		    	}
		    	conn.transactionPersist(gostSchueler);
		    	conn.transactionCommit();
    		} catch (final Exception e) {
    			if (e instanceof final WebApplicationException webAppException)
    				return webAppException.getResponse();
				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
    		} finally {
    			// Perform a rollback if necessary
    			conn.transactionRollback();
    		}
    	}
    	return Response.status(Status.OK).build();
	}

}
