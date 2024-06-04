package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsichtBereich;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanAufsichtsbereich;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenaufsichten;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenaufsichtenBereiche;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link StundenplanPausenaufsichtBereich}.
 */
public final class DataStundenplanPausenaufsichtenBereich extends DataManager<Long> {

	private final Long idStundenplan;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link StundenplanPausenaufsichtBereich}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idStundenplan   die ID des Stundenplans, dessen Pausenaufsichten abgefragt werden
	 */
	public DataStundenplanPausenaufsichtenBereich(final DBEntityManager conn, final Long idStundenplan) {
		super(conn);
		this.idStundenplan = idStundenplan;
	}


	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStundenplanPausenaufsichtenBereiche} in
	 * einen Core-DTO {@link StundenplanPausenaufsichtBereich}.
	 */
	public static final DTOMapper<DTOStundenplanPausenaufsichtenBereiche, StundenplanPausenaufsichtBereich> dtoMapper = (final DTOStundenplanPausenaufsichtenBereiche dto) -> {
		final StundenplanPausenaufsichtBereich daten = new StundenplanPausenaufsichtBereich();
		daten.id = dto.ID;
		daten.idAufsichtsbereich = dto.Aufsichtsbereich_ID;
		daten.idPausenaufsicht = dto.Pausenaufsicht_ID;
		daten.wochentyp = dto.Wochentyp;
		return daten;
	};


	@Override
	public Response getList() throws ApiOperationException {
		throw new UnsupportedOperationException();
	}


	private StundenplanPausenaufsichtBereich getZuordnung(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Zuordnung mit der ID null ist unzulässig.");
		final DTOStundenplanPausenaufsichtenBereiche dto = conn.queryByKey(DTOStundenplanPausenaufsichtenBereiche.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Zuordnung mit der ID %d gefunden.".formatted(id));
		return dtoMapper.apply(dto);
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		final StundenplanPausenaufsichtBereich daten = getZuordnung(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOStundenplanPausenaufsichtenBereiche>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw new ApiOperationException(Status.BAD_REQUEST);
		}),
		Map.entry("idPausenaufsicht", (conn, dto, value, map) -> {
			final DTOStundenplanPausenaufsichten paufsicht = conn.queryByKey(DTOStundenplanPausenaufsichten.class, value);
			if (paufsicht == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Pausenaufsicht mit der ID %d nicht gefunden.".formatted((Long) value));
			dto.Pausenaufsicht_ID = paufsicht.ID;
		}),
		Map.entry("idAufsichtsbereich", (conn, dto, value, map) -> {
			final DTOStundenplanAufsichtsbereich pbereich = conn.queryByKey(DTOStundenplanAufsichtsbereich.class, value);
			if (pbereich == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Aufsichtsbereich mit der ID %d nicht gefunden.".formatted((Long) value));
			dto.Aufsichtsbereich_ID = pbereich.ID;
		}),
		Map.entry("wochentyp", (conn, dto, value, map) -> dto.Wochentyp = JSONMapper.convertToInteger(value, false))
	);

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOStundenplanPausenaufsichtenBereiche.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("idPausenaufsicht", "idAufsichtsbereich", "wochentyp");

	private final ObjLongConsumer<DTOStundenplanPausenaufsichtenBereiche> initDTO = (dto, id) -> dto.ID = id;

	/**
	 * Fügt eine Pausenaufsicht mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige Core-DTO
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response add(final InputStream is) throws ApiOperationException {
		DataStundenplan.getDTOStundenplan(conn, idStundenplan);   // Prüfe, on der Stundenplan existiert
		return super.addBasic(is, DTOStundenplanPausenaufsichtenBereiche.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Fügt die Pausenaufsichten mit den übergebenen JSON-Daten der Datenbank hinzu und gibt die zugehörigen Core-DTOs
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response addMultiple(final InputStream is) throws ApiOperationException {
		DataStundenplan.getDTOStundenplan(conn, idStundenplan);   // Prüfe, on der Stundenplan existiert
		return super.addBasicMultiple(is, DTOStundenplanPausenaufsichtenBereiche.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}

	/**
	 * Löscht eine Pausenaufsicht
	 *
	 * @param id   die ID der Pausenaufsicht
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		return super.deleteBasic(id, DTOStundenplanPausenaufsichtenBereiche.class, dtoMapper);
	}


	/**
	 * Löscht mehrere Pausenaufsichten
	 *
	 * @param ids   die IDs der Pausenaufsichten
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response deleteMultiple(final List<Long> ids) throws ApiOperationException {
		final List<DTOStundenplanPausenaufsichtenBereiche> dtos = conn.queryByKeyList(DTOStundenplanPausenaufsichtenBereiche.class, ids);
		if (dtos.size() != ids.size())
			throw new ApiOperationException(Status.BAD_REQUEST, "Nicht alle Zuordnungen konnten anhand der übergebenen IDs in der DB gefunden werden.");
		// TODO Prüfe, ob die Bereiche auch alle zu dem Stundenplan gehören
		return super.deleteBasicMultiple(ids, DTOStundenplanPausenaufsichtenBereiche.class, dtoMapper);
	}

}
