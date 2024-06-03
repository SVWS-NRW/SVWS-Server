package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenaufsichten;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenaufsichtenBereiche;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenzeit;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link StundenplanPausenaufsicht}.
 */
public final class DataStundenplanPausenaufsichten extends DataManager<Long> {

	private final Long idStundenplan;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link StundenplanPausenaufsicht}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idStundenplan   die ID des Stundenplans, dessen Pausenaufsichten abgefragt werden
	 */
	public DataStundenplanPausenaufsichten(final DBEntityManager conn, final Long idStundenplan) {
		super(conn);
		this.idStundenplan = idStundenplan;
	}


	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}


	/**
	 * Ermittelt alle Stundenplan-Pausenaufsichten für den angebenenen Stundenplan aus der Datenbank.
	 *
	 * @param conn            die Datenbank-Verbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return eine Liste aller Pausenaufsichten des Stundenplans
	 */
	public static List<StundenplanPausenaufsicht> getAufsichten(final DBEntityManager conn, final long idStundenplan) {
		final List<StundenplanPausenaufsicht> daten = new ArrayList<>();
		// Bestimme die Pausenzeiten des Stundenplans
		final List<Long> pausenzeiten = conn.queryList(DTOStundenplanPausenzeit.QUERY_BY_STUNDENPLAN_ID, DTOStundenplanPausenzeit.class, idStundenplan)
				.stream().map(p -> p.ID).toList();
		if (pausenzeiten.isEmpty())
			return daten;
		// Bestimme die Aufsichten in dieser Pausenzeit
		final List<DTOStundenplanPausenaufsichten> dtoAufsichten = conn.queryList(DTOStundenplanPausenaufsichten.QUERY_LIST_BY_PAUSENZEIT_ID,
				DTOStundenplanPausenaufsichten.class, pausenzeiten);
		if (dtoAufsichten.isEmpty())
			return daten;
		// Bestimme die Zuordnung der Aufsichtsbereiche zu den Pausenaufsichten
		final Map<Long, List<DTOStundenplanPausenaufsichtenBereiche>> mapBereiche = conn.queryList(
				DTOStundenplanPausenaufsichtenBereiche.QUERY_LIST_BY_PAUSENAUFSICHT_ID, DTOStundenplanPausenaufsichtenBereiche.class,
				dtoAufsichten.stream().map(a -> a.ID).toList()).stream().collect(Collectors.groupingBy(b -> b.Pausenaufsicht_ID));
		for (final DTOStundenplanPausenaufsichten dtoAufsicht : dtoAufsichten) {
			final StundenplanPausenaufsicht aufsicht = new StundenplanPausenaufsicht();
			aufsicht.id = dtoAufsicht.ID;
			aufsicht.idPausenzeit = dtoAufsicht.Pausenzeit_ID;
			aufsicht.idLehrer = dtoAufsicht.Lehrer_ID;
			aufsicht.wochentyp = dtoAufsicht.Wochentyp;
			if (mapBereiche.containsKey(aufsicht.id))
				aufsicht.bereiche.addAll(mapBereiche.get(aufsicht.id).stream().map(b -> b.Aufsichtsbereich_ID).toList());
			daten.add(aufsicht);
		}
		return daten;
	}


	/**
	 * Ermittelt alle Stundenplan-Pausenaufsichten für den angegebenen Lehrer für den angebenenen
	 * Stundenplan aus der Datenbank.
	 *
	 * @param conn            die Datenbank-Verbindung
	 * @param idStundenplan   die ID des Stundenplans
	 * @param idLehrer        die ID des Lehrers
	 *
	 * @return eine Liste der Pausenaufsichten des Lehrers bei dem Stundenplan
	 */
	public static List<StundenplanPausenaufsicht> getAufsichtenVonLehrer(final DBEntityManager conn, final long idStundenplan, final long idLehrer) {
		return getAufsichten(conn, idStundenplan).stream().filter(a -> (a != null) && (a.idLehrer == idLehrer)).toList();
	}


	@Override
	public Response getList() throws ApiOperationException {
		if (idStundenplan == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einem Stundenplan mit der ID null ist unzulässig.");
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(idStundenplan));
		final List<StundenplanPausenaufsicht> daten = getAufsichten(conn, idStundenplan);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private StundenplanPausenaufsicht getAufsicht(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Pausenaufsicht mit der ID null ist unzulässig.");
		final DTOStundenplanPausenaufsichten dtoAufsicht = conn.queryByKey(DTOStundenplanPausenaufsichten.class, id);
		if (dtoAufsicht == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Pausenaufsicht mit der ID %d gefunden.".formatted(id));

		final List<Long> bereiche = conn.queryList(DTOStundenplanPausenaufsichtenBereiche.QUERY_BY_PAUSENAUFSICHT_ID,
				DTOStundenplanPausenaufsichtenBereiche.class, dtoAufsicht.ID).stream().map(b -> b.Aufsichtsbereich_ID).toList();
		final StundenplanPausenaufsicht daten = new StundenplanPausenaufsicht();
		daten.id = dtoAufsicht.ID;
		daten.idPausenzeit = dtoAufsicht.Pausenzeit_ID;
		daten.idLehrer = dtoAufsicht.Lehrer_ID;
		daten.wochentyp = dtoAufsicht.Wochentyp;
		daten.bereiche.addAll(bereiche);
		return daten;
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		final StundenplanPausenaufsicht daten = getAufsicht(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOStundenplanPausenaufsichten>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw new ApiOperationException(Status.BAD_REQUEST);
		}),
		Map.entry("idPausenzeit", (conn, dto, value, map) -> {
			final DTOStundenplanPausenzeit pzeit = conn.queryByKey(DTOStundenplanPausenzeit.class, value);
			if (pzeit == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Pausenzeit mit der ID %d nicht gefunden.".formatted((Long) value));
			dto.Pausenzeit_ID = pzeit.ID;
		}),
		Map.entry("idLehrer", (conn, dto, value, map) -> {
			final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, value);
			if (lehrer == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Lehrer mit der ID %d nicht gefunden.".formatted((Long) value));
			dto.Lehrer_ID = lehrer.ID;
		}),
		Map.entry("wochentyp", (conn, dto, value, map) -> dto.Wochentyp = JSONMapper.convertToInteger(value, false)),
		Map.entry("bereiche", (conn, dto, value, map) -> { /* Dies wird an anderer Stelle gehandhabt */	})
	);

	private void patchBereiche(final long id, final Map<String, Object> map) throws ApiOperationException {
		if (!map.containsKey("bereiche"))
			return;
		final List<Long> bereiche = JSONMapper.convertToListOfLong(map.get("bereiche"), false);
		// Entferne ggf. die alten Bereiche
		conn.transactionExecuteDelete("DELETE FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.Pausenaufsicht_ID = " + id);
		conn.transactionFlush();
		// Schreibe die neuen Bereiche
		long nextID = conn.transactionGetNextID(DTOStundenplanPausenaufsichtenBereiche.class);
		for (final Long bereich : bereiche)
			conn.transactionPersist(new DTOStundenplanPausenaufsichtenBereiche(nextID++, id, bereich));
		conn.transactionFlush();
	}


	private void patchInternal(final DTOStundenplanPausenaufsichten dto, final Map<String, Object> map) throws ApiOperationException {
		applyPatchMappings(conn, dto, map, patchMappings, null);
		// Persistiere das DTO in der Datenbank
		if (!conn.transactionPersist(dto))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		conn.transactionFlush();
		// Passe die Bereiche an
		patchBereiche(dto.ID, map);
	}


	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Ein Patch mit der ID null ist nicht möglich.");
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "In dem Patch sind keine Daten enthalten.");
		final DTOStundenplanPausenaufsichten dto = conn.queryByKey(DTOStundenplanPausenaufsichten.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		patchInternal(dto, map);
		return Response.status(Status.OK).build();
	}


	private static final Set<String> requiredCreateAttributes = Set.of("idPausenzeit", "idLehrer", "wochentyp");


	private final DTOMapper<DTOStundenplanPausenaufsichten, StundenplanPausenaufsicht> dtoMapper = (final DTOStundenplanPausenaufsichten u) -> getAufsicht(u.ID);


	private StundenplanPausenaufsicht addInternal(final long newID, final Map<String, Object> map) throws ApiOperationException {
		final DTOStundenplanPausenaufsichten dto = newDTO(DTOStundenplanPausenaufsichten.class, newID, (obj, id) -> obj.ID = id);
		patchInternal(dto, map);
		return getAufsicht(dto.ID);
	}


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
		final Map<String, Object> map = JSONMapper.toMap(is);
		for (final String attr : requiredCreateAttributes)
			if (!map.containsKey(attr))
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Attribut %s fehlt in der Anfrage".formatted(attr));
		// Erstelle das DTO und initialisiere es mit den übergeben Daten
		final StundenplanPausenaufsicht daten = addInternal(conn.transactionGetNextID(DTOStundenplanPausenaufsichten.class), map);
		return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(daten).build();
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
		final List<Map<String, Object>> multipleMaps = JSONMapper.toMultipleMaps(is);
		for (final Map<String, Object> map : multipleMaps)
			for (final String attr : requiredCreateAttributes)
				if (!map.containsKey(attr))
					throw new ApiOperationException(Status.BAD_REQUEST, "Das Attribut %s fehlt in der Anfrage bei mindestens einem Unterricht".formatted(attr));
		final List<StundenplanPausenaufsicht> daten = new ArrayList<>();
		long newID = conn.transactionGetNextID(DTOStundenplanPausenaufsichten.class);
		for (final Map<String, Object> map : multipleMaps)
			daten.add(addInternal(newID++, map));
		return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(daten).build();
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
		return super.deleteBasic(id, DTOStundenplanPausenaufsichten.class, dtoMapper);
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
		final List<Long> idsPausenzeiten = conn.queryByKeyList(DTOStundenplanPausenaufsichten.class, ids)
				.stream().map(p -> p.Pausenzeit_ID).toList();
		if (!idsPausenzeiten.isEmpty()) {
			final List<DTOStundenplanPausenzeit> dtos = conn.queryByKeyList(DTOStundenplanPausenzeit.class, idsPausenzeiten);
			for (final DTOStundenplanPausenzeit dto : dtos)
				if (dto.Stundenplan_ID != this.idStundenplan)
					throw new ApiOperationException(Status.BAD_REQUEST, "Der Pausenzeit-Eintrag einer Pausenaufsicht gehört nicht zu dem angegebenen Stundenplan.");
		}
		return super.deleteBasicMultiple(ids, DTOStundenplanPausenaufsichten.class, dtoMapper);
	}

}
