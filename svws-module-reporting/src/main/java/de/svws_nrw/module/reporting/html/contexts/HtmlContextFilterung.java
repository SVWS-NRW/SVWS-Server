package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import de.svws_nrw.module.reporting.filterung.ReportingFilterService;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Hilfsklasse für die Filterung der Daten eines {@link HtmlContext}. Liest die Filter-Einstellungen aus dem
 * {@link ReportingContext} und erzeugt über die übergebene {@link ReportingFilterung} ein {@link Predicate},
 * mit dem die Liste gefiltert wird.
 */
public final class HtmlContextFilterung {

	private HtmlContextFilterung() {
		throw new IllegalStateException("Hilfsklasse - Initialisierung nicht möglich.");
	}

	/**
	 * Filtert die übergebene Liste anhand der für den Typ konfigurierten Filterdefinition aus den ReportParametern.
	 * Liegt dort keine Filterdefinition vor (oder ist sie leer), wird die Liste unverändert (ohne Null-Werte) zurückgegeben.
	 * Die Eingabeliste wird nicht verändert.
	 *
	 * @param <T>              Der Reporting-Typ der Listenelemente.
	 * @param reportingContext Der Reporting-Context (liefert Filter-Service). Darf {@code null} sein.
	 * @param liste            Die zu filternde Liste. Darf {@code null} sein.
	 * @param filter           Die Filterkonfiguration des Reporting-Typs. Darf {@code null} sein.
	 * @param typ              Die Klasse des Reporting-Typs, unter deren {@code getSimpleName()} die Filterdefinition in
	 *                         den ReportParametern hinterlegt ist.
	 *
	 * @return Eine neue, gefilterte {@link ArrayList} ohne Null-Werte. Bei {@code null}-Eingabeliste wird eine leere Liste zurückgegeben.
	 */
	public static <T> List<T> filtere(final ReportingContext reportingContext, final List<T> liste, final ReportingFilterung<T> filter,
			final Class<T> typ) {
		if (liste == null) {
			return new ArrayList<>();
		}
		final List<T> listeNonNull = new ArrayList<>(liste.stream().filter(Objects::nonNull).toList());
		if (listeNonNull.isEmpty() || (filter == null) || (typ == null) || (reportingContext == null)) {
			return listeNonNull;
		}
		final ReportingFilterService filterService = reportingContext.filterService();
		if (filterService == null) {
			return listeNonNull;
		}
		final Predicate<T> predicate = filter.bedingung(filterService.getFilter(typ.getSimpleName()), null);
		listeNonNull.removeIf(predicate.negate());
		return listeNonNull;
	}
}
