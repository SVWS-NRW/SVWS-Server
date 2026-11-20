package de.svws_nrw.service.filter;

import java.util.Arrays;

/**
 * Die Kataloge, deren Werte als Auswahl für Filterattribute vom Typ catalog ausgeliefert werden können.
 */
public enum FilterCatalog {

	/** Der Katalog der Orte */
	ORTE("Orte");

	private final String name;

	FilterCatalog(final String name) {
		this.name = name;
	}

	/**
	 * Liefert den Namen des Katalogs zurück. Er steht als Quelle in den Filterattributen und wird in der URL des Endpunkts verwendet.
	 *
	 * @return Name des Katalogs
	 */
	public String getName() {
		return name;
	}

	/**
	 * Ermittelt den Katalog zu dem übergebenen Namen. Die Groß- und Kleinschreibung wird dabei nicht beachtet.
	 *
	 * @param name der Name des Katalogs, z. B. {@code Orte}
	 *
	 * @return der Katalog oder {@code null}, wenn es keinen Katalog mit dem Namen gibt
	 */
	public static FilterCatalog getByName(final String name) {
		return Arrays.stream(values())
				.filter(catalog -> catalog.name.equalsIgnoreCase(name))
				.findFirst()
				.orElse(null);
	}

}
