package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class ComparatorFactory {

	private ComparatorFactory() {
	}

	/**
	 * Erstellt einen optionalen Comparator basierend auf einer Sortierungsdefinition aus
	 * einem ReportingRepository. Falls keine gültige Sortierungsdefinition gefunden wird
	 * oder die benötigten Parameter fehlen, wird ein leeres {@link Optional} zurückgegeben.
	 *
	 * @param <T> Der Typ der Objekte, die vom Comparator verarbeitet werden sollen.
	 * @param reportingRepository Das Repository, das die benötigten Reporting-Parameter enthält.
	 * @param typName Der Typname, der verwendet wird, um eine entsprechende Sortierungsdefinition zu suchen.
	 * @param sortierungRegistry Die Registry, die die möglichen Sortierungsregeln bereitstellt.

	 * @return Ein Optional, das einen Comparator enthält, falls eine passende Definition gefunden wurde und
	 *         erfolgreich verarbeitet werden konnte; sonst ein leeres Optional.
	 */
	public static <T> Optional<Comparator<T>> buildOptionalComparator(
			final ReportingRepository reportingRepository,
			final String typName,
			final SortierungRegistry<T> sortierungRegistry) {

		if ((reportingRepository == null) || (reportingRepository.reportingParameter() == null))
			return Optional.empty();

		// Prüfe, ob eine Definition für die Sortierung des angegebenen Typs vorhanden ist.
		final List<String> attribute = reportingRepository.getSortierungsAttribute(typName, true);

		if (attribute.isEmpty())
			return Optional.empty();

		final List<String> validierungsfehler = new ArrayList<>();
		final Comparator<T> comparator = ComparatorBuilder.build(sortierungRegistry, attribute, validierungsfehler);

		if (!validierungsfehler.isEmpty()) {
			ReportingExceptionUtils.logInfo(
					"INFO: Es wurden folgende Attribute zur Sortierung übergeben, die nicht in der Registry definiert wurden: "
							+ String.join(", ", validierungsfehler),
					reportingRepository.logger(), LogLevel.INFO, 4);
		}

		return Optional.of(comparator);
	}
}
