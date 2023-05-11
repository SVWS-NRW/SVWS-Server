package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanKalenderwochenZuordnung;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanKalenderwochenzuordnung}.
 */
public final class DataStundenplanKalenderwochenzuordnung extends DataManager<Long> {

	private final Long stundenplanID;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanKalenderwochenzuordnung}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Kalenderwochen-Zuordnungen abgefragt werden
	 */
	public DataStundenplanKalenderwochenzuordnung(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStundenplanKalenderwochenZuordnung} in einen Core-DTO {@link StundenplanKalenderwochenzuordnung}.
	 */
	private static final Function<DTOStundenplanKalenderwochenZuordnung, StundenplanKalenderwochenzuordnung> dtoMapper = (final DTOStundenplanKalenderwochenZuordnung k) -> {
		final StundenplanKalenderwochenzuordnung daten = new StundenplanKalenderwochenzuordnung();
		daten.id = k.ID;
		daten.jahr = k.Jahr;
		daten.kw = k.KW;
		daten.wochentyp = k.Wochentyp;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt die Kalenderwochenzuordnungen des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Kalenderwochenzuordnungen
	 */
	public static List<StundenplanKalenderwochenzuordnung> getKalenderwochenzuordnungen(final @NotNull DBEntityManager conn, final long idStundenplan) {
		final List<DTOStundenplanKalenderwochenZuordnung> zuordnungen = conn.queryNamed("DTOStundenplanKalenderwochenZuordnung.stundenplan_id", idStundenplan, DTOStundenplanKalenderwochenZuordnung.class);
		final ArrayList<StundenplanKalenderwochenzuordnung> daten = new ArrayList<>();
		for (final DTOStundenplanKalenderwochenZuordnung z : zuordnungen)
			daten.add(dtoMapper.apply(z));
		return daten;
	}

	@Override
	public Response getList() {
		final List<StundenplanKalenderwochenzuordnung> daten = getKalenderwochenzuordnungen(conn, this.stundenplanID);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einer Kalenderwochen-Zuordnung mit der ID null ist unzulässig.");
		final DTOStundenplanKalenderwochenZuordnung raum = conn.queryByKey(DTOStundenplanKalenderwochenZuordnung.class, id);
		if (raum == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde keine Kalenderwochen-Zuordnung mit der ID %d gefunden.".formatted(id));
		final StundenplanKalenderwochenzuordnung daten = dtoMapper.apply(raum);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static void patchStundenplanKalenderwochenZuordnung(final DTOStundenplanKalenderwochenZuordnung dto,
			final Map<String, Object> map) throws WebApplicationException {
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
				case "jahr" -> dto.Jahr = JSONMapper.convertToInteger(value, false);
				case "kw" -> dto.KW = JSONMapper.convertToInteger(value, false);
				case "wochentyp" -> dto.Wochentyp = JSONMapper.convertToInteger(value, false);
				default -> throw OperationError.BAD_REQUEST.exception();
			}
		}
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Patch einer Kalenderwochen-Zuordnung mit der ID null ist nicht möglich.");
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			return OperationError.NOT_FOUND.getResponse("In dem Patch sind keine Daten enthalten.");
		try {
			conn.transactionBegin();
			final DTOStundenplanKalenderwochenZuordnung zuordnung = conn.queryByKey(DTOStundenplanKalenderwochenZuordnung.class, id);
			if (zuordnung == null)
				throw OperationError.NOT_FOUND.exception();
			patchStundenplanKalenderwochenZuordnung(zuordnung, map);
			conn.transactionPersist(zuordnung);
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
