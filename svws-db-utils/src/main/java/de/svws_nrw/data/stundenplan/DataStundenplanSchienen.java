package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanSchienen;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtSchiene;
import de.svws_nrw.db.utils.ApiOperationException;
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
		final List<DTOStundenplanSchienen> schienen = conn.queryList(
				DTOStundenplanSchienen.QUERY_BY_STUNDENPLAN_ID, DTOStundenplanSchienen.class, idStundenplan);
		final ArrayList<StundenplanSchiene> daten = new ArrayList<>();
		for (final DTOStundenplanSchienen s : schienen)
			daten.add(dtoMapper.apply(s));
		return daten;
	}

	/**
	 * Mappt die Schienen eines Unterrichts auf die ID des Unterrichts anhand
	 * einer Liste von Unterricht-IDs
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die Id des Stundenplans
	 * @param unterrichtIds   die Ids der Unterrichte, für die die Schienen gesucht werden
	 *
	 * @return eine Map von StundenplanSchiene auf die entsprechenden UnterrichtIds
	 */
	public static Map<Long, List<StundenplanSchiene>> getSchienenByUnterrichtId(final @NotNull DBEntityManager conn,
			final long idStundenplan, final List<Long> unterrichtIds) {
		final Map<Long, StundenplanSchiene> schienenById = DataStundenplanSchienen.getSchienen(conn, idStundenplan)
				.stream().collect(Collectors.toMap(s -> s.id, Function.identity()));
		final Map<Long, List<StundenplanSchiene>> schienenByUnterrichtId = new HashMap<>();
		final List<DTOStundenplanUnterrichtSchiene> listSchienen = unterrichtIds.isEmpty() ? new ArrayList<>()
				: conn.queryList(DTOStundenplanUnterrichtSchiene.QUERY_LIST_BY_UNTERRICHT_ID, DTOStundenplanUnterrichtSchiene.class, unterrichtIds);
		for (final DTOStundenplanUnterrichtSchiene dtoSUS : listSchienen) {
			final List<StundenplanSchiene> schienen = schienenByUnterrichtId.computeIfAbsent(dtoSUS.Unterricht_ID, id -> new ArrayList<>());
			schienen.add(schienenById.get(dtoSUS.Schiene_ID));
		}
		return schienenByUnterrichtId;
	}


	@Override
	public Response getList() {
		final List<StundenplanSchiene> daten = getSchienen(conn, this.stundenplanID);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Schiene eines Stundenplans mit der ID null ist unzulässig.");
		final DTOStundenplanSchienen schiene = conn.queryByKey(DTOStundenplanSchienen.class, id);
		if (schiene == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Schiene eines Stundenplans mit der ID %d gefunden.".formatted(id));
		final StundenplanSchiene daten = dtoMapper.apply(schiene);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Liste die Schienen aus der Datenbank ein und erstellt eine HashMap2D, welche von der
	 * Schienen-Nummer (int) und der Jahrgangs-ID (long) auf das DTO für die Schiene abbildet.
	 *
	 * @param conn            die Datenbank-Verbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die HashMap2D
	 */
	public static @NotNull HashMap2D<Integer, Long, DTOStundenplanSchienen> getMapDTOs(final DBEntityManager conn, final long idStundenplan) {
		final List<DTOStundenplanSchienen> listSchienen =
				conn.queryList(DTOStundenplanSchienen.QUERY_BY_STUNDENPLAN_ID, DTOStundenplanSchienen.class, idStundenplan);
		final HashMap2D<Integer, Long, DTOStundenplanSchienen> result = new HashMap2D<>();
		for (final DTOStundenplanSchienen schiene : listSchienen)
			result.put(schiene.Nummer, schiene.Jahrgang_ID, schiene);
		return result;
	}


	/**
	 * Ermittelt die Schienen, welche in der Kursliste definiert sind und erzeugt für bisher
	 * in der übergebenen map nicht bestehende Einträge beim Stundenplan neue Einträge für den Stundenplan.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 * @param kurse           die Liste der Kurse
	 * @param mapDTOs         die map mit den bestehenden Einträgen, welche von der Schienen-Nummer (int)
	 *                        und der Jahrgangs-ID (long) auf das DTO für die Schiene abbildet
	 */
	public static void updateSchienenFromKurslisteInternal(final DBEntityManager conn, final Long idStundenplan, final List<KursDaten> kurse,
			final @NotNull HashMap2D<Integer, Long, DTOStundenplanSchienen> mapDTOs) {
		final Set<Pair<Long, Integer>> setJahrgangsSchienen = new HashSet<>();
		for (final KursDaten kurs : kurse)
			for (final long idJahrgang : kurs.idJahrgaenge)
				for (final int schiene : kurs.schienen)
					if (!mapDTOs.contains(schiene, idJahrgang))
						setJahrgangsSchienen.add(new Pair<>(idJahrgang, schiene));
		long id = conn.transactionGetNextID(DTOStundenplanSchienen.class);
		for (final Pair<Long, Integer> s : setJahrgangsSchienen) {
			final DTOStundenplanSchienen dto = new DTOStundenplanSchienen(id++, idStundenplan, s.b, "Schiene " + s.b);
			dto.Jahrgang_ID = s.a;
			conn.transactionPersist(dto);
		}
		conn.transactionFlush();
	}


	/**
	 * Ermittelt die Schienen, welche in der Kursliste definiert sind und erzeugt dafür
	 * Einträge für den Stundenplan.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 * @param kurse           die Liste der Kurse
	 */
	public static void addSchienenFromKursliste(final DBEntityManager conn, final Long idStundenplan, final List<KursDaten> kurse) {
		updateSchienenFromKurslisteInternal(conn, idStundenplan, kurse, new HashMap2D<>());
	}


	/**
	 * Ermittelt die Schienen, welche in der Kursliste definiert sind und erzeugt für bisher
	 * nicht bestehende Einträge beim Stundenplan neue Einträge für den Stundenplan.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 * @param kurse           die Liste der Kurse
	 */
	public static void updateSchienenFromKursliste(final DBEntityManager conn, final Long idStundenplan, final List<KursDaten> kurse) {
		updateSchienenFromKurslisteInternal(conn, idStundenplan, kurse, getMapDTOs(conn, idStundenplan));
	}


	private static final Map<String, DataBasicMapper<DTOStundenplanSchienen>> patchMappings = Map.ofEntries(
			Map.entry("id", (conn, dto, value, map) -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}),
			Map.entry("idJahrgang", (conn, dto, value, map) -> dto.Jahrgang_ID = JSONMapper.convertToLong(value, false)),
			Map.entry("nummer", (conn, dto, value, map) -> dto.Nummer = JSONMapper.convertToInteger(value, false)),
			Map.entry("bezeichnung", (conn, dto, value, map) -> dto.Bezeichnung = JSONMapper.convertToString(value, false, false, 100)));


	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOStundenplanSchienen.class, patchMappings);
	}

}
