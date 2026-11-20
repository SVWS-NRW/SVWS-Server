package de.svws_nrw.service.filter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.core.data.filter.FilterCatalogValue;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Service-Klasse für das Abrufen der auswählbaren Werte eines Katalogs.
 */
public final class FilterCatalogService {

	private final Map<FilterCatalog, FilterCatalogSource> sourcesByCatalog;

	/**
	 * Erzeugt einen neuen Service. Die Abhängigkeiten werden von der {@link FilterCatalogServiceFactory} übergeben.
	 *
	 * @param sourcesByCatalog die Quellen der Kataloge, die abgerufen werden können, je Katalog
	 *
	 * @throws NullPointerException wenn die Quellen {@code null} sind
	 */
	FilterCatalogService(final Map<FilterCatalog, FilterCatalogSource> sourcesByCatalog) {
		this.sourcesByCatalog = Objects.requireNonNull(sourcesByCatalog, "Die Katalogquellen dürfen nicht null sein.");
	}

	/**
	 * Liefert alle auswählbaren Werte des Katalogs mit dem übergebenen Namen. Gibt es keinen Katalog mit dem Namen,
	 * wird eine {@link ApiOperationException} mit dem Status NOT_FOUND geworfen.
	 *
	 * @param name der Name des Katalogs, z. B. {@code Orte}
	 *
	 * @return die Liste der Werte des Katalogs
	 */
	public List<FilterCatalogValue> getByName(final String name) {
		final var catalog = FilterCatalog.getByName(name);
		if (catalog == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Der Katalog '%s' ist unbekannt.".formatted(name));
		}

		return sourcesByCatalog.get(catalog).loadValues();
	}

}
