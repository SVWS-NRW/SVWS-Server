package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungDaten;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnis;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnisTermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.klausurplanung.KlausurterminblockungAlgorithmus;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenKursklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenVorgaben;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
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

	private final int _abiturjahr;
	private long _idSchuljahresAbschnitt = -1;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKursklausur}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param idSchuljahresAbschnitt die ID des Schuljahresabschnitts. Wird nur gebraucht, falls die Startzeit der Klausur geändert werden muss.
	 */
	public DataGostKlausurenKursklausur(final DBEntityManager conn, final int abiturjahr, final long idSchuljahresAbschnitt) {
		super(conn);
		_abiturjahr = abiturjahr;
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

	private List<GostKursklausur> getKursKlausuren(final GostHalbjahr halbjahr) {
		final Map<Long, DTOGostKlausurenVorgaben> mapVorgaben = conn
				.query("SELECT v FROM DTOGostKlausurenVorgaben v WHERE v.Abi_Jahrgang = :jgid AND v.Halbjahr = :hj", DTOGostKlausurenVorgaben.class).setParameter("jgid", _abiturjahr)
				.setParameter("hj", halbjahr).getResultList().stream().collect(Collectors.toMap(v -> v.ID, v -> v));
		if (mapVorgaben.isEmpty()) {
			return new ArrayList<>();
		}

		final List<DTOGostKlausurenKursklausuren> kursklausuren = conn.queryNamed("DTOGostKlausurenKursklausuren.vorgabe_id.multiple", mapVorgaben.keySet(), DTOGostKlausurenKursklausuren.class);
		if (kursklausuren.isEmpty()) {
			return new ArrayList<>();
		}

		final Map<Long, List<DTOGostKlausurenSchuelerklausuren>> mapSchuelerklausuren = conn
				.queryNamed("DTOGostKlausurenSchuelerklausuren.kursklausur_id.multiple", kursklausuren.stream().map(k -> k.ID).toList(), DTOGostKlausurenSchuelerklausuren.class).stream()
				.collect(Collectors.groupingBy(s -> s.Kursklausur_ID));
		if (mapSchuelerklausuren.isEmpty()) {
			return new ArrayList<>();
		}

		final List<Long> kursIDs = kursklausuren.stream().map(k -> k.Kurs_ID).distinct().toList();
		final Map<Long, DTOKurs> mapKurse = conn.queryNamed("DTOKurs.id.multiple", kursIDs, DTOKurs.class).stream().collect(Collectors.toMap(k -> k.ID, k -> k));

		final List<GostKursklausur> daten = new ArrayList<>();
		kursklausuren.stream().forEach(k -> {
			final DTOGostKlausurenVorgaben v = mapVorgaben.get(k.Vorgabe_ID);
			final DTOKurs kurs = mapKurse.get(k.Kurs_ID);
			final List<DTOGostKlausurenSchuelerklausuren> sKlausuren = mapSchuelerklausuren.get(k.ID);
			if (sKlausuren != null && !sKlausuren.isEmpty()) {
				final GostKursklausur kk = dtoMapper.apply(k, DataGostKlausurenVorgabe.dtoMapper.apply(v), kurs, sKlausuren);
				daten.add(kk);
			}
		});
		return daten;
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
		final GostKlausurterminblockungErgebnis ergebnis = new KlausurterminblockungAlgorithmus().apply(blockungDaten);
		try {
			conn.transactionBegin();
			final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Klausuren_Termine");
			Long terminId = lastID == null ? 1 : lastID.MaxID + 1;
			for (final GostKlausurterminblockungErgebnisTermin ergebnisTermin : ergebnis.termine) {
				bearbeiteTermin(conn, ergebnisTermin, terminId++);
			}
			conn.transactionCommit();
			return true;
		} finally {
			conn.transactionRollback();
		}
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
				termin = new DTOGostKlausurenTermine(terminId, vorgabe.Abi_Jahrgang, vorgabe.Halbjahr, vorgabe.Quartal);
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
	 * TODO
	 *
	 * @param <One>   TODO
	 * @param <Two>   TODO
	 * @param <Three> TODO
	 * @param <Four>  TODO
	 * @param <Five>  TODO
	 */
	@FunctionalInterface
	interface Function5<One, Two, Three, Four, Five> {
		/**
		 * TODO
		 *
		 * @param one   TODO
		 * @param two   TODO
		 * @param three TODO
		 * @param four  TODO
		 *
		 * @return TODO
		 */
		Five apply(One one, Two two, Three three, Four four);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenVorgaben} in einen Core-DTO
	 * {@link GostKlausurvorgabe}.
	 */
	public static Function5<DTOGostKlausurenKursklausuren, GostKlausurvorgabe, DTOKurs, List<DTOGostKlausurenSchuelerklausuren>, GostKursklausur> dtoMapper = (final DTOGostKlausurenKursklausuren k,
			final GostKlausurvorgabe v, final DTOKurs kurs, final List<DTOGostKlausurenSchuelerklausuren> sKlausuren) -> {
		final GostKursklausur kk = DataGostKlausurenKursklausur.dtoMapper2.apply(k);
		kk.abijahr = v.abiJahrgang;
		kk.auswahlzeit = v.auswahlzeit;
		kk.bemerkungVorgabe = v.bemerkungVorgabe;
		kk.dauer = v.dauer;
		kk.kursart = v.kursart;
		kk.idFach = v.idFach;
		kk.quartal = v.quartal;
		kk.halbjahr = v.halbjahr;
		kk.kursKurzbezeichnung = kurs.KurzBez;
		kk.idLehrer = kurs.Lehrer_ID;
		try {
			kk.kursSchiene = Stream.of(kurs.Schienen.split(",")).mapToInt(Integer::parseInt).toArray();
//			String ss[] = kurs.Schienen.split(",");
//			for (int i = 0; i < ss.length; i++)
//				kk.kursSchiene[i] = Integer.parseInt(ss[i]);
		} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
			// TODO ExceptionHandling?
		}
		kk.istAudioNotwendig = v.istAudioNotwendig;
		kk.istMdlPruefung = v.istMdlPruefung;
		kk.istVideoNotwendig = v.istVideoNotwendig;
		if (sKlausuren != null)
			kk.schuelerIds = sKlausuren.stream().map(s -> s.Schueler_ID).toList();
		return kk;
	};

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
		// Kursklausuren für einen Abiturjahrgang in einem Gost-Halbjahr
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getKursKlausuren(DataGostKlausurenVorgabe.checkHalbjahr(halbjahr.intValue()))).build();
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

	private final Map<String, DataBasicMapper<DTOGostKlausurenKursklausuren>> patchMappings =
			Map.ofEntries(
				Map.entry("id", (dto, value, map) -> {
					final Long patch_id = JSONMapper.convertToLong(value, true);
					if ((patch_id == null) || (patch_id.longValue() != dto.ID))
						throw OperationError.BAD_REQUEST.exception();
				}),
				Map.entry("idVorgabe", (dto, value, map) -> {
					final long patch_vorgabeid = JSONMapper.convertToLong(value, false);
					if ((patch_vorgabeid != dto.Vorgabe_ID))
						throw OperationError.BAD_REQUEST.exception();
				}),

				Map.entry("idKurs", (dto, value, map) -> {
					final long patch_kursid = JSONMapper.convertToLong(value, false);
					if ((patch_kursid != dto.Kurs_ID))
						throw OperationError.BAD_REQUEST.exception();
				}),
				Map.entry("idTermin", (dto, value, map) -> {
					final Long newTermin = JSONMapper.convertToLong(value, true);
					if (newTermin != null) {
						final DTOGostKlausurenTermine termin = conn.queryByKey(DTOGostKlausurenTermine.class, newTermin);
						final DTOGostKlausurenVorgaben vorgabe = conn.queryByKey(DTOGostKlausurenVorgaben.class, dto.Vorgabe_ID);
						if (!Objects.equals(termin.Quartal, vorgabe.Quartal))
							throw OperationError.CONFLICT.exception("Klausur-Quartal entspricht nicht Termin-Quartal.");
					}
					dto.Termin_ID = newTermin;
				}),
				Map.entry("startzeit", (dto, value, map) -> dto.Startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440))
			);

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
		boolean startzeitPatched = false;
		for (final Entry<String, Object> entry : map.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			final DataBasicMapper<DTOGostKlausurenKursklausuren> mapper = patchMappings.get(key);
			if (mapper == null)
				throw OperationError.BAD_REQUEST.exception();
			if (key.equals("startzeit")) {
				final Integer startzeitNeu = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
				if (startzeitNeu == null && dto.Startzeit != null || startzeitNeu != null && !startzeitNeu.equals(dto.Startzeit))
					startzeitPatched = true;
			}
			mapper.map(dto, value, map);
		}
		conn.transactionPersist(dto);
		if (startzeitPatched) {
			if (_idSchuljahresAbschnitt == -1)
				throw OperationError.FORBIDDEN.exception("idAbschnitt muss übergeben werden, um Klausurzeit zu ändern");
			final List<Long> sks = conn
					.queryNamed("DTOGostKlausurenSchuelerklausuren.kursklausur_id", dto.ID, DTOGostKlausurenSchuelerklausuren.class).stream().map(sk -> sk.ID).toList();
			DataGostKlausurenSchuelerklausurraumstunde.transactionSetzeRaumZuSchuelerklausuren(conn, null, sks, _idSchuljahresAbschnitt);
		}
		return Response.status(Status.OK).build();
