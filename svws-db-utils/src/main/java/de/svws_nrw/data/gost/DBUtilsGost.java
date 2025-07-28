package de.svws_nrw.data.gost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostLeistungen;
import de.svws_nrw.core.data.gost.GostLeistungenFachbelegung;
import de.svws_nrw.core.data.gost.GostLeistungenFachwahl;
import de.svws_nrw.asd.data.schueler.Sprachendaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.gost.GostAbiturjahrUtils;
import de.svws_nrw.core.utils.gost.GostFachUtils;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.kataloge.jahrgaenge.JahrgaengeUtils;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.schueler.DBUtilsSchueler;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;

/**
 * Dies Klassen stellt Hilfmethoden für den Datenbankzugriff
 * zur Verfügung, welche in den Data-Klassen an mehreren Stellen
 * verwendet werden.
 */
public final class DBUtilsGost {

	private DBUtilsGost() {
		throw new IllegalStateException("Instantiation of " + DBUtilsGost.class.getName() + " not allowed");
	}

	/**
	 * Prüft, ob es die Schule eine Schulform mit einer Gymnasiale Oberstufe (GOSt) hat.
	 *
	 * @param conn   die aktuelle Datenbankverbindung
	 *
	 * @return das Datenbank-DTO der Schule, falls eine Schule mit Gymnasialer Oberstufe vorliegt
	 *
	 * @throws ApiOperationException    falls keine Schule definiert ist oder die Schulform keine Gymnasiale Oberstufe hat
	 */
	public static DTOEigeneSchule pruefeSchuleMitGOSt(final DBEntityManager conn) throws ApiOperationException {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		if (!conn.getUser().schuleHatGymOb())
			throw new ApiOperationException(Status.NOT_FOUND, "Die Schule hat eine Schulform ohne gymnasiale Oberstufe.");
		return schule;
	}


