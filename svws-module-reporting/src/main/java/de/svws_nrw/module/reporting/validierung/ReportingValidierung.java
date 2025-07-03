package de.svws_nrw.module.reporting.validierung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DBUtilsGostAbitur;
import de.svws_nrw.data.gost.DBUtilsGostLaufbahn;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanungBeratungsdaten;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.proxytypes.kurs.ProxyReportingKurs;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import jakarta.ws.rs.core.Response.Status;

/**
 * Statische Klasse mit Hilfsmethoden zur Validierung von Daten für das Reporting.
 */
public final class ReportingValidierung {

	private ReportingValidierung() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Validierung von Daten für das Reporting. Initialisierung nicht möglich.");
	}

	/**
	 * Validiert von der API übergebene Daten für Schüler. Bei fehlenden oder unstimmigen Daten wird eine ApiOperationException geworfen.
	 * Über den Parameter cacheDaten kann gesteuert werden, ob bereits abgerufene Daten aus der DB im Repository zwischengespeichert werden soll.
	 *
	 * @param reportingRepository    Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param idsSchueler            Liste der IDs der Schüler, die berücksichtigt werden sollen.
	 * @param mitGostLaufbahnplanung Legt fest, ob der Daten zur gymnasialen Oberstufe mit in den Kontext geladen werden sollen.
	 * @param mitAbiturDaten         Legt fest, ob die Daten zum Abitur in der gymnasialen Oberstufe mit in den Kontext geladen werden sollen.
	 *
	 * @throws ApiOperationException  im Fehlerfall
	 */
	public static void validiereDatenFuerSchueler(final ReportingRepository reportingRepository, final List<Long> idsSchueler,
			final boolean mitGostLaufbahnplanung,
			final boolean mitAbiturDaten) throws ApiOperationException {

		// Grunddaten prüfen.
		final DBEntityManager conn = reportingRepository.conn();

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Beginn der Validierung der Schülerdaten.");

		if (idsSchueler == null) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden keine Liste mit Schüler-IDs übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden keine Liste mit Schüler-IDs übergeben.");
		}

		// Entferne null-Elemente und Duplikate
		final List<Long> idsNonNull = idsSchueler.stream().filter(Objects::nonNull).distinct().toList();

		if (idsNonNull.isEmpty()) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden keine Schüler-IDs übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden keine Schüler-IDs übergeben.");
		}

		// Prüfe die Schüler-IDs. Erzeuge Maps, damit auch später leicht auf die Schülerdaten zugegriffen werden kann.
		final Map<Long, SchuelerStammdaten> mapSchueler = (new DataSchuelerStammdaten(conn)).getListByIds(idsNonNull).stream()
				.collect(Collectors.toMap(s -> s.id, s -> s));
		for (final Long sID : idsNonNull)
			if (mapSchueler.get(sID) == null) {
				reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden ungültige Schüler-IDs übergeben.");
				throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden ungültige Schüler-IDs übergeben.");
			}

		reportingRepository.mapSchuelerStammdaten().putAll(mapSchueler);

		// Prüfe Daten aus dem Bereich des Abiturs. Das gilt auch für die Laufbahnplanung, da dort Daten wie das Abiturjahr benötigt werden.
		if (mitAbiturDaten) {
			// Schule hat eine gym. Oberstufe? pruefeSchuleMitGOSt wirft eine NOT_FOUND-Exception, wenn die Schule keine GOSt hat.
			try {
				DBUtilsGost.pruefeSchuleMitGOSt(conn);
			} catch (final ApiOperationException aoe) {
				reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Keine Schule oder Schule ohne GOSt gefunden.");
				throw new ApiOperationException(Status.NOT_FOUND, aoe, "FEHLER: Keine Schule oder Schule ohne GOSt gefunden.");
			}

			final Map<Long, Abiturdaten> mapGostSchuelerAbiturdaten;

			try {
				mapGostSchuelerAbiturdaten = new HashMap<>(DBUtilsGostAbitur.getAbiturdatenFromIDs(conn, idsNonNull));
			} catch (final ApiOperationException aoe) {
				reportingRepository.logger().logLn(LogLevel.ERROR, 4,
						"FEHLER: Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt existieren.");
				throw new ApiOperationException(Status.NOT_FOUND, aoe,
						"FEHLER: Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt existieren.");
			}

			reportingRepository.mapGostSchuelerAbiturdaten().putAll(mapGostSchuelerAbiturdaten);
		}

		// Prüfe Daten aus dem Bereich der GOSt-Laufbahnplanung.
		if (mitGostLaufbahnplanung) {
			try {
				DBUtilsGost.pruefeSchuleMitGOSt(conn);
			} catch (final ApiOperationException aoe) {
				reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Keine Schule oder Schule ohne GOSt gefunden.");
				throw new ApiOperationException(Status.NOT_FOUND, aoe, "FEHLER: Keine Schule oder Schule ohne GOSt gefunden.");
			}

			// Lade die Beratungsdaten der Schüler zwecks Prüfung.
			final Map<Long, GostLaufbahnplanungBeratungsdaten> mapGostBeratungsdaten =
					new HashMap<>(new DataGostSchuelerLaufbahnplanungBeratungsdaten(conn).getMapFromIDs(idsNonNull));
			for (final Long sID : idsNonNull)
				if (mapGostBeratungsdaten.get(sID) == null) {
					reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.");
					throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.");
				}
			reportingRepository.mapGostBeratungsdaten().putAll(mapGostBeratungsdaten);

			// Lade Abiturdaten für die Gost-Laufbahnplanung zwecks Prüfung.
			final Map<Long, Abiturdaten> mapGostBeratungsdatenAbiturdaten;
			try {
				mapGostBeratungsdatenAbiturdaten = new HashMap<>(DBUtilsGostLaufbahn.getFromIDs(conn, idsNonNull));
			} catch (final ApiOperationException aoe) {
				reportingRepository.logger().logLn(LogLevel.ERROR, 4,
						"FEHLER: Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt-Laufbahnplanung existieren.");
				throw new ApiOperationException(Status.NOT_FOUND, aoe,
						"FEHLER: Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt-Laufbahnplanung existieren.");
			}
			for (final Abiturdaten abiturdaten : mapGostBeratungsdatenAbiturdaten.values()) {
				if ((abiturdaten == null) || (abiturdaten.abiturjahr <= 0)) {
					reportingRepository.logger().logLn(LogLevel.ERROR, 4,
							"FEHLER: Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt-Laufbahnplanung existieren.");
					throw new ApiOperationException(Status.NOT_FOUND,
							"FEHLER: Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt-Laufbahnplanung existieren.");
				}
			}
			reportingRepository.mapGostBeratungsdatenAbiturdaten().putAll(mapGostBeratungsdatenAbiturdaten);
		}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Ende der Validierung der Schülerdaten.");
	}


	/**
	 * Validiert von der API übergebene Daten für Klassen. Bei fehlenden oder unstimmigen Daten wird eine ApiOperationException geworfen.
	 * Über den Parameter cacheDaten kann gesteuert werden, ob bereits abgerufene Daten aus der DB im Repository zwischengespeichert werden soll.
	 *
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param idsKlassen   			Liste der IDs der Klassen, die berücksichtigt werden sollen.
	 *
	 * @throws ApiOperationException  im Fehlerfall
	 */
	public static void validiereDatenFuerKlassen(final ReportingRepository reportingRepository, final List<Long> idsKlassen)
			throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Beginn der Validierung der Klassendaten.");

		if (idsKlassen == null) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurde keine Liste mit Klassen-IDs übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden keine Liste mit Klassen-IDs übergeben.");
		}

		// Entferne null-Elemente und Duplikate
		final List<Long> idsNonNull = idsKlassen.stream().filter(Objects::nonNull).distinct().toList();

		if (idsNonNull.isEmpty()) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden keine Klassen-IDs übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden keine Klassen-IDs übergeben.");
		}

		// Prüfe die Klassen-IDs. Um die an dieser Stelle nicht notwendige, aber aufwendige Auflösung der vorherigen und nachfolgenden Klasse über die
		// Schuljahresabschnitte zu  vermeiden, prüfe nur anhand der DTOs zu den IDs, ob diese vorhanden sind.
		try {

			final List<DTOKlassen> dtoKlassen = new DataKlassendaten(reportingRepository.conn()).getDTOsByIds(idsNonNull);
			if (dtoKlassen.size() != idsNonNull.size())
				throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden ungültige Klassen-IDs übergeben.");
		} catch (final ApiOperationException aoe) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden ungültige Klassen-IDs übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden ungültige Klassen-IDs übergeben.");
		}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Ende der Validierung der Klassendaten.");
	}


	/**
	 * Validiert von der API übergebene Daten für Kurse. Bei fehlenden oder unstimmigen Daten wird eine ApiOperationException geworfen.
	 * Über den Parameter cacheDaten kann gesteuert werden, ob bereits abgerufene Daten aus der DB im Repository zwischengespeichert werden soll.
	 *
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param idsKurse   			Liste der IDs der Kurse, die berücksichtigt werden sollen.
	 *
	 * @throws ApiOperationException  im Fehlerfall
	 */
	public static void validiereDatenFuerKurse(final ReportingRepository reportingRepository, final List<Long> idsKurse)
			throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Beginn der Validierung der Kursdaten.");

		if (idsKurse == null) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurde keine Liste mit Kurs-IDs übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden keine Liste mit Kurs-IDs übergeben.");
		}

		// Entferne null-Elemente und Duplikate
		final List<Long> idsNonNull = idsKurse.stream().filter(Objects::nonNull).distinct().toList();

		if (idsNonNull.isEmpty()) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden keine Kurs-IDs übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden keine Kurs-IDs übergeben.");
		}

		// Prüfe die Kurs-IDs. Erzeuge Maps, damit auch später leicht auf die Kursdaten zugegriffen werden kann.
		final Map<Long, KursDaten> mapKursdaten =
				new DataKurse(reportingRepository.conn()).getListByIDs(idsNonNull, true).stream()
						.collect(Collectors.toMap(k -> k.id, k -> k));
		for (final Long kID : idsNonNull)
			if (mapKursdaten.get(kID) == null) {
				reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden ungültige Kurs-IDs übergeben.");
				throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden ungültige Kurs-IDs übergeben.");
			}

		// Daten sind valide, speichere diese nun gemäß Parameter im Repository.
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Speicherung der Daten aus der Validierung der Kursdaten im Repository.");
		mapKursdaten.values().stream()
				.map(k -> (ReportingKurs) new ProxyReportingKurs(reportingRepository, k))
				.forEach(k -> reportingRepository.mapKurse().putIfAbsent(k.id(), k));

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Ende der Validierung der Kursdaten.");
	}


	/**
	 * Validiert von der API übergebene Daten für Lehrer. Bei fehlenden oder unstimmigen Daten wird eine ApiOperationException geworfen.
	 *
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param idsLehrer   			Liste der IDs der Lehrer, die berücksichtigt werden sollen.
	 *
	 * @throws ApiOperationException  im Fehlerfall
	 */
	public static void validiereDatenFuerLehrer(final ReportingRepository reportingRepository, final List<Long> idsLehrer)
			throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Beginn der Validierung der Lehrerdaten.");

		if (idsLehrer == null) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurde keine Liste mit Lehrer-IDs übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden keine Liste mit  Lehrer-IDs übergeben.");
		}

		// Entferne null-Elemente und Duplikate
		final List<Long> idsNonNull = idsLehrer.stream().filter(Objects::nonNull).distinct().toList();

		if (idsNonNull.isEmpty()) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden keine Lehrer-IDs übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden keine Lehrer-IDs übergeben.");
		}

		// Prüfe die Lehrer-IDs anhand der Daten aus dem Repository.
		if (idsNonNull.stream().anyMatch(id -> reportingRepository.mapLehrerStammdaten().get(id) == null)) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden ungültige Lehrer-IDs übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden ungültige Lehrer-IDs übergeben.");
		}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Ende der Validierung der Lehrerdaten.");
	}


	/**
	 * Validiert von der API übergebene Daten für GOSt-Blockungsergebnis. Bei fehlenden oder unstimmigen Daten wird eine ApiOperationException geworfen.
	 *
	 * @param reportingRepository		Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public static void validiereDatenFuerGostKursplanungBlockungsergebnis(final ReportingRepository reportingRepository)
			throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Beginn der Validierung der Blockungsergebnisdaten.");

		if ((reportingRepository.reportingParameter().idsHauptdaten == null) || reportingRepository.reportingParameter().idsHauptdaten.isEmpty()) {
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "FEHLER: Es wurde keine ID für ein Blockungsergebnis übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurde keine ID für ein Blockungsergebnis übergeben.");
		}

		// Für die GOSt-Kursplanung muss die Schule eine Schule mit GOSt sein.
		try {
			DBUtilsGost.pruefeSchuleMitGOSt(reportingRepository.conn());
		} catch (final ApiOperationException e) {
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "FEHLER: Keine Schule oder Schule ohne GOSt gefunden.");
			throw new ApiOperationException(Status.NOT_FOUND, e, "FEHLER: Keine Schule oder Schule ohne GOSt gefunden.");
		}

		final Long idBlockungsergebnis = reportingRepository.reportingParameter().idsHauptdaten.getFirst();

		// Prüfe nun, ob es zur angegebenen Blockungsergebnis-ID ein Ergebnis gibt.
		try {
			final DBEntityManager conn = reportingRepository.conn();
			DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(conn,
					DataGostBlockungsergebnisse.getErgebnisFromID(conn, idBlockungsergebnis).blockungID);
		} catch (final ApiOperationException e) {
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "FEHLER: Mit der angegebenen Blockungsergebnis-ID konnte keine Daten ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, e, "FEHLER: Mit der angegebenen Blockungsergebnis-ID konnte keine Daten ermittelt werden.");
		}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Ende der Validierung der Blockungsergebnisdaten.");
	}

	/**
	 * Validiert von der API übergebene Daten für GOSt-Klausurplanung. Bei fehlenden oder unstimmigen Daten wird eine ApiOperationException geworfen.
	 *
	 * @param reportingRepository		Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public static void validiereDatenFuerGostKlausurplanungKlausurplan(final ReportingRepository reportingRepository)
			throws ApiOperationException {

		// Für die GOSt-Kursplanung muss die Schule eine Schule mit GOSt sein.
		try {
			DBUtilsGost.pruefeSchuleMitGOSt(reportingRepository.conn());
		} catch (final ApiOperationException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e, "FEHLER: Keine Schule oder Schule ohne GOSt gefunden.");
		}

		// Erwartete Datenstruktur in idsHauptdaten der Reporting-Parameter: Abiturjahr - GostHalbjahrID (0-5) - Abiturjahr - GostHalbjahrID (0-5) - usw.
		// Die Einträge müssen paarweise auftreten.
		// Ist die Liste leer, so sollen alle drei Abiturjahrgänge gedruckt werden.
		final List<Long> parameterDaten = reportingRepository.reportingParameter().idsHauptdaten.stream().filter(Objects::nonNull).toList();

		// Wenn die Liste der Stufen nicht leer ist, deren Werte prüfen.
		if (!parameterDaten.isEmpty()) {
			if ((parameterDaten.size() % 2) == 1) {
				// Anzahl der Angaben muss gerade sein, da Paare. Sonst liegt ein Fehler vor.
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"FEHLER: Die Anzahl der Parameter für Abiturjahrgang und Gost-Halbjahr ist falsch.");
			}
			final List<Pair<Integer, Integer>> selection = new ArrayList<>();

			try {
				// Lese die Parameter im Wechsel aus.
				for (int i = 0; i < parameterDaten.size(); i = i + 2) {
					selection.add(new Pair<>(Math.toIntExact(parameterDaten.get(i)), Math.toIntExact(parameterDaten.get(i + 1))));
				}

				// Prüfe die Parameter in den Listen auf plausible bzw. zugelassene Werte.
				for (final Pair<Integer, Integer> wert : selection) {
					if ((wert.a < 1900) || (wert.a > 10000))
						throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "FEHLER: Ein Abiturjahr liegt außerhalb des Wertebereichs.");
					if ((wert.b < 0) || (wert.b > 5))
						throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "FEHLER: Ein GOSt-Halbjahr liegt außerhalb des Wertebereichs.");
				}
			} catch (final Exception e) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
						"FEHLER: Die Parameter für Abiturjahrgang und GOSt-Halbjahr konnten nicht gelesen werden oder sind außerhalb des Wertebereichs.");
			}
		}
	}


	/**
	 * Validiert von der API übergebene Daten für GOSt-Klausurplanung. Bei fehlenden oder unstimmigen Daten wird eine ApiOperationException geworfen.
	 *
	 * @param reportingRepository		Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public static void validiereDatenFuerStundenplanung(final ReportingRepository reportingRepository)
			throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Beginn der Validierung der Stundenplanungsdaten.");

		if ((reportingRepository.reportingParameter().idsHauptdaten == null) || reportingRepository.reportingParameter().idsHauptdaten.isEmpty()) {
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "FEHLER: Es wurde keine ID für die Stundenplanung übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurde keine ID für die Stundenplanung übergeben.");
		}

		final Long idStundenplan = reportingRepository.reportingParameter().idsHauptdaten.getFirst();

		// Prüfe nun, ob es zur angegebenen Stundenplan-ID einen Stundenplan gibt.
		if ((idStundenplan == null) || (reportingRepository.stundenplan(idStundenplan) == null)) {
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "FEHLER: Mit der angegebenen Stundenplan-ID konnte kein Stundenplan ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Mit der angegebenen Stundenplan-ID konnte kein Stundenplan ermittelt werden.");
		}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Ende der Validierung der Stundenplanungsdaten.");
	}

}
