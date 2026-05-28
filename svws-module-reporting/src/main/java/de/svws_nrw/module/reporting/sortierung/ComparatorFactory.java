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
	 * Registry und Standardsortierung werden aus dem übergebenen {@link ReportingSortierung} bezogen.
	 *
	 * @param <T>                           Der Typ der Objekte, die vom Comparator verarbeitet werden sollen.
	 * @param sortierungService             Der Service, der die Sortierungsattribute ermittelt.
	 * @param logger                        Der Logger für Info- und Fehlermeldungen.
	 * @param typName                       Der Typname, der zur Suche der passenden Sortierdefinition in den ReportParametern verwendet wird.
	 * @param sortierung                    Die {@link ReportingSortierung}-Konfiguration des Typs (Registry + Standardsortierung).
	 * @param erzeugeComparatorZuSortierung Gibt an, ob die definierte Sortierung angewendet werden soll.
	 *
	 * @return Ein Comparator, der niemals {@code null} ist. Liegt keine Sortierungsdefinition vor oder ist
	 *         {@code erzeugeComparatorZuSortierung = false}, wird ein Identitäts-Comparator zurückgegeben.
	 */
	public static <T> Comparator<T> buildComparator(final ReportingSortierungService sortierungService, final Logger logger, final String typName,
			final ReportingSortierung<T> sortierung, final boolean erzeugeComparatorZuSortierung) {

		if (!erzeugeComparatorZuSortierung || (sortierungService == null)) {
			return sortierung.comparatorIdentitaet();
		}

		final List<String> attribute = sortierungService.getSortierungsAttribute(typName, sortierung.standardsortierung());

		if (attribute.isEmpty()) {
			return sortierung.comparatorIdentitaet();
		}

		final List<String> validierungsfehler = new ArrayList<>();
		final Comparator<T> comparator = sortierung.comparator(attribute, validierungsfehler);

		if (!validierungsfehler.isEmpty()) {
			ReportingExceptionUtils.logInfo(
					"INFO: Es wurden folgende Attribute zur Sortierung übergeben, die nicht in der Registry definiert wurden: "
							+ String.join(", ", validierungsfehler),
					logger, LogLevel.INFO, 4);
		}

		return comparator;
	}
}
