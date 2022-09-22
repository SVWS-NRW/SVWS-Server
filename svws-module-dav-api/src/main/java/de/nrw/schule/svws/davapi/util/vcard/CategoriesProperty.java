package de.nrw.schule.svws.davapi.util.vcard;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import jakarta.validation.constraints.NotNull;

/**
 * VCard Property für Kategorien.<br>
 * Beispiel:<br>
 * {@code CATEGORIES:category1,category2}
 * 
 * @see <a href=
 *      "https://datatracker.ietf.org/doc/html/rfc2426#section-3.6.1">RFC
 *      Dokumentation für Categories</a>
 */
public class CategoriesProperty implements VCardProperty {

	/**
	 * konstante für den Property Type
	 */
	private static final String CATEGORIES_STR = "CATEGORIES";

	/**
	 * Liste der Kategorien
	 */
	private List<String> categories = new Vector<>();

	/**
	 * erstellt ein Category Property mit einer Liste von Kategorien
	 * 
	 * @param categories eine Liste von Kategorien
	 */
	public CategoriesProperty(List<String> categories) {
		this.categories.addAll(categories);
	}

	/**
	 * erstellt ein CategoryProperty mit einer Kategorie
	 * 
	 * @param category die Kategorie
	 */
	public CategoriesProperty(@NotNull String category) {
		this.categories.add(category);
	}

	@Override
	public String getType() {
		return CATEGORIES_STR;
	}

	@Override
	public void serializeType(StringBuilder sb) {
		sb.append(getType());
	}

	@Override
	public void serializeProperty(StringBuilder sb) {
		Iterator<String> iter = categories.iterator();
		while (iter.hasNext()) {
			sb.append(iter.next());
			if (iter.hasNext()) {
				sb.append(PROPERTY_LIST_ENTRY_SEPARATOR);
			}
		}
	}
}