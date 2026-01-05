package de.svws_nrw.module.reporting.filterung;

import de.svws_nrw.core.data.reporting.ReportingFilterDefinition;
import de.svws_nrw.core.data.reporting.ReportingFilterEintrag;
import de.svws_nrw.core.data.reporting.ReportingFilterKriterium;
import de.svws_nrw.core.types.reporting.ReportingFilterOperation;
import de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Registry zur Verwaltung von Filter-Attributen und zur Erzeugung von Predicates basierend auf Filterdefinitionen.
 *
 * @param <T> Der Typ der Objekte, die gefiltert werden.
 */
public class FilterRegistry<T> {

	private final Map<String, Function<T, ?>> attributExtractors = new HashMap<>();

	/**
	 * Registriert ein Attribut mit einem zugehörigen Extraktor, der den Attributwert aus einem Objekt des generischen Typs T extrahiert.
	 *
	 * @param attributName Der Name des Attributs, das registriert werden soll. Der Name wird in Kleinbuchstaben und ohne führende
	 *                     oder nachfolgende Leerzeichen gespeichert.
	 * @param extractor    Die Funktion, die den Wert des Attributs aus einem Objekt des Typs T extrahiert.
	 *
	 * @return Die Instanz von {@code FilterRegistry}, um eine verkettete Nutzung zu ermöglichen.
	 */

	public FilterRegistry<T> registriereAttribut(final String attributName, final Function<T, ?> extractor) {
		attributExtractors.put(attributName.trim().toLowerCase(Locale.ROOT), extractor);
		return this;
	}

	/**
	 * Erzeugt ein Predicate aus einer ReportingFilterDefinition und sammelt Fehler für unbekannte Attribute.
	 *
	 * @param reportingFilterDefinition Die Filterdefinition aus den Parametern.
	 * @param validierungsfehler        Eine Liste, in der Namen unbekannter Attribute gesammelt werden (darf null sein).
	 *
	 * @return Ein Predicate, das true liefert, wenn das Objekt den Kriterien entspricht. Unbekannte Attribute werden ignoriert.
	 */
	public Predicate<T> erstelleFilter(final ReportingFilterDefinition reportingFilterDefinition, final List<String> validierungsfehler) {
		if ((reportingFilterDefinition == null) || (reportingFilterDefinition.kriterien == null) || reportingFilterDefinition.kriterien.isEmpty())
			return t -> true;

		// Die Hauptliste der Kriterien in der Definition wird standardmäßig mit UND verknüpft.
		Predicate<T> result = t -> true;
		for (final ReportingFilterKriterium kriterium : reportingFilterDefinition.kriterien) {
			result = result.and(baueKriterium(kriterium, validierungsfehler));
		}
		return result;
	}

	/**
	 * Baut ein {@link Predicate}, das auf dem übergebenen {@link ReportingFilterKriterium} basiert.
	 * Ein Kriterium verknüpft seine 'eintraege' und 'unterkriterien' mit der angegebenen 'verknuepfung'.
	 *
	 * @param reportingFilterKriterium Das {@link ReportingFilterKriterium}, welches die Logik für den Filter definiert.
	 * @param validierungsfehler       Eine Liste, in der Namen unbekannter Attribute gesammelt werden (darf null sein).
	 *
	 * @return Ein {@link Predicate}, das die Logik des übergebenen Filterkriteriums umsetzt.
	 */
	private Predicate<T> baueKriterium(final ReportingFilterKriterium reportingFilterKriterium, final List<String> validierungsfehler) {
		final ReportingFilterVerknuepfung verknuepfung = ReportingFilterVerknuepfung.getByID(reportingFilterKriterium.verknuepfung);

		// Bestimme den neutralen Startwert für die logische Kette.
		Predicate<T> result = (verknuepfung == ReportingFilterVerknuepfung.OR) ? (t -> false) : (t -> true);

		// 1. Verarbeite alle direkten Einträge
		for (final ReportingFilterEintrag eintrag : reportingFilterKriterium.eintraege) {
			final Predicate<T> p = baueEintragPredicate(eintrag, validierungsfehler);
			result = (verknuepfung == ReportingFilterVerknuepfung.OR) ? result.or(p) : result.and(p);
		}

		// 2. Verarbeite alle Unterkriterien (rekursiv)
		for (final ReportingFilterKriterium unterkriterium : reportingFilterKriterium.unterkriterien) {
			final Predicate<T> p = baueKriterium(unterkriterium, validierungsfehler);
			result = (verknuepfung == ReportingFilterVerknuepfung.OR) ? result.or(p) : result.and(p);
		}

		// Negierung anwenden, falls für diesen Block gewünscht
		return reportingFilterKriterium.nicht ? result.negate() : result;
	}

