package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanKalenderwochenZuordnung;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanKalenderwochenzuordnung}.
 */
public final class DataStundenplanKalenderwochenzuordnung extends DataManager<Long> {

	private Long stundenplanID = null;

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
	private static final DTOMapper<DTOStundenplanKalenderwochenZuordnung, StundenplanKalenderwochenzuordnung> dtoMapper = (final DTOStundenplanKalenderwochenZuordnung k) -> {
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
		final List<DTOStundenplanKalenderwochenZuordnung> zuordnungen = conn.queryList(DTOStundenplanKalenderwochenZuordnung.QUERY_BY_STUNDENPLAN_ID,
				DTOStundenplanKalenderwochenZuordnung.class, idStundenplan);
		final ArrayList<StundenplanKalenderwochenzuordnung> daten = new ArrayList<>();
		try {
			for (final DTOStundenplanKalenderwochenZuordnung z : zuordnungen)
				daten.add(dtoMapper.apply(z));
		} catch (@SuppressWarnings("unused") final ApiOperationException e) {
			// Nichts zu tun, da die Exception nicht auftreten wird
		}
		return daten;
	}

	@Override
	public Response getList() {
		final List<StundenplanKalenderwochenzuordnung> daten = getKalenderwochenzuordnungen(conn, this.stundenplanID);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Kalenderwochen-Zuordnung mit der ID null ist unzulässig.");
		final DTOStundenplanKalenderwochenZuordnung raum = conn.queryByKey(DTOStundenplanKalenderwochenZuordnung.class, id);
		if (raum == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Kalenderwochen-Zuordnung mit der ID %d gefunden.".formatted(id));
		final StundenplanKalenderwochenzuordnung daten = dtoMapper.apply(raum);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOStundenplanKalenderwochenZuordnung>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw new ApiOperationException(Status.BAD_REQUEST);
		}),
		Map.entry("jahr", (conn, dto, value, map) -> {
			dto.Jahr = JSONMapper.convertToInteger(value, false);
			if (DateUtils.gibIstJahrUngueltig(dto.Jahr))
				throw new ApiOperationException(Status.BAD_REQUEST);
			}),
		Map.entry("kw", (conn, dto, value, map) -> dto.KW = JSONMapper.convertToInteger(value, false)),
		Map.entry("wochentyp", (conn, dto, value, map) -> dto.Wochentyp = JSONMapper.convertToInteger(value, false))
	);


	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOStundenplanKalenderwochenZuordnung.class, patchMappings);
	}

	private static final Set<String> requiredCreateAttributes = Set.of("jahr", "kw", "wochentyp");

	private final ObjLongConsumer<DTOStundenplanKalenderwochenZuordnung> initDTO = (dto, id) -> {
		dto.ID = id;
		dto.Stundenplan_ID = this.stundenplanID;
	};


	/**
	 * Fügt eine Kalenderwochenzuordnung mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige CoreDTO
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response add(final InputStream is) throws ApiOperationException {
		DataStundenplan.getDTOStundenplan(conn, stundenplanID);   // Prüfe, on der Stundenplan existiert
		return super.addBasic(is, DTOStundenplanKalenderwochenZuordnung.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Fügt mehrere Kalenderwochenzuordnungen mit den übergebenen JSON-Daten der Datenbank hinzu und gibt die zugehörigen CoreDTOs
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response addMultiple(final InputStream is) throws ApiOperationException {
		DataStundenplan.getDTOStundenplan(conn, stundenplanID);   // Prüfe, on der Stundenplan existiert
		return super.addBasicMultiple(is, DTOStundenplanKalenderwochenZuordnung.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht eine Kalenderwochenzuordnung
	 *
	 * @param id   die ID de Kalenderwochenzuordnung
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		return super.deleteBasic(id, DTOStundenplanKalenderwochenZuordnung.class, dtoMapper);
	}


	/**
	 * Löscht mehrere Kalenderwochenzuordnungen
	 *
	 * @param ids   die IDs der Kalenderwochenzuordnungen
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response deleteMultiple(final List<Long> ids) throws ApiOperationException {
		final List<DTOStundenplanKalenderwochenZuordnung> dtos = conn.queryByKeyList(DTOStundenplanKalenderwochenZuordnung.class, ids);
		for (final DTOStundenplanKalenderwochenZuordnung dto : dtos)
			if (dto.Stundenplan_ID != this.stundenplanID)
				throw new ApiOperationException(Status.BAD_REQUEST, "Mindestens eine Kalenderwochenzuordnung gehört nicht zu dem angegebenen Stundenplan.");
		return super.deleteBasicMultiple(ids, DTOStundenplanKalenderwochenZuordnung.class, dtoMapper);
	}


}
