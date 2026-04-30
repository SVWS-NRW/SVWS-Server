package de.svws_nrw.module.reporting.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.LongFunction;
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
			final Function<List<Long>, List<S>> stammdatenLoader, final LongFunction<R> reportingObjektErsteller,
			final ToLongFunction<S> idExtractor, final Optional<Comparator<R>> comparatorOptional,
			final String datentyp, final Logger logger) {

		if (ids == null) {
			return new ArrayList<>();
		}

		final List<Long> idsNonNull = ids.stream().filter(Objects::nonNull).filter(id -> id >= 0).distinct().toList();
		if (idsNonNull.isEmpty()) {
			return new ArrayList<>();
		}

		if (!ladeFehlendeStammdaten(idsNonNull, mapStammdaten, stammdatenLoader, idExtractor, datentyp, logger)) {
			return new ArrayList<>();
		}

		final List<R> result = erzeugeReportingObjekte(idsNonNull, mapStammdaten, mapReportingObjekte, reportingObjektErsteller);

		return comparatorOptional
				.map(comparator -> result.stream().sorted(comparator).toList())
				.orElse(result);
	}

	/**
	 * Lädt fehlende Stammdaten aus der Datenbank nach und übernimmt sie in die Stammdaten-Map.
	 *
	 * @param <S> Der Typ der Stammdaten (z. B. LehrerStammdaten, SchuelerStammdaten)
	 * @param idsNonNull Die Liste der IDs, für die Stammdaten geladen werden sollen
	 * @param mapStammdaten Die Map für die Stammdaten
	 * @param stammdatenLoader Die Funktion zum Laden der Stammdaten aus der Datenbank
	 * @param idExtractor Die Funktion zum Extrahieren der ID aus den Stammdaten
	 * @param datentyp Der Typ der Stammdaten (z. B. "Lehrer", "Schüler")
	 * @param logger Der Logger für Fehlerprotokollierung
	 *
	 * @return {@code true}, wenn das Laden erfolgreich war (oder nichts zu laden war), sonst {@code false}.
	 */
	private static <S> boolean ladeFehlendeStammdaten(final List<Long> idsNonNull, final Map<Long, S> mapStammdaten,
			final Function<List<Long>, List<S>> stammdatenLoader, final ToLongFunction<S> idExtractor,
			final String datentyp, final Logger logger) {
		final List<Long> fehlendeIds = idsNonNull.stream().filter(id -> !mapStammdaten.containsKey(id)).toList();
		if (fehlendeIds.isEmpty()) {
			return true;
		}
		try {
			for (final S stammdaten : stammdatenLoader.apply(fehlendeIds)) {
				if (stammdaten != null) {
					mapStammdaten.put(idExtractor.applyAsLong(stammdaten), stammdaten);
				}
			}
			return true;
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"FEHLER: Fehler bei der Ermittlung der fehlenden %sstammdaten einer %sliste aus der Datenbank im ReportingRepository."
							.formatted(datentyp, datentyp),
					e, logger, LogLevel.ERROR, 0);
			return false;
		}
	}

	/**
	 * Erstellt für alle IDs mit vorhandenen Stammdaten ein Reporting-Objekt und cached es in der übergebenen Map.
	 *
	 * <p>Hier kann kein {@code mapReportingObjekte.computeIfAbsent} verwendet werden:
	 * {@code computeIfAbsent} wirft eine {@link java.util.ConcurrentModificationException}, falls bei der Erstellung
	 * des Objekts (über {@code reportingObjektErsteller.apply}) intern weitere Aufrufe auf diese Map erfolgen (z. B.
	 * bei zirkulären Abhängigkeiten wie Klasse → Schüler → Klasse).</p>
	 *
	 * <p>Des Weiteren muss hier {@code putIfAbsent} (und nicht nur {@code put}) verwendet werden: Falls es eine
	 * zirkuläre Abhängigkeit gibt, wird während der Ausführung von {@code apply} das Objekt durch einen
	 * geschachtelten Aufruf bereits erzeugt und in die Map gelegt. Ein einfaches {@code put} am Ende dieses äußeren
	 * Aufrufs würde die korrekte, intern bereits verknüpfte Instanz überschreiben. {@code putIfAbsent} wirft das im
	 * äußeren Aufruf erzeugte (und nicht richtig verknüpfte) Objekt weg, falls schon ein Eintrag existiert, und
	 * behält die durch die Rekursion entstandene Instanz.</p>
	 *
	 * @param <S> Der Typ der Stammdaten (z. B. LehrerStammdaten, SchuelerStammdaten)
	 * @param <R> Der Typ der Reporting-Objekte (z. B. ReportingLehrer, ReportingSchueler)
	 * @param idsNonNull Die Liste der IDs, die ausgewertet werden sollen. Darf keine {@code null}-Werte enthalten.
	 * @param mapStammdaten Eine Map, die Stammdaten enthält und zur Validierung der IDs verwendet wird.
	 * @param mapReportingObjekte Eine Map, die bereits existierende Reporting-Objekte enthält. Fehlende Reporting-Objekte werden hier neu erstellt.
	 * @param reportingObjektErsteller Eine Funktion, die ein Reporting-Objekt auf Basis einer ID erstellt.
	 *
	 * @return Eine Liste mit den Reporting-Objekten, die basierend auf den IDs aus {@code idsNonNull} erstellt oder abgerufen wurden.
	 */
	private static <S, R> List<R> erzeugeReportingObjekte(final List<Long> idsNonNull, final Map<Long, S> mapStammdaten,
			final Map<Long, R> mapReportingObjekte, final LongFunction<R> reportingObjektErsteller) {
		final List<R> result = new ArrayList<>();
		idsNonNull.stream().filter(mapStammdaten::containsKey).forEach(id -> {
			if (!mapReportingObjekte.containsKey(id)) {
				mapReportingObjekte.putIfAbsent(id, reportingObjektErsteller.apply(id));
			}
			result.add(mapReportingObjekte.get(id));
		});
		return result;
	}
}
