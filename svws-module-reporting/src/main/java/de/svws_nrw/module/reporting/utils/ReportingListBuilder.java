package de.svws_nrw.module.reporting.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;

/**
 * Generische Utility-Klasse zum Laden und Erstellen von Listen aus Reporting-Objekten basierend auf IDs.
 */
public final class ReportingListBuilder {

	private ReportingListBuilder() {
	}

	/**
	 * Generische Hilfsmethode zum Laden und Erstellen einer Liste von Reporting-Objekten basierend auf IDs.
	 *
	 * @param <S> Der Typ der Stammdaten (z. B. LehrerStammdaten, SchuelerStammdaten)
	 * @param <R> Der Typ der Reporting-Objekte (z.B. ReportingLehrer, ReportingSchueler)
	 * @param ids Die Liste der IDs, für die Reporting-Objekte erstellt werden sollen
	 * @param mapStammdaten Die Map mit bereits geladenen Stammdaten
	 * @param mapReportingObjekte Eine Map mit bereits erstellten Reporting-Objekten
	 * @param stammdatenLoader Funktion zum Laden fehlender Stammdaten aus der DB
	 * @param reportingObjektErsteller Funktion zum Erstellen eines Reporting-Objekts aus Stammdaten
	 * @param idExtractor Funktion zum Extrahieren der ID aus einem Stammdaten-Objekt
	 * @param comparatorOptional Optional: Comparator für die Sortierung
	 * @param datentyp Bezeichnung des Datentyps für Fehlermeldungen (z. B. "Lehrer", "Schüler")
	 * @param logger Der Logger für Fehlermeldungen
	 *
	 * @return Eine sortierte Liste von Reporting-Objekten
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public static <S, R> List<R> erstelleReportingListe(final List<Long> ids, final Map<Long, S> mapStammdaten, final Map<Long, R> mapReportingObjekte,
			final Function<List<Long>, List<S>> stammdatenLoader, final Function<Long, R> reportingObjektErsteller,
			final ToLongFunction<S> idExtractor, final Optional<Comparator<R>> comparatorOptional,
			final String datentyp, final Logger logger) {

		final List<R> result = new ArrayList<>();

		if (ids == null) {
			return result;
		}

		final List<Long> idsNonNull = ids.stream().filter(Objects::nonNull).filter(id -> id >= 0).distinct().toList();

		if (idsNonNull.isEmpty()) {
			return result;
		}

		// Fehlende Stammdaten ermitteln
		final List<Long> fehlendeIds = idsNonNull.stream().filter(id -> !mapStammdaten.containsKey(id)).toList();

		if (!fehlendeIds.isEmpty()) {
			try {
				final List<S> fehlendeStammdaten = stammdatenLoader.apply(fehlendeIds);
				for (final S stammdaten : fehlendeStammdaten) {
					if (stammdaten != null) {
						final Long id = idExtractor.applyAsLong(stammdaten);
						if (id != null) {
							mapStammdaten.put(id, stammdaten);
						}
					}
				}
			} catch (final Exception e) {
				ReportingExceptionUtils.logException(
						"FEHLER: Fehler bei der Ermittlung der fehlenden %sstammdaten einer %sliste aus der Datenbank im ReportingRepository."
								.formatted(datentyp, datentyp),
						e, logger, LogLevel.ERROR, 0);
				return result;
			}
		}

		// Reporting-Objekte erstellen
		idsNonNull.stream().filter(mapStammdaten::containsKey).forEach(id -> result.add(mapReportingObjekte.computeIfAbsent(id, reportingObjektErsteller)));

		// Sortierung anwenden
		return comparatorOptional
				.map(comparator -> result.stream().sorted(comparator).toList())
				.orElse(result);
	}
}