	/**
	 * Prüft, ob es die Schule eine Schulform mit einer Gymnasiale Oberstufe (GOSt) hat.
	 *
	 * @param conn     die aktuelle Datenbankverbindung
	 * @param abijahr  der Abiturjahrgang
	 *
	 * @return das Datenbank-DTO der Schule, falls eine Schule mit Gymnasialer Oberstufe vorliegt
	 *
	 * @throws ApiOperationException    falls keine Schule definiert ist oder die Schulform keine Gymnasiale Oberstufe hat
	 */
	public static int pruefeSchuleMitGOStAndGetSchuljahr(final DBEntityManager conn, final int abijahr) throws ApiOperationException {
		final @NotNull DTOEigeneSchule schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
		int schuljahr = abijahr - 1;
		if (schuljahr < 0) {
			final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
			if (schuljahresabschnitt == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Keine gültiger Schuljahresabschnitt vorhanden.");
			schuljahr = schuljahresabschnitt.Jahr;
		}
		return schuljahr;
	}


	/**
	 * Prüft, ob in dem angebenen Schuljahresabschnitt für das angebene Halbjahr der gymnasialen Oberstufe
	 * bereits Kurse der gymnasialen Oberstufe vorhanden sind oder nicht.
	 *
	 * @param conn       die aktuelle Datenbankverbindung
	 * @param halbjahr   das Halbjahr
	 * @param abschnitt  der Schuljahresabschnitt
	 *
	 * @return true, wenn bereits Kurse vorhanden sind und ansonsten false
	 */
	public static boolean pruefeHatOberstufenKurseInAbschnitt(final DBEntityManager conn,
			final GostHalbjahr halbjahr, final Schuljahresabschnitt abschnitt) {
		final List<DTOKurs> kurse = conn.queryList("SELECT e FROM DTOKurs e WHERE e.ASDJahrgang = ?1 AND e.Schuljahresabschnitts_ID = ?2",
				DTOKurs.class, halbjahr.jahrgang, abschnitt.id);
		for (final DTOKurs kurs : kurse) {
			final GostKursart kursart = GostKursart.fromKuerzel(kurs.KursartAllg);
			if (kursart != null)
				return true;
		}
		return false;
	}


	/**
	 * Ermittelt in dem angebenen Schuljahresabschnitt für das angebene Halbjahr der gymnasialen Oberstufe
	 * die Menge der Kurse (DB-DTOs) der gymnasialen Oberstufe.
	 *
	 * @param conn       die aktuelle Datenbankverbindung
	 * @param halbjahr   das Halbjahr
	 * @param abschnitt  der Schuljahresabschnitt
	 *
	 * @return die Menge der Kurse (DB-DTOs)
	 */
	public static Set<DTOKurs> getOberstufenKurseInAbschnitt(final DBEntityManager conn, final GostHalbjahr halbjahr,
			final Schuljahresabschnitt abschnitt) {
		final List<DTOKurs> kurse = conn.queryList("SELECT e FROM DTOKurs e WHERE e.ASDJahrgang = ?1 AND e.Schuljahresabschnitts_ID = ?2",
				DTOKurs.class, halbjahr.jahrgang, abschnitt.id);
		final Set<DTOKurs> result = new HashSet<>();
		for (final DTOKurs kurs : kurse) {
			final GostKursart kursart = GostKursart.fromKuerzel(kurs.KursartAllg);
			if (kursart != null)
				result.add(kurs);
		}
		return result;
	}


	/**
	 * Prüft, ob in dem angebenen Schuljahresabschnitt für das angebene Halbjahr der gymnasialen Oberstufe
	 * bereits Kurse der gymnasialen Oberstufe vorhanden sind und Schülern in diesem Abschnitt bei diesen
	 * Kursen bereits Quartalsnoten oder Noten zugewiesen wurden oder nicht.
	 *
	 * @param conn       die aktuelle Datenbankverbindung
	 * @param halbjahr   das Halbjahr
	 * @param abschnitt  der Schuljahresabschnitt
	 *
	 * @return true, wenn bereits Kurse vorhanden sind und Schüler dort Quartalsnoten oder Noten zugewiesen
	 *     wurden, ansonsten false
	 */
	public static List<DTOSchuelerLernabschnittsdaten> getLernabschnitteFuerGostHalbjahrInAbschnitt(final DBEntityManager conn,
			final GostHalbjahr halbjahr, final Schuljahresabschnitt abschnitt) {
		// Bestimme alle Jahrgänge der Schule, welche den passenden ASD-Jahrgang haben
		final List<DTOJahrgang> listJahrgaengeGost = conn.queryList(DTOJahrgang.QUERY_BY_ASDJAHRGANG, DTOJahrgang.class, halbjahr.jahrgang);
		final List<Long> listJahrgaengeGostIDs = listJahrgaengeGost.stream().map(j -> j.ID).toList();
		// Bestimme die SchuelerLernabschnitte von Schülern der Stufe
		return (listJahrgaengeGostIDs.isEmpty()) ? new ArrayList<>()
				: conn.queryList(
						"SELECT sla FROM DTOSchuelerLernabschnittsdaten sla JOIN DTOSchueler s ON s.Geloescht <> true AND sla.Schueler_ID = s.ID AND sla.Schuljahresabschnitts_ID = ?1 AND sla.Jahrgang_ID IN ?2",
						DTOSchuelerLernabschnittsdaten.class, abschnitt.id, listJahrgaengeGostIDs);
	}



	/**
	 * Prüft, ob in dem angebenen Schuljahresabschnitt für das angebene Halbjahr der gymnasialen Oberstufe
	 * bereits Kurse der gymnasialen Oberstufe vorhanden sind und Schülern in diesem Abschnitt bei diesen
	 * Kursen bereits Quartalsnoten oder Noten zugewiesen wurden oder nicht.
	 *
	 * @param conn       die aktuelle Datenbankverbindung
	 * @param halbjahr   das Halbjahr
	 * @param abschnitt  der Schuljahresabschnitt
	 *
	 * @return true, wenn bereits Kurse vorhanden sind und Schüler dort Quartalsnoten oder Noten zugewiesen
	 *     wurden, ansonsten false
	 */
	public static boolean pruefeHatNotenFuerOberstufeInAbschnitt(final DBEntityManager conn, final GostHalbjahr halbjahr,
			final Schuljahresabschnitt abschnitt) {
		// Bestimme die SchuelerLernabschnitte von Schülern der Stufe
		final List<DTOSchuelerLernabschnittsdaten> schuelerLernabschnittsdaten = getLernabschnitteFuerGostHalbjahrInAbschnitt(conn, halbjahr, abschnitt);
		final List<Long> idsSchuelerLernabschnittsdaten = schuelerLernabschnittsdaten.stream().map(l -> l.ID).toList();
		if (idsSchuelerLernabschnittsdaten.isEmpty())
			return false;
		// Bestimme die Schueler-Leistungsdaten zu den Lernabschnitten, welche einen (Quartals-)Noteneintrag aufweisen
		final List<DTOSchuelerLeistungsdaten> leistungsdaten = conn.queryList(
				"SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID IN ?1 AND"
				+ " NOT (((e.NotenKrz IS NULL) OR (e.NotenKrz = '') OR (e.NotenKrz = 'AT')) AND"
				+ " ((e.NotenKrzQuartal IS NULL) OR (e.NotenKrzQuartal = '') OR (e.NotenKrzQuartal = 'AT')))",
				DTOSchuelerLeistungsdaten.class,
				idsSchuelerLernabschnittsdaten);
		// ... und prüfe diese Lernabschnitte, ob sie Einträge für die gymnasiale Oberstufe beinhalten
		for (final DTOSchuelerLeistungsdaten l : leistungsdaten) {
			if (GostKursart.fromKuerzel(l.KursartAllg) == null)
				continue;
			return true;
		}
		return false;
	}


	/**
	 * Entfernt die Leistungsdaten für das angegeben Halbjahr der gymnasialen Oberstufe bei den Schülern des Abiturjahrgangs,
	 * welcher durch den Schuljahresabschnitt und das Halbjahr der gymnasialen Oberstufe gegeben ist.
	 * Dies wird nur durchgeführt, wenn Kurse für die gymnasiale Oberstufe angelegt sind und es keine Leistungsdaten
	 * für Oberstufenkursen bei den Schüler gibt, welche bereits Noten beinhalten.
	 *
	 * @param conn       die aktuelle Datenbankverbindung
	 * @param halbjahr   das Halbjahr
	 * @param abschnitt  der Schuljahresabschnitt
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static void deleteOberstufenKurseUndLeistungsdaten(final DBEntityManager conn, final GostHalbjahr halbjahr,
			final Schuljahresabschnitt abschnitt) throws ApiOperationException {
		// Bestimme zunächst die Kurse der gymnasialen Oberstufe für diesen Fall
		final Set<DTOKurs> kurse = getOberstufenKurseInAbschnitt(conn, halbjahr, abschnitt);
		if (kurse.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es konnten keine Kurse für die Gymnasiale Oberstufe gefunden werden, so dass auch keine Leistungsdaten entfernt werden können.");
		// Bestimme nun die Lernabschnitte von Schülern des zugehörigen Abiturjahrgangs und die Leistungsdaten, die ggf. entfernt werden müssen
		final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = getLernabschnitteFuerGostHalbjahrInAbschnitt(conn, halbjahr, abschnitt);
		final List<DTOSchuelerLeistungsdaten> listRemove = new ArrayList<>();
		if (!lernabschnitte.isEmpty()) {
			final Map<Long, DTOSchuelerLernabschnittsdaten> mapLernabschnittsdaten = lernabschnitte.stream().collect(Collectors.toMap(l -> l.ID, l -> l));
			// Bestimme die Schueler-Leistungsdaten zu den Lernabschnitten
			final List<DTOSchuelerLeistungsdaten> leistungsdaten = conn.queryList(DTOSchuelerLeistungsdaten.QUERY_LIST_BY_ABSCHNITT_ID,
					DTOSchuelerLeistungsdaten.class, mapLernabschnittsdaten.keySet());
			// ... und prüfe diese Lernabschnitte, ob sie Einträge für die gymnasiale Oberstufe beinhalten
			for (final DTOSchuelerLeistungsdaten leistung : leistungsdaten) {
				// Prüfe, ob es sich bei den Leistungsdaten um einen Kurs der gymnasialen Oberstufe handelt oder nicht ...
				final GostKursart kursart = GostKursart.fromKuerzel(leistung.KursartAllg);
				if (kursart == null)
					continue;
				// ... es handelt sich um einen Kurs der gymnasialen Oberstufe, also prüfe zunächst, ob Noten vorliegen, wenn ja, dann darf diese Operation nicht beendet werden
				final DTOSchuelerLernabschnittsdaten lernabschnitt = mapLernabschnittsdaten.get(leistung.Abschnitt_ID);
				final Note noteQuartal = Note.fromKuerzel(leistung.NotenKrzQuartal);
				final Note noteHalbjahr = Note.fromKuerzel(leistung.NotenKrz);
				if ((!((noteQuartal == Note.KEINE) || (noteQuartal == Note.ATTEST))) || (!((noteHalbjahr == Note.KEINE) || (noteHalbjahr == Note.ATTEST))))
					throw new ApiOperationException(Status.BAD_GATEWAY,
							"Es liegen bereits Noten für Leistungsdaten bei mindestens einem Schüler (ID=%d) vor, so dass die Leistungsdaten nicht entfernt werden dürfen."
									.formatted(lernabschnitt.Schueler_ID));
				// ... die Leistungsdaten können entfernt werden
				listRemove.add(leistung);
			}
		}
		// Entfernen der Leistungsdaten
		if (!listRemove.isEmpty()) {
			conn.transactionRemoveAll(listRemove);
			conn.transactionFlush();
		}
		// Entferne die Menge der Kurse der gymnasialen Oberstufe für das Halbjahr dieses Abiturjahrgangs
		conn.transactionRemoveAll(kurse);
		conn.transactionFlush();
	}


	/**
	 * Prüft, ob der Schüler bei dem angegebehen GOSt-Halbjahr des angegeben Halbjahres an der Schule gewesen ist.
	 *
	 * @param dto                        der Schüler
	 * @param halbjahr                   das GOSt-Halbjahr
	 * @param abijahrgang                der Abiturjahrgang
	 * @param mapSchuljahresabschnitte   die Schuljahresabschnitte, welche ihrer ID zugeordnet sind
	 *
	 * @return true, wenn der Schüler an der Schule ist, und ansonsten false
	 */
	public static boolean pruefeIstAnSchule(final DTOSchueler dto, final GostHalbjahr halbjahr, final int abijahrgang,
			final Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte) {
		// Ist ein aktueller Schuljahresabschnitt zugewiesen? Das ist notwendig, wenn der Schüler an der Schule ist oder war
		if (dto.Schuljahresabschnitts_ID == null)
			return false;
		// Dieser Schuljahresabschnitt muss auch gültig sein
		final DTOSchuljahresabschnitte schuljahresabschnitt = mapSchuljahresabschnitte.get(dto.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			return false;
		// In dem Fall, dass der Schüler bereits abgegangen ist, wird das Entlassdatum und der Schuljahresabschnitt mit dem Schuljahresabschnitt des GOSt-Halbjahres abgegleichen
		final SchuelerStatus status = SchuelerStatus.data().getWertByID(dto.idStatus);
		if ((status == SchuelerStatus.ABGANG) || (status == SchuelerStatus.ABSCHLUSS)) {
			final int blockungSchuljahr = halbjahr.getSchuljahrFromAbiturjahr(abijahrgang);
			final int[] entlassung = (dto.Entlassdatum == null) ? null : DateUtils.getSchuljahrUndHalbjahrFromDateISO8601(dto.Entlassdatum);
			if (entlassung == null) {
				// Prüfe, ob der aktuelle Schuljahresabschnitt des Schülers < dem Schuljahresabschnitt des GOSt-Halbjahres / der Blockung ist -> dann muss der Schüler ignoriert werden
				if ((schuljahresabschnitt.Jahr < blockungSchuljahr)
						|| ((schuljahresabschnitt.Jahr == blockungSchuljahr) && (schuljahresabschnitt.Abschnitt < halbjahr.halbjahr)))
					return false;
			} else {
				// Prüfe, ob der Schuljahresabschnitt der Entlassung des Schülers < dem Schuljahresabschnitt des GOSt-Halbjahres / der Blockung ist -> dann muss der Schüler ignoriert werden
				if ((entlassung[0] < blockungSchuljahr) || ((entlassung[0] == blockungSchuljahr) && (entlassung[1] < halbjahr.halbjahr)))
					return false;
			}
		}
		return true;
	}


	/**
	 * Bestimmt für den übergegebenen Lernabschnitt eines Schülers das zugehörige Abiturjahr.
	 *
	 * @param schulform         die Schulform der Schule des Schülers
	 * @param schulgliederung   die Schulgliederung des Schülers
	 * @param schuljahr         das aktuelle Schuljahr, in welchem sich der Schüler befindet
	 * @param jahrgang          der Jahrgang des Schülers
	 *
	 * @return das voraussichtliche Jahr des Abiturs
	 */
	public static Integer getAbiturjahr(final Schulform schulform, final Schulgliederung schulgliederung, final int schuljahr, final Jahrgaenge jahrgang) {
		if ((schulgliederung == null) || (jahrgang == null))
			return null;
		return GostAbiturjahrUtils.getGostAbiturjahr(schulform, schulgliederung, schuljahr, jahrgang.daten(schuljahr).kuerzel);
	}


	private static void getLeistung(final GostLeistungen daten, final DTOSchuelerLernabschnittsdaten lernabschnitt,
			final DTOSchuelerLeistungsdaten leistung, final DTOSchuljahresabschnitte abschnittLeistungsdaten,
			final Jahrgaenge jahrgang, final GostHalbjahr halbjahr, final Sprachendaten sprachendaten,
			final GostFaecherManager gostFaecher, final Map<String, GostLeistungenFachwahl> faecher) {
		// Prüfe, ob die Kursart eine Kursart der Oberstufe ist.
		final GostKursart kursart = GostKursart.fromKuerzel(leistung.KursartAllg);
		if (kursart == null)
			return;
		// Prüfe, ob das Fach ein Fach der Oberstufe ist
		final GostFach gostFach = gostFaecher.get(leistung.Fach_ID);
		if (gostFach == null)
			return;
		// Füge die Fächer aus den Leistungsdaten zunächst in die HashMap ein...
		GostLeistungenFachwahl fach = faecher.get(gostFach.kuerzelAnzeige);
		if (fach == null) {
			fach = new GostLeistungenFachwahl();
			fach.fach = gostFach;
			faecher.put(gostFach.kuerzelAnzeige, fach);
		}
		// Prüfe ggf., ob eine Sprache fortgeführt wurde oder nicht
		final String fremdsprache = GostFachUtils.getFremdsprache(gostFach);
		if (fremdsprache != null)
			fach.istFSNeu = (SprachendatenUtils.istNeueinsetzbareSpracheInGOSt(sprachendaten, fremdsprache));

		final GostAbiturFach tmpAbiFach = GostAbiturFach.fromIDString(leistung.AbiFach);
		fach.abiturfach = (tmpAbiFach == null) ? null : tmpAbiFach.id;

		// Füge eine Belegung der Kurse für die einzelnen Fächer in dem Halbjahr ein
		final GostLeistungenFachbelegung belegung = new GostLeistungenFachbelegung();
		belegung.id = leistung.ID;
		belegung.schuljahr = abschnittLeistungsdaten.Jahr;
		belegung.halbjahrKuerzel = halbjahr.kuerzel;
		belegung.abschnitt = abschnittLeistungsdaten.Abschnitt;
		belegung.abschnittGewertet = lernabschnitt.SemesterWertung;
		belegung.jahrgang = jahrgang.daten(belegung.schuljahr).kuerzel;
		belegung.idKurs = leistung.Kurs_ID;
		belegung.lehrer = leistung.Fachlehrer_ID;
		belegung.notenKuerzel = leistung.NotenKrz;
		belegung.kursartKuerzel = kursart.kuerzel;
		belegung.istSchriftlich = (kursart == GostKursart.LK)
				|| ((kursart == GostKursart.GK) && (("GKS".equals(leistung.Kursart))
						|| ("AB3".equals(leistung.Kursart))
						|| ("AB4".equals(leistung.Kursart) && (halbjahr != GostHalbjahr.Q22))));
		belegung.bilingualeSprache = gostFach.biliSprache;
		belegung.wochenstunden = (leistung.Wochenstunden == null)
				? kursart.getWochenstunden(fach.istFSNeu)
				: leistung.Wochenstunden;
		belegung.fehlstundenGesamt = (leistung.FehlStd == null) ? 0 : leistung.FehlStd;
		belegung.fehlstundenUnentschuldigt = (leistung.uFehlStd == null) ? 0 : leistung.uFehlStd;
		fach.belegungen.add(belegung);

		// Ermittle ggf. das Projektkursthema und die zughörigen Leitfächer
		if (kursart == GostKursart.PJK) {
			daten.projektkursLeitfach1Kuerzel = gostFach.projektKursLeitfach1Kuerzel;
			daten.projektkursLeitfach2Kuerzel = gostFach.projektKursLeitfach2Kuerzel;
			if ((leistung.Lernentw != null) && (!"".equals(leistung.Lernentw)))
				daten.projektkursThema = leistung.Lernentw;
		}
	}


	/**
	 * Ermittelt die Leistungsdaten der gymnasialen Oberstufe für den Schüler mit der
	 * angegebenen ID aus der Datenbank.
	 *
	 * @param conn   die Datenbank-Verbindung
	 * @param id     die ID des Schülers
	 *
	 * @return die Leistungsdaten der gymnasialen Oberstufe für den Schüler mit der
	 *         angegebenen ID
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostLeistungen getLeistungsdaten(final DBEntityManager conn, final long id) throws ApiOperationException {
		final Schulform schulform = conn.getUser().schuleGetSchulform();
		final Map<Long, DTOJahrgang> mapJahrgaenge = conn.queryAll(DTOJahrgang.class).stream().collect(Collectors.toMap(j -> j.ID, j -> j));

		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND);

		final Map<Long, DTOSchuljahresabschnitte> schuljahresabschnitte =
				conn.queryAll(DTOSchuljahresabschnitte.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));
		final DTOSchuljahresabschnitte abschnittSchueler = schuljahresabschnitte.get(schueler.Schuljahresabschnitts_ID);
		if (abschnittSchueler == null)
			throw new ApiOperationException(Status.NOT_FOUND);

		final Sprachendaten sprachendaten = DBUtilsSchueler.getSchuelerSprachendaten(conn, id);

		// Bestimme alle Lernabschnitte der Oberstufe des Schülers, sortiert nach dem Schuljahr und dem Abschnitt
		final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHUELER_ID,
				DTOSchuelerLernabschnittsdaten.class, id).stream()
				.sorted((l1, l2) -> {
					final DTOSchuljahresabschnitte a1 = schuljahresabschnitte.get(l1.Schuljahresabschnitts_ID);
					final DTOSchuljahresabschnitte a2 = schuljahresabschnitte.get(l2.Schuljahresabschnitts_ID);
					return (a1.Jahr != a2.Jahr) ? Integer.compare(a1.Jahr, a2.Jahr) : Integer.compare(a1.Abschnitt, a2.Abschnitt);
				})
				.toList();

		// Bestimme den neuesten Lernabschnitt des Schülers. Aus diesem kann das voraussichliche Abiturjahr ermittelt werden.
		final DTOSchuelerLernabschnittsdaten aktLernabschnitt = lernabschnitte.get(lernabschnitte.size() - 1);
		final Schulgliederung schulgliederung = (aktLernabschnitt.Schulgliederung == null)
				? Schulgliederung.getDefault(schulform)
				: Schulgliederung.data().getWertByKuerzel(aktLernabschnitt.Schulgliederung);
		final DTOJahrgang dtoAktJahrgang = mapJahrgaenge.get(aktLernabschnitt.Jahrgang_ID);
		final Jahrgaenge aktJahrgang =
				((dtoAktJahrgang == null) || (dtoAktJahrgang.ASDJahrgang == null)) ? null : Jahrgaenge.data().getWertBySchluessel(dtoAktJahrgang.ASDJahrgang);
		final Integer abiturjahr = getAbiturjahr(schulform, schulgliederung, abschnittSchueler.Jahr, aktJahrgang);
		final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherManager(abschnittSchueler.Jahr, conn, abiturjahr);

		// Ermittle nun die Leistungsdaten aus den Lernabschnitten
		final GostLeistungen daten = new GostLeistungen();
		daten.id = schueler.ID;
		daten.aktuellesSchuljahr = abschnittSchueler.Jahr;
		daten.aktuellerJahrgang = (aktJahrgang == null) ? null : aktJahrgang.daten(abschnittSchueler.Jahr).kuerzel;
		daten.sprachendaten = sprachendaten;
		final String biliZweig = aktLernabschnitt.BilingualerZweig;
		if ((biliZweig != null) && (!"".equals(biliZweig)))
			daten.bilingualeSprache = biliZweig.toUpperCase().substring(0, 1);
		// eine Map zur temporären Speicherung der Fächer -> muss später noch sortiert werden
		final Map<String, GostLeistungenFachwahl> faecher = new HashMap<>();
		for (final DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte) {
			final DTOSchuljahresabschnitte abschnittLeistungsdaten = schuljahresabschnitte.get(lernabschnitt.Schuljahresabschnitts_ID);
			if (abschnittLeistungsdaten == null)
				continue;

			final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(lernabschnitt.Jahrgang_ID);
			final Jahrgaenge jahrgang =
					((dtoJahrgang == null) || (dtoJahrgang.ASDJahrgang == null)) ? null : Jahrgaenge.data().getWertBySchluessel(dtoJahrgang.ASDJahrgang);
			if (jahrgang == null)
				continue;
			final GostHalbjahr halbjahr =
					GostHalbjahr.fromJahrgangUndHalbjahr(jahrgang.daten(abschnittSchueler.Jahr).kuerzel, abschnittLeistungsdaten.Abschnitt);
			if (halbjahr == null)
				continue;
			if (Boolean.TRUE.equals(lernabschnitt.SemesterWertung))
				daten.bewertetesHalbjahr[halbjahr.id] = true;
			final List<DTOSchuelerLeistungsdaten> leistungen = conn.queryList(DTOSchuelerLeistungsdaten.QUERY_BY_ABSCHNITT_ID,
					DTOSchuelerLeistungsdaten.class, lernabschnitt.ID);
			if (leistungen.isEmpty())
				daten.bewertetesHalbjahr[halbjahr.id] = false;
			for (final DTOSchuelerLeistungsdaten leistung : leistungen)
				getLeistung(daten, lernabschnitt, leistung, abschnittLeistungsdaten, jahrgang, halbjahr, sprachendaten, gostFaecher, faecher);
		}
		// Sortiere Fächer anhand der SII-Sortierung der Fächer
		faecher.values().stream()
				.sorted((a, b) -> Integer.compare(a.fach.sortierung, b.fach.sortierung))
				.forEach(daten.faecher::add);
		return daten;
	}



	/**
	 * Ermittelt die Leistungsdaten der gymnasialen Oberstufe für die Schüler mit den
	 * angegebenen IDs aus der Datenbank.
	 *
	 * @param schuljahr   das Schuljahr der Schule
	 * @param conn        die Datenbank-Verbindung
	 * @param ids         die IDs der Schüler
	 *
	 * @return die Leistungsdaten der gymnasialen Oberstufe für die Schüler mit den angegebenen IDs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Map<Long, GostLeistungen> getLeistungsdaten(final int schuljahr, final DBEntityManager conn, final List<Long> ids)
			throws ApiOperationException {
		final Map<Long, DTOJahrgang> mapJahrgaenge = conn.queryAll(DTOJahrgang.class).stream().collect(Collectors.toMap(j -> j.ID, j -> j));
		// TODO Ermittle die Abi-Jahrgangsspezifische Fächerliste !
		final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherManager(schuljahr, conn, null);

		final Map<Long, DTOSchuljahresabschnitte> schuljahresabschnitte =
				conn.queryAll(DTOSchuljahresabschnitte.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));

		// TODO optimize DB-Access by using db queries with IN (...)
		final HashMap<Long, GostLeistungen> result = new HashMap<>();
		for (final Long id : ids) {
			if (id == null)
				throw new ApiOperationException(Status.BAD_REQUEST);

			final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
			if (schueler == null)
				throw new ApiOperationException(Status.NOT_FOUND);

			final DTOSchuljahresabschnitte abschnittSchueler = schuljahresabschnitte.get(schueler.Schuljahresabschnitts_ID);
			if (abschnittSchueler == null)
				throw new ApiOperationException(Status.NOT_FOUND);

			final Sprachendaten sprachendaten = DBUtilsSchueler.getSchuelerSprachendaten(conn, id);

			// Bestimme alle Lernabschnitte der Oberstufe des Schülers, sortiert nach dem Schuljahr und dem Abschnitt
			final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHUELER_ID,
					DTOSchuelerLernabschnittsdaten.class, id).stream()
					.sorted((l1, l2) -> {
						final DTOSchuljahresabschnitte a1 = schuljahresabschnitte.get(l1.Schuljahresabschnitts_ID);
						final DTOSchuljahresabschnitte a2 = schuljahresabschnitte.get(l2.Schuljahresabschnitts_ID);
						return (a1.Jahr != a2.Jahr) ? Integer.compare(a1.Jahr, a2.Jahr) : Integer.compare(a1.Abschnitt, a2.Abschnitt);
					})
					.toList();

			// Bestimme den neuesten Lernabschnitt des Schülers. Aus diesem kann das voraussichliche Abiturjahr ermittelt werden.
			final DTOSchuelerLernabschnittsdaten aktLernabschnitt = lernabschnitte.get(lernabschnitte.size() - 1);
			final DTOJahrgang dtoAktJahrgang = mapJahrgaenge.get(aktLernabschnitt.Jahrgang_ID);
			final Jahrgaenge aktJahrgang =
					((dtoAktJahrgang == null) || (dtoAktJahrgang.ASDJahrgang == null)) ? null
							: Jahrgaenge.data().getWertBySchluessel(dtoAktJahrgang.ASDJahrgang);

			// Ermittle nun die Leistungsdaten aus den Lernabschnitten
			final GostLeistungen daten = new GostLeistungen();
			daten.id = schueler.ID;
			daten.aktuellesSchuljahr = abschnittSchueler.Jahr;
			daten.aktuellerJahrgang = (aktJahrgang == null) ? null : aktJahrgang.daten(schuljahr).kuerzel;
			daten.sprachendaten = sprachendaten;
			final String biliZweig = aktLernabschnitt.BilingualerZweig;
			if ((biliZweig != null) && (!"".equals(biliZweig)))
				daten.bilingualeSprache = biliZweig.toUpperCase().substring(0, 1);
			// eine HashMap zur temporären Speicherung der Fächer -> muss später noch sortiert werden
			final HashMap<String, GostLeistungenFachwahl> faecher = new HashMap<>();
			for (final DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte) {
				final DTOSchuljahresabschnitte abschnittLeistungsdaten = schuljahresabschnitte.get(lernabschnitt.Schuljahresabschnitts_ID);
				if (abschnittLeistungsdaten == null)
					continue;
				final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(lernabschnitt.Jahrgang_ID);
				final Jahrgaenge jahrgang =
						((dtoJahrgang == null) || (dtoJahrgang.ASDJahrgang == null)) ? null : Jahrgaenge.data().getWertBySchluessel(dtoJahrgang.ASDJahrgang);
				if ((jahrgang == null) || !JahrgaengeUtils.istGymOb(jahrgang.daten(schuljahr).kuerzel))
					continue;
				final GostHalbjahr halbjahr = GostHalbjahr.fromJahrgangUndHalbjahr(jahrgang.daten(schuljahr).kuerzel, abschnittLeistungsdaten.Abschnitt);
				if (halbjahr == null)
					continue;
				if (Boolean.TRUE.equals(lernabschnitt.SemesterWertung))
					daten.bewertetesHalbjahr[halbjahr.id] = true;

				final List<DTOSchuelerLeistungsdaten> leistungen = conn.queryList(DTOSchuelerLeistungsdaten.QUERY_BY_ABSCHNITT_ID,
						DTOSchuelerLeistungsdaten.class, lernabschnitt.ID);
				if (leistungen.isEmpty())
					daten.bewertetesHalbjahr[halbjahr.id] = false;
				for (final DTOSchuelerLeistungsdaten leistung : leistungen)
					getLeistung(daten, lernabschnitt, leistung, abschnittLeistungsdaten, jahrgang, halbjahr, sprachendaten, gostFaecher, faecher);
			}
			// Sortiere Fächer anhand der Sortierung der Fächer
			faecher.values().stream()
					.sorted((a, b) -> Integer.compare(a.fach.sortierung, b.fach.sortierung))
					.forEach(daten.faecher::add);
			result.put(id, daten);
		}
		return result;
	}


	/**
	 * Ermittelt die Leistungsdaten der gymnasialen Oberstufe für die Schüler mit den
	 * angegebenen IDs aus der Datenbank.
	 *
	 * @param ids                          die IDs der Schüler
	 * @param gostFaecherManager           der Manager für die Fächer des Abiturjahrgangs
	 * @param mapSchuljahresabschnitte     die Schuljahresabschnitte
	 * @param mapSchueler                  die DTOs der Schüler
	 * @param mapAlleGostAbschnitte        die Lernabschnitte der Schüler
	 * @param mapLeistungenByAbschnittID   die Leistungsdaten zu den Lernabschnitten
	 * @param mapSprachendaten             die Sprachendaten der Schüler
	 * @param mapJahrgaenge                die Jahrgänge der Schule
	 *
	 * @return die Leistungsdaten der gymnasialen Oberstufe für die Schüler mit den
	 *         angegebenen IDs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Map<Long, GostLeistungen> getLeistungsdatenFromDTOs(final List<Long> ids, final GostFaecherManager gostFaecherManager,
			final Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte, final Map<Long, DTOSchueler> mapSchueler,
			final Map<Long, List<DTOSchuelerLernabschnittsdaten>> mapAlleGostAbschnitte,
			final Map<Long, List<DTOSchuelerLeistungsdaten>> mapLeistungenByAbschnittID,
			final Map<Long, Sprachendaten> mapSprachendaten,
			final Map<Long, DTOJahrgang> mapJahrgaenge) throws ApiOperationException {
		final HashMap<Long, GostLeistungen> result = new HashMap<>();
		for (final long id : ids) {
			final DTOSchueler schueler = mapSchueler.get(id);
			if (schueler == null)
				throw new ApiOperationException(Status.NOT_FOUND);

			final DTOSchuljahresabschnitte abschnittSchueler = mapSchuljahresabschnitte.get(schueler.Schuljahresabschnitts_ID);
			if (abschnittSchueler == null)
				throw new ApiOperationException(Status.NOT_FOUND);

			final Sprachendaten sprachendaten = mapSprachendaten.get(id);

			// Bestimme alle Lernabschnitte der Oberstufe des Schülers, sortiert nach dem Schuljahr und dem Abschnitt
			final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = mapAlleGostAbschnitte.get(id)
					.stream()
					.sorted((l1, l2) -> {
						final DTOSchuljahresabschnitte a1 = mapSchuljahresabschnitte.get(l1.Schuljahresabschnitts_ID);
						final DTOSchuljahresabschnitte a2 = mapSchuljahresabschnitte.get(l2.Schuljahresabschnitts_ID);
						return (a1.Jahr != a2.Jahr) ? Integer.compare(a1.Jahr, a2.Jahr) : Integer.compare(a1.Abschnitt, a2.Abschnitt);
					})
					.toList();

			// Bestimme den neuesten Lernabschnitt des Schülers. Aus diesem kann das voraussichliche Abiturjahr ermittelt werden.
			final DTOSchuelerLernabschnittsdaten aktLernabschnitt = lernabschnitte.isEmpty() ? null : lernabschnitte.get(lernabschnitte.size() - 1);

			// Ermittle nun die Leistungsdaten aus den Lernabschnitten
			final GostLeistungen daten = new GostLeistungen();
			daten.id = schueler.ID;
			daten.aktuellesSchuljahr = abschnittSchueler.Jahr;
			if (aktLernabschnitt == null) {
				daten.aktuellerJahrgang = null;
				daten.bilingualeSprache = null;
			} else {
				final DTOJahrgang jg = (aktLernabschnitt.Jahrgang_ID == null) ? null : mapJahrgaenge.get(aktLernabschnitt.Jahrgang_ID);
				daten.aktuellerJahrgang = (jg == null) ? null : Jahrgaenge.data().getWertByKuerzel(jg.ASDJahrgang).daten(daten.aktuellesSchuljahr).kuerzel;
				final String biliZweig = aktLernabschnitt.BilingualerZweig;
				if ((biliZweig != null) && (!"".equals(biliZweig)))
					daten.bilingualeSprache = biliZweig.toUpperCase().substring(0, 1);
			}
			daten.sprachendaten = sprachendaten;
			// eine HashMap zur temporären Speicherung der Fächer -> muss später noch sortiert werden
			final HashMap<String, GostLeistungenFachwahl> faecher = new HashMap<>();
			for (final DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte) {
				final DTOSchuljahresabschnitte abschnittLeistungsdaten = mapSchuljahresabschnitte.get(lernabschnitt.Schuljahresabschnitts_ID);
				if (abschnittLeistungsdaten == null)
					continue;
				final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(lernabschnitt.Jahrgang_ID);
				final Jahrgaenge jahrgang = ((dtoJahrgang == null) || (dtoJahrgang.ASDJahrgang == null))
						? null : Jahrgaenge.data().getWertByKuerzel(dtoJahrgang.ASDJahrgang);
				if ((jahrgang == null) || !JahrgaengeUtils.istGymOb(jahrgang.daten(daten.aktuellesSchuljahr).kuerzel))
					continue;
				final GostHalbjahr halbjahr =
						GostHalbjahr.fromJahrgangUndHalbjahr(jahrgang.daten(daten.aktuellesSchuljahr).kuerzel, abschnittLeistungsdaten.Abschnitt);
				if (halbjahr == null)
					continue;
				if (Boolean.TRUE.equals(lernabschnitt.SemesterWertung))
					daten.bewertetesHalbjahr[halbjahr.id] = true;

				List<DTOSchuelerLeistungsdaten> leistungen = mapLeistungenByAbschnittID.get(lernabschnitt.ID);
				if (leistungen == null)
					leistungen = new ArrayList<>();
				if (leistungen.isEmpty())
					daten.bewertetesHalbjahr[halbjahr.id] = false;
				for (final DTOSchuelerLeistungsdaten leistung : leistungen)
					getLeistung(daten, lernabschnitt, leistung, abschnittLeistungsdaten, jahrgang, halbjahr, sprachendaten, gostFaecherManager, faecher);
			}
			// Sortiere Fächer anhand der Sortierung der Fächer
			faecher.values().stream()
					.sorted((a, b) -> Integer.compare(a.fach.sortierung, b.fach.sortierung))
					.forEach(daten.faecher::add);
			result.put(id, daten);
		}
		return result;
	}

}
