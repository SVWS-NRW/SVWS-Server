package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schule.Raum;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanRaum;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtRaum;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanRaum}.
 */
public final class DataStundenplanRaeume extends DataManager<Long> {

	private Long stundenplanID = null;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanRaum}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Räume abgefragt werden
	 */
	public DataStundenplanRaeume(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStundenplanRaum} in einen Core-DTO {@link StundenplanRaum}.
	 */
	private static final DTOMapper<DTOStundenplanRaum, StundenplanRaum> dtoMapper = (final DTOStundenplanRaum r) -> {
		final StundenplanRaum daten = new StundenplanRaum();
		daten.id = r.ID;
		daten.kuerzel = r.Kuerzel;
		daten.beschreibung = r.Beschreibung;
		daten.groesse = r.Groesse;
		return daten;
	};


	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}


	/**
	 * Gibt die Räume des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Räume
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<StundenplanRaum> getRaeume(final @NotNull DBEntityManager conn, final long idStundenplan) throws ApiOperationException {
		final List<DTOStundenplanRaum> raeume = conn.queryList(DTOStundenplanRaum.QUERY_BY_STUNDENPLAN_ID, DTOStundenplanRaum.class, idStundenplan);
		final ArrayList<StundenplanRaum> daten = new ArrayList<>();
		for (final DTOStundenplanRaum r : raeume)
			daten.add(dtoMapper.apply(r));
		return daten;
	}


	/**
	 * Mappt die Stundenplanraeume auf die gegebenen UnterrichtIds
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 * @param unterrichtIds   die Unterrichte, für die die Räume gesucht und gemappt werden sollen
	 *
	 * @return eine Map, in der die Räume der jeweiligen UnterrichtId zugeordnet ist
	 * @throws ApiOperationException
	 */
	public static Map<Long, List<StundenplanRaum>> getRaeumeByUnterrichtId(final @NotNull DBEntityManager conn,
			final long idStundenplan, final List<Long> unterrichtIds) throws ApiOperationException {
		final Map<Long, StundenplanRaum> raumById = DataStundenplanRaeume.getRaeume(conn, idStundenplan).stream()
				.collect(Collectors.toMap(r -> r.id, Function.identity()));
		final Map<Long, List<StundenplanRaum>> raeumeByUnterrichtId = new HashMap<>();
		final List<DTOStundenplanUnterrichtRaum> listRaeume = unterrichtIds.isEmpty() ? new ArrayList<>()
				: conn.queryList(DTOStundenplanUnterrichtRaum.QUERY_LIST_BY_UNTERRICHT_ID, DTOStundenplanUnterrichtRaum.class, unterrichtIds);
		for (final DTOStundenplanUnterrichtRaum r : listRaeume) {
			final List<StundenplanRaum> raeume = raeumeByUnterrichtId.computeIfAbsent(r.Unterricht_ID, id -> new ArrayList<>());
			if (raumById.containsKey(r.Raum_ID))
				raeume.add(raumById.get(r.Raum_ID));
		}
		return raeumeByUnterrichtId;
	}


	/**
	 * Ermittelt zu Stundenplans mit der angegebenen ID und dem Raumkürzel den zugehörigen
	 * Raumeintrag in der Datenbank. Existiert ein solcher noch nicht, so wird ein neuer Raum mit Standardinformationen erzeugt.
	 *
	 * @param conn                die Datenbankverbindung
	 * @param idStundenplan       die ID des Stundenplans
	 * @param kuerzel             das Kürzel des Raums
	 *
	 * @return der Zeitrastereintrag
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static StundenplanRaum getOrCreateRaum(final @NotNull DBEntityManager conn, final long idStundenplan, final String kuerzel)
			throws ApiOperationException {
		final List<DTOStundenplanRaum> raeume = conn.queryList(
				"SELECT e FROM DTOStundenplanRaum e WHERE e.Stundenplan_ID = ?1 AND e.Kuerzel = ?2", DTOStundenplanRaum.class, idStundenplan, kuerzel);
		final DTOStundenplanRaum raum;
		if (raeume.isEmpty()) {
			final long id = conn.transactionGetNextID(DTOStundenplanRaum.class);
			raum = new DTOStundenplanRaum(id, idStundenplan, kuerzel, kuerzel, 30);
			conn.transactionPersist(raum);
			conn.transactionFlush();
			return dtoMapper.apply(raum);
		}
		if (raeume.size() == 1)
			return dtoMapper.apply(raeume.get(0));
		throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
				"Mehrfach-Einträge für das Kürzel %s im Stundenplan mit der ID %d.".formatted(kuerzel, idStundenplan));
	}


	@Override
	public Response getList() throws ApiOperationException {
		final List<StundenplanRaum> daten = getRaeume(conn, this.stundenplanID);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einem Raum eines Stundenplans mit der ID null ist unzulässig.");
		final DTOStundenplanRaum raum = conn.queryByKey(DTOStundenplanRaum.class, id);
		if (raum == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Raum eines Stundenplans mit der ID %d gefunden.".formatted(id));
		final StundenplanRaum daten = dtoMapper.apply(raum);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOStundenplanRaum>> patchMappings = Map.ofEntries(
			Map.entry("id", (conn, dto, value, map) -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}),
			Map.entry("kuerzel", (conn, dto, value, map) -> dto.Kuerzel = JSONMapper.convertToString(value, false, false, 20)),
			Map.entry("beschreibung", (conn, dto, value, map) -> dto.Beschreibung = JSONMapper.convertToString(value, false, true, 1000)),
			Map.entry("groesse", (conn, dto, value, map) -> dto.Groesse = JSONMapper.convertToInteger(value, false)));


	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOStundenplanRaum.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("kuerzel", "groesse");

	private final ObjLongConsumer<DTOStundenplanRaum> initDTO = (dto, id) -> {
		dto.ID = id;
		dto.Stundenplan_ID = this.stundenplanID;
	};

	/**
	 * Fügt einen Raum mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige CoreDTO
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
		return super.addBasic(is, DTOStundenplanRaum.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Fügt mehrere Räume mit den übergebenen JSON-Daten der Datenbank hinzu und gibt die zugehörigen CoreDTOs
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
		return super.addBasicMultiple(is, DTOStundenplanRaum.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Kopiert die Räume des allgemeinen Katalogs zu den Räumen des angegebenen Stundenplans hinzu. Dabei wird die
	 * angegebene Datenbankverbingung genutzt, welche eine offene Transaktion haben muss.
	 *
	 * @param conn             die Datenbankverbindung
	 * @param dtoStundenplan   das DTO des Stundenplans
	 * @param raeume           die hinzuzufügenden Räume
	 */
	public static void addRaeume(final @NotNull DBEntityManager conn, final DTOStundenplan dtoStundenplan, final List<Raum> raeume) {
		long id = conn.transactionGetNextID(DTOStundenplanRaum.class);
		for (final Raum raum : raeume)
			conn.transactionPersist(new DTOStundenplanRaum(id++, dtoStundenplan.ID, raum.kuerzel, raum.beschreibung, raum.groesse));
		conn.transactionFlush();
	}


	/**
	 * Löscht einen Raum
	 *
	 * @param id   die ID des Raums
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		return super.deleteBasic(id, DTOStundenplanRaum.class, dtoMapper);
	}


	/**
	 * Löscht mehrere Räume
	 *
	 * @param ids   die IDs der Räume
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response deleteMultiple(final List<Long> ids) throws ApiOperationException {
		final List<DTOStundenplanRaum> dtos = conn.queryByKeyList(DTOStundenplanRaum.class, ids);
		for (final DTOStundenplanRaum dto : dtos)
			if (dto.Stundenplan_ID != this.stundenplanID)
				throw new ApiOperationException(Status.BAD_REQUEST, "Der Raum-Eintrag gehört nicht zu dem angegebenen Stundenplan.");
		return super.deleteBasicMultiple(ids, DTOStundenplanRaum.class, dtoMapper);
	}

}
