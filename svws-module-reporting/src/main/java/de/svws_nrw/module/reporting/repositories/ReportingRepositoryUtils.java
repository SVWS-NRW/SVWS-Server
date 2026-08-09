package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 * Generische Utility-Klasse zum gesammelten Nachladen fehlender Einträge in Repository-Maps
 * sowie zum Erstellen von Listen aus Reporting-Objekten basierend auf IDs.
 */
final class ReportingRepositoryUtils {

	private ReportingRepositoryUtils() {
	}

	/**
	 * Generische Hilfsmethode zum Laden und Erstellen einer Liste von Reporting-Objekten basierend auf IDs.
	 *
	 * @param <S> Der Typ der Stammdaten (z. B. LehrerStammdaten, SchuelerStammdaten)
	 * @param <R> Der Typ der Reporting-Objekte (z.B. ReportingLehrer, ReportingSchueler)
	 * @param ids Die Liste der IDs, für die Reporting-Objekte erstellt werden sollen
	 * @param mapStammdaten Die Map mit bereits geladenen Stammdaten
	 * @param mapReportingObjekte Eine Map mit bereits erstellten Reporting-Objekten
	 * @param stammdatenLoader Funktion zum Laden fehlender Stammdaten aus der DB (liefert eine Liste)
	 * @param reportingObjektErsteller Funktion zum Erstellen eines Reporting-Objekts aus Stammdaten
	 * @param idExtractor Funktion zum Extrahieren der ID aus einem Stammdaten-Objekt
	 * @param comparator Comparator für die Sortierung (niemals {@code null}; ein Identitäts-Comparator lässt die Reihenfolge unverändert)
	 * @param filter Predicate, das nur akzeptierte Objekte in die Rückgabe-Liste lässt; ist {@code null}, wird nicht gefiltert.
	 *               Die Stammdaten- und Reporting-Objekt-Maps werden vollständig befüllt — die Filterung wirkt nur auf die Rückgabe.
	 * @param datentyp Bezeichnung des Datentyps für Fehlermeldungen (z. B. "Lehrer", "Schüler")
	 * @param logger Der Logger für Fehlermeldungen
	 *
	 * @return Eine ggf. gefilterte und sortierte Liste von Reporting-Objekten
	 */
	@SuppressWarnings("java:S107")
	public static <S, R> List<R> erstelleReportingListe(final List<Long> ids, final Map<Long, S> mapStammdaten, final Map<Long, R> mapReportingObjekte,
			final Function<List<Long>, List<S>> stammdatenLoader, final LongFunction<R> reportingObjektErsteller, final ToLongFunction<S> idExtractor,
			final Comparator<R> comparator, final Predicate<R> filter, final String datentyp, final Logger logger) {

		if ((ids == null) || ids.isEmpty()) {
			return new ArrayList<>();
		}
		final List<Long> idsNonNull = ids.stream().filter(Objects::nonNull).distinct().filter(id -> id >= 0).toList();
		if (idsNonNull.isEmpty()) {
			return new ArrayList<>();
		}

		// Wir adaptieren den Listen-Loader zu einem Map-Loader, damit wir die einheitliche Lademethode nutzen können
		final Function<List<Long>, Map<Long, S>> mapLoader = fehlendeIds -> {
			final Map<Long, S> resultMap = new HashMap<>();
			for (final S data : stammdatenLoader.apply(fehlendeIds)) {
				if (data != null) {
					resultMap.put(idExtractor.applyAsLong(data), data);
				}
			}
			return resultMap;
		};

		// Nachladen aller Daten über die nun einzige Lademethode
		ladeFehlendeWerteInRepositoryMap(idsNonNull, mapStammdaten, mapLoader, datentyp, logger);

		final List<R> result = erzeugeReportingObjekte(idsNonNull, mapStammdaten, mapReportingObjekte, reportingObjektErsteller);

		final Stream<R> stream = (filter == null) ? result.stream() : result.stream().filter(filter);
		return stream.sorted(comparator).toList();
	}

