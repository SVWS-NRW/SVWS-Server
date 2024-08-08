package de.svws_nrw.module.reporting.validierung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData;
import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DBUtilsGostAbitur;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanungBeratungsdaten;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausuren;
import de.svws_nrw.data.schueler.DataSchuelerLernabschnittsdaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
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
	 * @param reportingRepository	Das Repository mit Daten zum Reporting.
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

		if ((idsSchueler == null) || idsSchueler.isEmpty()) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden keine Schüler-IDs übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Es wurden keine Schüler-IDs übergeben.");
		}

		// Prüfe die Schüler-IDs. Erzeuge Maps, damit auch später leicht auf die Schülerdaten zugegriffen werden kann.
		final Map<Long, SchuelerStammdaten> mapSchueler =
				DataSchuelerStammdaten.getListStammdaten(conn, idsSchueler).stream().collect(Collectors.toMap(s -> s.id, s -> s));
		for (final Long sID : idsSchueler)
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
					new HashMap<>(new DataGostSchuelerLaufbahnplanungBeratungsdaten(conn).getMapFromIDs(idsSchueler));

			for (final Long sID : idsSchueler)
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

			for (final Long sID : idsSchueler) {
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
			reportingRepository.mapAktuelleLernabschnittsdaten()
					.putAll(new DataSchuelerLernabschnittsdaten(conn)
							.getListFromSchuelerIDsUndSchuljahresabschnittID(idsSchueler, reportingRepository.aktuellerSchuljahresabschnitt().id, false)
							.stream().collect(Collectors.toMap(l -> l.schuelerID, l -> l)));
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Ende der Speicherung der Daten aus der Validierung der Schülerdaten im Repository.");
		}
	}


	/**
	 * Validiert von der API übergebene Daten für GOSt-Blockungsergebnis. Bei fehlenden oder unstimmigen Daten wird eine ApiOperationException geworfen.
	 *
	 * @param reportingRepository	Das Repository mit Daten zum Reporting.
	 *
	 * @throws ApiOperationException  im Fehlerfall
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
			DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(reportingRepository.conn(),
					DataGostBlockungsergebnisse.getErgebnisFromID(reportingRepository.conn(), idBlockungsergebnis).blockungID);
		} catch (final ApiOperationException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e, "FEHLER: Mit der angegebenen Blockungsergebnis-ID konnte keine Daten ermittelt werden..");
		}
	}

	/**
	 * Validiert von der API übergebene Daten für GOSt-Klausurplanung. Bei fehlenden oder unstimmigen Daten wird eine ApiOperationException geworfen.
	 *
	 * @param reportingRepository	Das Repository mit Daten zum Reporting.
	 *
	 * @throws ApiOperationException  im Fehlerfall
	 */
	public static void validiereDatenFuerGostKlausurplanungKlausurplan(final ReportingRepository reportingRepository)
			throws ApiOperationException {

		// Für die GOSt-Kursplanung muss die Schule eine Schule mit GOSt sein.
		try {
			DBUtilsGost.pruefeSchuleMitGOSt(reportingRepository.conn());
		} catch (final ApiOperationException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e, "FEHLER: Keine Schule oder Schule ohne GOSt gefunden.");
		}

		// Erwartete Datensturktur in idsHauptdaten der Reporting-Parameter: Abiturjahr - GostHalbjahrID (0-5) - Abiturjahr - GostHalbjahrID (0-5) - usw.
		// null ist nicht erlaubt und die Einträge müssen paarweise auftreten.
		final List<Long> parameterDaten = reportingRepository.reportingParameter().idsHauptdaten.stream().filter(Objects::nonNull).toList();

		if ((parameterDaten.size() < 2) && ((parameterDaten.size() % 2) == 1))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "FEHLER: Die Anzahl der Parameter für Abiturjahrgang und Gost-Halbjahr ist falsch.");

		final List<Pair<Integer, Integer>> selection = new ArrayList<>();

		try {
			// Lese die Parameter im Wechsel aus.

			for (int i = 0; i < parameterDaten.size(); i = i + 2) {
				selection.add(new Pair<>(Math.toIntExact(parameterDaten.get(i)), Math.toIntExact(parameterDaten.get(i + 1))));
			}

			// Prüfe die Parameter in den Listen
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

		// TODO: Parameter von DataGostKlausuren.getAllData sollten eine Liste fassen können, statt einem einzigen Jahrgang.
		try {
			final GostKlausurenCollectionAllData allData = DataGostKlausuren.getAllData(reportingRepository.conn(), selection);
			final GostKlausurplanManager gostKlausurManager = new GostKlausurplanManager(allData);
		} catch (final ApiOperationException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e,
					"FEHLER: Zum ausgewählten Schuljahresabschnitt konnten keine Klausurplanungsdaten ermittelt werden.");
		}
	}

}
