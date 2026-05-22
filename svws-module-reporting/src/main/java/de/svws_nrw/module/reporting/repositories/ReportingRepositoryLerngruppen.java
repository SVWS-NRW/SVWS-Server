package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingKlasse;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingKurs;
import de.svws_nrw.module.reporting.types.lerngruppen.ProxyReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ProxyReportingKurs;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 * Domänen-Repository für Klassen und Kurse.
 * Die Daten werden bei Bedarf aus der Datenbank nachgeladen und im Cache gehalten.
 */
public class ReportingRepositoryLerngruppen {

	private final ReportingContext reportingContext;

	private final Map<Long, ReportingKlasse> mapKlassen = new HashMap<>();
	private final Map<Long, KlassenDaten> mapKlassenStammdaten = new HashMap<>();
	private final Map<Long, ReportingKurs> mapKurse = new HashMap<>();
	private final Map<Long, Map<Long, Double>> mapKurslehrerWochenstunden = new HashMap<>();
	private final Map<Long, List<KlassenDaten>> mapKlassenDatenBySchuljahresabschnitt = new HashMap<>();
	private final Map<Long, List<KursDaten>> mapKursDatenBySchuljahresabschnitt = new HashMap<>();
	private final Map<Long, KursDaten> mapKursDaten = new HashMap<>();

	/**
	 * Erstellt ein neues ReportingLerngruppenRepository.
	 *
	 * @param reportingContext Der zentrale Reporting-Context mit Zugriff auf die domänenspezifischen Repositories.
	 */
	public ReportingRepositoryLerngruppen(final ReportingContext reportingContext) {
		this.reportingContext = reportingContext;
	}


	// ##### Klassen #####

	/**
	 * Gibt die Map der bereits erzeugten ReportingKlasse-Objekte zurück, indiziert nach Klassen-ID.
	 *
	 * @return Map der Klassen
	 */
	public Map<Long, ReportingKlasse> klassen() {
		return mapKlassen;
	}

	/**
	 * Gibt das ReportingKlasse-Objekt zur übergebenen ID zurück. Fehlt der Eintrag im Cache, wird er aus der Datenbank nachgeladen.
	 * Die Methode delegiert an {@link #klassen(List, boolean)}, damit auch die Map der Klassenstammdaten konsistent gefüllt wird.
	 *
	 * @param idKlasse Die eindeutige ID der Klasse.
	 *
	 * @return Das ReportingKlasse-Objekt oder null, falls die Klasse nicht existiert.
	 */
	public ReportingKlasse klasse(final long idKlasse) {
		if (idKlasse < 0) {
			return null;
		}
		if (mapKlassen.containsKey(idKlasse)) {
			return mapKlassen.get(idKlasse);
		}
		final List<ReportingKlasse> result = klassen(List.of(idKlasse), false);
		return result.isEmpty() ? null : result.get(0);
	}

	/**
	 * Gibt eine nach Standardsortierung sortierte Liste von ReportingKlasse-Objekten zu den übergebenen IDs zurück.
	 *
	 * @param idsKlassen Liste der Klassen-IDs.
	 *
	 * @return Sortierte Liste von ReportingKlasse-Objekten.
	 */
	public List<ReportingKlasse> klassen(final List<Long> idsKlassen) {
		return klassen(idsKlassen, true);
	}

