package de.svws_nrw.data.gost.klausurplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionHjData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.gost.DataGostFaecher;
import de.svws_nrw.data.gost.DataGostJahrgangSchuelerliste;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.data.lehrer.DataLehrerliste;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.data.schule.DataSchuljahresabschnitte;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenKursklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Data-Manager für die Klausuren der gymnasialen Oberstufe
 */
public final class DataGostKlausuren {

	private final int _abiturjahr;
	private final DBEntityManager conn;

	/**
	 * Erstellt einen neuen DataManager für den Core-DTO
	 * {@link GostKlausurtermin}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 */
	public DataGostKlausuren(final DBEntityManager conn, final int abiturjahr) {
		this.conn = conn;
		_abiturjahr = abiturjahr;
	}

	/**
	 * Sammelt alle Daten, die für die Klausurplanung der gesamten Oberstufe nötig sind.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param jgs Liste von GostKlausurenCollectionHjData-Objekten, für die die Daten gesammelt werden sollen
	 *
	 * @return die DataCollection
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostKlausurenCollectionAllData getAllData(final DBEntityManager conn, final List<GostKlausurenCollectionHjData> jgs)
			throws ApiOperationException {

		final GostKlausurenCollectionAllData data = new GostKlausurenCollectionAllData();

		final List<SchuelerListeEintrag> schuelerNichtSenden = new ArrayList<>();

		for (final GostKlausurenCollectionHjData jg : jgs) {
			jg.data = new DataGostKlausurenKursklausur(conn).getKlausurDataCollection(jg.abiturjahrgang, jg.gostHalbjahr, false);

			final List<SchuelerListeEintrag> schuelerJahrgang = new DataGostJahrgangSchuelerliste(conn, jg.abiturjahrgang).getAllSchueler();
			schuelerNichtSenden.addAll(schuelerJahrgang);
			if (jg.schueler != null)
				jg.schueler = schuelerJahrgang;
			if (jg.faecher != null)
				jg.faecher = DataGostFaecher.getFaecherManager(conn, jg.abiturjahrgang).faecher();

			final GostHalbjahr gj = GostHalbjahr.fromID(jg.gostHalbjahr);
			final Schuljahresabschnitt sja =
					DataSchuljahresabschnitte.getFromSchuljahrUndAbschnitt(conn, gj.getSchuljahrFromAbiturjahr(jg.abiturjahrgang), gj.halbjahr);
			if (sja != null) {
				jg.schuljahresabschnitt = sja.id;
				jg.kurse.addAll(DataKurse.getKursListenFuerAbschnitt(conn, sja.id, true));
			}
			jg.raumdata =
					new DataGostKlausurenSchuelerklausurraumstunde(conn).getRaumDataByTerminids(jg.data.termine.stream().map(t -> t.id).toList());
			data.datacontained.add(jg);
		}

		// Schüler nachladen, die in der Klausurplanung vorkommen, aber zu keinem Oberstufenjahrgang gehören
		final List<Long> missingSchuelerIds = jgs.stream().flatMap(jg -> jg.data.schuelerklausuren.stream()).map(sk -> sk.idSchueler)
				.filter(item -> !schuelerNichtSenden.stream().map(s -> s.id).distinct().toList().contains(item)).toList();
		if (!missingSchuelerIds.isEmpty()) {
			jgs.getFirst().schueler = jgs.getFirst().schueler == null ? new ArrayList<>() : new ArrayList<>(jgs.getFirst().schueler);
			jgs.getFirst().schueler.addAll(ladeSchuelerByIds(-1, conn, missingSchuelerIds));
		}
		data.lehrer.addAll(new DataLehrerliste(conn, null).getLehrerListe(false));
		return data;
	}

	private static List<SchuelerListeEintrag> ladeSchuelerByIds(final int schuljahr, final DBEntityManager conn, final List<Long> schuelerIds) {
		if (schuelerIds.isEmpty())
			return new ArrayList<>();
		final List<DTOSchueler> schuelerListe = conn.queryList(DTOSchueler.QUERY_LIST_BY_ID, DTOSchueler.class, schuelerIds);
		return schuelerListe.stream().map(s -> DataSchuelerliste.erstelleSchuelerlistenEintrag(s, schuljahr, null, null, null)).toList();
	}

	/**
	 * Erzeugt die Gost-Kursklausuren und Gost-Schülerklausuren aus den Klausurvorlagen einer Jahrgangsstufe
	 * im übergebenen Gost-Halbjahr für die existierenden Kurse.
	 *
	 * @param hj      das Gost-Halbjahr
	 * @param quartal das Quartal
	 *
	 * @return die Anzahl der erzeugten Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public GostKlausurenCollectionData createKlausuren(final int hj, final int quartal) throws ApiOperationException {
		final GostHalbjahr halbjahr = GostHalbjahr.fromID(hj);

		final List<GostKlausurvorgabe> vorgaben = new DataGostKlausurenVorgabe(conn).getKlausurvorgaben(_abiturjahr, hj, false);
		if (vorgaben.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Klausurvorgaben für dieses Halbjahr definiert.");

		final GostKlausurplanManager manager = new GostKlausurplanManager(vorgaben);

		final List<GostKursklausur> existingKlausuren = new DataGostKlausurenKursklausur(conn).getKursklausurenZuVorgaben(vorgaben);
		final Map<Long, Map<Long, GostKursklausur>> mapKursidVorgabeIdKursklausur = existingKlausuren.stream()
				.collect(Collectors.groupingBy(k -> k.idKurs, Collectors.toMap(k -> k.idVorgabe, Function.identity())));

		final List<DTOSchuljahresabschnitte> sjaList =
				conn.query("SELECT s FROM DTOSchuljahresabschnitte s WHERE s.Jahr = :jahr AND s.Abschnitt = :abschnitt", DTOSchuljahresabschnitte.class)
						.setParameter("jahr", halbjahr.getSchuljahrFromAbiturjahr(_abiturjahr))
						.setParameter("abschnitt", (halbjahr.id % 2) + 1)
						.getResultList();
		if ((sjaList == null) || (sjaList.size() != 1))
			throw new ApiOperationException(Status.NOT_FOUND, "Noch kein Schuljahresabschnitt für dieses Halbjahr definiert.");

		final DTOSchuljahresabschnitte sja = sjaList.get(0);

		// Kurse ermitteln
		final List<DTOKurs> kurse =
				conn.query("SELECT k FROM DTOKurs k WHERE k.Schuljahresabschnitts_ID = :sja AND k.ASDJahrgang = :jg", DTOKurs.class)
						.setParameter("sja", sja.ID)
						.setParameter("jg", halbjahr.jahrgang)
						.getResultList();

		final List<DTOGostKlausurenKursklausuren> kursklausuren = new ArrayList<>();
		final List<DTOGostKlausurenSchuelerklausuren> schuelerklausuren = new ArrayList<>();
		long idNextKursklausur = conn.transactionGetNextID(DTOGostKlausurenKursklausuren.class);

		final List<DTOKurs> missingVorgaben = new ArrayList<>();

		for (final DTOKurs kurs : kurse) {
			final List<DTOSchuelerLernabschnittsdaten> laDaten = getLernabschnittsdatenZuKurs(conn, sja.Jahr, kurs);
			final List<GostKlausurvorgabe> listKursVorgaben = manager.vorgabeGetMengeByHalbjahrAndQuartalAndKursartallgAndFachid(_abiturjahr, halbjahr, quartal,
					GostKursart.fromKuerzelOrException(kurs.KursartAllg), kurs.Fach_ID);
			if (listKursVorgaben.isEmpty() && !laDaten.isEmpty())
				missingVorgaben.add(kurs);
			for (final GostKlausurvorgabe vorgabe : listKursVorgaben) {
				if (!(mapKursidVorgabeIdKursklausur.containsKey(kurs.ID) && mapKursidVorgabeIdKursklausur.get(kurs.ID).containsKey(vorgabe.id))) {
					final DTOGostKlausurenKursklausuren kursklausur = new DTOGostKlausurenKursklausuren(idNextKursklausur, vorgabe.id, kurs.ID);
					final List<DTOGostKlausurenSchuelerklausuren> listSk = createSchuelerklausurenZuKursklausur(kursklausur, laDaten);
					if (!listSk.isEmpty()) {
						idNextKursklausur++;
						kursklausuren.add(kursklausur);
						schuelerklausuren.addAll(listSk);
					}
				}
			}
		}

		// ID in Schülerklausuren einfügen
		long idNextSchuelerklausur = conn.transactionGetNextID(DTOGostKlausurenSchuelerklausuren.class);
		for (final DTOGostKlausurenSchuelerklausuren sk : schuelerklausuren)
			sk.ID = idNextSchuelerklausur++;

		// SchülerklausurTermine erstellen und ID in SchülerklausurTermine einfügen
		final List<DTOGostKlausurenSchuelerklausurenTermine> sktermine = createSchuelerklausurenTermineZuSchuelerklausuren(schuelerklausuren);
		long idNextSchuelerklausurTermin = conn.transactionGetNextID(DTOGostKlausurenSchuelerklausurenTermine.class);
		for (final DTOGostKlausurenSchuelerklausurenTermine skt : sktermine)
			skt.ID = idNextSchuelerklausurTermin++;

		if (!conn.transactionPersistAll(kursklausuren) || !conn.transactionPersistAll(schuelerklausuren) || !conn.transactionPersistAll(sktermine))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		final GostKlausurenCollectionData retKlausuren = new GostKlausurenCollectionData();
		retKlausuren.kursklausuren = new DataGostKlausurenKursklausur(conn).mapList(kursklausuren);
		retKlausuren.schuelerklausuren = new DataGostKlausurenSchuelerklausur(conn).mapList(schuelerklausuren);
		retKlausuren.schuelerklausurtermine = new DataGostKlausurenSchuelerklausurTermin(conn).mapList(sktermine);
		if (!missingVorgaben.isEmpty()) {
			final String kursText = missingVorgaben.size() == 1 ? "den Kurs" : "die Kurse";
			final String kursListe = missingVorgaben.stream().map(k -> k.KurzBez).collect(Collectors.joining(", "));
			final String wurdeText = missingVorgaben.size() > 1 ? "n" : "";
			retKlausuren.description = String.format("Für %s %s wurde%s keine Klausur erzeugt, da die entsprechende Klausurvorgabe fehlt.",
			    kursText, kursListe, wurdeText
			);
		}
		return retKlausuren;
	}

	private static List<DTOGostKlausurenSchuelerklausuren> createSchuelerklausurenZuKursklausur(final DTOGostKlausurenKursklausuren kursklausur,
			final List<DTOSchuelerLernabschnittsdaten> lernDaten) {
		final List<DTOGostKlausurenSchuelerklausuren> listSchuelerklausuren = new ArrayList<>();
		for (final DTOSchuelerLernabschnittsdaten lad : lernDaten) {
			listSchuelerklausuren.add(new DTOGostKlausurenSchuelerklausuren(-1L, kursklausur.ID, lad.Schueler_ID));
		}
		return listSchuelerklausuren;
	}

	private static List<DTOGostKlausurenSchuelerklausurenTermine> createSchuelerklausurenTermineZuSchuelerklausuren(
			final List<DTOGostKlausurenSchuelerklausuren> sks) {
		final List<DTOGostKlausurenSchuelerklausurenTermine> listSchuelerklausurenTermine = new ArrayList<>();
		for (final DTOGostKlausurenSchuelerklausuren sk : sks) {
			listSchuelerklausurenTermine.add(new DTOGostKlausurenSchuelerklausurenTermine(-1L, sk.ID, 0));
		}
		return listSchuelerklausurenTermine;
	}

	private static List<DTOSchuelerLernabschnittsdaten> getLernabschnittsdatenZuKurs(final DBEntityManager conn, final int schuljahr, final DTOKurs kurs) {
		final Integer[] validStatus = {
				Integer.parseInt(SchuelerStatus.AKTIV.daten(schuljahr).kuerzel),
				Integer.parseInt(SchuelerStatus.EXTERN.daten(schuljahr).kuerzel)
		};
		return conn.query(
				"SELECT lad FROM DTOSchuelerLernabschnittsdaten lad JOIN DTOSchuelerLeistungsdaten sld ON sld.Abschnitt_ID = lad.ID JOIN DTOSchueler s"
				+ " ON lad.Schueler_ID = s.ID JOIN DTOSchuljahresabschnitte sla ON sla.ID = lad.Schuljahresabschnitts_ID WHERE sld.Kurs_ID = :kursid"
				+ " AND ((( lad.ASDJahrgang IN ('EF', 'Q1') OR (lad.ASDJahrgang = 'Q2' AND sla.Abschnitt = 1)) AND"
				+ " sld.Kursart IN ('LK1', 'LK2', 'AB3', 'AB4', 'GKS')) OR (lad.ASDJahrgang = 'Q2' AND sla.Abschnitt = 2 AND"
				+ " sld.Kursart IN ('LK1', 'LK2', 'AB3'))) AND s.idStatus IN :sstatus AND s.Geloescht = :sgeloescht",
				DTOSchuelerLernabschnittsdaten.class).setParameter("kursid", kurs.ID)
				.setParameter("sstatus", Arrays.asList(validStatus))
				.setParameter("sgeloescht", false)
				.getResultList();
	}

	/**
	 * Gibt die Blockungsdaten für die Blockung mit der angegebenen ID als GZip-Json
	 * zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abijahr das Jahr, in welchem der Jahrgang Abitur
	 * @param halbjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 *
	 * @return die DataCollection
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response getFehlendDataGZip(final DBEntityManager conn, final int abijahr, final GostHalbjahr halbjahr) throws ApiOperationException {
		return JSONMapper.gzipFileResponseFromObject(getFehlendData(conn, abijahr, halbjahr), "klausurdatenfehlend_%d-%d.json.gz".formatted(abijahr, halbjahr.id));
	}

	static Schuljahresabschnitt getSchuljahresabschnittFromAbijahrUndHalbjahr(final DBEntityManager conn, final int abijahr, final GostHalbjahr halbjahr) {
		return DataSchuljahresabschnitte.getFromSchuljahrUndAbschnitt(conn, halbjahr.getSchuljahrFromAbiturjahr(abijahr), halbjahr.halbjahr);
	}

	/**
	 * Gibt die Blockungsdaten für die Blockung mit der angegebenen ID als GZip-Json
	 * zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abijahr das Jahr, in welchem der Jahrgang Abitur
	 * @param halbjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 *
	 * @return die DataCollection
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostKlausurenCollectionHjData getFehlendData(final DBEntityManager conn, final int abijahr, final GostHalbjahr halbjahr) throws ApiOperationException {
		final GostKlausurenCollectionData klausuren = new DataGostKlausurenKursklausur(conn).getKlausurDataCollection(abijahr, halbjahr.id, true);
		return ermittleFehlendeKlausuren(conn, abijahr, halbjahr, getSchuljahresabschnittFromAbijahrUndHalbjahr(conn, abijahr, halbjahr), klausuren);
	}

	private static GostKlausurenCollectionHjData ermittleFehlendeKlausuren(final DBEntityManager conn, final int abijahr,
		    final GostHalbjahr halbjahr, final Schuljahresabschnitt sja, final GostKlausurenCollectionData alleDaten) {

		  final GostKlausurenCollectionHjData fehlend = new GostKlausurenCollectionHjData(abijahr, halbjahr.id);
		  final List<DTOKurs> kurse = conn
		      .query("SELECT k FROM DTOKurs k WHERE k.Schuljahresabschnitts_ID = :sja AND k.ASDJahrgang = :jg", DTOKurs.class)
		      .setParameter("jg", halbjahr.jahrgang)
		      .setParameter("sja", sja.id)
		      .getResultList();
		  final GostKlausurplanManager manager = new GostKlausurplanManager(alleDaten);

		  for (final DTOKurs kurs : kurse) {
		    final GostKursart kursart = GostKursart.fromKuerzelOrException(kurs.KursartAllg);
		    final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = getLernabschnittsdatenZuKurs(conn, sja.schuljahr, kurs);
		    for (final int quartal : new int[] { 1, 2 }) {
		      final GostKlausurvorgabe vorgabe = manager
		          .vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(abijahr, halbjahr, quartal, kursart, kurs.Fach_ID);

		      if (lernabschnitte.isEmpty()) {
		        bearbeiteKursOhneSchueler(fehlend, manager, vorgabe, kurs);
		        continue;
		      }
		      if (vorgabe == null) {
		        erzeugeFehlendeVorgabe(fehlend, manager, abijahr, halbjahr, quartal, kurs);
		        continue;
		      }
		      bearbeiteKursMitVorgabe(fehlend, manager, vorgabe, kurs, lernabschnitte);
		    }
		  }
		  return fehlend;
		}

		private static void bearbeiteKursOhneSchueler(final GostKlausurenCollectionHjData fehlend, final GostKlausurplanManager manager,
		    final GostKlausurvorgabe vorgabe, final DTOKurs kurs) {
		  if (vorgabe == null) return;
		  final GostKursklausur kursklausur = manager.kursklausurByVorgabeAndKursid(vorgabe, kurs.ID);
		  if (kursklausur == null) return;
		  fehlend.data.kursklausuren.add(kursklausur);
		  manager.kursklausurfehlendAdd(kursklausur);
		  fehlend.data.schuelerklausuren.addAll(manager.schuelerklausurGetMengeByKursklausur(kursklausur));
		}

		private static void erzeugeFehlendeVorgabe(final GostKlausurenCollectionHjData fehlend, final GostKlausurplanManager manager,
		    final int abijahr, final GostHalbjahr halbjahr, final int quartal, final DTOKurs kurs) {
		  if (manager.vorgabefehlendGetByHalbjahrAndQuartalAndKursartallgAndFachid(
		      abijahr, halbjahr, quartal, GostKursart.fromKuerzelOrException(kurs.KursartAllg), kurs.Fach_ID) != null) return;

		  final GostKlausurvorgabe neueVorgabe = new GostKlausurvorgabe();
		  neueVorgabe.abiJahrgang = abijahr;
		  neueVorgabe.halbjahr = halbjahr.id;
		  neueVorgabe.idFach = kurs.Fach_ID;
		  neueVorgabe.kursart = kurs.KursartAllg;
		  neueVorgabe.quartal = quartal;
		  fehlend.data.vorgaben.add(neueVorgabe);
		  manager.vorgabefehlendAdd(neueVorgabe);
		}

		private static void bearbeiteKursMitVorgabe(final GostKlausurenCollectionHjData fehlend, final GostKlausurplanManager manager,
		    final GostKlausurvorgabe vorgabe, final DTOKurs kurs, final List<DTOSchuelerLernabschnittsdaten> lernabschnitte) {

		  final GostKursklausur kursklausur = manager.kursklausurByVorgabeAndKursid(vorgabe, kurs.ID);
		  if (kursklausur == null) {
		    final GostKursklausur neueKursklausur = new GostKursklausur();
		    neueKursklausur.idKurs = kurs.ID;
		    neueKursklausur.idVorgabe = vorgabe.id;
		    fehlend.data.kursklausuren.add(neueKursklausur);
		    return;
		  }

		  final Map<Long, GostSchuelerklausur> schuelerklausurenMap =
		      manager.schuelerklausurGetMengeByKursklausur(kursklausur)
		             .stream().collect(Collectors.toMap(sk -> sk.idSchueler, sk -> sk));

		  for (final DTOSchuelerLernabschnittsdaten la : lernabschnitte) {
		    if (manager.schuelerklausurByKursklausurAndSchuelerid(kursklausur, la.Schueler_ID) == null) {
		      final GostSchuelerklausur fehlendeSchuelerklausur = new GostSchuelerklausur();
		      fehlendeSchuelerklausur.idKursklausur = kursklausur.id;
		      fehlendeSchuelerklausur.idSchueler = la.Schueler_ID;
		      fehlend.data.schuelerklausuren.add(fehlendeSchuelerklausur);
		    } else {
		      schuelerklausurenMap.remove(la.Schueler_ID);
		    }
		  }
		  fehlend.data.schuelerklausuren.addAll(schuelerklausurenMap.values());
		}

	/**
	 * Konvertiert einen leeren oder nur aus Leerzeichen bestehenden String in {@code null}.
	 * Wenn der Eingabestring bereits {@code null} ist, wird ebenfalls {@code null} zurückgegeben.
	 *
	 * @param s Der Eingabestring, der überprüft und konvertiert werden soll.
	 * @return {@code null}, wenn die Eingabe {@code null} ist oder nur aus Leerzeichen besteht;
	 *         ansonsten der ursprüngliche String.
	 */
	public static String convertEmptyStringToNull(final String s) {
		return (s == null || s.strip().isEmpty()) ? null : s.strip();
	}

}