	/**
	 * Stellt sicher, dass für die übergebenen IDs Einträge in der Repository-Map existieren.
	 * Schlägt das gesammelte Laden fehl, wird ein Fallback ausgelöst, bei dem alle noch fehlenden IDs
	 * einzeln geladen werden, um fehlerhafte Datensätze zu isolieren.
	 *
	 * @param <T>               Der Typ der Werte in der Repository-Map.
	 * @param ids               Die Menge an IDs, für die Daten beschafft werden sollen.
	 * @param repositoryMap     Die Repository-Map, in die geladene Werte eingetragen werden.
	 * @param bulkLoader        Eine Funktion, die eine Liste von IDs entgegennimmt und eine Map mit Ergebnissen liefert.
	 * @param datenbezeichnung  Eine sprechende Bezeichnung der Daten für Log-Meldungen.
	 * @param logger            Logger für Fehler- und Info-Meldungen.
	 */
	public static <T> void ladeFehlendeWerteInRepositoryMap(final Collection<Long> ids, final Map<Long, T> repositoryMap,
			final Function<List<Long>, Map<Long, T>> bulkLoader, final String datenbezeichnung, final Logger logger) {

		if ((ids == null) || ids.isEmpty()) {
			return;
		}
		final List<Long> fehlendeIds = ids.stream().filter(Objects::nonNull).distinct().filter(id -> !repositoryMap.containsKey(id)).toList();
		if (fehlendeIds.isEmpty()) {
			return;
		}

		try {
			// Versuch: Bulk-Load
			repositoryMap.putAll(bulkLoader.apply(fehlendeIds));
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"INFO: Fehler beim gesammelten Laden von " + datenbezeichnung + ". Versuche einzelnes Laden für fehlende IDs.", e, logger,
					LogLevel.INFO, 0);

			// Fallback: Jede fehlende ID einzeln probieren
			for (final Long id : fehlendeIds) {
				try {
					repositoryMap.putAll(bulkLoader.apply(List.of(id)));
				} catch (final Exception exSingle) {
					ReportingExceptionUtils.logException(
							"INFO: Fehler beim einzelnen Laden von " + datenbezeichnung + " für ID " + id + ".", exSingle, logger, LogLevel.INFO, 0);
					// Markieren, dass für diese ID das Laden fehlschlug, um Endlosschleifen bei erneuter Anfrage zu vermeiden
					repositoryMap.put(id, null);
				}
			}
		}
	}

	/**
	 * Stellt sicher, dass für die übergebenen IDs Einträge für Listen-Daten in der Map existieren.
	 * Anders als bei normalen Werten, wird bei fehlenden Ergebnissen immer eine leere Liste hinterlegt,
	 * sodass Konsumenten nie {@code null} sehen.
	 *
	 * @param <T>               Der Element-Typ der Listen in der Repository-Map.
	 * @param ids               Die Menge an IDs, für die Listen-Daten beschafft werden sollen.
	 * @param repositoryMap     Die Repository-Map, in die geladene Listen eingetragen werden.
	 * @param bulkLoader        Eine Funktion, die eine Liste von IDs entgegennimmt und eine Map mit Ergebnislisten liefert.
	 * @param datenbezeichnung  Eine sprechende Bezeichnung der Daten für Log-Meldungen.
	 * @param logger            Logger für Fehler- und Info-Meldungen.
	 */
	public static <T> void ladeFehlendeListenInRepositoryMap(final Collection<Long> ids, final Map<Long, List<T>> repositoryMap,
			final Function<List<Long>, Map<Long, List<T>>> bulkLoader, final String datenbezeichnung, final Logger logger) {

		if ((ids == null) || ids.isEmpty()) {
			return;
		}
		final List<Long> fehlendeIds = ids.stream().filter(Objects::nonNull).distinct().filter(id -> !repositoryMap.containsKey(id)).toList();
		if (fehlendeIds.isEmpty()) {
			return;
		}

		try {
			repositoryMap.putAll(bulkLoader.apply(fehlendeIds));
			// Falls die DB für einige IDs keine Liste geliefert hat, setzen wir eine leere Liste als Fallback
			for (final Long id : fehlendeIds) {
				repositoryMap.putIfAbsent(id, new ArrayList<>());
			}
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"INFO: Fehler beim gesammelten Laden von " + datenbezeichnung + ". Versuche einzelnes Laden für fehlende IDs.", e, logger,
					LogLevel.INFO, 0);

			for (final Long id : fehlendeIds) {
				try {
					final Map<Long, List<T>> singleResult = bulkLoader.apply(List.of(id));
					final List<T> daten = singleResult.get(id);
					repositoryMap.put(id, (daten != null) ? daten : new ArrayList<>());
				} catch (final Exception exSingle) {
					ReportingExceptionUtils.logException(
							"INFO: Fehler beim einzelnen Laden von " + datenbezeichnung + " für ID " + id + ". Setze leere Liste.", exSingle, logger,
							LogLevel.INFO, 0);
					repositoryMap.put(id, new ArrayList<>());
				}
			}
		}
	}

	/**
	 * Erstellt für alle IDs mit vorhandenen Stammdaten ein Reporting-Objekt und cached es in der übergebenen Map.
	 * Nutzt `putIfAbsent` um Fehler bei zirkulären Abhängigkeiten (z.B. Klasse -> Schüler -> Klasse) zu vermeiden.
	 * <p>Maßgeblich ist der tatsächlich vorhandene Wert, nicht der Schlüssel: {@link #ladeFehlendeWerteInRepositoryMap} hinterlegt für eine technisch nicht
	 * ladbare ID den Fehler-Marker {@code put(id, null)}. Dieser erfüllt zwar {@code containsKey}, steht aber für "keine Stammdaten". Würde er als Datensatz
	 * gewertet, erhielte der Ersteller {@code null}-Stammdaten und liefe in eine {@link NullPointerException}. Der betroffene Datensatz wird stattdessen
	 * ausgelassen.</p>
	 *
	 * @param <S> Der Typ der Stammdaten (z. B. LehrerStammdaten, SchuelerStammdaten)
	 * @param <R> Der Typ des Reporting-Objektes (z. B. LehrerReporting, SchuelerReporting)
	 * @param idsNonNull Die Liste der IDs für die Reporting-Objekte erstellt werden sollen
	 * @param mapStammdaten Die Map mit den Stammdaten
	 * @param mapReportingObjekte Die Map für das Caching der Reporting-Objekte
	 * @param reportingObjektErsteller Die Funktion zum Erstellen des Reporting-Objektes
	 *
	 * @return Die Liste der erstellten Reporting-Objekte
	 */
	private static <S, R> List<R> erzeugeReportingObjekte(final List<Long> idsNonNull, final Map<Long, S> mapStammdaten,
			final Map<Long, R> mapReportingObjekte, final LongFunction<R> reportingObjektErsteller) {

		final List<R> result = new ArrayList<>();
		idsNonNull.stream().filter(id -> mapStammdaten.get(id) != null).forEach(id -> {
			if (!mapReportingObjekte.containsKey(id)) {
				mapReportingObjekte.putIfAbsent(id, reportingObjektErsteller.apply(id));
			}
			result.add(mapReportingObjekte.get(id));
		});
		return result;
	}
}
