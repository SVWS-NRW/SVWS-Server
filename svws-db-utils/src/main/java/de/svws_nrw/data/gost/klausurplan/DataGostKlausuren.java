package de.svws_nrw.data.gost.klausurplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.data.gost.GostJahrgang;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DataGostFaecher;
import de.svws_nrw.data.gost.DataGostJahrgangSchuelerliste;
import de.svws_nrw.data.gost.DataGostJahrgangsliste;
import de.svws_nrw.data.kurse.DataKursliste;
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
	 * Gibt die Blockungsdaten für die Blockung mit der angegebenen ID als GZip-Json
	 * zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param selection das Jahr, in welchem der Jahrgang Abitur machen wird
	//	 * @param halbjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 *
	 * @return die DataCollection
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response getAllDataGZip(final DBEntityManager conn, final List<Pair<Integer, Integer>> selection) throws ApiOperationException {
		final GostKlausurenCollectionAllData coll = getAllData(conn, selection);
		return JSONMapper.gzipFileResponseFromObject(coll, "klausurdaten.json.gz");
	}

	/**
	 * Sammelt alle Daten, die für die Klausurplanung der gesamten Oberstufe nötig sind.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param selection das Jahr, in welchem der Jahrgang Abitur machen wird
	 *
	 * @return die DataCollection
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostKlausurenCollectionAllData getAllData(final DBEntityManager conn, final List<Pair<Integer, Integer>> selection)
			throws ApiOperationException {

		// Pair<Abiturjahr, GostHalbjahr (-1 für alle Halbjahre)>
		final List<Pair<Integer, Integer>> jgs = new ArrayList<>();
		if (selection == null) {
			for (final GostJahrgang jg : DataGostJahrgangsliste.getGostJahrgangsliste(conn))
				jgs.add(new Pair<>(jg.abiturjahr, -1));
		} else
			jgs.addAll(selection);

		final GostKlausurenCollectionAllData data = new GostKlausurenCollectionAllData();
		data.fehlend = new GostKlausurenCollectionAllData();
		for (final Pair<Integer, Integer> jg : jgs) {
			final GostKlausurenCollectionAllData klausuren = new DataGostKlausurenKursklausur(conn).getKlausurDataCollection(jg.a, jg.b, true);
			klausuren.metadata.faecher = DataGostFaecher.getFaecherManager(conn, jg.a).faecher();

			if (jg.a != -1) {
				klausuren.fehlend = new GostKlausurenCollectionAllData();
				klausuren.metadata.schueler.addAll(new DataGostJahrgangSchuelerliste(conn, jg.a).getAllSchueler());
				// Schüler nachladen, die in der Klausurplanung vorkommen, aber zu keinem Oberstufenjahrgang gehören
				final List<Long> missingSchuelerIds = klausuren.schuelerklausuren.stream().map(sk -> sk.idSchueler)
						.filter(item -> !klausuren.metadata.schueler.stream().map(s -> s.id).toList().contains(item)).toList();
				klausuren.metadata.schueler.addAll(ladeSchuelerByIds(jg.a - 1, conn, missingSchuelerIds));

				for (final GostHalbjahr gj : GostHalbjahr.values()) {
					final Schuljahresabschnitt sja =
							DataSchuljahresabschnitte.getFromSchuljahrUndAbschnitt(conn, gj.getSchuljahrFromAbiturjahr(jg.a), gj.halbjahr);
					if (sja != null) {
						getFehlendeKlausuren(conn, new Pair<>(jg.a, gj), sja, klausuren);
						klausuren.metadata.kurse.addAll(DataKursliste.getKursListenFuerAbschnitt(conn, sja.id, true));
					}
				}
			}

			data.addAll(klausuren);

		}

		data.metadata.lehrer.addAll(DataLehrerliste.getLehrerListe(conn));
		data.raumdata =
				new DataGostKlausurenSchuelerklausurraumstunde(conn).getSchuelerklausurraumstundenByTerminids(data.termine.stream().map(t -> t.id).toList());
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
	public GostKlausurenCollectionAllData createKlausuren(final int hj, final int quartal) throws ApiOperationException {
		final int schuljahr = DBUtilsGost.pruefeSchuleMitGOStAndGetSchuljahr(conn, _abiturjahr);
		final GostHalbjahr halbjahr = GostHalbjahr.fromID(hj);

		final List<GostKlausurvorgabe> vorgaben = new DataGostKlausurenVorgabe(conn).getKlausurvorgaben(_abiturjahr, hj, false);
		if (vorgaben.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Klausurvorgaben für dieses Halbjahr definiert.");

		final GostKlausurplanManager manager = new GostKlausurplanManager(schuljahr, vorgaben);

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

		for (final DTOKurs kurs : kurse) {
			final List<DTOSchuelerLernabschnittsdaten> laDaten = getLernabschnittsdatenZuKurs(conn, manager.getSchuljahr(), kurs);
			final List<GostKlausurvorgabe> listKursVorgaben = manager.vorgabeGetMengeByHalbjahrAndQuartalAndKursartallgAndFachid(_abiturjahr, halbjahr, quartal,
					GostKursart.fromKuerzelOrException(kurs.KursartAllg), kurs.Fach_ID);
			if (listKursVorgaben.isEmpty() && !laDaten.isEmpty())
				//TODO Fehlermeldung an Client, dass es zu diesem Kurs/Fach keine Vorgaben gibt
				throw new ApiOperationException(Status.NOT_FOUND, "Keine Klausurvorgaben für diesen Kurs definiert: " + kurs.KurzBez);
			for (final GostKlausurvorgabe vorgabe : listKursVorgaben) {
				if (!(mapKursidVorgabeIdKursklausur.containsKey(kurs.ID) && mapKursidVorgabeIdKursklausur.get(kurs.ID).containsKey(vorgabe.idVorgabe))) {
					final DTOGostKlausurenKursklausuren kursklausur = new DTOGostKlausurenKursklausuren(idNextKursklausur, vorgabe.idVorgabe, kurs.ID);
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
		final GostKlausurenCollectionAllData retKlausuren = new GostKlausurenCollectionAllData();
		retKlausuren.kursklausuren = new DataGostKlausurenKursklausur(conn).mapList(kursklausuren);
		retKlausuren.schuelerklausuren = new DataGostKlausurenSchuelerklausur(conn).mapList(schuelerklausuren);
		retKlausuren.schuelerklausurtermine = new DataGostKlausurenSchuelerklausurTermin(conn).mapList(sktermine);
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
//				.setParameter("kursart", Arrays.asList((hj == 5) ? new String[] { "LK1", "LK2", "AB3" } : new String[] { "LK1", "LK2", "AB3", "AB4", "GKS" }))
				.setParameter("sstatus", Arrays.asList(validStatus))
				.setParameter("sgeloescht", false)
				.getResultList();
	}

	private static void getFehlendeKlausuren(final DBEntityManager conn, final Pair<Integer, GostHalbjahr> abijahr, final Schuljahresabschnitt sja,
			final GostKlausurenCollectionAllData allData) {

		// Kurse ermitteln
		final List<DTOKurs> kurse =
				conn.query("SELECT k FROM DTOKurs k WHERE k.Schuljahresabschnitts_ID = :sja AND k.ASDJahrgang = :jg", DTOKurs.class)
						.setParameter("jg", abijahr.b.jahrgang)
						.setParameter("sja", sja.id)
						.getResultList();

		final GostKlausurplanManager manager = new GostKlausurplanManager(abijahr.a - 1, allData);

		for (final DTOKurs kurs : kurse) {
			final GostKursart kursart = GostKursart.fromKuerzelOrException(kurs.KursartAllg);
			final List<DTOSchuelerLernabschnittsdaten> laDaten = getLernabschnittsdatenZuKurs(conn, manager.getSchuljahr(), kurs);

			for (final int quartal : new int[] { 1, 2 }) {
				final GostKlausurvorgabe vorgabe =
						manager.vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(abijahr.a, abijahr.b, quartal, kursart, kurs.Fach_ID);
				if (laDaten.isEmpty()) {
					if (vorgabe != null) {
						final GostKursklausur kursklausur = manager.kursklausurByVorgabeAndKursid(vorgabe, kurs.ID);
						if (kursklausur != null) {
							allData.fehlend.kursklausuren.add(kursklausur);
							manager.kursklausurfehlendAdd(kursklausur);
						}
					}
				} else {
					if (vorgabe == null && manager.vorgabefehlendGetByHalbjahrAndQuartalAndKursartallgAndFachid(abijahr.a, abijahr.b, quartal, kursart,
							kurs.Fach_ID) == null) {
						final GostKlausurvorgabe vorgabeFehlend = new GostKlausurvorgabe();
						vorgabeFehlend.abiJahrgang = abijahr.a;
						vorgabeFehlend.halbjahr = abijahr.b.id;
						vorgabeFehlend.idFach = kurs.Fach_ID;
						vorgabeFehlend.kursart = kurs.KursartAllg;
						vorgabeFehlend.quartal = quartal;
						allData.fehlend.vorgaben.add(vorgabeFehlend);
						manager.vorgabefehlendAdd(vorgabeFehlend);
					}
					if (vorgabe != null) {
						final GostKursklausur kursklausur = manager.kursklausurByVorgabeAndKursid(vorgabe, kurs.ID);
						if (kursklausur == null) {
							final GostKursklausur kursklausurFehlend = new GostKursklausur();
							kursklausurFehlend.idKurs = kurs.ID;
							kursklausurFehlend.idVorgabe = vorgabe.idVorgabe;
							allData.fehlend.kursklausuren.add(kursklausurFehlend);
						} else {
							final Map<Long, GostSchuelerklausur> mapSks =
									manager.schuelerklausurGetMengeByKursklausur(kursklausur).stream().collect(Collectors.toMap(sk -> sk.idSchueler, sk -> sk));
							for (final DTOSchuelerLernabschnittsdaten lad : laDaten) {
								if (manager.schuelerklausurByKursklausurAndSchuelerid(kursklausur, lad.Schueler_ID) == null) {
									final GostSchuelerklausur schuelerklausurFehlend = new GostSchuelerklausur();
									schuelerklausurFehlend.idKursklausur = kursklausur.id;
									schuelerklausurFehlend.idSchueler = lad.Schueler_ID;
									allData.fehlend.schuelerklausuren.add(schuelerklausurFehlend);
								} else {
									mapSks.remove(lad.Schueler_ID);
								}
							}
							allData.fehlend.schuelerklausuren.addAll(mapSks.values());
						}
					}
				}
			}
		}
	}

}
