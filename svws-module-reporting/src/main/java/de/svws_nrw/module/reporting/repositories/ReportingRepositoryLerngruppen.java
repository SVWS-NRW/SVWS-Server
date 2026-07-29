package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
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
	private final Map<Long, List<Long>> mapKursSchuelerIds = new HashMap<>();

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
				ReportingKlasse.SORTIERUNG, sortiereListe);
		final Predicate<ReportingKlasse> filter = ReportingKlasse.FILTER.bedingung(
				this.reportingContext.filterService().getFilter(ReportingKlasse.class.getSimpleName()), null);

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
				comparator, filter,
				"Klassen", this.reportingContext.logger());
	}

	/**
	 * Gibt die Reporting-Objekte aller Klassen des übergebenen Schuljahresabschnitts zurück. Stammdaten und Reporting-Objekte werden gecacht,
	 * sodass spätere Einzel-Lookups über {@link #klasse(long)} dasselbe Objekt liefern. Die Stammdaten werden über
	 * {@link #klassenBySchuljahresabschnitt(long)} bezogen (Bulk-Pfad pro Schuljahresabschnitt).
	 *
	 * @param idSchuljahresabschnitt Die ID des Schuljahresabschnitts.
	 *
	 * @return Liste der ReportingKlasse-Objekte des Schuljahresabschnitts. Leere Liste, falls keine Daten ermittelt werden konnten.
	 */
	public List<ReportingKlasse> klassen(final long idSchuljahresabschnitt) {
		final List<KlassenDaten> klassendaten = klassenBySchuljahresabschnitt(idSchuljahresabschnitt);
		final List<ReportingKlasse> result = new ArrayList<>(klassendaten.size());
		for (final KlassenDaten daten : klassendaten) {
			mapKlassenStammdaten.putIfAbsent(daten.id, daten);
			result.add(mapKlassen.computeIfAbsent(daten.id, id -> new ProxyReportingKlasse(this.reportingContext, daten)));
		}
		return result;
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
	 * Gibt das ReportingKurs-Objekt zur übergebenen ID zurück. Fehlt der Eintrag im Cache, wird er aus der Datenbank nachgeladen.
	 * Die Methode delegiert an {@link #kurse(List, boolean)}, damit auch die Map der Kursstammdaten konsistent gefüllt wird.
	 *
	 * @param idKurs Die eindeutige ID des Kurses.
	 *
	 * @return Das ReportingKurs-Objekt oder null, falls der Kurs nicht existiert.
	 */
	public ReportingKurs kurs(final long idKurs) {
		if (idKurs < 0) {
			return null;
		}
		final List<ReportingKurs> result = kurse(List.of(idKurs), false);
		return result.isEmpty() ? null : result.get(0);
	}

	/**
	 * Gibt die Liste der Schüler-IDs zum übergebenen Kurs zurück. Beim ersten Zugriff werden die Zuordnungen für alle bereits bekannten Kurse,
	 * deren Schüler-IDs noch nicht im Cache liegen, in einem einzigen Datenbank-Query (Bulk) ermittelt und gecacht. Folgeaufrufe für andere Kurse
	 * desselben Sets liefern dann unmittelbar aus dem Cache.
	 *
	 * @param idKurs Die ID des Kurses.
	 *
	 * @return Liste der Schüler-IDs des Kurses. Leere Liste, falls der Kurs keine Schüler hat.
	 */
	public List<Long> kursSchuelerIds(final long idKurs) {
		if (mapKursSchuelerIds.containsKey(idKurs)) {
			return mapKursSchuelerIds.get(idKurs);
		}
		// Bulk-Load für die angefragte ID sowie alle bereits bekannten Kurse, deren Schüler-IDs noch nicht im Cache liegen.
		final List<Long> idsZuLaden = new ArrayList<>();
		idsZuLaden.add(idKurs);
		for (final Long id : mapKurse.keySet()) {
			if ((id != idKurs) && !mapKursSchuelerIds.containsKey(id)) {
				idsZuLaden.add(id);
			}
		}
		for (final Long id : idsZuLaden) {
			mapKursSchuelerIds.put(id, new ArrayList<>());
		}
		final List<DTOKursSchueler> dtos = this.reportingContext.conn()
				.queryList("SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID IN ?1 AND e.LernabschnittWechselNr = 0",
						DTOKursSchueler.class, idsZuLaden);
		for (final DTOKursSchueler ks : dtos) {
			final List<Long> list = mapKursSchuelerIds.get(ks.Kurs_ID);
			if (list != null) {
				list.add(ks.Schueler_ID);
			}
		}
		return mapKursSchuelerIds.get(idKurs);
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
				ReportingKurs.SORTIERUNG, sortiereListe);
		final Predicate<ReportingKurs> filter = ReportingKurs.FILTER.bedingung(
				this.reportingContext.filterService().getFilter(ReportingKurs.class.getSimpleName()), null);

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
				comparator, filter,
				"Kurse", this.reportingContext.logger());
	}

	/**
	 * Gibt die Reporting-Objekte aller Kurse des übergebenen Schuljahresabschnitts zurück. Stammdaten und Reporting-Objekte werden gecacht,
	 * sodass spätere Einzel-Lookups dasselbe Objekt liefern. Die Stammdaten werden über {@link #kurseBySchuljahresabschnitt(long)} bezogen
	 * (Bulk-Pfad pro Schuljahresabschnitt).
	 *
	 * @param idSchuljahresabschnitt Die ID des Schuljahresabschnitts.
	 *
	 * @return Liste der ReportingKurs-Objekte des Schuljahresabschnitts. Leere Liste, falls keine Daten ermittelt werden konnten.
	 */
	public List<ReportingKurs> kurse(final long idSchuljahresabschnitt) {
		final List<KursDaten> kursDaten = kurseBySchuljahresabschnitt(idSchuljahresabschnitt);
		final List<ReportingKurs> result = new ArrayList<>(kursDaten.size());
		for (final KursDaten daten : kursDaten) {
			mapKursDaten.putIfAbsent(daten.id, daten);
			result.add(mapKurse.computeIfAbsent(daten.id, id -> new ProxyReportingKurs(this.reportingContext, daten)));
		}
		return result;
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
