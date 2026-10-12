package de.svws_nrw.service.filter;

import java.util.List;

/**
 * Eine Filterkategorie, die zusammengehörige Filterattribute einer Domäne gruppiert.
 *
 * @param key Schlüssel der Kategorie
 * @param label Anzeigename der Kategorie
 * @param order Sortierreihenfolge der Kategorie
 * @param attributes Schlüssel der zugeordneten Filterattribute
 */
public record FilterCategory(
		FilterCategoryKey key,
		String label,
		Integer order,
		List<FilterAttributeKey> attributes
) {

}