	/**
	 * Gibt eine Liste von ReportingKlasse-Objekten zu den übergebenen IDs zurück, optional sortiert.
	 * Fehlende Klassenstammdaten werden im Bulk aus der Datenbank nachgeladen.
	 *
	 * @param idsKlassen    Liste der Klassen-IDs.
	 * @param sortiereListe Gibt an, ob die definierte Sortierung angewendet werden soll.
	 *
	 * @return Liste von ReportingKlasse-Objekten.
	 */
	public List<ReportingKlasse> klassen(final List<Long> idsKlassen, final boolean sortiereListe) {
		final Comparator<ReportingKlasse> comparator = ComparatorFactory.buildComparator(this.reportingContext.sortierungService(),
				this.reportingContext.logger(), ReportingKlasse.class.getSimpleName(),
				SortierungRegistryReportingKlasse.sortierungRegistry(), sortiereListe);

		return ReportingRepositoryUtils.erstelleReportingListe(idsKlassen, mapKlassenStammdaten, mapKlassen,
				fehlendeIds -> {
					final DataKlassendaten dataKlassendaten = new DataKlassendaten(this.reportingContext.conn());
					final List<DTOKlassen> dtos = dataKlassendaten.getDTOsByIds(fehlendeIds);
					// Sollte der Fall eintreten, dass die IDs der Klassen aus unterschiedlichen Schuljahresabschnitten stammen,
					// so werden die Klassen in Abschnittsgruppen getrennt abgefragt.
					final Map<Long, List<Long>> idsByAbschnitt = dtos.stream()
							.collect(Collectors.groupingBy(dto -> dto.Schuljahresabschnitts_ID,
									Collectors.mapping(dto -> dto.ID, Collectors.toList())));
					final List<KlassenDaten> result = new ArrayList<>();
					for (final Map.Entry<Long, List<Long>> entry : idsByAbschnitt.entrySet()) {
						result.addAll(dataKlassendaten.getListByIdsOhneSchueler(entry.getValue(), entry.getKey()));
					}
					return result;
				},
				key -> {
					final KlassenDaten daten = mapKlassenStammdaten.get(key);
					/* Der Aufruf 'klasse' über den Schuljahresabschnitt ruft durch Überladung folgende Methode auf:
					 * @see de.svws_nrw.module.reporting.types.schule.ProxyReportingSchuljahresabschnitt.mapKlassen.
					 * Damit werden alle Klassen des Schuljahresabschnitts aus dem Cache oder aus der Datenbank geladen und nicht jede Klasse einzeln.
					 */
					this.reportingContext.repositorySchule().schuljahresabschnitt(daten.idSchuljahresabschnitt).klasse(key);
					return new ProxyReportingKlasse(this.reportingContext, daten);
				},
				stammdaten -> stammdaten.id,
				comparator,
				"Klassen", this.reportingContext.logger());
	}

