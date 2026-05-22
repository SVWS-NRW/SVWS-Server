package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ComparatorFactory {

	private ComparatorFactory() {
	}

	/**
	 * Erstellt einen Comparator basierend auf einer Sortierungsdefinition aus einem {@link ReportingSortierungService}.
	 * Wird {@code erzeugeComparatorZuSortierung = false} übergeben, keine gültige Sortierungsdefinition gefunden oder fehlen die
	 * benötigten Parameter, so wird ein Identitäts-Comparator {@code (a, b) -> 0} zurückgegeben, der die Reihenfolge
	 * der Elemente unverändert lässt.
	 *
	 * @param <T> Der Typ der Objekte, die vom Comparator verarbeitet werden sollen.
	 * @param sortierungService             Der Service, der die Sortierungsattribute ermittelt.
	 * @param logger                        Der Logger für Info- und Fehlermeldungen.
	 * @param typName                       Der Typname, der verwendet wird, um eine entsprechende Sortierungsdefinition zu suchen.
	 * @param sortierungRegistry            Die Registry, die die möglichen Sortierungsregeln bereitstellt.
	 * @param erzeugeComparatorZuSortierung Gibt an, ob die definierte Sortierung angewendet werden soll. Bei {@code false} wird direkt der Identitäts-Comparator zurückgegeben.
	 *
	 * @return Ein Comparator, der niemals {@code null} ist. Liegt keine Sortierungsdefinition vor oder ist
	 *         {@code erzeugeComparatorZuSortierung = false}, so lässt der erzeugte Comparator die Reihenfolge der Elemente unverändert.
	 */
	public static <T> Comparator<T> buildComparator(final ReportingSortierungService sortierungService, final Logger logger, final String typName,
			final SortierungRegistry<T> sortierungRegistry, final boolean erzeugeComparatorZuSortierung) {

		if (!erzeugeComparatorZuSortierung || (sortierungService == null)) {
			return Comparators.verketten(List.of());
		}

		// Prüfe, ob eine Definition für die Sortierung des angegebenen Typs vorhanden ist.
		final List<String> attribute = sortierungService.getSortierungsAttribute(typName, true);

		if (attribute.isEmpty()) {
			return Comparators.verketten(List.of());
		}

		final List<String> validierungsfehler = new ArrayList<>();
		final Comparator<T> comparator = ComparatorBuilder.build(sortierungRegistry, attribute, validierungsfehler);

		if (!validierungsfehler.isEmpty()) {
			ReportingExceptionUtils.logInfo(
					"INFO: Es wurden folgende Attribute zur Sortierung übergeben, die nicht in der Registry definiert wurden: "
							+ String.join(", ", validierungsfehler),
					logger, LogLevel.INFO, 4);
		}

		return comparator;
	}
}
