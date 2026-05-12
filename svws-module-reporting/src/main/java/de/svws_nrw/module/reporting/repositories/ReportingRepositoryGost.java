package de.svws_nrw.module.reporting.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.gost.DBUtilsGostLaufbahn;
import de.svws_nrw.data.gost.DataGostAbiturdaten;
import de.svws_nrw.data.gost.DataGostAbiturjahrgangFachwahlen;
import de.svws_nrw.data.gost.DataGostJahrgangFachkombinationen;
import de.svws_nrw.data.gost.DataGostJahrgangsdaten;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanungBeratungsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 * Domänen-Repository für GOSt-Daten (Abiturjahrgänge, Beratungsdaten, Kursplanung).
 * Wird lazy befüllt, d. h. es erfolgt keine Initialisierungslogik im Konstruktor.
 */
public class ReportingRepositoryGost {

	private final ReportingContext reportingContext;

	private final Map<Integer, GostJahrgangsdaten> mapAbiturjahrgangDaten = new HashMap<>();
	private final Map<Integer, GostFaecherManager> mapAbiturjahrgangFaecher = new HashMap<>();
	private final Map<Long, GostLaufbahnplanungBeratungsdaten> mapBeratungsdaten = new HashMap<>();
	private final Map<Long, Abiturdaten> mapBeratungsdatenAbiturdaten = new HashMap<>();
	private final Map<Long, Abiturdaten> mapSchuelerAbiturdaten = new HashMap<>();
	private final Map<Long, ReportingGostKursplanungKurs> mapKursplanungKurse = new HashMap<>();
	private final Map<Integer, List<GostStatistikFachwahl>> mapFachwahlen = new HashMap<>();

	/**
	 * Erstellt ein neues ReportingRepositoryGost.
	 *
	 * @param reportingContext Der zentrale Reporting-Context mit Zugriff auf die domänenspezifischen Repositories.
	 */
	public ReportingRepositoryGost(final ReportingContext reportingContext) {
		this.reportingContext = reportingContext;
	}


	// ##### Jahrgangsdaten und Fächer #####

	/**
	 * Gibt die Map der Jahrgangsdaten zu den Abiturjahrgängen zurück, indiziert nach dem Abiturjahrgang.
	 *
	 * @return Map der Daten zu den Abiturjahrgängen
	 */
	public Map<Integer, GostJahrgangsdaten> abiturjahrgangDaten() {
		return mapAbiturjahrgangDaten;
	}

	/**
	 * Gibt die Map der Fächermanager zu den Abiturjahrgängen zurück, indiziert nach dem Abiturjahrgang.
	 *
	 * @return Map der Fächermanager zu den Abiturjahrgängen
	 */
	public Map<Integer, GostFaecherManager> abiturjahrgangFaecher() {
		return mapAbiturjahrgangFaecher;
	}

	/**
	 * Lädt die GOSt-Jahrgangsdaten zum übergebenen Abiturjahr aus der Datenbank.
	 *
	 * @param abiturjahr Das Abiturjahr des Jahrgangs.
	 *
	 * @return Die GOSt-Jahrgangsdaten.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public GostJahrgangsdaten jahrgangsdaten(final int abiturjahr) throws ApiOperationException {
		return DataGostJahrgangsdaten.getJahrgangsdaten(this.reportingContext.conn(), abiturjahr);
	}

	/**
	 * Lädt den GOSt-FächerManager zum übergebenen Abiturjahr aus der Datenbank und ergänzt die Fachkombinationen.
	 *
	 * @param abiturjahr Das Abiturjahr des Jahrgangs.
	 *
	 * @return Der mit Fachkombinationen ergänzte FächerManager des Abiturjahrgangs.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public GostFaecherManager faecherManager(final int abiturjahr) throws ApiOperationException {
		final int auswahlSchuljahr = this.reportingContext.repositorySchule().auswahlSchuljahresabschnitt().schuljahr();
		final GostFaecherManager faecherManager =
				DBUtilsFaecherGost.getFaecherManager(auswahlSchuljahr, this.reportingContext.conn(), abiturjahr);
		faecherManager.addFachkombinationenAll(
				DataGostJahrgangFachkombinationen.getFachkombinationen(this.reportingContext.conn(), abiturjahr));
		return faecherManager;
	}


	// ##### Beratungsdaten und Beratungs-Abiturdaten #####

	/**
	 * Gibt die Map der GOSt-Laufbahnberatungsdaten zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map mit GOSt-Beratungsdaten der Schüler
	 */
	public Map<Long, GostLaufbahnplanungBeratungsdaten> beratungsdaten() {
		return mapBeratungsdaten;
	}

