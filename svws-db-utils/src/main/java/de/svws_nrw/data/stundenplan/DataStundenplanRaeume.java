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

import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanRaum;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtRaum;
import de.svws_nrw.db.utils.OperationError;
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
	private static final Function<DTOStundenplanRaum, StundenplanRaum> dtoMapper = (final DTOStundenplanRaum r) -> {
		final StundenplanRaum daten = new StundenplanRaum();
		daten.id = r.ID;
		daten.kuerzel = r.Kuerzel;
		daten.beschreibung = r.Beschreibung;
		daten.groesse = r.Groesse;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}


	/**
	 * Gibt die Räume des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Räume
	 */
	public static List<StundenplanRaum> getRaeume(final @NotNull DBEntityManager conn, final long idStundenplan) {
		final List<DTOStundenplanRaum> raeume = conn.queryNamed("DTOStundenplanRaum.stundenplan_id", idStundenplan, DTOStundenplanRaum.class);
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
	 */
	public static Map<Long, List<StundenplanRaum>> getRaeumeByUnterrichtId(final @NotNull DBEntityManager conn,
			final long idStundenplan, final List<Long> unterrichtIds) {
		final Map<Long, StundenplanRaum> raumById = DataStundenplanRaeume.getRaeume(conn, idStundenplan).stream()
				.collect(Collectors.toMap(r -> r.id, Function.identity()));
		final Map<Long, List<StundenplanRaum>> raeumeByUnterrichtId = new HashMap<>();
		final List<DTOStundenplanUnterrichtRaum> listRaeume = unterrichtIds.isEmpty() ? new ArrayList<>()
				: conn.queryNamed("DTOStundenplanUnterrichtRaum.unterricht_id.multiple", unterrichtIds, DTOStundenplanUnterrichtRaum.class);
		for (final DTOStundenplanUnterrichtRaum r : listRaeume) {
			final List<StundenplanRaum> raeume = raeumeByUnterrichtId.computeIfAbsent(r.Unterricht_ID, id -> new ArrayList<>());
			if (raumById.containsKey(r.Raum_ID))
				raeume.add(raumById.get(r.Raum_ID));
		}
		return raeumeByUnterrichtId;
	}


	@Override
	public Response getList() {
		final List<StundenplanRaum> daten = getRaeume(conn, this.stundenplanID);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Raum eines Stundenplans mit der ID null ist unzulässig.");
		final DTOStundenplanRaum raum = conn.queryByKey(DTOStundenplanRaum.class, id);
		if (raum == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde kein Raum eines Stundenplans mit der ID %d gefunden.".formatted(id));
		final StundenplanRaum daten = dtoMapper.apply(raum);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOStundenplanRaum>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("kuerzel", (conn, dto, value, map) -> dto.Kuerzel = JSONMapper.convertToString(value, false, false, 20)),
		Map.entry("beschreibung", (conn, dto, value, map) -> dto.Beschreibung = JSONMapper.convertToString(value, false, true, 1000)),
		Map.entry("groesse", (conn, dto, value, map) -> dto.Groesse = JSONMapper.convertToInteger(value, false))
	);


	@Override
	public Response patch(final Long id, final InputStream is) {
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
	 */
	public Response add(final InputStream is) {
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
	 */
	public Response addMultiple(final InputStream is) {
		DataStundenplan.getDTOStundenplan(conn, stundenplanID);   // Prüfe, on der Stundenplan existiert
		return super.addBasicMultiple(is, DTOStundenplanRaum.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht einen Raum
	 *
	 * @param id   die ID des Raums
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
		return super.deleteBasic(id, DTOStundenplanRaum.class, dtoMapper);
	}


	/**
	 * Löscht mehrere Räume
	 *
	 * @param ids   die IDs der Räume
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response deleteMultiple(final List<Long> ids) {
		final List<DTOStundenplanRaum> dtos = conn.queryNamed("DTOStundenplanRaum.primaryKeyQuery.multiple", ids, DTOStundenplanRaum.class);
		for (final DTOStundenplanRaum dto : dtos)
			if (dto.Stundenplan_ID != this.stundenplanID)
				throw OperationError.BAD_REQUEST.exception("Der Raum-Eintrag gehört nicht zu dem angegebenen Stundenplan.");
		return super.deleteBasicMultiple(ids, DTOStundenplanRaum.class, dtoMapper);
	}

}
