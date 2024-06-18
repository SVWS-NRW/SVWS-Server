package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjLongConsumer;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenzeit;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenzeitKlassenzuordnung;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanPausenzeit}.
 */
public final class DataStundenplanPausenzeiten extends DataManager<Long> {

	private Long stundenplanID = null;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanPausenzeit}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Pausenzeiten abgefragt werden
	 */
	public DataStundenplanPausenzeiten(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStundenplanPausenzeit} in einen Core-DTO {@link StundenplanPausenzeit}.
	 */
	private static final DTOMapper<DTOStundenplanPausenzeit, StundenplanPausenzeit> dtoMapper = (final DTOStundenplanPausenzeit p) -> {
		final StundenplanPausenzeit daten = new StundenplanPausenzeit();
		daten.id = p.ID;
		daten.wochentag = p.Tag;
		daten.beginn = p.Beginn;
		daten.ende = p.Ende;
		daten.bezeichnung = p.Bezeichnung;
		return daten;
	};


	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}

	/**
	 * Gibt die Pausenzeiten des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Pausenzeiten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<StundenplanPausenzeit> getPausenzeiten(final @NotNull DBEntityManager conn, final long idStundenplan) throws ApiOperationException {
		final List<DTOStundenplanPausenzeit> pausenzeiten = conn.queryList(DTOStundenplanPausenzeit.QUERY_BY_STUNDENPLAN_ID,
				DTOStundenplanPausenzeit.class, idStundenplan);
		final List<Long> idsPausenzeiten = pausenzeiten.stream().map(p -> p.ID).toList();
		final Map<Long, List<Long>> mapKlassen = idsPausenzeiten.isEmpty() ? new HashMap<>()
				: conn.queryList(DTOStundenplanPausenzeitKlassenzuordnung.QUERY_LIST_BY_PAUSENZEIT_ID, DTOStundenplanPausenzeitKlassenzuordnung.class,
						idsPausenzeiten).stream()
						.collect(Collectors.groupingBy(pkz -> pkz.Pausenzeit_ID, Collectors.mapping(pkz -> pkz.Klassen_ID, Collectors.toUnmodifiableList())));
		final ArrayList<StundenplanPausenzeit> daten = new ArrayList<>();
		for (final DTOStundenplanPausenzeit p : pausenzeiten) {
			final StundenplanPausenzeit mapped = dtoMapper.apply(p);
			final List<Long> idsKlassen = mapKlassen.get(p.ID);
			if (idsKlassen != null)
				mapped.klassen.addAll(idsKlassen);
			daten.add(mapped);
		}
		return daten;
	}

	@Override
	public Response getList() throws ApiOperationException {
		final List<StundenplanPausenzeit> daten = getPausenzeiten(conn, this.stundenplanID);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Pausenzeit eines Stundenplans mit der ID null ist unzulässig.");
		final DTOStundenplanPausenzeit pausenzeit = conn.queryByKey(DTOStundenplanPausenzeit.class, id);
		if (pausenzeit == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Pausenzeit eines Stundenplans mit der ID %d gefunden.".formatted(id));
		final List<Long> klassen = conn.queryList(DTOStundenplanPausenzeitKlassenzuordnung.QUERY_BY_PAUSENZEIT_ID,
				DTOStundenplanPausenzeitKlassenzuordnung.class, id).stream().map(pkz -> pkz.Klassen_ID).toList();
		final StundenplanPausenzeit daten = dtoMapper.apply(pausenzeit);
		daten.klassen.addAll(klassen);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOStundenplanPausenzeit>> patchMappings = Map.ofEntries(
			Map.entry("id", (conn, dto, value, map) -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST, "Die IDs der Daten des Patches stimmen nicht mit der ID des Aufrufs überein.");
			}),
			Map.entry("wochentag", (conn, dto, value, map) -> dto.Tag = JSONMapper.convertToIntegerInRange(value, false, 1, 8)),
			Map.entry("beginn", (conn, dto, value, map) -> dto.Beginn = JSONMapper.convertToIntegerInRange(value, true, 0, 1440)),
			Map.entry("ende", (conn, dto, value, map) -> dto.Ende = JSONMapper.convertToIntegerInRange(value, true, 0, 1440)),
			Map.entry("bezeichnung", (conn, dto, value, map) -> dto.Bezeichnung = JSONMapper.convertToString(value, false, false, 40)),
			Map.entry("klassen", (conn, dto, value, map) -> {
				// Prüfe zunächst die übergebenen IDs, ob diese auch IDs von Klassen des Schuljahresabschnittes des Stundenplans sind
				final DTOStundenplan dtoStundenplan = conn.queryByKey(DTOStundenplan.class, dto.Stundenplan_ID);
				final List<Long> idsKlassen = JSONMapper.convertToListOfLong(value, false);
				final List<DTOKlassen> klassen = idsKlassen.isEmpty() ? new ArrayList<>() : conn.queryByKeyList(DTOKlassen.class, idsKlassen);
				if (idsKlassen.size() != klassen.size())
					throw new ApiOperationException(Status.BAD_REQUEST, "Nicht alle angegebenen Klassen-IDs lassen sich Klassen zuordnen");
				final List<DTOKlassen> klassenVonStundenplan =
						klassen.stream().filter(k -> k.Schuljahresabschnitts_ID == dtoStundenplan.Schuljahresabschnitts_ID).toList();
				if (klassen.size() != klassenVonStundenplan.size())
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Nicht alle angegebenen Klassen-IDs gehören zu Klassen des Schuljahresabschnittes des Stundenplans");
				// Bestimme die bereits existierenden Klasseneinträge und vergleiche diese mit den geforderten
				final List<DTOStundenplanPausenzeitKlassenzuordnung> existing = conn.queryList(DTOStundenplanPausenzeitKlassenzuordnung.QUERY_BY_PAUSENZEIT_ID,
						DTOStundenplanPausenzeitKlassenzuordnung.class, dto.ID);
				final Map<Long, DTOStundenplanPausenzeitKlassenzuordnung> mapExisting =
						existing.stream().collect(Collectors.toMap(pkl -> pkl.Klassen_ID, pkl -> pkl));
				final Set<Long> idsKlassenNeu = new HashSet<>();
				final Set<Long> idsKlassenVorhanden = new HashSet<>();
				for (final long idKlasse : idsKlassen) {
					if (mapExisting.keySet().contains(idKlasse))
						idsKlassenVorhanden.add(idKlasse);
					else
						idsKlassenNeu.add(idKlasse);
				}
				// Entferne die alten Klassenzuordnungen
				mapExisting.keySet().removeAll(idsKlassenVorhanden);
				for (final DTOStundenplanPausenzeitKlassenzuordnung pkl : mapExisting.values())
					conn.transactionRemove(pkl);
				// Erzeuge die neuen Klassenzuordnungen
				long idNext = conn.transactionGetNextID(DTOStundenplanPausenzeitKlassenzuordnung.class);
				for (final long idKlasse : idsKlassenNeu)
					conn.transactionPersist(new DTOStundenplanPausenzeitKlassenzuordnung(idNext++, dto.ID, idKlasse));
				conn.transactionFlush();
			}));

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOStundenplanPausenzeit.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("wochentag", "beginn", "ende");

	private final ObjLongConsumer<DTOStundenplanPausenzeit> initDTO = (dto, id) -> {
		dto.ID = id;
		dto.Stundenplan_ID = this.stundenplanID;
	};

	/**
	 * Fügt eine Pausenzeit mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige CoreDTO
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
		return super.addBasic(is, DTOStundenplanPausenzeit.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Fügt mehrere Pausenzeiten mit den übergebenen JSON-Daten der Datenbank hinzu und gibt die zugehörigen CoreDTOs
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
		return super.addBasicMultiple(is, DTOStundenplanPausenzeit.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht eine Pausenzeit
	 *
	 * @param id   die ID der Pausenzeit
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		return super.deleteBasic(id, DTOStundenplanPausenzeit.class, dtoMapper);
	}


	/**
	 * Löscht mehrere Pausenzeiten
	 *
	 * @param ids   die IDs der Pausenzeiten
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response deleteMultiple(final List<Long> ids) throws ApiOperationException {
		if (ids.isEmpty())
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(new ArrayList<>()).build();
		final List<DTOStundenplanPausenzeit> dtos = conn.queryByKeyList(DTOStundenplanPausenzeit.class, ids);
		for (final DTOStundenplanPausenzeit dto : dtos)
			if (dto.Stundenplan_ID != this.stundenplanID)
				throw new ApiOperationException(Status.BAD_REQUEST, "Der Pausenzeit-Eintrag gehört nicht zu dem angegebenen Stundenplan.");
		return super.deleteBasicMultiple(ids, DTOStundenplanPausenzeit.class, dtoMapper);
	}

}
