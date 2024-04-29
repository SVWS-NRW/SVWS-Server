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
import de.svws_nrw.core.data.schueler.Sprachendaten;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.gost.GostAbiturjahrUtils;
import de.svws_nrw.core.utils.gost.GostFachUtils;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.schueler.DBUtilsSchueler;
import de.svws_nrw.data.schule.SchulUtils;
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
		final Schulform schulform = schule.Schulform;
		if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
			throw new ApiOperationException(Status.NOT_FOUND);
		return schule;
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
			final GostHalbjahr halbjahr, final DTOSchuljahresabschnitte abschnitt) {
		final List<DTOKurs> kurse = conn.queryList("SELECT e FROM DTOKurs e WHERE e.ASDJahrgang = ?1 AND e.Schuljahresabschnitts_ID = ?2",
				DTOKurs.class, halbjahr.jahrgang, abschnitt.ID);
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
			final DTOSchuljahresabschnitte abschnitt) {
		final List<DTOKurs> kurse = conn.queryList("SELECT e FROM DTOKurs e WHERE e.ASDJahrgang = ?1 AND e.Schuljahresabschnitts_ID = ?2",
			DTOKurs.class, halbjahr.jahrgang, abschnitt.ID);
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
	public static boolean pruefeHatNotenFuerOberstufeInAbschnitt(final DBEntityManager conn,
			final GostHalbjahr halbjahr, final DTOSchuljahresabschnitte abschnitt) {
    	// Bestimme alle Jahrgänge der Schule, welche den passenden ASD-Jahrgang haben
    	final List<DTOJahrgang> listJahrgaengeGost = conn.queryNamed("DTOJahrgang.asdjahrgang", halbjahr.jahrgang, DTOJahrgang.class);
    	final List<Long> listJahrgaengeGostIDs = listJahrgaengeGost.stream().map(j -> j.ID).toList();
    	if (listJahrgaengeGostIDs.isEmpty())
    		return false;
    	// Bestimme die SchuelerLernabschnitte von Schülern der Stufe
    	final List<DTOSchuelerLernabschnittsdaten> schuelerLernabschnittsdaten = conn.queryList(
    		"SELECT sla FROM DTOSchuelerLernabschnittsdaten sla JOIN DTOSchueler s ON s.Geloescht <> true AND sla.Schueler_ID = s.ID AND sla.Schuljahresabschnitts_ID = ?1 AND sla.Jahrgang_ID IN ?2",
    		DTOSchuelerLernabschnittsdaten.class, abschnitt.ID, listJahrgaengeGostIDs);
    	final List<Long> idsSchuelerLernabschnittsdaten = schuelerLernabschnittsdaten.stream().map(l -> l.ID).toList();
    	if (idsSchuelerLernabschnittsdaten.isEmpty())
    		return false;
    	// Bestimme die Schueler-Leistungsdaten zu den Lernabschnitten, welche einen (Quartals-)Noteneintrag aufweisen
    	final List<DTOSchuelerLeistungsdaten> leistungsdaten = conn.queryList("SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID IN ?1 AND NOT (e.NotenKrz IS NULL AND e.NotenKrzQuartal IS NULL)", DTOSchuelerLeistungsdaten.class,
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
	 * Prüft, ob der Schüler bei dem angegebehen GOSt-Halbjahr des angegeben Halbjahres an der Schule gewesen ist.
	 *
	 * @param dto                        der Schüler
	 * @param halbjahr                   das GOSt-Halbjahr
	 * @param abijahrgang                der Abiturjahrgang
	 * @param mapSchuljahresabschnitte   die Schuljahresabschnitte, welche ihrer ID zugeordnet sind
	 *
	 * @return true, wenn der Schüler an der Schule ist, und ansonsten false
	 */
	public static boolean pruefeIstAnSchule(final DTOSchueler dto, final GostHalbjahr halbjahr, final int abijahrgang, final Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte) {
		// Ist ein aktueller Schuljahresabschnitt zugewiesen? Das ist notwendig, wenn der Schüler an der Schule ist oder war
		if (dto.Schuljahresabschnitts_ID == null)
			return false;
		// Dieser Schuljahresabschnitt muss auch gültig sein
		final DTOSchuljahresabschnitte schuljahresabschnitt = mapSchuljahresabschnitte.get(dto.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			return false;
		// In dem Fall, dass der Schüler bereits abgegangen ist, wird das Entlassdatum und der Schuljahresabschnitt mit dem Schuljahresabschnitt des GOSt-Halbjahres abgegleichen
		if ((dto.Status == SchuelerStatus.ABGANG) || (dto.Status == SchuelerStatus.ABSCHLUSS)) {
			final int blockungSchuljahr = halbjahr.getSchuljahrFromAbiturjahr(abijahrgang);
			final int[] entlassung = (dto.Entlassdatum == null) ? null : DateUtils.getSchuljahrUndHalbjahrFromDateISO8601(dto.Entlassdatum);
			if (entlassung == null) {
				// Prüfe, ob der aktuelle Schuljahresabschnitt des Schülers < dem Schuljahresabschnitt des GOSt-Halbjahres / der Blockung ist -> dann muss der Schüler ignoriert werden
				if ((schuljahresabschnitt.Jahr < blockungSchuljahr) || ((schuljahresabschnitt.Jahr == blockungSchuljahr) && (schuljahresabschnitt.Abschnitt < halbjahr.halbjahr)))
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
		return GostAbiturjahrUtils.getGostAbiturjahr(schulform, schulgliederung, schuljahr, jahrgang.daten.kuerzel);
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
		final @NotNull DTOEigeneSchule schule = SchulUtils.getDTOSchule(conn);
		final Map<Long, DTOJahrgang> mapJahrgaenge = conn.queryAll(DTOJahrgang.class).stream().collect(Collectors.toMap(j -> j.ID, j -> j));

		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND);

		final Map<Long, DTOSchuljahresabschnitte> schuljahresabschnitte = conn.queryAll(DTOSchuljahresabschnitte.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));
		final DTOSchuljahresabschnitte abschnittSchueler = schuljahresabschnitte.get(schueler.Schuljahresabschnitts_ID);
		if (abschnittSchueler == null)
			throw new ApiOperationException(Status.NOT_FOUND);

		final Sprachendaten sprachendaten = DBUtilsSchueler.getSchuelerSprachendaten(conn, id);

		// Bestimme alle Lernabschnitte der Oberstufe des Schülers, sortiert nach dem Schuljahr und dem Abschnitt
		final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryNamed("DTOSchuelerLernabschnittsdaten.schueler_id", id, DTOSchuelerLernabschnittsdaten.class)
				.stream()
				.sorted((l1, l2) -> {
					final DTOSchuljahresabschnitte a1 = schuljahresabschnitte.get(l1.Schuljahresabschnitts_ID);
					final DTOSchuljahresabschnitte a2 = schuljahresabschnitte.get(l2.Schuljahresabschnitts_ID);
					return (a1.Jahr != a2.Jahr) ? Integer.compare(a1.Jahr, a2.Jahr) : Integer.compare(a1.Abschnitt, a2.Abschnitt);
				})
				.toList();

		// Bestimme den neuesten Lernabschnitt des Schülers. Aus diesem kann das voraussichliche Abiturjahr ermittelt werden.
		final DTOSchuelerLernabschnittsdaten aktLernabschnitt = lernabschnitte.get(lernabschnitte.size() - 1);
    	final Schulgliederung schulgliederung = aktLernabschnitt.Schulgliederung == null
    			? Schulgliederung.getDefault(schule.Schulform)
    			: aktLernabschnitt.Schulgliederung;
    	final DTOJahrgang dtoAktJahrgang = mapJahrgaenge.get(aktLernabschnitt.Jahrgang_ID);
    	final Jahrgaenge aktJahrgang = (dtoAktJahrgang == null) || (dtoAktJahrgang.ASDJahrgang == null) ? null : Jahrgaenge.getByKuerzel(dtoAktJahrgang.ASDJahrgang);
		final Integer abiturjahr = getAbiturjahr(schule.Schulform, schulgliederung, abschnittSchueler.Jahr, aktJahrgang);
    	final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherManager(conn, abiturjahr);

		// Ermittle nun die Leistungsdaten aus den Lernabschnitten
		final GostLeistungen daten = new GostLeistungen();
		daten.id = schueler.ID;
		daten.aktuellesSchuljahr = abschnittSchueler.Jahr;
		daten.aktuellerJahrgang = aktJahrgang == null ? null : aktJahrgang.daten.kuerzel;
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
	    	final Jahrgaenge jahrgang = (dtoJahrgang == null) || (dtoJahrgang.ASDJahrgang == null) ? null : Jahrgaenge.getByKuerzel(dtoJahrgang.ASDJahrgang);
	    	if (jahrgang == null)
	    		continue;
			final GostHalbjahr halbjahr = GostHalbjahr.fromJahrgangUndHalbjahr(jahrgang.daten.kuerzel, abschnittLeistungsdaten.Abschnitt);
			if (halbjahr == null)
				continue;
			if (Boolean.TRUE.equals(lernabschnitt.SemesterWertung))
				daten.bewertetesHalbjahr[halbjahr.id] = true;
			final List<DTOSchuelerLeistungsdaten> leistungen = conn.queryNamed("DTOSchuelerLeistungsdaten.abschnitt_id", lernabschnitt.ID, DTOSchuelerLeistungsdaten.class);
			if (leistungen.isEmpty())
				daten.bewertetesHalbjahr[halbjahr.id] = false;
			for (final DTOSchuelerLeistungsdaten leistung : leistungen) {
				// Prüfe, ob die Kursart eine Kursart der Oberstufe ist.
				final GostKursart kursart = GostKursart.fromKuerzel(leistung.KursartAllg);
				if (kursart == null)
					continue;
				// Prüfe, ob das Fach ein Fach der Oberstufe ist
				final GostFach gostFach = gostFaecher.get(leistung.Fach_ID);
				if (gostFach == null)
					continue;
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
				belegung.jahrgang = jahrgang.daten.kuerzel;
				belegung.lehrer = leistung.Fachlehrer_ID;
				belegung.notenKuerzel = leistung.NotenKrz.kuerzel;
				belegung.kursartKuerzel = kursart.kuerzel;
				belegung.istSchriftlich = (kursart == GostKursart.LK)
						|| ((kursart == GostKursart.GK) && (("GKS".equals(leistung.Kursart))
								|| ("AB3".equals(leistung.Kursart))
								|| ("AB4".equals(leistung.Kursart) && (halbjahr != GostHalbjahr.Q22))));
				belegung.bilingualeSprache = gostFach.biliSprache;
				belegung.wochenstunden = leistung.Wochenstunden == null
						? kursart.getWochenstunden(fach.istFSNeu)
						: leistung.Wochenstunden;
				belegung.fehlstundenGesamt = leistung.FehlStd == null ? 0 : leistung.FehlStd;
				belegung.fehlstundenUnentschuldigt = leistung.uFehlStd == null ? 0 : leistung.uFehlStd;
				fach.belegungen.add(belegung);

				// Ermittle ggf. das Projektkursthema und die zughörigen Leitfächer
				if (kursart == GostKursart.PJK) {
					daten.projektkursLeitfach1Kuerzel = gostFach.projektKursLeitfach1Kuerzel;
					daten.projektkursLeitfach2Kuerzel = gostFach.projektKursLeitfach2Kuerzel;
					if ((leistung.Lernentw != null) && (!"".equals(leistung.Lernentw)))
						daten.projektkursThema = leistung.Lernentw;
				}
			}
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
	 * @param conn   die Datenbank-Verbindung
	 * @param ids    die IDs der Schüler
	 *
	 * @return die Leistungsdaten der gymnasialen Oberstufe für die Schüler mit den
	 *         angegebenen IDs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Map<Long, GostLeistungen> getLeistungsdaten(final DBEntityManager conn, final List<Long> ids) throws ApiOperationException {
		final Map<Long, DTOJahrgang> mapJahrgaenge = conn.queryAll(DTOJahrgang.class).stream().collect(Collectors.toMap(j -> j.ID, j -> j));
		// TODO Ermittle die Abi-Jahrgangsspezifische Fächerliste !
    	final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherManager(conn, null);

		final Map<Long, DTOSchuljahresabschnitte> schuljahresabschnitte = conn.queryAll(DTOSchuljahresabschnitte.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));

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
			final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryNamed("DTOSchuelerLernabschnittsdaten.schueler_id", id, DTOSchuelerLernabschnittsdaten.class)
					.stream()
					.sorted((l1, l2) -> {
						final DTOSchuljahresabschnitte a1 = schuljahresabschnitte.get(l1.Schuljahresabschnitts_ID);
						final DTOSchuljahresabschnitte a2 = schuljahresabschnitte.get(l2.Schuljahresabschnitts_ID);
						return (a1.Jahr != a2.Jahr) ? Integer.compare(a1.Jahr, a2.Jahr) : Integer.compare(a1.Abschnitt, a2.Abschnitt);
					})
					.toList();

			// Bestimme den neuesten Lernabschnitt des Schülers. Aus diesem kann das voraussichliche Abiturjahr ermittelt werden.
			final DTOSchuelerLernabschnittsdaten aktLernabschnitt = lernabschnitte.get(lernabschnitte.size() - 1);
	    	final DTOJahrgang dtoAktJahrgang = mapJahrgaenge.get(aktLernabschnitt.Jahrgang_ID);
	    	final Jahrgaenge aktJahrgang = (dtoAktJahrgang == null) || (dtoAktJahrgang.ASDJahrgang == null) ? null : Jahrgaenge.getByKuerzel(dtoAktJahrgang.ASDJahrgang);

			// Ermittle nun die Leistungsdaten aus den Lernabschnitten
			final GostLeistungen daten = new GostLeistungen();
			daten.id = schueler.ID;
			daten.aktuellesSchuljahr = abschnittSchueler.Jahr;
			daten.aktuellerJahrgang = aktJahrgang == null ? null : aktJahrgang.daten.kuerzel;
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
		    	final Jahrgaenge jahrgang = (dtoJahrgang == null) || (dtoJahrgang.ASDJahrgang == null) ? null : Jahrgaenge.getByKuerzel(dtoJahrgang.ASDJahrgang);
				if ((jahrgang == null) || !JahrgangsUtils.istGymOb(jahrgang.daten.kuerzel))
					continue;
				final GostHalbjahr halbjahr = GostHalbjahr.fromJahrgangUndHalbjahr(jahrgang.daten.kuerzel, abschnittLeistungsdaten.Abschnitt);
				if (halbjahr == null)
					continue;
				if (Boolean.TRUE.equals(lernabschnitt.SemesterWertung))
					daten.bewertetesHalbjahr[halbjahr.id] = true;

				final List<DTOSchuelerLeistungsdaten> leistungen = conn.queryNamed("DTOSchuelerLeistungsdaten.abschnitt_id", lernabschnitt.ID, DTOSchuelerLeistungsdaten.class);
				if (leistungen.isEmpty())
					daten.bewertetesHalbjahr[halbjahr.id] = false;
				for (final DTOSchuelerLeistungsdaten leistung : leistungen) {
					// Prüfe, ob die Kursart eine Kursart der Oberstufe ist.
					final GostKursart kursart = GostKursart.fromKuerzel(leistung.KursartAllg);
					if (kursart == null)
						continue;
					final GostFach gostFach = gostFaecher.get(leistung.Fach_ID);
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
					belegung.jahrgang = jahrgang.daten.kuerzel;
					belegung.lehrer = leistung.Fachlehrer_ID;
					belegung.notenKuerzel = leistung.NotenKrz.kuerzel;
					belegung.kursartKuerzel = kursart.kuerzel;
					belegung.istSchriftlich = (kursart == GostKursart.LK)
							|| ((kursart == GostKursart.GK) && (("GKS".equals(leistung.Kursart))
									|| ("AB3".equals(leistung.Kursart))
									|| ("AB4".equals(leistung.Kursart) && (halbjahr != GostHalbjahr.Q22))));
					belegung.bilingualeSprache = gostFach.biliSprache;
					belegung.wochenstunden = leistung.Wochenstunden == null
							? kursart.getWochenstunden(fach.istFSNeu)
							: leistung.Wochenstunden;
					belegung.fehlstundenGesamt = leistung.FehlStd;
					belegung.fehlstundenUnentschuldigt = leistung.uFehlStd;
					fach.belegungen.add(belegung);

					// Ermittle ggf. das Projektkursthema und die zughörigen Leitfächer
					if (kursart == GostKursart.PJK) {
						daten.projektkursLeitfach1Kuerzel = gostFach.projektKursLeitfach1Kuerzel;
						daten.projektkursLeitfach2Kuerzel = gostFach.projektKursLeitfach2Kuerzel;
						if ((leistung.Lernentw != null) && (!"".equals(leistung.Lernentw)))
							daten.projektkursThema = leistung.Lernentw;
					}
				}
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
				daten.aktuellerJahrgang = (jg == null) ? null : Jahrgaenge.getByKuerzel(jg.ASDJahrgang).daten.kuerzel;
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
		    	final Jahrgaenge jahrgang = (dtoJahrgang == null) || (dtoJahrgang.ASDJahrgang == null) ? null : Jahrgaenge.getByKuerzel(dtoJahrgang.ASDJahrgang);
				if ((jahrgang == null) || !JahrgangsUtils.istGymOb(jahrgang.daten.kuerzel))
					continue;
				final GostHalbjahr halbjahr = GostHalbjahr.fromJahrgangUndHalbjahr(jahrgang.daten.kuerzel, abschnittLeistungsdaten.Abschnitt);
				if (halbjahr == null)
					continue;
				if (Boolean.TRUE.equals(lernabschnitt.SemesterWertung))
					daten.bewertetesHalbjahr[halbjahr.id] = true;

				List<DTOSchuelerLeistungsdaten> leistungen = mapLeistungenByAbschnittID.get(lernabschnitt.ID);
				if (leistungen == null)
					leistungen = new ArrayList<>();
				if (leistungen.isEmpty())
					daten.bewertetesHalbjahr[halbjahr.id] = false;
				for (final DTOSchuelerLeistungsdaten leistung : leistungen) {
					// Prüfe, ob die Kursart eine Kursart der Oberstufe ist.
					final GostKursart kursart = GostKursart.fromKuerzel(leistung.KursartAllg);
					if (kursart == null)
						continue;
					final GostFach gostFach = gostFaecherManager.get(leistung.Fach_ID);
					if (gostFach == null)
						continue;
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
					belegung.jahrgang = jahrgang.daten.kuerzel;
					belegung.lehrer = leistung.Fachlehrer_ID;
					belegung.notenKuerzel = leistung.NotenKrz.kuerzel;
					belegung.kursartKuerzel = kursart.kuerzel;
					belegung.istSchriftlich = (kursart == GostKursart.LK)
							|| ((kursart == GostKursart.GK) && (("GKS".equals(leistung.Kursart))
									|| ("AB3".equals(leistung.Kursart))
									|| ("AB4".equals(leistung.Kursart) && (halbjahr != GostHalbjahr.Q22))));
					belegung.bilingualeSprache = gostFach.biliSprache;
					belegung.wochenstunden = leistung.Wochenstunden == null
							? kursart.getWochenstunden(fach.istFSNeu)
							: leistung.Wochenstunden;
					belegung.fehlstundenGesamt = leistung.FehlStd == null ? 0 : leistung.FehlStd;
					belegung.fehlstundenUnentschuldigt = leistung.uFehlStd == null ? 0 : leistung.uFehlStd;
					fach.belegungen.add(belegung);

					// Ermittle ggf. das Projektkursthema und die zughörigen Leitfächer
					if (kursart == GostKursart.PJK) {
						daten.projektkursLeitfach1Kuerzel = gostFach.projektKursLeitfach1Kuerzel;
						daten.projektkursLeitfach2Kuerzel = gostFach.projektKursLeitfach2Kuerzel;
						if ((leistung.Lernentw != null) && (!"".equals(leistung.Lernentw)))
							daten.projektkursThema = leistung.Lernentw;
					}
				}
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