	private Predicate<T> baueEintragPredicate(final ReportingFilterEintrag eintrag, final List<String> validierungsfehler) {
		final Function<T, ?> extractor = attributExtractors.get(eintrag.attribut.trim().toLowerCase(Locale.ROOT));
		if (extractor == null) {
			if (validierungsfehler != null)
				validierungsfehler.add(eintrag.attribut);
			return t -> true;
		}

		return obj -> {
			final Object value = extractor.apply(obj);
			if (value == null)
				return false;
			return pruefeWertGegenEintrag(value, eintrag);
		};
	}

	private boolean pruefeWertGegenEintrag(final Object value, final ReportingFilterEintrag eintrag) {
		final ReportingFilterOperation operation = ReportingFilterOperation.getByID(eintrag.operation);

		if (operation == ReportingFilterOperation.IN)
			return pruefeIn(value, eintrag.werte);
		if (operation == ReportingFilterOperation.BETWEEN)
			return pruefeBetween(value, eintrag.werte);

		final String filterWert = eintrag.werte.isEmpty() ? "" : eintrag.werte.getFirst();
		return vergleicheWert(value, filterWert, operation);
	}

	private boolean pruefeIn(final Object value, final List<String> werte) {
		for (final String filterWert : werte) {
			// Nutze hier die gleiche Vergleichslogik wie bei EQUAL, um Typkonvertierung & Case-Insensitivity zu gewährleisten
			if (vergleicheWert(value, filterWert, ReportingFilterOperation.EQUAL)) {
				return true;
			}
		}
		return false;
	}

	private boolean pruefeBetween(final Object value, final List<String> werte) {
		if (werte.size() != 2)
			return false;
		final String start = werte.get(0);
		final String ende = werte.get(1);
		return vergleicheWert(value, start, ReportingFilterOperation.GREATER_OR_EQUAL)
				&& vergleicheWert(value, ende, ReportingFilterOperation.LESS_OR_EQUAL);
	}

	@SuppressWarnings("rawtypes")
	private boolean vergleicheWert(final Object value, final String filterWert, final ReportingFilterOperation reportingFilterOperation) {
		switch (value) {
			case final String sVal -> {
				return vergleicheString(sVal, filterWert, reportingFilterOperation);
			}
			case final Number nVal -> {
				try {
					final double filterNum = Double.parseDouble(filterWert);
					return vergleicheNumber(nVal.doubleValue(), filterNum, reportingFilterOperation);
				} catch (final NumberFormatException e) {
					return false;
				}
			}
			case final Boolean bVal -> {
				return vergleicheBoolean(bVal, Boolean.parseBoolean(filterWert), reportingFilterOperation);
			}
			case final Comparable cVal -> {
				return vergleicheComparable(cVal, filterWert, reportingFilterOperation);
			}
			default -> {
				// Kein Typ zum Vergleichen.
			}
		}
		// Fallback: String-Vergleich der toString()-Repräsentation
		return vergleicheString(value.toString(), filterWert, reportingFilterOperation);
	}

