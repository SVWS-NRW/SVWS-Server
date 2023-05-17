package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanSchienen;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanSchiene}.
 */
public final class DataStundenplanSchienen extends DataManager<Long> {

	private final Long stundenplanID;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanSchiene}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Schienen abgefragt werden
	 */
	public DataStundenplanSchienen(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStundenplanSchienen} in einen Core-DTO {@link StundenplanRaum}.
	 */
	private static final Function<DTOStundenplanSchienen, StundenplanSchiene> dtoMapper = (final DTOStundenplanSchienen s) -> {
		final StundenplanSchiene daten = new StundenplanSchiene();
		daten.id = s.ID;
		daten.idJahrgang = s.Jahrgang_ID;
		daten.nummer = s.Nummer;
		daten.bezeichnung = s.Bezeichnung;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt die Schienen des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Schienen
	 */
	public static List<StundenplanSchiene> getSchienen(final @NotNull DBEntityManager conn, final long idStundenplan) {
		final List<DTOStundenplanSchienen> schienen = conn.queryNamed("DTOStundenplanSchienen.stundenplan_id", idStundenplan, DTOStundenplanSchienen.class);
		final ArrayList<StundenplanSchiene> daten = new ArrayList<>();
		for (final DTOStundenplanSchienen s : schienen)
			daten.add(dtoMapper.apply(s));
		return daten;
	}


	@Override
	public Response getList() {
		final List<StundenplanSchiene> daten = getSchienen(conn, this.stundenplanID);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einer Schiene eines Stundenplans mit der ID null ist unzulässig.");
		final DTOStundenplanSchienen schiene = conn.queryByKey(DTOStundenplanSchienen.class, id);
		if (schiene == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde keine Schiene eines Stundenplans mit der ID %d gefunden.".formatted(id));
		final StundenplanSchiene daten = dtoMapper.apply(schiene);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOStundenplanSchienen>> patchMappings = Map.ofEntries(
		Map.entry("id", (dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("idJahrgang", (dto, value, map) -> dto.Jahrgang_ID = JSONMapper.convertToLong(value, false)),
		Map.entry("nummer", (dto, value, map) -> dto.Nummer = JSONMapper.convertToInteger(value, false)),
		Map.entry("bezeichnung", (dto, value, map) -> dto.Bezeichnung = JSONMapper.convertToString(value, false, false, 100))
	);


	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOStundenplanSchienen.class, patchMappings);
	}

}