	/**
	 * Lädt die GOSt-Laufbahnberatungsdaten zu den übergebenen Schüler-IDs aus der Datenbank.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Beratungsdaten geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und den zugehörigen Beratungsdaten als Wert.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public Map<Long, GostLaufbahnplanungBeratungsdaten> beratungsdaten(final List<Long> idsSchueler) throws ApiOperationException {
		return new DataGostSchuelerLaufbahnplanungBeratungsdaten(this.reportingContext.conn()).getMapFromIDs(idsSchueler);
	}

	/**
	 * Gibt die Map der Abiturdaten zurück, die im Rahmen der Laufbahnberatung ermittelt wurden, indiziert nach Schüler-ID.
	 *
	 * @return Map mit GOSt-Beratungsdaten-Abiturdaten
	 */
	public Map<Long, Abiturdaten> beratungsdatenAbiturdaten() {
		return mapBeratungsdatenAbiturdaten;
	}

	/**
	 * Lädt die im Rahmen der Laufbahnberatung ermittelten Abiturdaten zu den übergebenen Schüler-IDs aus der Datenbank.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Beratungs-Abiturdaten geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und den zugehörigen Abiturdaten als Wert.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public Map<Long, Abiturdaten> beratungsdatenAbiturdaten(final List<Long> idsSchueler) throws ApiOperationException {
		return DBUtilsGostLaufbahn.getMapFromIDs(this.reportingContext.conn(), idsSchueler);
	}


	// ##### Abiturdaten der Schüler #####

	/**
	 * Gibt die Map der GOSt-Abiturdaten der Schüler zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map mit GOSt-Abiturdaten der Schüler
	 */
	public Map<Long, Abiturdaten> schuelerAbiturdaten() {
		return mapSchuelerAbiturdaten;
	}

	/**
	 * Lädt die GOSt-Abiturdaten zu den übergebenen Schüler-IDs aus der Datenbank.
	 * Das Abiturjahr wird beim Konstruktor von {@link DataGostAbiturdaten} mit {@code null} übergeben, da
	 * {@link DataGostAbiturdaten#getMapAbiturdatenFromIDs(List)} nicht auf das Abiturjahr angewiesen ist.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Abiturdaten geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und den zugehörigen Abiturdaten als Wert.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public Map<Long, Abiturdaten> schuelerAbiturdaten(final List<Long> idsSchueler) throws ApiOperationException {
		return new DataGostAbiturdaten(this.reportingContext.conn(), null).getMapAbiturdatenFromIDs(idsSchueler);
	}


	// ##### Kursplanung #####

	/**
	 * Gibt die Map der aktuell geladenen Kursplanungs-Kurse zurück, indiziert nach Kurs-ID.
	 *
	 * @return Map der aktuell geladenen Kursplanung-Kurse
	 */
	public Map<Long, ReportingGostKursplanungKurs> kursplanungKurse() {
		return mapKursplanungKurse;
	}


	// ##### Fachwahlen #####

	/**
	 * Gibt die GOSt-Fachwahlstatistik für den übergebenen Abiturjahrgang zurück. Die Daten werden bei erstem Zugriff aus
	 * der Datenbank geladen und im Cache gehalten.
	 *
	 * @param abiturjahr Das Abiturjahr des Jahrgangs.
	 *
	 * @return Liste der Fachwahl-Statistikeinträge des Abiturjahrgangs. Leere Liste, falls keine Daten ermittelt werden konnten.
	 */
	public List<GostStatistikFachwahl> fachwahlen(final int abiturjahr) {
		return mapFachwahlen.computeIfAbsent(abiturjahr, jahr -> {
			try {
				return new DataGostAbiturjahrgangFachwahlen(this.reportingContext.conn(), jahr).getFachwahlen();
			} catch (final ApiOperationException e) {
				ReportingExceptionUtils.logException(
						"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der GOSt-Fachwahlstatistik.", e,
						this.reportingContext.logger(), LogLevel.INFO, 0);
				return List.of();
			}
		});
	}
}
