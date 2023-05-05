package de.svws_nrw.data.gost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.abschluss.gost.GostFachManager;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostLeistungen;
import de.svws_nrw.core.data.gost.GostLeistungenFachbelegung;
import de.svws_nrw.core.data.gost.GostLeistungenFachwahl;
import de.svws_nrw.core.data.schueler.Sprachendaten;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.utils.gost.GostAbiturjahrUtils;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
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
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;

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
	 * @throws WebApplicationException    falls keine Schule definiert ist oder die Schulform keine Gymnasiale Oberstufe hat
	 */
	public static DTOEigeneSchule pruefeSchuleMitGOSt(final DBEntityManager conn) throws WebApplicationException {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw OperationError.NOT_FOUND.exception();
		final Schulform schulform = schule.Schulform;
		if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
			throw OperationError.NOT_FOUND.exception();
		return schule;
	}


	/**
	 * Prüft, ob in dem angebenen Schuljahresabschnitt für das angebene Halbjahr der gymnasialen Oberstufe
	 * bereits Kurse der gymnasialen Oberstufe vorhanden sidn oder nicht.
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
	 * Bestimmt für den übergegebenen Lernabschnitt eines Schülers das zugehörige Abiturjahr.
	 *
	 * @param schulform       die Schulform der Schule des Schülers
	 * @param lernabschnitt   der aktuelle Lernabschnitt des Schülers
	 * @param schuljahr       das aktuelle Schuljahr, in welchem sich der Schüler befindet
	 *
	 * @return das voraussichtliche Jahr des Abiturs
	 */
	public static Integer getAbiturjahr(final Schulform schulform, final DTOSchuelerLernabschnittsdaten lernabschnitt, final int schuljahr) {
		if (lernabschnitt == null)
			return null;
		return GostAbiturjahrUtils.getGostAbiturjahr(schulform, lernabschnitt.Schulgliederung, schuljahr, lernabschnitt.ASDJahrgang);
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
	 */
	public static GostLeistungen getLeistungsdaten(final DBEntityManager conn, final long id) {
		final @NotNull DTOEigeneSchule schule = SchulUtils.getDTOSchule(conn);

		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
		if (schueler == null)
			throw OperationError.NOT_FOUND.exception();

		final Map<Long, DTOSchuljahresabschnitte> schuljahresabschnitte = conn.queryAll(DTOSchuljahresabschnitte.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));
		final DTOSchuljahresabschnitte abschnittSchueler = schuljahresabschnitte.get(schueler.Schuljahresabschnitts_ID);
		if (abschnittSchueler == null)
			throw OperationError.NOT_FOUND.exception();

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

		final Integer abiturjahr = getAbiturjahr(schule.Schulform, aktLernabschnitt, abschnittSchueler.Jahr);
    	final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherListeGost(conn, abiturjahr);

		// Ermittle nun die Leistungsdaten aus den Lernabschnitten
		final GostLeistungen daten = new GostLeistungen();
		daten.id = schueler.ID;
		daten.aktuellesSchuljahr = abschnittSchueler.Jahr;
		daten.aktuellerJahrgang = aktLernabschnitt.ASDJahrgang;
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

			final GostHalbjahr halbjahr = GostHalbjahr.fromJahrgangUndHalbjahr(lernabschnitt.ASDJahrgang, abschnittLeistungsdaten.Abschnitt);
			if ((halbjahr != null) && (lernabschnitt.SemesterWertung))
				daten.bewertetesHalbjahr[halbjahr.id] = true;

			final List<DTOSchuelerLeistungsdaten> leistungen = conn.queryNamed("DTOSchuelerLeistungsdaten.abschnitt_id", lernabschnitt.ID, DTOSchuelerLeistungsdaten.class);
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
				final String fremdsprache = GostFachManager.getFremdsprache(gostFach);
				if (fremdsprache != null)
					fach.istFSNeu = (!SprachendatenUtils.istFortfuehrbareSpracheInGOSt(sprachendaten, fremdsprache));

				final GostAbiturFach tmpAbiFach = GostAbiturFach.fromIDString(leistung.AbiFach);
				fach.abiturfach = (tmpAbiFach == null) ? null : tmpAbiFach.id;

				// Füge eine Belegung der Kurse für die einzelnen Fächer in dem Halbjahr ein
				final GostLeistungenFachbelegung belegung = new GostLeistungenFachbelegung();
				belegung.id = leistung.ID;
				belegung.schuljahr = abschnittLeistungsdaten.Jahr;
				belegung.halbjahrKuerzel = (halbjahr == null) ? null : halbjahr.kuerzel;
				belegung.abschnitt = abschnittLeistungsdaten.Abschnitt;
				belegung.abschnittGewertet = lernabschnitt.SemesterWertung;
				belegung.jahrgang = lernabschnitt.ASDJahrgang;
				belegung.lehrer = leistung.Fachlehrer_ID;
				belegung.notenKuerzel = leistung.NotenKrz.kuerzel;
				belegung.kursartKuerzel = kursart.kuerzel;
				belegung.istSchriftlich = (kursart == GostKursart.LK)
						|| ((kursart == GostKursart.GK) && (("GKS".equals(leistung.Kursart))
								|| ("AB3".equals(leistung.Kursart))
								|| ("AB4".equals(leistung.Kursart) && (halbjahr != GostHalbjahr.Q22))));
				belegung.bilingualeSprache = gostFach.biliSprache;
				belegung.wochenstunden = leistung.Wochenstunden;
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
	 */
	public static Map<Long, GostLeistungen> getLeistungsdaten(final DBEntityManager conn, final List<Long> ids) {
		// TODO Ermittle die Abi-Jahrgangsspezifische Fächerliste !
    	final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherListeGost(conn, null);

		final Map<Long, DTOSchuljahresabschnitte> schuljahresabschnitte = conn.queryAll(DTOSchuljahresabschnitte.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));

    	// TODO optimize DB-Access by using db queries with IN (...)
    	final HashMap<Long, GostLeistungen> result = new HashMap<>();
    	for (final Long id : ids) {
    		if (id == null)
    			throw OperationError.BAD_REQUEST.exception();

			final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
			if (schueler == null)
				throw OperationError.NOT_FOUND.exception();

			final DTOSchuljahresabschnitte abschnittSchueler = schuljahresabschnitte.get(schueler.Schuljahresabschnitts_ID);
			if (abschnittSchueler == null)
				throw OperationError.NOT_FOUND.exception();

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

			// Ermittle nun die Leistungsdaten aus den Lernabschnitten
			final GostLeistungen daten = new GostLeistungen();
			daten.id = schueler.ID;
			daten.aktuellesSchuljahr = abschnittSchueler.Jahr;
			daten.aktuellerJahrgang = aktLernabschnitt.ASDJahrgang;
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
				if (!("EF".equals(lernabschnitt.ASDJahrgang) || "Q1".equals(lernabschnitt.ASDJahrgang) || "Q2".equals(lernabschnitt.ASDJahrgang)))
					continue;
				final GostHalbjahr halbjahr = GostHalbjahr.fromJahrgangUndHalbjahr(lernabschnitt.ASDJahrgang, abschnittLeistungsdaten.Abschnitt);
				if (lernabschnitt.SemesterWertung)
					daten.bewertetesHalbjahr[halbjahr.id] = true;

				final List<DTOSchuelerLeistungsdaten> leistungen = conn.queryNamed("DTOSchuelerLeistungsdaten.abschnitt_id", lernabschnitt.ID, DTOSchuelerLeistungsdaten.class);
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
					final String fremdsprache = GostFachManager.getFremdsprache(gostFach);
					if (fremdsprache != null)
						fach.istFSNeu = (!SprachendatenUtils.istFortfuehrbareSpracheInGOSt(sprachendaten, fremdsprache));

					final GostAbiturFach tmpAbiFach = GostAbiturFach.fromIDString(leistung.AbiFach);
					fach.abiturfach = (tmpAbiFach == null) ? null : tmpAbiFach.id;

					// Füge eine Belegung der Kurse für die einzelnen Fächer in dem Halbjahr ein
					final GostLeistungenFachbelegung belegung = new GostLeistungenFachbelegung();
					belegung.id = leistung.ID;
					belegung.schuljahr = abschnittLeistungsdaten.Jahr;
					belegung.halbjahrKuerzel = (halbjahr == null) ? null : halbjahr.kuerzel;
					belegung.abschnitt = abschnittLeistungsdaten.Abschnitt;
					belegung.abschnittGewertet = lernabschnitt.SemesterWertung;
					belegung.jahrgang = lernabschnitt.ASDJahrgang;
					belegung.lehrer = leistung.Fachlehrer_ID;
					belegung.notenKuerzel = leistung.NotenKrz.kuerzel;
					belegung.kursartKuerzel = kursart.kuerzel;
					belegung.istSchriftlich = (kursart == GostKursart.LK)
							|| ((kursart == GostKursart.GK) && (("GKS".equals(leistung.Kursart))
									|| ("AB3".equals(leistung.Kursart))
									|| ("AB4".equals(leistung.Kursart) && (halbjahr != GostHalbjahr.Q22))));
					belegung.bilingualeSprache = gostFach.biliSprache;
					belegung.wochenstunden = leistung.Wochenstunden;
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
			// Sortiere Fächer anhand der SII-Sortierung der Fächer
			faecher.values().stream()
				.sorted((a, b) -> Integer.compare(a.fach.sortierung, b.fach.sortierung))
				.forEach(daten.faecher::add);
			result.put(id, daten);
    	}
    	return result;
	}

}
