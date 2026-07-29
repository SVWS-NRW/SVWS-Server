package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.gost.GostSchuelerGKLWahl;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.gost.DataGostAbiturdaten;
import de.svws_nrw.data.gost.DataGostJahrgangFachkombinationen;
import de.svws_nrw.data.gost.DataGostJahrgangsdaten;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanungBeratungsdaten;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.service.gost.GostServiceFactoryBuilder;
import de.svws_nrw.service.gost.klausuren.GostKlausurenServiceFactoryBuilder;
import de.svws_nrw.service.gost.klausuren.GostKlausurenVorgabeService;

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
	private final Map<Integer, List<GostStatistikFachwahl>> mapFachwahlen = new HashMap<>();
	private final Map<Long, GostSchuelerGKLWahl> mapGklWahlen = new HashMap<>();
	private final Map<Long, GostKlausurvorgabe> mapKlausurvorgaben = new HashMap<>();

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
		final List<Long> ids = new ArrayList<>(this.reportingContext.repositorySchueler().idsGeladenerSchueler());
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

		return filtereMap(idsSchueler, mapBeratungsdaten);
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
		final List<Long> ids = new ArrayList<>(this.reportingContext.repositorySchueler().idsGeladenerSchueler());
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
				ids -> GostServiceFactoryBuilder.getGostServiceFactory().getGostAbiturdatenService().getList(ids)
						.stream().collect(Collectors.toMap(a -> a.schuelerID, a -> a)),
				"GOSt-Beratungsdaten-Abiturdaten",
				this.reportingContext.logger());

		return filtereMap(idsSchueler, mapBeratungsdatenAbiturdaten);
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
		final List<Long> ids = new ArrayList<>(this.reportingContext.repositorySchueler().idsGeladenerSchueler());
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

		return filtereMap(idsSchueler, mapSchuelerAbiturdaten);
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
				return GostServiceFactoryBuilder.getGostServiceFactory().getGostJahrgangFachwahlService().getFachwahlStatistik(jahr);
			} catch (final ApiOperationException e) {
				ReportingExceptionUtils.logException(
						"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der GOSt-Fachwahlstatistik.", e,
						this.reportingContext.logger(), LogLevel.INFO, 0);
				return List.of();
			}
		});
	}


	// ##### Gleichwertige Komplexe Lernleistungen (GKL, Abitur ab 2030) #####

	/**
	 * Liefert die Wahlen zu den Gleichwertigen Komplexen Lernleistungen (GKL) des übergebenen Schülers. Beim ersten Zugriff
	 * werden die Daten für alle bekannten Schüler gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Die GKL-Wahlen des Schülers. Enthält ausschließlich {@code null}-Werte, falls für den Schüler keine Wahlen
	 *         hinterlegt sind oder das Laden fehlgeschlagen ist.
	 */
	public GostSchuelerGKLWahl gklWahl(final long idSchueler) {
		final List<Long> ids = new ArrayList<>(this.reportingContext.repositorySchueler().idsGeladenerSchueler());
		ids.add(idSchueler);
		ReportingRepositoryUtils.ladeFehlendeWerteInRepositoryMap(
				ids,
				mapGklWahlen,
				this::ladeGklWahlen,
				"GOSt-GKL-Wahlen",
				this.reportingContext.logger());
		final GostSchuelerGKLWahl wahl = mapGklWahlen.get(idSchueler);
		return (wahl != null) ? wahl : new GostSchuelerGKLWahl();
	}

	/**
	 * Lädt die GKL-Wahlen zu den übergebenen Schüler-IDs mit einer gesammelten Datenbankabfrage. Für Schüler ohne
	 * GOSt-Eintrag wird ein leeres Wahl-Objekt hinterlegt, damit die IDs nicht erneut abgefragt werden.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren GKL-Wahlen geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und den zugehörigen GKL-Wahlen als Wert.
	 */
	private Map<Long, GostSchuelerGKLWahl> ladeGklWahlen(final List<Long> idsSchueler) {
		final Map<Long, DTOGostSchueler> dtos = this.reportingContext.conn()
				.queryList(DTOGostSchueler.QUERY_LIST_BY_SCHUELER_ID, DTOGostSchueler.class, idsSchueler)
				.stream().collect(Collectors.toMap(dto -> dto.Schueler_ID, dto -> dto));
		final Map<Long, GostSchuelerGKLWahl> result = new HashMap<>();
		for (final Long id : idsSchueler) {
			final GostSchuelerGKLWahl wahl = new GostSchuelerGKLWahl();
			wahl.idSchueler = id;
			final DTOGostSchueler dto = dtos.get(id);
			if (dto != null) {
				wahl.idKlausurvorgabeEF_Sprachen = dto.GKL_EF_AF1_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeEF_GW = dto.GKL_EF_AF2_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeEF_NW = dto.GKL_EF_AF3_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_Sprachen = dto.GKL_Q_AF1_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_GW = dto.GKL_Q_AF2_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_NW = dto.GKL_Q_AF3_Klausurvorgabe_ID;
			}
			result.put(id, wahl);
		}
		return result;
	}

	/**
	 * Liefert zu den übergebenen IDs die zugehörigen GOSt-Klausurvorgaben. Die Daten werden bei erstem Zugriff aus der
	 * Datenbank geladen und im Cache gehalten.
	 *
	 * @param idsKlausurvorgaben Die IDs der Klausurvorgaben.
	 *
	 * @return Map mit der ID der Klausurvorgabe als Schlüssel und der zugehörigen Klausurvorgabe als Wert. IDs, zu denen
	 *         keine Klausurvorgabe gefunden werden konnte, fehlen in der Map.
	 */
	public Map<Long, GostKlausurvorgabe> klausurvorgaben(final List<Long> idsKlausurvorgaben) {
		ReportingRepositoryUtils.ladeFehlendeWerteInRepositoryMap(
				idsKlausurvorgaben,
				mapKlausurvorgaben,
				ids -> gostKlausurenVorgabeService().getListByIds(ids).stream()
						.collect(Collectors.toMap(vorgabe -> vorgabe.id, vorgabe -> vorgabe)),
				"GOSt-Klausurvorgaben",
				this.reportingContext.logger());
		return filtereMap(idsKlausurvorgaben, mapKlausurvorgaben);
	}

	/**
	 * Erstellt einen neuen Service für den Zugriff auf die GOSt-Klausurvorgaben. Anders als die übrigen GOSt-Services in
	 * dieser Klasse wird dieser Service nicht über {@link GostServiceFactoryBuilder} bezogen, da dessen
	 * {@code GostServiceFactory} ihn nicht anbietet — stattdessen wird dieselbe {@link GostKlausurenServiceFactory}
	 * genutzt, über die auch der Klausurplanungs-Controller den Service erzeugt.
	 *
	 * @return der Service
	 */
	private static GostKlausurenVorgabeService gostKlausurenVorgabeService() {
		return GostKlausurenServiceFactoryBuilder.getGostKlausurenServiceFactory().getGostKlausurenVorgabeService();
	}


	// ##### Hilfsmethoden #####

	/**
	 * Liefert für die übergebenen IDs eine neue Map mit den zugehörigen Werten aus dem Cache. {@code null}-IDs sowie IDs
	 * ohne Cache-Eintrag (Wert {@code null}) werden übersprungen. Wird {@code null} als ID-Liste übergeben, ist das
	 * Ergebnis eine leere Map.
	 *
	 * @param <V>   Der Wertetyp der Cache-Map.
	 * @param ids   Die IDs, deren Cache-Einträge in das Ergebnis übernommen werden sollen.
	 * @param cache Die Cache-Map, aus der die Werte gelesen werden.
	 *
	 * @return Eine neue Map mit den ID/Wert-Paaren der vorhandenen Cache-Einträge.
	 */
	private static <V> Map<Long, V> filtereMap(final List<Long> ids, final Map<Long, V> cache) {
		final Map<Long, V> result = new HashMap<>();
		if (ids == null) {
			return result;
		}
		for (final Long id : ids) {
			if (id == null) {
				continue;
			}
			final V wert = cache.get(id);
			if (wert != null) {
				result.put(id, wert);
			}
		}
		return result;
	}
}
