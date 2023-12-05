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

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.kurse.KursListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanSchienen;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtSchiene;
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
				: conn.queryNamed("DTOStundenplanUnterrichtSchiene.unterricht_id.multiple", unterrichtIds, DTOStundenplanUnterrichtSchiene.class);
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
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einer Schiene eines Stundenplans mit der ID null ist unzulässig.");
		final DTOStundenplanSchienen schiene = conn.queryByKey(DTOStundenplanSchienen.class, id);
		if (schiene == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde keine Schiene eines Stundenplans mit der ID %d gefunden.".formatted(id));
		final StundenplanSchiene daten = dtoMapper.apply(schiene);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Ermittel die Schienen, welche in der Kursliste definiert sind und erzeugt dafür Einträge für den Stundenplan.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 * @param kurse           die Liste der Kurse
	 */
	public static void addSchienenFromKursliste(final DBEntityManager conn, final Long idStundenplan, final List<KursListeEintrag> kurse) {
		final Set<Pair<Long, Integer>> setJahrgangsSchienen = new HashSet<>();
		for (final KursListeEintrag kurs : kurse)
			for (final long idJahrgang : kurs.idJahrgaenge)
				for (final int schiene : kurs.schienen)
					setJahrgangsSchienen.add(new Pair<>(idJahrgang, schiene));
		long id = conn.transactionGetNextID(DTOStundenplanSchienen.class);
		for (final Pair<Long, Integer> s : setJahrgangsSchienen) {
			final DTOStundenplanSchienen dto = new DTOStundenplanSchienen(id++, idStundenplan, s.b, "Schiene " + s.b);
			dto.Jahrgang_ID = s.a;
			conn.transactionPersist(dto);
		}
		conn.transactionFlush();
	}


	private static final Map<String, DataBasicMapper<DTOStundenplanSchienen>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("idJahrgang", (conn, dto, value, map) -> dto.Jahrgang_ID = JSONMapper.convertToLong(value, false)),
		Map.entry("nummer", (conn, dto, value, map) -> dto.Nummer = JSONMapper.convertToInteger(value, false)),
		Map.entry("bezeichnung", (conn, dto, value, map) -> dto.Bezeichnung = JSONMapper.convertToString(value, false, false, 100))
	);


	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOStundenplanSchienen.class, patchMappings);
	}

}
