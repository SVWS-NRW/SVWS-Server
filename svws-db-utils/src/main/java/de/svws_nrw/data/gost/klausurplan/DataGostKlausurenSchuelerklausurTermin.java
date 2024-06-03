package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenDataCollection;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostNachschreibterminblockungKonfiguration;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurterminraumstunde;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurvorgabenManager;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKursklausurManager;
import de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungNachschreiberAlgorithmus;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermineRaumstunden;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurraumstunde}.
 */
public final class DataGostKlausurenSchuelerklausurTermin extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurraumstunde}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenSchuelerklausurTermin(final DBEntityManager conn) {
		super(conn);
	}

	private static final Set<String> patchForbiddenAttributes = Set.of("idSchuelerklausur");

	private final Map<String, DataBasicMapper<DTOGostKlausurenSchuelerklausurenTermine>> patchMappings =
			Map.ofEntries(
				Map.entry("id", (conn, dto, value, map) -> {
					final Long patch_id = JSONMapper.convertToLong(value, false);
					if ((patch_id == null) || (patch_id.longValue() != dto.ID))
						throw new ApiOperationException(Status.BAD_REQUEST);
				}),
				Map.entry("idSchuelerklausur", (conn, dto, value, map) -> dto.Schuelerklausur_ID = JSONMapper.convertToLong(value, false)),
				Map.entry("idTermin", (conn, dto, value, map) -> dto.Termin_ID = JSONMapper.convertToLong(value, true)),
				Map.entry("startzeit", (conn, dto, value, map) -> dto.Startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440)),
				Map.entry("bemerkung", (conn, dto, value, map) -> dto.Bemerkungen = JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Klausuren_Schuelerklausuren_Termine.col_Bemerkungen.datenlaenge()))
			);

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenSchuelerklausuren} in einen Core-DTO
	 * {@link GostSchuelerklausur}.
	 */
	public static final DTOMapper<DTOGostKlausurenSchuelerklausurenTermine, GostSchuelerklausurTermin> dtoMapper = (final DTOGostKlausurenSchuelerklausurenTermine skt) -> {
		final GostSchuelerklausurTermin daten = new GostSchuelerklausurTermin();
		daten.id = skt.ID;
		daten.idSchuelerklausur = skt.Schuelerklausur_ID;
		daten.folgeNr = skt.Folge_Nr;
		daten.idTermin = skt.Termin_ID;
		daten.startzeit = skt.Startzeit;
		daten.bemerkung = skt.Bemerkungen;
		return daten;
	};

	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response get(final Long idTermin) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasicFiltered(id, is, DTOGostKlausurenSchuelerklausurenTermine.class, patchMappings, patchForbiddenAttributes);
	}

	/**
	 * Erstellt einen neuen Gost-Schuelerklausurtermin
	 *
	 * @param id   die ID der Schülerklausur
	 *
	 * @return Eine Response mit dem neuen Gost-Klausurtermin
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response create(final long id) throws ApiOperationException {
		final DTOGostKlausurenSchuelerklausurenTermine lastTermin = conn.query("SELECT skt FROM DTOGostKlausurenSchuelerklausurenTermine skt WHERE skt.Schuelerklausur_ID = :skid ORDER BY skt.Folge_Nr DESC", DTOGostKlausurenSchuelerklausurenTermine.class)
				.setParameter("skid", id)
				.setMaxResults(1)
				.getSingleResult();
		final List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> raumstunden = conn.queryList(
				DTOGostKlausurenSchuelerklausurenTermineRaumstunden.QUERY_BY_SCHUELERKLAUSURTERMIN_ID,
				DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class, lastTermin.ID);
		conn.transactionRemoveAll(raumstunden);
		final DTOGostKlausurenSchuelerklausurenTermine newTermin = new DTOGostKlausurenSchuelerklausurenTermine(
				conn.transactionGetNextID(DTOGostKlausurenSchuelerklausurenTermine.class), id, lastTermin.Folge_Nr + 1);
		conn.transactionPersist(newTermin);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(dtoMapper.apply(newTermin)).build();
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausuren gehörigen
	 * GostSchuelerklausurtermin-Objekte zurück.
	 *
	 * @param conn    x
	 * @param klausuren die Liste der GostSchuelerklausuren
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurtermin-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostSchuelerklausurTermin> getSchuelerklausurtermineZuSchuelerklausuren(final DBEntityManager conn, final List<GostSchuelerklausur> klausuren) throws ApiOperationException {
		return getSchuelerklausurtermineZuSchuelerklausurids(conn, klausuren.stream().map(sk -> sk.id).toList());
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausuren-IDs gehörigen
	 * GostSchuelerklausurtermin-Objekte zurück.
	 *
	 * @param conn    x
	 * @param listSkIds die Liste der GostSchuelerklausuren-IDs
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurtermin-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostSchuelerklausurTermin> getSchuelerklausurtermineZuSchuelerklausurids(final DBEntityManager conn, final List<Long> listSkIds) throws ApiOperationException {
		if (listSkIds.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenSchuelerklausurenTermine> terminDTOs = conn.queryList(
				DTOGostKlausurenSchuelerklausurenTermine.QUERY_LIST_BY_SCHUELERKLAUSUR_ID, DTOGostKlausurenSchuelerklausurenTermine.class, listSkIds);
		return DTOMapper.mapList(terminDTOs, dtoMapper);
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurtermin-IDs gehörigen
	 * GostSchuelerklausurtermin-Objekte zurück.
	 *
	 * @param conn    x
	 * @param listSkIds die Liste der GostSchuelerklausurtermin-IDs
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurtermin-Objekte
	 * @throws ApiOperationException
	 */
	public static List<GostSchuelerklausurTermin> getSchuelerklausurtermineZuSchuelerklausurterminids(final DBEntityManager conn, final List<Long> listSkIds) throws ApiOperationException {
		if (listSkIds.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenSchuelerklausurenTermine> terminDTOs = conn.queryByKeyList(DTOGostKlausurenSchuelerklausurenTermine.class, listSkIds);
		return DTOMapper.mapList(terminDTOs, dtoMapper);
	}

	/**
	 * Liefert die zu einer Liste von Klausurtermin-IDs gehörigen
	 * GostSchuelerklausurtermin-Objekte zurück.
	 *
	 * @param conn    x
	 * @param listTerminIds die Liste der GostSchuelerklausurtermin-IDs
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurtermin-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostSchuelerklausurTermin> getSchuelerklausurtermineZuTerminids(final DBEntityManager conn, final List<Long> listTerminIds) throws ApiOperationException {
		if (listTerminIds.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenSchuelerklausurenTermine> terminDTOs = conn.queryList(
				DTOGostKlausurenSchuelerklausurenTermine.QUERY_LIST_BY_TERMIN_ID, DTOGostKlausurenSchuelerklausurenTermine.class, listTerminIds);
		return DTOMapper.mapList(terminDTOs, dtoMapper);
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminraumstunden gehörigen
	 * GostSchuelerklausurtermin-Objekte zurück.
	 *
	 * @param conn    x
	 * @param listSktrs die Liste der GostSchuelerklausurterminraumstunden
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausurtermin-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostSchuelerklausurTermin> getSchuelerklausurtermineZuSchuelerklausurterminraumstunden(final DBEntityManager conn, final List<GostSchuelerklausurterminraumstunde> listSktrs) throws ApiOperationException {
		if (listSktrs.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenSchuelerklausurenTermine> terminDTOs = conn.queryByKeyList(DTOGostKlausurenSchuelerklausurenTermine.class,
				listSktrs.stream().map(skrs -> skrs.idSchuelerklausurtermin).distinct().toList());
		return DTOMapper.mapList(terminDTOs, dtoMapper);
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen gehörigen
	 * Datenbank-DTO-Objekte zurück.
	 *
	 * @param conn    x
	 * @param listSkts die Liste der GostSchuelerklausurtermine
	 *
	 * @return die Liste der zugehörigen Datenbank-DTOs
	 */
	public static List<DTOGostKlausurenSchuelerklausurenTermine> getSchuelerklausurterminDTOsZuSchuelerklausurterminen(final DBEntityManager conn, final List<GostSchuelerklausurTermin> listSkts) {
		if (listSkts.isEmpty())
			return new ArrayList<>();
		return conn.queryByKeyList(DTOGostKlausurenSchuelerklausurenTermine.class, listSkts.stream().map(skt -> skt.id).toList());
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen gehörigen
	 * Datenbank-DTO-Objekte zurück.
	 *
	 * @param conn    x
	 * @param listIds die Liste der IDs der GostSchuelerklausurtermine
	 *
	 * @return die Liste der zugehörigen Datenbank-DTOs
	 */
	public static List<DTOGostKlausurenSchuelerklausurenTermine> getSchuelerklausurterminDTOsById(final DBEntityManager conn, final List<Long> listIds) {
		if (listIds.isEmpty())
			return new ArrayList<>();
		return conn.queryByKeyList(DTOGostKlausurenSchuelerklausurenTermine.class, listIds);
	}

	/**
	 * Löscht den angegebenen Gost-Schuelerklausurtermin
	 *
	 * @param idSkt   die ID des zu löschenden Schülerklausurtermins
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response delete(final long idSkt) throws ApiOperationException {
		return super.deleteBasic(idSkt, DTOGostKlausurenSchuelerklausurenTermine.class, dtoMapper);
	}

	/**
	 * Startet den NachschreibterminblockungAlgorithmus mit den übergebenen
	 * GostKlausurenDataCollection-Daten und persistiert die Blockung in der Datenbank.
	 *
	 * @param conn          Connection
	 * @param config das GostNachschreibterminblockungKonfiguration-Objekt
	 *
	 * @return das GostKlausurenDataCollection mit der persistierten Blockung
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostKlausurenDataCollection blocken(final DBEntityManager conn, final GostNachschreibterminblockungKonfiguration config) throws ApiOperationException {
		final List<GostSchuelerklausurTermin> listSktsManager = new ArrayList<>();
		listSktsManager.addAll(config.schuelerklausurtermine);
		listSktsManager.addAll(DataGostKlausurenSchuelerklausur.getSchuelerKlausurenZuTerminIds(conn, config.termine.stream().map(t -> t.id).toList()));

		final List<GostSchuelerklausur> listSks = DataGostKlausurenSchuelerklausur.getSchuelerklausurenZuSchuelerklausurterminen(conn, listSktsManager);
		final List<GostKursklausur> listKks = DataGostKlausurenKursklausur.getKursklausurenZuSchuelerklausuren(conn, listSks);

		final GostKlausurvorgabenManager vMan = new GostKlausurvorgabenManager(DataGostKlausurenVorgabe.getKlausurvorgabenZuKursklausuren(conn, listKks));
		final GostKursklausurManager kMan = new GostKursklausurManager(vMan, listKks, config.termine, listSks, listSktsManager);

		final KlausurblockungNachschreiberAlgorithmus blockAlgo = new KlausurblockungNachschreiberAlgorithmus();

		final List<Pair<GostSchuelerklausurTermin, Long>> blockung = blockAlgo.berechne(config, kMan);

		final Map<Long, DTOGostKlausurenSchuelerklausurenTermine> mapNachschreiber = getSchuelerklausurterminDTOsZuSchuelerklausurterminen(conn, config.schuelerklausurtermine).stream().collect(Collectors.toMap(skt -> skt.ID, skt -> skt));
		final Map<Long, DTOGostKlausurenTermine> mapNeueTermine = new HashMap<>();

		long idNextTermin = conn.transactionGetNextID(DTOGostKlausurenTermine.class);
		for (final Pair<GostSchuelerklausurTermin, Long> zuordnung: blockung) {
			final DTOGostKlausurenSchuelerklausurenTermine dtoSkt = DeveloperNotificationException.ifMapGetIsNull(mapNachschreiber, zuordnung.a.id);
			if (zuordnung.b >= 0) {
				dtoSkt.Termin_ID = zuordnung.b;
			} else {
				final GostKlausurvorgabe v = kMan.vorgabeBySchuelerklausurTermin(zuordnung.a);
				DTOGostKlausurenTermine neuerTermin = mapNeueTermine.get(zuordnung.b);
				if (neuerTermin == null) {
					neuerTermin = new DTOGostKlausurenTermine(idNextTermin++, v.abiJahrgang, GostHalbjahr.fromIDorException(v.halbjahr), v.quartal, false, true);
					conn.transactionPersist(neuerTermin);
					conn.transactionFlush();
					mapNeueTermine.put(zuordnung.b, neuerTermin);
				}
				if (neuerTermin.Quartal != v.quartal) // mehrer Klausurquartale im neuen Termin gemischt
					neuerTermin.Quartal = 0;
				dtoSkt.Termin_ID = neuerTermin.ID;
			}

		}
		conn.transactionPersistAll(mapNachschreiber.values());

		final GostKlausurenDataCollection blockungsDaten = new GostKlausurenDataCollection();
		blockungsDaten.schuelerklausurtermine = DTOMapper.mapList(mapNachschreiber.values(), dtoMapper);
		blockungsDaten.termine = DTOMapper.mapList(mapNeueTermine.values(), DataGostKlausurenTermin.dtoMapper);
		return blockungsDaten;
	}


}
