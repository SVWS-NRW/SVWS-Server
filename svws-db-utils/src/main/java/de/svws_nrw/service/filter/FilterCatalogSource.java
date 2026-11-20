package de.svws_nrw.service.filter;

import java.util.List;

import de.svws_nrw.core.data.filter.FilterCatalogValue;

/**
 * Eine Quelle für die auswählbaren Werte eines Katalogs. Jeder Katalog, der über den Filter-Endpunkt ausgeliefert wird,
 * implementiert dieses Interface und liefert seine Werte damit im selben Schema.
 */
public interface FilterCatalogSource {

	/**
	 * Liefert den Katalog zurück, dessen Werte diese Quelle liefert.
	 *
	 * @return der Katalog
	 */
	FilterCatalog getCatalog();

	/**
	 * Lädt alle auswählbaren Werte des Katalogs.
	 *
	 * @return Liste der Werte
	 */
	List<FilterCatalogValue> loadValues();

}
