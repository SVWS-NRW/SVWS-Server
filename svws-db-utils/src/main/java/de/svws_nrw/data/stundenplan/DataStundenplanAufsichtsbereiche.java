package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.stundenplan.StundenplanAufsichtsbereich;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanAufsichtsbereich;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanAufsichtsbereich}.
 */
public final class DataStundenplanAufsichtsbereiche extends DataManager<Long> {

	private Long stundenplanID = null;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanAufsichtsbereich}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Aufsichtsbereiche abgefragt werden
	 */
	public DataStundenplanAufsichtsbereiche(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStundenplanAufsichtsbereich} in einen Core-DTO {@link StundenplanAufsichtsbereich}.
	 */
	private static final DTOMapper<DTOStundenplanAufsichtsbereich, StundenplanAufsichtsbereich> dtoMapper = (final DTOStundenplanAufsichtsbereich a) -> {
		final StundenplanAufsichtsbereich daten = new StundenplanAufsichtsbereich();
		daten.id = a.ID;
		daten.kuerzel = a.Kuerzel;
		daten.beschreibung = a.Beschreibung;
		return daten;
	};


	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}

	/**
	 * Gibt die Aufsichtsbereiche des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Aufsichtsbereiche
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<StundenplanAufsichtsbereich> getAufsichtsbereiche(final @NotNull DBEntityManager conn, final long idStundenplan)
			throws ApiOperationException {
		final List<DTOStundenplanAufsichtsbereich> aufsichtsbereiche = conn.queryList(DTOStundenplanAufsichtsbereich.QUERY_BY_STUNDENPLAN_ID,
				DTOStundenplanAufsichtsbereich.class, idStundenplan);
		final ArrayList<StundenplanAufsichtsbereich> daten = new ArrayList<>();
		for (final DTOStundenplanAufsichtsbereich a : aufsichtsbereiche)
			daten.add(dtoMapper.apply(a));
		return daten;
	}

	@Override
	public Response getList() throws ApiOperationException {
		final List<StundenplanAufsichtsbereich> daten = getAufsichtsbereiche(conn, this.stundenplanID);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einem Aufsichtsbereich eines Stundenplans mit der ID null ist unzulässig.");
		final DTOStundenplanAufsichtsbereich aufsichtsbereich = conn.queryByKey(DTOStundenplanAufsichtsbereich.class, id);
		if (aufsichtsbereich == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Aufsichtsbereich eines Stundenplans mit der ID %d gefunden.".formatted(id));
		final StundenplanAufsichtsbereich daten = dtoMapper.apply(aufsichtsbereich);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOStundenplanAufsichtsbereich>> patchMappings = Map.ofEntries(
			Map.entry("id", (conn, dto, value, map) -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}),
			Map.entry("kuerzel", (conn, dto, value, map) -> dto.Kuerzel = JSONMapper.convertToString(value, false, false, 20)),
			Map.entry("beschreibung", (conn, dto, value, map) -> dto.Beschreibung = JSONMapper.convertToString(value, false, true, 1000)));

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOStundenplanAufsichtsbereich.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("kuerzel");

	private final ObjLongConsumer<DTOStundenplanAufsichtsbereich> initDTO = (dto, id) -> {
		dto.ID = id;
		dto.Stundenplan_ID = this.stundenplanID;
	};

	/**
	 * Fügt einen Aufsichtsbereich mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige CoreDTO
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
		return super.addBasic(is, DTOStundenplanAufsichtsbereich.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Fügt mehrere Aufsichtsbereiche mit den übergebenen JSON-Daten der Datenbank hinzu und gibt die zugehörigen CoreDTOs
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
		return super.addBasicMultiple(is, DTOStundenplanAufsichtsbereich.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht einen Aufsichtsbereich
	 *
	 * @param id   die ID des Aufsichtsbereichs
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		return super.deleteBasic(id, DTOStundenplanAufsichtsbereich.class, dtoMapper);
	}


	/**
	 * Löscht mehrere Aufsichtsbereiche
	 *
	 * @param ids   die IDs der Aufsichtsbereiche
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response deleteMultiple(final List<Long> ids) throws ApiOperationException {
		final List<DTOStundenplanAufsichtsbereich> dtos = conn.queryByKeyList(DTOStundenplanAufsichtsbereich.class, ids);
		for (final DTOStundenplanAufsichtsbereich dto : dtos)
			if (dto.Stundenplan_ID != this.stundenplanID)
				throw new ApiOperationException(Status.BAD_REQUEST, "Der Aufsichtsbereich-Eintrag gehört nicht zu dem angegebenen Stundenplan.");
		return super.deleteBasicMultiple(ids, DTOStundenplanAufsichtsbereich.class, dtoMapper);
	}

}
