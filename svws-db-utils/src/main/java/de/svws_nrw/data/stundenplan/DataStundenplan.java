package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link Stundenplan}.
 */
public final class DataStundenplan extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Stundenplan}.
	 *
	 * @param conn   die Datenbank-Verbindung
	 */
	public DataStundenplan(final DBEntityManager conn) {
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
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Stundenplan mit der ID null ist unzulässig.");
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, id);
		if (stundenplan == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(id));
		final List<StundenplanZeitraster> zeitraster = DataStundenplanZeitraster.getZeitraster(conn, id);
		final List<StundenplanRaum> raeume = DataStundenplanRaeume.getRaeume(conn, id);
		final List<StundenplanPausenzeit> pausenzeiten = DataStundenplanPausenzeiten.getPausenzeiten(conn, id);
		final List<StundenplanKalenderwochenzuordnung> kalenderwochenzuordnung = DataStundenplanKalenderwochenzuordnung.getKalenderwochenzuordnungen(conn, id);
		// Erstelle das Core-DTO-Objekt für die Response
		final Stundenplan daten = new Stundenplan();
		daten.id = stundenplan.ID;
		daten.idSchuljahresabschnitt = stundenplan.Schuljahresabschnitts_ID;
		daten.gueltigAb = stundenplan.Beginn;
		daten.gueltigBis = stundenplan.Ende;
		daten.bezeichnungStundenplan = stundenplan.Beschreibung;
		daten.wochenTypModell = stundenplan.WochentypModell;
		daten.zeitraster.addAll(zeitraster);
		daten.raeume.addAll(raeume);
		daten.pausenzeiten.addAll(pausenzeiten);
		daten.kalenderwochenZuordnung.addAll(kalenderwochenzuordnung);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static void patchStundenplan(final DTOStundenplan dto, final Map<String, Object> map) throws WebApplicationException {
		for (final Entry<String, Object> entry : map.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			switch (key) {
				// Basisdaten
				case "id" -> {
					final Long patch_id = JSONMapper.convertToLong(value, true);
					if ((patch_id == null) || (patch_id.longValue() != dto.ID))
						throw OperationError.BAD_REQUEST.exception();
				}
				case "idSchuljahresabschnitt" -> throw OperationError.BAD_REQUEST.exception();
				case "gueltigAb" -> dto.Beginn = JSONMapper.convertToString(value, false, false, null);
				case "gueltigBis" -> dto.Ende = JSONMapper.convertToString(value, false, false, null);
				case "bezeichnungStundenplan" -> dto.Beschreibung = JSONMapper.convertToString(value, false, false, 1000);
				case "wochenTypModell" -> dto.WochentypModell = JSONMapper.convertToInteger(value, false);
				default -> throw OperationError.BAD_REQUEST.exception();
			}
		}
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Patch bei einem Stundenplan mit der ID null ist nicht möglich.");
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			return OperationError.NOT_FOUND.getResponse("In dem Patch sind keine Daten enthalten.");
		try {
			conn.transactionBegin();
			final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, id);
			if (stundenplan == null)
				throw OperationError.NOT_FOUND.exception();
			patchStundenplan(stundenplan, map);
			conn.transactionPersist(stundenplan);
			conn.transactionCommit();
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webAppException)
				return webAppException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			conn.transactionRollback();
		}
		return Response.status(Status.OK).build();
	}

}
