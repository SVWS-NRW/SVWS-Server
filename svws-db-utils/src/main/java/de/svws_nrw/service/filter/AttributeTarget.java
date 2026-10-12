package de.svws_nrw.service.filter;

import java.util.Objects;

/**
 * Die technische Zuordnung eines Filterattributs zu dem Entity-Feld, aus dem der zu filternde Wert ermittelt wird.
 *
 * @param entityName Name der Entity
 * @param entityProperty Name des Entity Properties
 */
public record AttributeTarget(String entityName, String entityProperty) {

	/**
	 * Erzeugt ein neues {@link AttributeTarget} Objekt.
	 *
	 * @param entityName Name der Entity
	 * @param entityProperty Name des Entity Properties
	 *
	 * @throws NullPointerException wenn einer der Parameter {@code null} ist
	 */
	public AttributeTarget {
		Objects.requireNonNull(entityName, "Der Name der Entity darf nicht null sein.");
		Objects.requireNonNull(entityProperty, "Der Name des Entity Properties darf nicht null sein.");
	}

	/**
	 * Erzeugt ein neues {@link AttributeTarget} Objekt.
	 *
	 * @param entityName Name der Entity
	 * @param entityProperty Name des Entity Properties
	 *
	 * @return {@link AttributeTarget}
	 */
	public static AttributeTarget of(final String entityName, final String entityProperty) {
		return new AttributeTarget(entityName, entityProperty);
	}
}