//		return patch(id, -1, is);
	}

//	/**
//	 * Startet den KlausurterminblockungAlgorithmus mit den übergebenen
//	 * GostKlausurterminblockungDaten und persistiert die Blockung in der Datenbank.
//	 *
//	 * @param is Connection
//	 * @param id   die ID der Kursklausur
//	 * @param idAbschnitt ds
//	 *
//	 * @return das Kursklausur-Objekt
//	 *
//	 */
//	public Response patch(final Long id, final long idAbschnitt, final InputStream is) {
//		final Map<String, Object> map = JSONMapper.toMap(is);
//		if (map.size() > 0) {
//			long idIs = -1;
//			if (id == null)
//				idIs = JSONMapper.convertToLong(map.get("id"), false);
//			final DTOGostKlausurenKursklausuren kursklausur = conn.queryByKey(DTOGostKlausurenKursklausuren.class, id != null ? id : idIs);
//			if (kursklausur == null)
//				throw OperationError.NOT_FOUND.exception();
//			for (final Entry<String, Object> entry : map.entrySet()) {
//				final String key = entry.getKey();
//				final Object value = entry.getValue();
//				switch (key) {
//				case "id" -> {
//					final Long patch_id = JSONMapper.convertToLong(value, true);
//					if ((patch_id == null) || (patch_id.longValue() != kursklausur.ID))
//						throw OperationError.BAD_REQUEST.exception();
//				}
//				case "idVorgabe" -> {
//					final long patch_vorgabeid = JSONMapper.convertToLong(value, false);
//					if ((patch_vorgabeid != kursklausur.Vorgabe_ID))
//						throw OperationError.BAD_REQUEST.exception();
//				}
//				case "idKurs" -> {
//					final long patch_kursid = JSONMapper.convertToLong(value, false);
//					if ((patch_kursid != kursklausur.Kurs_ID))
//						throw OperationError.BAD_REQUEST.exception();
//				}
//				case "idTermin" -> {
//					final Long newTermin = JSONMapper.convertToLong(value, true);
//					if (newTermin != null) {
//						final DTOGostKlausurenTermine termin = conn.queryByKey(DTOGostKlausurenTermine.class, newTermin);
//						final DTOGostKlausurenVorgaben vorgabe = conn.queryByKey(DTOGostKlausurenVorgaben.class, kursklausur.Vorgabe_ID);
//						if (!Objects.equals(termin.Quartal, vorgabe.Quartal))
//							throw OperationError.CONFLICT.exception("Klausur-Quartal entspricht nicht Termin-Quartal.");
//					}
//					kursklausur.Termin_ID = newTermin;
//				}
//				case "startzeit" -> {
//					final int startzeitNeu = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
//					if (startzeitNeu != kursklausur.Startzeit) {
//						if (idAbschnitt == -1)
//							throw OperationError.FORBIDDEN.exception("idAbschnitt muss übergeben werden, um Klausurzeit zu ändern");
//						final List<Long> sks = conn
//								.queryNamed("DTOGostKlausurenSchuelerklausuren.kursklausur_id", id, DTOGostKlausurenSchuelerklausuren.class).stream().map(sk -> sk.ID).toList();
//						DataGostKlausurenSchuelerklausurraumstunde.transactionSetzeRaumZuSchuelerklausuren(conn, null, sks, idAbschnitt);
//					}
//					kursklausur.Startzeit = startzeitNeu;
//				}
//				default -> throw OperationError.BAD_REQUEST.exception();
//				}
//			}
//			conn.transactionPersist(kursklausur);
//			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(dtoMapper2.apply(kursklausur)).build();
//		}
//		return null;
////		return Response.status(Status.OK).build();
//	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

}