	/**
	 * Gibt die Klassendaten zum übergebenen Schuljahresabschnitt zurück. Die Daten werden bei erstem Zugriff aus der Datenbank geladen
	 * und im Cache gehalten.
	 *
	 * @param idSchuljahresabschnitt Die ID des Schuljahresabschnitts.
	 *
	 * @return Liste der Klassendaten des Schuljahresabschnitts. Leere Liste, falls keine Daten ermittelt werden konnten.
	 */
	public List<KlassenDaten> klassenBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return mapKlassenDatenBySchuljahresabschnitt.computeIfAbsent(idSchuljahresabschnitt, id -> {
			try {
				return new DataKlassendaten(this.reportingContext.conn()).getListBySchuljahresabschnittID(id, true);
			} catch (final Exception e) {
				ReportingExceptionUtils.logException(
						"FEHLER: Fehler bei der Erstellung der Klassenliste für den Schuljahresabschnitt %d.".formatted(id), e,
						this.reportingContext.logger(), LogLevel.ERROR, 0);
				return new ArrayList<>();
			}
		});
	}


	// ##### Kurse #####

	/**
	 * Gibt die Map der bereits erzeugten ReportingKurs-Objekte zurück, indiziert nach Kurs-ID.
	 *
	 * @return Map der Kurse
	 */
	public Map<Long, ReportingKurs> kurse() {
		return mapKurse;
	}

	/**
	 * Gibt die Kursdaten zum übergebenen Kurs zurück. Die Daten werden bei erstem Zugriff aus der Datenbank geladen und im Cache gehalten.
	 *
	 * @param idKurs Die ID des Kurses.
	 *
	 * @return Die Kursdaten oder null, falls die Daten nicht ermittelt werden konnten.
	 */
	public KursDaten kurs(final long idKurs) {
		if (mapKursDaten.containsKey(idKurs)) {
			return mapKursDaten.get(idKurs);
		}
		try {
			final KursDaten kursDaten = DataKurse.getKursdaten(this.reportingContext.conn(), idKurs);
			mapKursDaten.put(idKurs, kursDaten);
			return kursDaten;
		} catch (final ApiOperationException e) {
			ReportingExceptionUtils.logException(
					"FEHLER: Fehler bei der Ermittlung der Daten des Kurses %d.".formatted(idKurs), e,
					this.reportingContext.logger(), LogLevel.ERROR, 0);
			mapKursDaten.put(idKurs, null);
			return null;
		}
	}

	/**
	 * Gibt eine nach Standardsortierung sortierte Liste von ReportingKurs-Objekten zu den übergebenen IDs zurück.
	 *
	 * @param idsKurse Liste der Kurs-IDs.
	 *
	 * @return Sortierte Liste von ReportingKurs-Objekten.
	 */
	public List<ReportingKurs> kurse(final List<Long> idsKurse) {
		return kurse(idsKurse, true);
	}

	/**
	 * Gibt eine Liste von ReportingKurs-Objekten zu den übergebenen IDs zurück, optional sortiert.
	 * Fehlende Kursstammdaten werden im Bulk aus der Datenbank nachgeladen.
	 *
	 * @param idsKurse      Liste der Kurs-IDs.
	 * @param sortiereListe Gibt an, ob die definierte Sortierung angewendet werden soll.
	 *
	 * @return Liste von ReportingKurs-Objekten.
	 */
	public List<ReportingKurs> kurse(final List<Long> idsKurse, final boolean sortiereListe) {
		final Comparator<ReportingKurs> comparator = ComparatorFactory.buildComparator(this.reportingContext.sortierungService(),
				this.reportingContext.logger(), ReportingKurs.class.getSimpleName(),
				SortierungRegistryReportingKurs.sortierungRegistry(), sortiereListe);

		return ReportingRepositoryUtils.erstelleReportingListe(idsKurse, mapKursDaten, mapKurse,
				fehlendeIds -> new DataKurse(this.reportingContext.conn()).getListByIDs(fehlendeIds, false),
				key -> {
					final KursDaten daten = mapKursDaten.get(key);
					/* Der Aufruf 'kurs' über den Schuljahresabschnitt ruft durch Überladung folgende Methode auf:
					 * @see de.svws_nrw.module.reporting.types.schule.ProxyReportingSchuljahresabschnitt.mapKurse.
					 * Damit werden alle Kurse des Schuljahresabschnitts aus dem Cache oder aus der Datenbank geladen und nicht jeder Kurs einzeln.
					 */
					this.reportingContext.repositorySchule().schuljahresabschnitt(daten.idSchuljahresabschnitt).kurs(key);
					return new ProxyReportingKurs(this.reportingContext, daten);
				},
				stammdaten -> stammdaten.id,
				comparator,
				"Kurse", this.reportingContext.logger());
	}

	/**
	 * Gibt die Kursdaten zum übergebenen Schuljahresabschnitt zurück. Die Daten werden bei erstem Zugriff aus der Datenbank geladen
	 * und im Cache gehalten.
	 *
	 * @param idSchuljahresabschnitt Die ID des Schuljahresabschnitts.
	 *
	 * @return Liste der Kursdaten des Schuljahresabschnitts. Leere Liste, falls keine Daten ermittelt werden konnten.
	 */
	public List<KursDaten> kurseBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return mapKursDatenBySchuljahresabschnitt.computeIfAbsent(idSchuljahresabschnitt, id -> {
			try {
				return new DataKurse(this.reportingContext.conn()).getListBySchuljahresabschnittID(id, true);
			} catch (final Exception e) {
				ReportingExceptionUtils.logException(
						"FEHLER: Fehler bei der Erstellung der Liste der Kurse für den Schuljahresabschnitt %d.".formatted(id), e,
						this.reportingContext.logger(), LogLevel.ERROR, 0);
				return new ArrayList<>();
			}
		});
	}

	/**
	 * Gibt die Zuordnung der zusätzlichen Kurslehrer mit ihren Wochenstundenanteilen zum übergebenen Kurs zurück.
	 * Die Daten werden aus den {@link DTOKursLehrer}-Einträgen ermittelt und im Cache gehalten.
	 *
	 * @param idKurs Die ID des Kurses.
	 *
	 * @return Map Lehrer-ID → Wochenstundenanteil. Leere Map, falls keine zusätzlichen Kurslehrer existieren.
	 */
	public Map<Long, Double> kurslehrerWochenstunden(final long idKurs) {
		return mapKurslehrerWochenstunden.computeIfAbsent(idKurs, id -> {
			final List<DTOKursLehrer> dtoKursLehrer =
					this.reportingContext.conn().queryList(DTOKursLehrer.QUERY_BY_KURS_ID, DTOKursLehrer.class, id);
			if (dtoKursLehrer.isEmpty()) {
				return new LinkedHashMap<>();
			}
			return dtoKursLehrer.stream()
					.filter(Objects::nonNull)
					.collect(Collectors.toMap(k -> k.Lehrer_ID, k -> k.Anteil, (a, b) -> a, LinkedHashMap::new));
		});
	}
}
