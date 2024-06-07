package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrs;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenDataCollection;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungDaten;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnis;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnisTermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausurRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurvorgabenManager;
import de.svws_nrw.core.utils.gost.klausurplanung.KlausurterminblockungAlgorithmus;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenKursklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenVorgaben;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKursklausur}.
 */
public final class DataGostKlausurenKursklausur extends DataManager<Long> {

	private long _idSchuljahresAbschnitt = -1;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKursklausur}.
	 *
	 * @param conn                   die Datenbank-Verbindung für den
	 *                               Datenbankzugriff
	 * @param abiturjahr             das Jahr, in welchem der Jahrgang Abitur machen
	 *                               wird
	 * @param idSchuljahresAbschnitt die ID des Schuljahresabschnitts. Wird nur
	 *                               gebraucht, falls die Startzeit der Klausur
	 *                               geändert werden muss.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public DataGostKlausurenKursklausur(final DBEntityManager conn, final int abiturjahr, final long idSchuljahresAbschnitt) throws ApiOperationException {
		super(conn);
		if ((abiturjahr != -1) && (conn.queryByKey(DTOGostJahrgangsdaten.class, abiturjahr) == null))
			throw new ApiOperationException(Status.BAD_REQUEST, "Jahrgang nicht gefunden, ID: " + abiturjahr);
		_idSchuljahresAbschnitt = idSchuljahresAbschnitt;
		if ((idSchuljahresAbschnitt != -1) && (conn.queryByKey(DTOSchuljahresabschnitte.class, idSchuljahresAbschnitt) == null))
			throw new ApiOperationException(Status.BAD_REQUEST, "Schuljahresabschnitt nicht gefunden, ID: " + idSchuljahresAbschnitt);
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr das Gost-Halbjahr
	 * @param ganzesSchuljahr true, um Klausuren für das gesamte Schuljahr zu erhalten, false nur für das übergeben Halbjahr
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKursklausur> getKursKlausuren(final DBEntityManager conn, final int abiturjahr, final int halbjahr, final boolean ganzesSchuljahr)
			throws ApiOperationException {
		return getKursklausurenZuVorgaben(conn, DataGostKlausurenVorgabe.getKlausurvorgaben(conn, abiturjahr, halbjahr, ganzesSchuljahr));
	}

	/**
	 * Gibt die Liste der Kursklausuren zu den übergebenen Klausurvorgaben zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param vorgaben die Liste der Klausurvorgaben, zu denen die Kursklausuren gesucht werden.
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKursklausur> getKursklausurenZuVorgaben(final DBEntityManager conn, final List<GostKlausurvorgabe> vorgaben)
			throws ApiOperationException {
		if (vorgaben.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenKursklausuren> kursKlausurDTOs = conn.queryList(DTOGostKlausurenKursklausuren.QUERY_LIST_BY_VORGABE_ID,
				DTOGostKlausurenKursklausuren.class, vorgaben.stream().map(v -> v.idVorgabe).toList());
		return DTOMapper.mapList(kursKlausurDTOs, dtoMapper);
	}

	/**
	 * Gibt die Liste der Kursklausuren zur übergeben Termin-ID zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idTermin 	 die ID des Klausurtermins
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKursklausur> getKursklausurenZuTerminid(final DBEntityManager conn, final long idTermin) throws ApiOperationException {
		return getKursklausurenZuTerminids(conn, ListUtils.create1(idTermin));
	}

	/**
	 * Gibt die Liste der Kursklausuren zur übergeben Termin-ID zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idsTermin 	 die ID des Klausurtermins
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKursklausur> getKursklausurenZuTerminids(final DBEntityManager conn, final List<Long> idsTermin) throws ApiOperationException {
		for (final long idTermin : idsTermin)
			if (DataGostKlausurenTermin.getKlausurterminZuId(conn, idTermin) == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Klausurtermin mit ID %d existiert nicht.".formatted(idTermin));
		final List<DTOGostKlausurenKursklausuren> kursKlausurDTOs = conn.queryList(DTOGostKlausurenKursklausuren.QUERY_LIST_BY_TERMIN_ID,
				DTOGostKlausurenKursklausuren.class, idsTermin);
		return DTOMapper.mapList(kursKlausurDTOs, dtoMapper);
	}

	/**
	 * Gibt die Liste der Kursklausuren zu den übergebenen Schülerklausuren zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param schuelerklausuren die Liste der Schülerklausuren, zu denen die Kursklausuren gesucht werden.
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKursklausur> getKursklausurenZuSchuelerklausuren(final DBEntityManager conn, final List<GostSchuelerklausur> schuelerklausuren)
			throws ApiOperationException {
		if (schuelerklausuren.isEmpty())
			return new ArrayList<>();
		return getKursklausurenZuIds(conn, schuelerklausuren.stream().map(sk -> sk.idKursklausur).distinct().toList());
	}

	/**
	 * Gibt die Liste der Kursklausuren zu den übergebenen Schülerklausuren zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param kkids die Liste der IDs der gesuchten Kursklausuren
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKursklausur> getKursklausurenZuIds(final DBEntityManager conn, final List<Long> kkids) throws ApiOperationException {
		if (kkids.isEmpty())
			return new ArrayList<>();
		return DTOMapper.mapList(getKursklausurenDTOsZuIds(conn, kkids), DataGostKlausurenKursklausur.dtoMapper);
	}

	/**
	 * Gibt die Liste der Kursklausuren-DTOs zu den übergebenen IDs zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param kkids die Liste der IDs der gesuchten Kursklausuren
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<DTOGostKlausurenKursklausuren> getKursklausurenDTOsZuIds(final DBEntityManager conn, final List<Long> kkids)
			throws ApiOperationException {
		if (kkids.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenKursklausuren> kks = conn.queryByKeyList(DTOGostKlausurenKursklausuren.class, kkids);
		if (kks.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Kursklausur-DTOs zu IDs nicht gefunden.");
		return kks;
	}


	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr das Gost-Halbjahr
	 * @param ganzesSchuljahr true, um Klausuren für das gesamte Schuljahr zu erhalten, false nur für das übergeben Halbjahr
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostKlausurenDataCollection getKlausurDataCollection(final DBEntityManager conn, final int abiturjahr, final int halbjahr,
			final boolean ganzesSchuljahr) throws ApiOperationException {
		final GostKlausurenDataCollection data = new GostKlausurenDataCollection();
		data.vorgaben = DataGostKlausurenVorgabe.getKlausurvorgaben(conn, abiturjahr, halbjahr, ganzesSchuljahr);
		data.kursklausuren = getKursklausurenZuVorgaben(conn, data.vorgaben);
		data.schuelerklausuren = DataGostKlausurenSchuelerklausur.getSchuelerKlausurenZuKursklausuren(conn, data.kursklausuren);
		data.schuelerklausurtermine = DataGostKlausurenSchuelerklausurTermin.getSchuelerklausurtermineZuSchuelerklausuren(conn, data.schuelerklausuren);
		data.termine = DataGostKlausurenTermin.getKlausurtermine(conn, abiturjahr, halbjahr, ganzesSchuljahr);
		return data;
	}




	/**
	 * Startet den KlausurterminblockungAlgorithmus mit den übergebenen
	 * GostKlausurterminblockungDaten und persistiert die Blockung in der Datenbank.
	 *
	 * @param conn          Connection
	 * @param blockungDaten das GostKlausurterminblockungDaten-Objekt
	 *
	 * @return true, wenn die Blockung erstellt werden konnte, false, wenn nicht.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostKlausurenDataCollection blocken(final DBEntityManager conn, final GostKlausurterminblockungDaten blockungDaten)
			throws ApiOperationException {
		final GostKlausurenDataCollection blockung = new GostKlausurenDataCollection();
		blockungDaten.richKlausuren = enrichKursklausuren(conn, blockungDaten.klausuren);
		final GostKlausurterminblockungErgebnis ergebnis = new KlausurterminblockungAlgorithmus().apply(blockungDaten);

		long idNextTermin = conn.transactionGetNextID(DTOGostKlausurenTermine.class);

		for (final GostKlausurterminblockungErgebnisTermin ergebnisTermin : ergebnis.termine) {
			bearbeiteTermin(conn, ergebnisTermin, idNextTermin++, blockung);
		}
		return blockung;
	}

	private static void bearbeiteTermin(final DBEntityManager conn, final GostKlausurterminblockungErgebnisTermin ergebnisTermin, final long terminId,
			final GostKlausurenDataCollection blockung) throws ApiOperationException {
		DTOGostKlausurenTermine termin = null;
		final List<DTOGostKlausurenKursklausuren> listKlausuren = getKursklausurenDTOsZuIds(conn, ergebnisTermin.kursklausuren);
		final List<GostKlausurvorgabe> listVorgaben = DataGostKlausurenVorgabe.getKlausurvorgabenZuKursklausurDTOs(conn, listKlausuren);
		final GostKlausurvorgabenManager vorgabenManager = new GostKlausurvorgabenManager(listVorgaben);
		for (final DTOGostKlausurenKursklausuren klausur : listKlausuren) {
			final GostKlausurvorgabe vorgabe = vorgabenManager.vorgabeGetByIdOrException(klausur.Vorgabe_ID);
			if (termin == null) {
				termin = new DTOGostKlausurenTermine(terminId, vorgabe.abiJahrgang, GostHalbjahr.fromIDorException(vorgabe.halbjahr),
						vorgabe.quartal, true, false);
				conn.transactionPersist(termin);
				blockung.termine.add(DataGostKlausurenTermin.dtoMapper.apply(termin));
				conn.transactionFlush();
			}
			if ((termin.Abi_Jahrgang != vorgabe.abiJahrgang) || (termin.Halbjahr != GostHalbjahr.fromIDorException(vorgabe.halbjahr))
				|| (termin.Quartal != vorgabe.quartal))
				throw new ApiOperationException(Status.CONFLICT, "Kursklausurn mit unterschiedlichen Jahrgängen, Halbjahren oder Quartalen an einem Termin.");
			klausur.Termin_ID = termin.ID;
			conn.transactionPersist(klausur);
			blockung.kursklausuren.add(dtoMapper.apply(klausur));
		}
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenKursklausuren} in einen Core-DTO
	 * {@link GostKursklausur}.
	 */
	public static final DTOMapper<DTOGostKlausurenKursklausuren, GostKursklausur> dtoMapper = (final DTOGostKlausurenKursklausuren k) -> {
		final GostKursklausur kk = new GostKursklausur();
		kk.id = k.ID;
		kk.idVorgabe = k.Vorgabe_ID;
		kk.idKurs = k.Kurs_ID;
		kk.idTermin = k.Termin_ID;
		kk.startzeit = k.Startzeit;
		return kk;
	};

	@Override
	public Response get(final Long halbjahr) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Startet den KlausurterminblockungAlgorithmus mit den übergebenen
	 * GostKlausurterminblockungDaten und persistiert die Blockung in der Datenbank.
	 *
	 * @param conn Connection
	 * @param id   die ID der Kursklausur
	 *
	 * @return das Kursklausur-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 *
	 */
	public static GostKursklausur getKursklausurById(final DBEntityManager conn, final long id) throws ApiOperationException {
		final DTOGostKlausurenKursklausuren data = conn.queryByKey(DTOGostKlausurenKursklausuren.class, id);
		return (data == null) ? null : dtoMapper.apply(data);
	}

	private static final Set<String> forbiddenPatchAttributes = Set.of("id", "idVorgabe", "idKurs");

	private final Map<String, DataBasicMapper<DTOGostKlausurenKursklausuren>> patchMappings = Map.ofEntries(
			Map.entry("idVorgabe", (conn, dto, value, map) -> dto.Vorgabe_ID = JSONMapper.convertToLong(value, false)),
			Map.entry("idKurs", (conn, dto, value, map) -> dto.Kurs_ID = JSONMapper.convertToLong(value, false)),
			Map.entry("idTermin", (conn, dto, value, map) -> {
				final Long newTermin = JSONMapper.convertToLong(value, true);
				if (newTermin != null) {
					final DTOGostKlausurenTermine termin = conn.queryByKey(DTOGostKlausurenTermine.class, newTermin);
					final DTOGostKlausurenVorgaben vorgabe = conn.queryByKey(DTOGostKlausurenVorgaben.class, dto.Vorgabe_ID);
					if ((termin.Quartal != 0) && !Objects.equals(termin.Quartal, vorgabe.Quartal))
						throw new ApiOperationException(Status.CONFLICT, "Klausur-Quartal entspricht nicht Termin-Quartal.");
				}
				dto.Termin_ID = newTermin;
			}),
			Map.entry("startzeit", (conn, dto, value, map) -> dto.Startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440)));

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Ein Patch mit der ID null ist nicht möglich.");
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "In dem Patch sind keine Daten enthalten.");
		final DTOGostKlausurenKursklausuren dto = conn.queryByKey(DTOGostKlausurenKursklausuren.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<GostSchuelerklausur> sks = DataGostKlausurenSchuelerklausur.getSchuelerKlausurenZuKursklausuren(conn,
				ListUtils.create1(DataGostKlausurenKursklausur.dtoMapper.apply(dto)));
		final List<GostSchuelerklausurTermin> skts = DataGostKlausurenSchuelerklausurTermin.getSchuelerklausurtermineZuSchuelerklausuren(conn, sks);
		final List<Long> skts_ids = skts.stream().map(skt -> skt.id).toList();
		GostKlausurenCollectionSkrsKrs result = new GostKlausurenCollectionSkrsKrs();
		for (final Entry<String, Object> entry : map.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			if ((forbiddenPatchAttributes != null) && forbiddenPatchAttributes.contains(key))
				throw new ApiOperationException(Status.FORBIDDEN, "Attribut %s darf nicht im Patch enthalten sein.".formatted(key));
			final DataBasicMapper<DTOGostKlausurenKursklausuren> mapper = patchMappings.get(key);
			if (mapper == null)
				throw new ApiOperationException(Status.BAD_REQUEST);
			if (key.equals("startzeit")) {
				final Integer startzeitNeu = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
				if (((startzeitNeu == null) && (dto.Startzeit != null)) || ((startzeitNeu != null) && !startzeitNeu.equals(dto.Startzeit))) {
					dto.Startzeit = startzeitNeu;
					conn.transactionPersist(dto);
					if (_idSchuljahresAbschnitt == -1)
						throw new ApiOperationException(Status.FORBIDDEN, "idAbschnitt muss übergeben werden, um Klausurzeit zu ändern");
					final GostKlausurraumRich krRich = new GostKlausurraumRich();
					krRich.id = -1;
					krRich.schuelerklausurterminIDs = skts_ids;
					result = DataGostKlausurenSchuelerklausurraumstunde.transactionSetzeRaumZuSchuelerklausuren(conn, ListUtils.create1(krRich),
							_idSchuljahresAbschnitt);
				}
			}
			if (key.equals("idTermin")) {
				dto.Startzeit = null; // Bei Zuweisung eines neuen Termins wird individuelle Startzeit gelöscht
				final GostKlausurraumRich krRich = new GostKlausurraumRich();
				krRich.id = -1;
				krRich.schuelerklausurterminIDs = skts_ids;
				result = DataGostKlausurenSchuelerklausurraumstunde.loescheRaumZuSchuelerklausurenTransaction(conn, ListUtils.create1(krRich)); // Auch alle Raumzuweisungen werden gelöscht
			}
			mapper.map(conn, dto, value, map);
		}
		conn.transactionPersist(dto);
		result.kursKlausurPatched = dtoMapper.apply(dto);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Erzeugt eine Liste von GostKursklausurRich-Objekten, die für die Klausurblockung benötigte Informationen anreichert.
	 *
	 * @param conn Connection
	 * @param kursklausuren   die Liste der anzureichernden GostKursklausur-Objekte
	 *
	 * @return die Liste von GostKursklausurRich-Objekten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKursklausurRich> enrichKursklausuren(final DBEntityManager conn, final List<GostKursklausur> kursklausuren)
			throws ApiOperationException {
		final List<GostKursklausurRich> richKlausuren = new ArrayList<>();
		if (kursklausuren.isEmpty())
			return richKlausuren;

		final List<GostKlausurvorgabe> listVorgaben = DataGostKlausurenVorgabe.getKlausurvorgabenZuKursklausuren(conn, kursklausuren);
		if (listVorgaben.isEmpty())
			return new ArrayList<>();

		final GostKlausurvorgabenManager vorgabenManager = new GostKlausurvorgabenManager(listVorgaben);

		final Map<Long, List<DTOGostKlausurenSchuelerklausuren>> mapSchuelerklausuren = conn.queryList(
				DTOGostKlausurenSchuelerklausuren.QUERY_LIST_BY_KURSKLAUSUR_ID, DTOGostKlausurenSchuelerklausuren.class,
				kursklausuren.stream().map(k -> k.id).toList()).stream().collect(Collectors.groupingBy(s -> s.Kursklausur_ID));
		if (mapSchuelerklausuren.isEmpty())
			return new ArrayList<>();

		final List<Long> kursIDs = kursklausuren.stream().map(k -> k.idKurs).distinct().toList();
		final Map<Long, DTOKurs> mapKurse = conn.queryByKeyList(DTOKurs.class, kursIDs).stream().collect(Collectors.toMap(k -> k.ID, k -> k));

		for (final var k : kursklausuren) {
			final GostKlausurvorgabe v = vorgabenManager.vorgabeGetByIdOrException(k.idVorgabe);
			final DTOKurs kurs = mapKurse.get(k.idKurs);
			final List<DTOGostKlausurenSchuelerklausuren> sKlausuren = mapSchuelerklausuren.get(k.id);
			if ((sKlausuren != null) && !sKlausuren.isEmpty()) {
				final GostKursklausurRich kkr = new GostKursklausurRich();
				kkr.abijahr = v.abiJahrgang;
				kkr.bemerkung = k.bemerkung;
				kkr.halbjahr = v.halbjahr;
				kkr.id = k.id;
				kkr.idFach = v.idFach;
				kkr.idKurs = k.idKurs;
				kkr.idLehrer = kurs.Lehrer_ID;
				kkr.idTermin = k.idTermin;
				kkr.idVorgabe = v.idVorgabe;
				kkr.kursart = v.kursart;
				kkr.kursKurzbezeichnung = kurs.KurzBez;
				try {
					kkr.kursSchiene = Stream.of(kurs.Schienen.split(",")).mapToInt(Integer::parseInt).toArray();
				} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Falsche Formatierung des Attributs Schienen (%s) bei Kurs %d.".formatted(kurs.Schienen, kurs.ID));
				}
				kkr.quartal = v.quartal;
				kkr.schuelerIds = sKlausuren.stream().map(s -> s.Schueler_ID).toList();
				kkr.startzeit = k.startzeit;
				richKlausuren.add(kkr);
			}
		}
		return richKlausuren;
	}



}