	private boolean vergleicheString(final String wert, final String filterWert, final ReportingFilterOperation reportingFilterOperation) {
		if (reportingFilterOperation == null)
			return false;

		final String wertLower = wert.toLowerCase(Locale.ROOT);
		final String filterLower = filterWert.toLowerCase(Locale.ROOT);

		return switch (reportingFilterOperation) {
			case EQUAL -> wertLower.equals(filterLower);
			case NOT_EQUAL -> !wertLower.equals(filterLower);
			case CONTAINS -> wertLower.contains(filterLower);
			case STARTS_WITH -> wertLower.startsWith(filterLower);
			case ENDS_WITH -> wertLower.endsWith(filterLower);
			case GREATER -> wertLower.compareTo(filterLower) > 0;
			case GREATER_OR_EQUAL -> wertLower.compareTo(filterLower) >= 0;
			case LESS -> wertLower.compareTo(filterLower) < 0;
			case LESS_OR_EQUAL -> wertLower.compareTo(filterLower) <= 0;
			default -> false;
		};
	}

	private boolean vergleicheNumber(final double wert, final double filterWert, final ReportingFilterOperation reportingFilterOperation) {
		if (reportingFilterOperation == null)
			return false;
		return switch (reportingFilterOperation) {
			case EQUAL -> Double.compare(wert, filterWert) == 0;
			case NOT_EQUAL -> Double.compare(wert, filterWert) != 0;
			case GREATER -> wert > filterWert;
			case GREATER_OR_EQUAL -> wert >= filterWert;
			case LESS -> wert < filterWert;
			case LESS_OR_EQUAL -> wert <= filterWert;
			default -> false;
		};
	}

	private boolean vergleicheBoolean(final boolean wert, final boolean filterWert, final ReportingFilterOperation reportingFilterOperation) {
		if (reportingFilterOperation == ReportingFilterOperation.EQUAL)
			return wert == filterWert;
		if (reportingFilterOperation == ReportingFilterOperation.NOT_EQUAL)
			return wert != filterWert;
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean vergleicheComparable(final Comparable wert, final String filterWert, final ReportingFilterOperation reportingFilterOperation) {
		if (wert == null)
			return false;

		// 1. Wenn der Wert ohnehin ein String ist, nutze die String-Logik.
		if (wert instanceof final String s) {
			return vergleicheString(s, filterWert, reportingFilterOperation);
		}

		// 2. Versuche, den String-Filterwert in den passenden Zieltyp zu konvertieren.
		Comparable parsedFilterWert = null;
		try {
			switch (wert) {
				case final Integer i -> parsedFilterWert = Integer.valueOf(filterWert);
				case final Long l -> parsedFilterWert = Long.valueOf(filterWert);
				case final Double v -> parsedFilterWert = Double.valueOf(filterWert);
				case final Float v -> parsedFilterWert = Float.valueOf(filterWert);
				case final Boolean b -> parsedFilterWert = Boolean.valueOf(filterWert);
				case final java.time.LocalDate localDate ->
					// Hier wird das ISO-Format yyyy-MM-dd erwartet, wie es meistens in APIs verwendet wird.
					parsedFilterWert = java.time.LocalDate.parse(filterWert);
				case final java.time.LocalDateTime localDateTime -> parsedFilterWert = java.time.LocalDateTime.parse(filterWert);
				default -> {
					// Typ konnte nicht geparsed werden.
				}
			}
			// Hier können bei Bedarf weitere Typen ergänzt werden
		} catch (final Exception e) {
			// Parsing fehlgeschlagen (z. B. keine Zahl, falsches Datumsformat). Es kann nicht typgerecht verglichen werden.
			return false;
		}

		// 3. Fallback: Wenn der Typ nicht erkannt wurde, vergleiche die String-Repräsentationen
		if (parsedFilterWert == null) {
			return vergleicheString(wert.toString(), filterWert, reportingFilterOperation);
		}

		// 4. Der eigentliche typgerechte Vergleich
		final int cmp = wert.compareTo(parsedFilterWert);

		return switch (reportingFilterOperation) {
			case EQUAL -> cmp == 0;
			case NOT_EQUAL -> cmp != 0;
			case GREATER -> cmp > 0;
			case GREATER_OR_EQUAL -> cmp >= 0;
			case LESS -> cmp < 0;
			case LESS_OR_EQUAL -> cmp <= 0;
			default -> false;
		};
	}
}
