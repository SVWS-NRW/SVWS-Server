package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrs;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenDataCollection;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungDaten;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnis;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnisTermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausurRich;
import de.svws_nrw.core.utils.gost.klausurplanung.KlausurterminblockungAlgorithmus;
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
import de.svws_nrw.db.utils.OperationError;
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
	 */
	public DataGostKlausurenKursklausur(final DBEntityManager conn, final int abiturjahr, final long idSchuljahresAbschnitt) {
		super(conn);
		if (abiturjahr != -1 && conn.queryByKey(DTOGostJahrgangsdaten.class, abiturjahr) == null)
			throw OperationError.BAD_REQUEST.exception("Jahrgang nicht gefunden, ID: " + abiturjahr);
		_idSchuljahresAbschnitt = idSchuljahresAbschnitt;
		if (idSchuljahresAbschnitt != -1 && conn.queryByKey(DTOSchuljahresabschnitte.class, idSchuljahresAbschnitt) == null)
			throw OperationError.BAD_REQUEST.exception("Schuljahresabschnitt nicht gefunden, ID: " + idSchuljahresAbschnitt);
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
	 */
	public static List<GostKursklausur> getKursKlausuren(final DBEntityManager conn, final int abiturjahr, final int halbjahr, final boolean ganzesSchuljahr) {
		return getKursklausurenZuVorgaben(conn, DataGostKlausurenVorgabe.getKlausurvorgaben(conn, abiturjahr, halbjahr, ganzesSchuljahr));
	}

	/**
	 * Gibt die Liste der Kursklausuren zu den übergebenen Klausurvorgaben zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param vorgaben die Liste der Klausurvorgaben, zu denen die Kursklausuren gesucht werden.
	 *
	 * @return die Liste der Schülerklausuren
	 */
	public static List<GostKursklausur> getKursklausurenZuVorgaben(final DBEntityManager conn, final List<GostKlausurvorgabe> vorgaben) {
		if (vorgaben.isEmpty())
			return new ArrayList<>();
		List<DTOGostKlausurenKursklausuren> kursKlausurDTOs = conn.queryNamed("DTOGostKlausurenKursklausuren.vorgabe_id.multiple", vorgaben.stream().map(v -> v.idVorgabe).toList(), DTOGostKlausurenKursklausuren.class);
		return kursKlausurDTOs.stream().map(dtoMapper2::apply).toList();
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
	 */
	public static GostKlausurenDataCollection getKlausurDataCollection(final DBEntityManager conn, final int abiturjahr, final int halbjahr, final boolean ganzesSchuljahr) {
		final GostKlausurenDataCollection data = new GostKlausurenDataCollection();
		data.vorgaben = DataGostKlausurenVorgabe.getKlausurvorgaben(conn, abiturjahr, halbjahr, ganzesSchuljahr);
		data.kursklausuren = getKursklausurenZuVorgaben(conn, data.vorgaben);
		data.schuelerklausuren = DataGostKlausurenSchuelerklausur.getSchuelerKlausurenZuKursklausuren(conn, data.kursklausuren);
		data.schuelerklausurtermine = DataGostKlausurenSchuelerklausurTermin.getSchuelerklausurtermineZuSchuelerklausuren(conn, data.schuelerklausuren);
		data.termine.addAll(DataGostKlausurenTermin.getKlausurtermineZuKursklausuren(conn, data.kursklausuren));
		data.termine.addAll(DataGostKlausurenTermin.getKlausurtermineZuSchuelerklausurterminen(conn, data.schuelerklausurtermine));
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
	 */
	public static boolean blocken(final DBEntityManager conn, final GostKlausurterminblockungDaten blockungDaten) {
		blockungDaten.richKlausuren = enrichKursklausuren(conn, blockungDaten.klausuren);
		final GostKlausurterminblockungErgebnis ergebnis = new KlausurterminblockungAlgorithmus().apply(blockungDaten);

		long idNextTermin = conn.transactionGetNextID(DTOGostKlausurenTermine.class);

		for (final GostKlausurterminblockungErgebnisTermin ergebnisTermin : ergebnis.termine) {
			bearbeiteTermin(conn, ergebnisTermin, idNextTermin++);
		}
		return true;
	}

	private static void bearbeiteTermin(final DBEntityManager conn, final GostKlausurterminblockungErgebnisTermin ergebnisTermin, final long terminId) {
		DTOGostKlausurenTermine termin = null;
		for (final long klausurId : ergebnisTermin.kursklausuren) {
			final DTOGostKlausurenKursklausuren klausur = conn.queryByKey(DTOGostKlausurenKursklausuren.class, klausurId);
			if (klausur == null)
				throw OperationError.NOT_FOUND.exception("Kursklausur mit ID " + klausurId + " nicht gefunden.");
			final DTOGostKlausurenVorgaben vorgabe = conn.queryByKey(DTOGostKlausurenVorgaben.class, klausur.Vorgabe_ID);
			if (vorgabe == null)
				throw OperationError.NOT_FOUND.exception("Klausurvorgabe mit ID " + klausur.Vorgabe_ID + " nicht gefunden.");
			if (termin == null) {
				termin = new DTOGostKlausurenTermine(terminId, vorgabe.Abi_Jahrgang, vorgabe.Halbjahr, vorgabe.Quartal, true, false);
				conn.transactionPersist(termin);
				conn.transactionFlush();
			}
			if (termin.Abi_Jahrgang != vorgabe.Abi_Jahrgang || termin.Halbjahr != vorgabe.Halbjahr || termin.Quartal != vorgabe.Quartal)
				throw OperationError.CONFLICT.exception("Kursklausurn mit unterschiedlichen Jahrgängen, Halbjahren oder Quartalen an einem Termin.");
			klausur.Termin_ID = termin.ID;
			conn.transactionPersist(klausur);
		}
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenKursklausuren} in einen Core-DTO
	 * {@link GostKursklausur}.
	 */
	public static final Function<DTOGostKlausurenKursklausuren, GostKursklausur> dtoMapper2 = (final DTOGostKlausurenKursklausuren k) -> {
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
	 */
	public static GostKursklausur getKursklausurById(final DBEntityManager conn, final long id) {
		final DTOGostKlausurenKursklausuren data = conn.queryByKey(DTOGostKlausurenKursklausuren.class, id);
		if (data != null)
			return dtoMapper2.apply(data);
		return null;
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
					if (termin.Quartal != 0 && !Objects.equals(termin.Quartal, vorgabe.Quartal))
						throw OperationError.CONFLICT.exception("Klausur-Quartal entspricht nicht Termin-Quartal.");
				}
				dto.Termin_ID = newTermin;
			}),
			Map.entry("startzeit", (conn, dto, value, map) -> dto.Startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440)));

	@Override
	public Response patch(final Long id, final InputStream is) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Ein Patch mit der ID null ist nicht möglich.");
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			return OperationError.NOT_FOUND.getResponse("In dem Patch sind keine Daten enthalten.");
		final DTOGostKlausurenKursklausuren dto = conn.queryByKey(DTOGostKlausurenKursklausuren.class, id);
		if (dto == null)
			throw OperationError.NOT_FOUND.exception();
		final List<DTOGostKlausurenSchuelerklausuren> sks = conn.queryNamed("DTOGostKlausurenSchuelerklausuren.kursklausur_id", dto.ID, DTOGostKlausurenSchuelerklausuren.class);
		final List<Long> sks_ids = sks.stream().map(sk -> sk.ID).toList();
		GostKlausurenCollectionSkrsKrs result = new GostKlausurenCollectionSkrsKrs();
		for (final Entry<String, Object> entry : map.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			if (forbiddenPatchAttributes != null && forbiddenPatchAttributes.contains(key))
				throw OperationError.FORBIDDEN.exception("Attribut %s darf nicht im Patch enthalten sein.".formatted(key));
			final DataBasicMapper<DTOGostKlausurenKursklausuren> mapper = patchMappings.get(key);
			if (mapper == null)
				throw OperationError.BAD_REQUEST.exception();
			if (key.equals("startzeit")) {
				final Integer startzeitNeu = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
				if (startzeitNeu == null && dto.Startzeit != null || startzeitNeu != null && !startzeitNeu.equals(dto.Startzeit)) {
					dto.Startzeit = startzeitNeu;
					conn.transactionPersist(dto);
					if (_idSchuljahresAbschnitt == -1)
						throw OperationError.FORBIDDEN.exception("idAbschnitt muss übergeben werden, um Klausurzeit zu ändern");
					result = DataGostKlausurenSchuelerklausurraumstunde.transactionSetzeRaumZuSchuelerklausuren(conn, null, sks_ids, _idSchuljahresAbschnitt);
				}
			}
			if (key.equals("idTermin")) {
				dto.Startzeit = null; // Bei Zuweisung eines neuen Termins wird individuelle Startzeit gelöscht
				result = DataGostKlausurenSchuelerklausurraumstunde.loescheRaumZuSchuelerklausurenTransaction(conn, sks_ids); // Auch alle Raumzuweisungen werden gelöscht
			}
			mapper.map(conn, dto, value, map);
		}
		conn.transactionPersist(dto);
		result.kursKlausurPatched = dtoMapper2.apply(dto);
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
	 */
	public static List<GostKursklausurRich> enrichKursklausuren(final DBEntityManager conn, final List<GostKursklausur> kursklausuren) {
		List<GostKursklausurRich> richKlausuren = new ArrayList<>();

		final Map<Long, GostKlausurvorgabe> mapVorgaben = DataGostKlausurenVorgabe.getKlausurvorgabenZuKursklausuren(conn, kursklausuren).stream().collect(Collectors.toMap(v -> v.idVorgabe, v -> v));
		if (mapVorgaben.isEmpty()) {
			return new ArrayList<>();
		}

		final Map<Long, List<DTOGostKlausurenSchuelerklausuren>> mapSchuelerklausuren = conn
				.queryNamed("DTOGostKlausurenSchuelerklausuren.kursklausur_id.multiple", kursklausuren.stream().map(k -> k.id).toList(), DTOGostKlausurenSchuelerklausuren.class).stream()
				.collect(Collectors.groupingBy(s -> s.Kursklausur_ID));
		if (mapSchuelerklausuren.isEmpty()) {
			return new ArrayList<>();
		}

		final List<Long> kursIDs = kursklausuren.stream().map(k -> k.idKurs).distinct().toList();
		final Map<Long, DTOKurs> mapKurse = conn.queryNamed("DTOKurs.id.multiple", kursIDs, DTOKurs.class).stream().collect(Collectors.toMap(k -> k.ID, k -> k));

		kursklausuren.stream().forEach(k -> {
			final GostKlausurvorgabe v = mapVorgaben.get(k.idVorgabe);
			final DTOKurs kurs = mapKurse.get(k.idKurs);
			final List<DTOGostKlausurenSchuelerklausuren> sKlausuren = mapSchuelerklausuren.get(k.id);
			if (sKlausuren != null && !sKlausuren.isEmpty()) {
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
					throw OperationError.BAD_REQUEST.exception("Falsche Formatierung des Attributs Schienen (%s) bei Kurs %d.".formatted(kurs.Schienen, kurs.ID));
				}
				kkr.quartal = v.quartal;
				kkr.schuelerIds = sKlausuren.stream().map(s -> s.Schueler_ID).toList();
				kkr.startzeit = k.startzeit;
				richKlausuren.add(kkr);
			}
		});
		return richKlausuren;
	}



}
