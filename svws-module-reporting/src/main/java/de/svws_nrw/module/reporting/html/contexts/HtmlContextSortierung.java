package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierungService;

/**
 * Hilfsklasse für die Sortierung der Daten eines {@link HtmlContext}. Liest die Sortier-Einstellungen aus dem
 * {@link ReportingContext} und erzeugt über die {@link ComparatorFactory} einen Comparator auf Basis der übergebenen
 * {@link ReportingSortierung}.
 */
public final class HtmlContextSortierung {

	private HtmlContextSortierung() {
		throw new IllegalStateException("Hilfsklasse - Initialisierung nicht möglich.");
	}

	/**
	 * Sortiert die übergebene Liste anhand der für den Typ konfigurierten Sortierung aus den ReportParametern.
	 * Liegt dort keine benutzerdefinierte Sortierung vor, wird die Standardsortierung des Typs verwendet.
	 * Null-Werte werden aus dem Ergebnis entfernt; die Eingabeliste wird nicht verändert.
	 *
	 * @param <T>              Der Reporting-Typ der Listenelemente.
	 * @param reportingContext Der Reporting-Context (liefert Sortier-Service und Logger). Darf {@code null} sein.
	 * @param liste            Die zu sortierende Liste. Darf {@code null} sein.
	 * @param sortierung       Die Sortierkonfiguration des Reporting-Typs. Darf {@code null} sein.
	 * @param typ              Die Klasse des Reporting-Typs, unter deren {@code getSimpleName()} die Sortierdefinition in
	 *                         den ReportParametern hinterlegt ist.
	 *
	 * @return Eine neue, sortierte {@link ArrayList} ohne Null-Werte. Bei {@code null}-Eingabeliste wird eine leere Liste zurückgegeben.
	 */
	public static <T> List<T> sortiere(final ReportingContext reportingContext, final List<T> liste, final ReportingSortierung<T> sortierung,
			final Class<T> typ) {
		if (liste == null) {
			return new ArrayList<>();
		}
		final List<T> listeNonNull = new ArrayList<>(liste.stream().filter(Objects::nonNull).toList());
		if (listeNonNull.isEmpty() || (sortierung == null) || (typ == null)) {
			return listeNonNull;
		}
		final ReportingSortierungService sortierungService = (reportingContext == null) ? null : reportingContext.sortierungService();
		final Comparator<T> comparator = ComparatorFactory.buildComparator(
				sortierungService,
				(reportingContext == null) ? null : reportingContext.logger(),
				typ.getSimpleName(),
				sortierung,
				true);
		listeNonNull.sort(comparator);
		return listeNonNull;
	}
}
