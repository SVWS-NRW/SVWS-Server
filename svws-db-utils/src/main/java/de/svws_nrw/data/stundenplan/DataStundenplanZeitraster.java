package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanZeitraster;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanZeitraster}.
 */
public final class DataStundenplanZeitraster extends DataManager<Long> {

	private final Long stundenplanID;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanZeitraster}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Zeitraster abgefragt wird
	 */
	public DataStundenplanZeitraster(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStundenplanZeitraster} in einen Core-DTO {@link StundenplanZeitraster}.
	 */
	private static final Function<DTOStundenplanZeitraster, StundenplanZeitraster> dtoMapper = (final DTOStundenplanZeitraster z) -> {
		final StundenplanZeitraster daten = new StundenplanZeitraster();
		daten.id = z.ID;
		daten.wochentag = z.Tag;
		daten.unterrichtstunde = z.Stunde;
		daten.stundenbeginn = z.Beginn;
		daten.stundenende = z.Ende;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt das Zeitraster des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return das Zeitraster
	 */
	public static List<StundenplanZeitraster> getZeitraster(final @NotNull DBEntityManager conn, final long idStundenplan) {
		final List<DTOStundenplanZeitraster> zeitraster = conn.queryNamed("DTOStundenplanZeitraster.stundenplan_id", idStundenplan, DTOStundenplanZeitraster.class);
		final ArrayList<StundenplanZeitraster> daten = new ArrayList<>();
		for (final DTOStundenplanZeitraster z : zeitraster)
			daten.add(dtoMapper.apply(z));
		return daten;
	}

	@Override
	public Response getList() {
		final List<StundenplanZeitraster> daten = getZeitraster(conn, this.stundenplanID);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Zeitrastereintrag eines Stundenplans mit der ID null ist unzulässig.");
		final DTOStundenplanZeitraster eintrag = conn.queryByKey(DTOStundenplanZeitraster.class, id);
		if (eintrag == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde kein Zeitrastereintrag eines Stundenplans mit der ID %d gefunden.".formatted(id));
		final StundenplanZeitraster daten = dtoMapper.apply(eintrag);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static void patchStundenplanZeitraster(final DTOStundenplanZeitraster dto, final Map<String, Object> map) throws WebApplicationException {
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
				case "wochentag" -> dto.Tag = JSONMapper.convertToInteger(value, false);
				case "unterrichtstunde" -> dto.Tag = JSONMapper.convertToInteger(value, false);
				case "stundenbeginn" -> dto.Beginn = JSONMapper.convertToString(value, false, false, null);
				case "stundenende" -> dto.Ende = JSONMapper.convertToString(value, false, false, null);
				default -> throw OperationError.BAD_REQUEST.exception();
			}
		}
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Patch eines Zeitraster-Eintrags mit der ID null ist nicht möglich.");
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			return OperationError.NOT_FOUND.getResponse("In dem Patch sind keine Daten enthalten.");
		try {
			conn.transactionBegin();
			final DTOStundenplanZeitraster eintrag = conn.queryByKey(DTOStundenplanZeitraster.class, id);
			if (eintrag == null)
				throw OperationError.NOT_FOUND.exception();
			patchStundenplanZeitraster(eintrag, map);
			conn.transactionPersist(eintrag);
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
