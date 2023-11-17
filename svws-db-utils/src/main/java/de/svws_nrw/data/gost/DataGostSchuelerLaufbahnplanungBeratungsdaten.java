package de.svws_nrw.data.gost;

import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

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


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOGostSchueler} in einen Core-DTO {@link StundenplanLehrer}.
	 */
	private static final Function<DTOGostSchueler, GostLaufbahnplanungBeratungsdaten> dtoMapper = (final DTOGostSchueler dto) -> {
		final GostLaufbahnplanungBeratungsdaten daten = new GostLaufbahnplanungBeratungsdaten();
		daten.beratungslehrerID = dto.Beratungslehrer_ID;
		daten.beratungsdatum = dto.DatumBeratung;
		daten.kommentar = dto.Kommentar;
		daten.ruecklaufdatum = dto.DatumRuecklauf;
		return daten;
	};


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
		final GostLaufbahnplanungBeratungsdaten daten = getFromID(schueler_id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Gibt die Beratungsdaten eines Schülers zur GostLaufbahnplanung zurück.
	 *
	 * @param schueler_id	Die ID des Schülers
	 * @return				Die GostLaufbahnplanungBeratungsdaten des Schülers.
	 */
	public GostLaufbahnplanungBeratungsdaten getFromID(final Long schueler_id) throws WebApplicationException {
		if (schueler_id == null)
			throw OperationError.NOT_FOUND.exception("Es wurde keine Schüler-ID übergeben.");
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOGostSchueler gostSchueler = conn.queryByKey(DTOGostSchueler.class, schueler_id);
		if (gostSchueler == null)
			throw OperationError.NOT_FOUND.exception("Es wurde kein Schüler mit Daten zur GOSt mit der ID " + schueler_id + " gefunden.");
		return dtoMapper.apply(gostSchueler);
	}

	/**
	 * Gibt die Beratungsdaten zu mehreren Schülern zur GostLaufbahnplanung zurück.
	 *
	 * @param schueler_ids	Die IDs der Schüler
	 * @return				Die GostLaufbahnplanungBeratungsdaten der Schüler.
	 */
	public Map<Long, GostLaufbahnplanungBeratungsdaten> getMapFromIDs(final List<Long> schueler_ids) throws WebApplicationException {
		if (schueler_ids == null)
			throw OperationError.NOT_FOUND.exception("Es wurden keine Schüler-IDs übergeben.");
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final Map<Long, DTOGostSchueler> mapGostSchueler = conn
			.queryNamed("DTOGostSchueler.schueler_id.multiple", schueler_ids, DTOGostSchueler.class)
			.stream().collect(Collectors.toMap(s -> s.Schueler_ID, s -> s));

		final Map<Long, GostLaufbahnplanungBeratungsdaten> result = new HashMap<>();
		for (final Long sID : schueler_ids) {
			final var schueler = mapGostSchueler.get(sID);
			if (schueler == null)
				throw OperationError.NOT_FOUND.exception("Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.");
			result.put(sID, dtoMapper.apply(schueler));
		}
		return result;
	}



	@Override
	public Response patch(final Long schueler_id, final InputStream is) {
    	final Map<String, Object> map = JSONMapper.toMap(is);
    	if (!map.isEmpty()) {
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
		    			case "beratungsdatum" -> gostSchueler.DatumBeratung = JSONMapper.convertToString(value, true, false, null);
		    			case "kommentar" -> gostSchueler.Kommentar = JSONMapper.convertToString(value, true, true, Schema.tab_Gost_Schueler.col_Kommentar.datenlaenge());
		    			case "ruecklaufdatum" -> gostSchueler.DatumRuecklauf = JSONMapper.convertToString(value, true, false, null);
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
