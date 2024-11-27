package de.svws_nrw.module.reporting.validierung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DBUtilsGostAbitur;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanungBeratungsdaten;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
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
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param idsSchueler   		Liste der IDs der Schüler, die berücksichtigt werden sollen.
	 * @param mitGostDaten 			Legt fest, ob der Daten zur gymnasialen Oberstufe mit in den Kontext geladen werden sollen.
	 * @param mitAbiturDaten 		Legt fest, ob die Daten zum Abitur in der gymnasialen Oberstufe mit in den Kontext geladen werden sollen.
	 * @param cacheDaten 			Legt fest, ob die zur Validierung geladenen Daten im Repository gespeichert werden sollen.
	 *
	 * @throws ApiOperationException  im Fehlerfall
	 */
	public static void validiereDatenFuerSchueler(final ReportingRepository reportingRepository, final List<Long> idsSchueler, final boolean mitGostDaten,
			final boolean mitAbiturDaten, final boolean cacheDaten) throws ApiOperationException {

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
		final Map<Long, SchuelerStammdaten> mapSchueler =
				DataSchuelerStammdaten.getListStammdaten(conn, idsNonNull).stream().collect(Collectors.toMap(s -> s.id, s -> s));
		for (final Long sID : idsNonNull)
			if (mapSchueler.get(sID) == null) {
				reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden ungültige Schüler-IDs übergeben.");
				throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden ungültige Schüler-IDs übergeben.");
			}

		// Nur, wenn Daten zur gymnasialen Oberstufe mit angefordert werden.
		if (mitGostDaten) {
			// Schule hat eine gym. Oberstufe? pruefeSchuleMitGOSt wirft eine NOT_FOUND-Exception, wenn die Schule keine GOSt hat.
			try {
				DBUtilsGost.pruefeSchuleMitGOSt(conn);
			} catch (final ApiOperationException aoe) {
				reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Keine Schule oder Schule ohne GOSt gefunden.");
				throw new ApiOperationException(Status.NOT_FOUND, aoe, "FEHLER: Keine Schule oder Schule ohne GOSt gefunden.");
			}

			final Map<Long, GostLaufbahnplanungBeratungsdaten> mapGostBeratungsdaten =
					new HashMap<>(new DataGostSchuelerLaufbahnplanungBeratungsdaten(conn).getMapFromIDs(idsNonNull));

			for (final Long sID : idsNonNull)
				if (mapGostBeratungsdaten.get(sID) == null) {
					reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.");
					throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.");
				}
			reportingRepository.mapGostBeratungsdaten().putAll(mapGostBeratungsdaten);
		}

		// Nur, wenn Daten zum Abitur in der gymnasialen Oberstufe mit angefordert werden.
		if (mitAbiturDaten) {
			// Schule hat eine gym. Oberstufe? pruefeSchuleMitGOSt wirft eine NOT_FOUND-Exception, wenn die Schule keine GOSt hat.
			try {
				DBUtilsGost.pruefeSchuleMitGOSt(conn);
			} catch (final ApiOperationException aoe) {
				reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Keine Schule oder Schule ohne GOSt gefunden.");
				throw new ApiOperationException(Status.NOT_FOUND, aoe, "FEHLER: Keine Schule oder Schule ohne GOSt gefunden.");
			}

			final Map<Long, Abiturdaten> mapGostSchuelerAbiturdaten = new HashMap<>();

			for (final Long sID : idsNonNull) {
				try {
					mapGostSchuelerAbiturdaten.put(sID, DBUtilsGostAbitur.getAbiturdaten(conn, sID));
				} catch (final ApiOperationException aoe) {
					reportingRepository.logger().logLn(LogLevel.ERROR, 4,
							"FEHLER: Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt existieren.");
					throw new ApiOperationException(Status.NOT_FOUND, aoe,
							"FEHLER: Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt existieren.");
				}
			}

			reportingRepository.mapGostSchuelerAbiturdaten().putAll(mapGostSchuelerAbiturdaten);
		}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Ende der Validierung der Schülerdaten.");

		// Daten sind valide, speichere diese nun gemäß Parameter im Repository.
		if (cacheDaten) {
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Beginn der Speicherung der Daten aus der Validierung der Schülerdaten im Repository.");
			reportingRepository.mapSchuelerStammdaten().putAll(mapSchueler);
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Ende der Speicherung der Daten aus der Validierung der Schülerdaten im Repository.");
		}
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
	 * @param cacheDaten 			Legt fest, ob die zur Validierung geladenen Daten im Repository gespeichert werden sollen.
	 *
	 * @throws ApiOperationException  im Fehlerfall
	 */
	public static void validiereDatenFuerKurse(final ReportingRepository reportingRepository, final List<Long> idsKurse, final boolean cacheDaten)
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
				new DataKurse(reportingRepository.conn()).getListByIDs(idsNonNull, cacheDaten).stream()
						.collect(Collectors.toMap(k -> k.id, k -> k));
		for (final Long kID : idsNonNull)
			if (mapKursdaten.get(kID) == null) {
				reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden ungültige Kurs-IDs übergeben.");
				throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden ungültige Kurs-IDs übergeben.");
			}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Ende der Validierung der Kursdaten.");

		// Daten sind valide, speichere diese nun gemäß Parameter im Repository.
		if (cacheDaten) {
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Beginn der Speicherung der Daten aus der Validierung der Kursdaten im Repository.");
			final List<ReportingKurs> listeKurse =
					mapKursdaten.values().stream().map(k -> (ReportingKurs) new ProxyReportingKurs(reportingRepository, k)).toList();
			listeKurse.forEach(k -> reportingRepository.mapKurse().putIfAbsent(k.id(), k));
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Ende der Speicherung der Daten aus der Validierung der Kursdaten im Repository.");
		}
	}


	/**
	 * Validiert von der API übergebene Daten für Lehrer. Bei fehlenden oder unstimmigen Daten wird eine ApiOperationException geworfen.
	 * Über den Parameter cacheDaten kann gesteuert werden, ob bereits abgerufene Daten aus der DB im Repository zwischengespeichert werden soll.
	 *
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param idsLehrer   			Liste der IDs der Lehrer, die berücksichtigt werden sollen.
	 * @param cacheDaten 			Legt fest, ob die zur Validierung geladenen Daten im Repository gespeichert werden sollen.
	 *
	 * @throws ApiOperationException  im Fehlerfall
	 */
	public static void validiereDatenFuerLehrer(final ReportingRepository reportingRepository, final List<Long> idsLehrer, final boolean cacheDaten)
			throws ApiOperationException {

		// Grunddaten prüfen.
		final DBEntityManager conn = reportingRepository.conn();

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

		// Prüfe die Lehrer-IDs. Erzeuge Maps, damit auch später leicht auf die Lehrerdaten zugegriffen werden kann.
		final Map<Long, LehrerStammdaten> mapLehrer =
				new DataLehrerStammdaten(reportingRepository.conn()).getFromIDs(conn, idsNonNull).stream()
						.collect(Collectors.toMap(l -> l.id, l -> l));
		for (final Long lID : idsNonNull)
			if (mapLehrer.get(lID) == null) {
				reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden ungültige Lehrer-IDs übergeben.");
				throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden ungültige Lehrer-IDs übergeben.");
			}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Ende der Validierung der Lehrerdaten.");

		// Daten sind valide, speichere diese nun gemäß Parameter im Repository.
		if (cacheDaten) {
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Beginn der Speicherung der Daten aus der Validierung der Lehrerdaten im Repository.");
			reportingRepository.mapLehrerStammdaten().putAll(mapLehrer);
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Ende der Speicherung der Daten aus der Validierung der Lehrerdaten im Repository.");
		}
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

		// Für die GOSt-Kursplanung muss die Schule eine Schule mit GOSt sein.
		try {
			DBUtilsGost.pruefeSchuleMitGOSt(reportingRepository.conn());
		} catch (final ApiOperationException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e, "FEHLER: Keine Schule oder Schule ohne GOSt gefunden.");
		}

		final Long idBlockungsergebnis = reportingRepository.reportingParameter().idsHauptdaten.getFirst();

		// Prüfe nun, ob es zur angegebenen Blockungsergebnis-ID ein Ergebnis gibt.
		try {
			final DBEntityManager conn = reportingRepository.conn();
			DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(conn,
					DataGostBlockungsergebnisse.getErgebnisFromID(conn, idBlockungsergebnis).blockungID);
		} catch (final ApiOperationException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e, "FEHLER: Mit der angegebenen Blockungsergebnis-ID konnte keine Daten ermittelt werden..");
		}
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

}
