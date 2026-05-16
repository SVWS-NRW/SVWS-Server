package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionHjData;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.gost.DBUtilsGostLaufbahn;
import de.svws_nrw.data.gost.DataGostAbiturdaten;
import de.svws_nrw.data.gost.DataGostAbiturjahrgangFachwahlen;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.data.gost.DataGostJahrgangFachkombinationen;
import de.svws_nrw.data.gost.DataGostJahrgangsdaten;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanungBeratungsdaten;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausuren;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import jakarta.ws.rs.core.Response.Status;

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

	/** Zwischenspeicher für die Liste der vorhandenen Abiturjahrgänge. */
	private List<Integer> abiturjahrgaenge = null;

	/**
	 * Erstellt ein neues ReportingRepositoryGost.
	 *
	 * @param reportingContext Der zentrale Reporting-Context mit Zugriff auf die domänenspezifischen Repositories.
	 */
	public ReportingRepositoryGost(final ReportingContext reportingContext) {
		this.reportingContext = reportingContext;
	}


	// ##### GOSt-Schul-Vorbedingung #####

	/**
	 * Gibt die Abi-Jahrgänge zurück, die in der Datenbank zu DTOGostJahrgangsdaten hinterlegt sind.
	 * Die Liste wird beim ersten Aufruf aus der Datenbank geladen und für die Lebensdauer des Reporting-Contexts zwischengespeichert.
	 *
	 * @return Liste der vorhandenen Abi-Jahrgänge.
	 */
	public List<Integer> abiturjahrgaenge() {
		if (abiturjahrgaenge == null) {
			abiturjahrgaenge = this.reportingContext.conn().queryAll(DTOGostJahrgangsdaten.class).stream().map(aj -> aj.Abi_Jahrgang).toList();
		}
		return abiturjahrgaenge;
	}


	// ##### Jahrgangsdaten und Fächer #####

	/**
	 * Lädt die GOSt-Jahrgangsdaten zum übergebenen Abiturjahr aus der Datenbank. Die Daten werden bei erstem Zugriff aus der
	 * Datenbank geladen und im Cache gehalten.
	 *
	 * @param abiturjahr Das Abiturjahr des Jahrgangs.
	 *
	 * @return Die GOSt-Jahrgangsdaten.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public GostJahrgangsdaten jahrgangsdaten(final int abiturjahr) throws ApiOperationException {
		return mapAbiturjahrgangDaten.computeIfAbsent(abiturjahr,
				jahr -> DataGostJahrgangsdaten.getJahrgangsdaten(this.reportingContext.conn(), jahr));
	}

	/**
	 * Lädt den GOSt-FächerManager zum übergebenen Abiturjahr aus der Datenbank und ergänzt die Fachkombinationen. Die Daten
	 * werden bei erstem Zugriff aus der Datenbank geladen und im Cache gehalten.
	 *
	 * @param abiturjahr Das Abiturjahr des Jahrgangs.
	 *
	 * @return Der mit Fachkombinationen ergänzte FächerManager des Abiturjahrgangs.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public GostFaecherManager faecherManager(final int abiturjahr) throws ApiOperationException {
		return mapAbiturjahrgangFaecher.computeIfAbsent(abiturjahr, jahr -> {
			final int auswahlSchuljahr = this.reportingContext.repositorySchule().auswahlSchuljahresabschnitt().schuljahr();
			final GostFaecherManager manager =
					DBUtilsFaecherGost.getFaecherManager(auswahlSchuljahr, this.reportingContext.conn(), jahr);
			manager.addFachkombinationenAll(
					DataGostJahrgangFachkombinationen.getFachkombinationen(this.reportingContext.conn(), jahr));
			return manager;
		});
	}


	// ##### Beratungsdaten und Beratungs-Abiturdaten #####

	/**
	 * Liefert die GOSt-Laufbahnberatungsdaten zum übergebenen Schüler. Beim ersten Zugriff werden die Daten für alle bekannten
	 * Schüler gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Die Beratungsdaten oder {@code null}, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public GostLaufbahnplanungBeratungsdaten beratungsdaten(final long idSchueler) {
		final List<Long> ids = new ArrayList<>(this.reportingContext.repositorySchueler().stammdaten().keySet());
		ids.add(idSchueler);
		beratungsdaten(ids);
		return mapBeratungsdaten.get(idSchueler);
	}

	/**
	 * Lädt die GOSt-Laufbahnberatungsdaten zu den übergebenen Schüler-IDs aus der Datenbank und übernimmt sie in den Cache.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Beratungsdaten geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und den zugehörigen Beratungsdaten als Wert.
	 */
	public Map<Long, GostLaufbahnplanungBeratungsdaten> beratungsdaten(final List<Long> idsSchueler) {
		ReportingRepositoryUtils.ladeFehlendeWerteInRepositoryMap(
				idsSchueler,
				mapBeratungsdaten,
				ids -> new DataGostSchuelerLaufbahnplanungBeratungsdaten(this.reportingContext.conn()).getMapFromIDs(ids),
				"GOSt-Beratungsdaten",
				this.reportingContext.logger());

		final Map<Long, GostLaufbahnplanungBeratungsdaten> result = new HashMap<>();
		if (idsSchueler != null) {
			for (final Long id : idsSchueler) {
				if ((id != null) && (mapBeratungsdaten.get(id) != null)) {
					result.put(id, mapBeratungsdaten.get(id));
				}
			}
		}
		return result;
	}

	/**
	 * Liefert die im Rahmen der Laufbahnberatung ermittelten Abiturdaten zum übergebenen Schüler. Beim ersten Zugriff werden
	 * die Daten für alle bekannten Schüler gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Die Beratungs-Abiturdaten oder {@code null}, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public Abiturdaten beratungsdatenAbiturdaten(final long idSchueler) {
		final List<Long> ids = new ArrayList<>(this.reportingContext.repositorySchueler().stammdaten().keySet());
		ids.add(idSchueler);
		beratungsdatenAbiturdaten(ids);
		return mapBeratungsdatenAbiturdaten.get(idSchueler);
	}

	/**
	 * Lädt die im Rahmen der Laufbahnberatung ermittelten Abiturdaten zu den übergebenen Schüler-IDs aus der Datenbank und
	 * übernimmt sie in den Cache.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Beratungs-Abiturdaten geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und den zugehörigen Abiturdaten als Wert.
	 */
	public Map<Long, Abiturdaten> beratungsdatenAbiturdaten(final List<Long> idsSchueler) {
		ReportingRepositoryUtils.ladeFehlendeWerteInRepositoryMap(
				idsSchueler,
				mapBeratungsdatenAbiturdaten,
				ids -> DBUtilsGostLaufbahn.getMapFromIDs(this.reportingContext.conn(), ids),
				"GOSt-Beratungsdaten-Abiturdaten",
				this.reportingContext.logger());

		final Map<Long, Abiturdaten> result = new HashMap<>();
		if (idsSchueler != null) {
			for (final Long id : idsSchueler) {
				if ((id != null) && (mapBeratungsdatenAbiturdaten.get(id) != null)) {
					result.put(id, mapBeratungsdatenAbiturdaten.get(id));
				}
			}
		}
		return result;
	}

	// ##### Abiturdaten der Schüler #####

	/**
	 * Liefert die GOSt-Abiturdaten zum übergebenen Schüler. Beim ersten Zugriff werden die Daten für alle bekannten Schüler
	 * gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Die Abiturdaten oder {@code null}, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public Abiturdaten schuelerAbiturdaten(final long idSchueler) {
		final List<Long> ids = new ArrayList<>(this.reportingContext.repositorySchueler().stammdaten().keySet());
		ids.add(idSchueler);
		schuelerAbiturdaten(ids);
		return mapSchuelerAbiturdaten.get(idSchueler);
	}

	/**
	 * Lädt die GOSt-Abiturdaten zu den übergebenen Schüler-IDs aus der Datenbank und übernimmt sie in den Cache.
	 * Das Abiturjahr wird beim Konstruktor von {@link DataGostAbiturdaten} mit {@code null} übergeben, da
	 * {@link DataGostAbiturdaten#getMapAbiturdatenFromIDs(List)} nicht auf das Abiturjahr angewiesen ist.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Abiturdaten geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und den zugehörigen Abiturdaten als Wert.
	 */
	public Map<Long, Abiturdaten> schuelerAbiturdaten(final List<Long> idsSchueler) {
		ReportingRepositoryUtils.ladeFehlendeWerteInRepositoryMap(
				idsSchueler,
				mapSchuelerAbiturdaten,
				ids -> new DataGostAbiturdaten(this.reportingContext.conn(), null).getMapAbiturdatenFromIDs(ids),
				"GOSt-Abiturdaten",
				this.reportingContext.logger());

		final Map<Long, Abiturdaten> result = new HashMap<>();
		if (idsSchueler != null) {
			for (final Long id : idsSchueler) {
				if ((id != null) && (mapSchuelerAbiturdaten.get(id) != null)) {
					result.put(id, mapSchuelerAbiturdaten.get(id));
				}
			}
		}
		return result;
	}


	// ##### Blockungsergebnis-Vorbedingung #####

	/**
	 * Prüft, ob zur übergebenen Blockungsergebnis-ID ein lesbares Ergebnis in der Datenbank existiert.
	 *
	 * @param idBlockungsergebnis Die ID des Blockungsergebnisses.
	 *
	 * @throws ApiOperationException Falls keine Daten ermittelt werden konnten ({@link Status#NOT_FOUND}).
	 */
	public void pruefeBlockungsergebnis(final long idBlockungsergebnis) throws ApiOperationException {
		try {
			DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(this.reportingContext.conn(),
					DataGostBlockungsergebnisse.getErgebnisFromID(this.reportingContext.conn(), idBlockungsergebnis).blockungID);
		} catch (final ApiOperationException aoe) {
			this.reportingContext.logger().logLn(LogLevel.ERROR, 4,
					"FEHLER: Mit der angegebenen Blockungsergebnis-ID konnte keine Daten ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, aoe,
					"FEHLER: Mit der angegebenen Blockungsergebnis-ID konnte keine Daten ermittelt werden.");
		}
	}

	/**
	 * Lädt das GOSt-Blockungsergebnis zur übergebenen ID aus der Datenbank.
	 *
	 * @param idBlockungsergebnis Die ID des Blockungsergebnisses.
	 *
	 * @return Das geladene Blockungsergebnis.
	 *
	 * @throws ApiOperationException Falls das Ergebnis nicht ermittelt werden konnte.
	 */
	public GostBlockungsergebnis blockungsergebnis(final long idBlockungsergebnis) throws ApiOperationException {
		return DataGostBlockungsergebnisse.getErgebnisFromID(this.reportingContext.conn(), idBlockungsergebnis);
	}

	/**
	 * Lädt den Blockungsdaten-Manager zur angegebenen Blockungs-ID aus der Datenbank.
	 *
	 * @param idBlockung Die ID der Blockung.
	 *
	 * @return Der Blockungsdaten-Manager zur Blockung.
	 *
	 * @throws ApiOperationException Falls die Daten nicht ermittelt werden konnten.
	 */
	public GostBlockungsdatenManager blockungsdatenManager(final long idBlockung) throws ApiOperationException {
		return DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(this.reportingContext.conn(), idBlockung);
	}


	// ##### Klausurplanungsdaten #####

	/**
	 * Lädt alle GOSt-Klausurplanungsdaten zur übergebenen Auswahl an Abiturjahrgang/Halbjahr-Paaren.
	 *
	 * @param selection Die Auswahl aus Abiturjahrgang und GOSt-Halbjahr.
	 *
	 * @return Die zusammengeführten Klausurplanungsdaten zur Auswahl.
	 *
	 * @throws ApiOperationException Falls die Daten nicht ermittelt werden konnten.
	 */
	public GostKlausurenCollectionAllData klausurplanDaten(final List<GostKlausurenCollectionHjData> selection) throws ApiOperationException {
		return DataGostKlausuren.getAllData(this.reportingContext.conn(), selection);
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
