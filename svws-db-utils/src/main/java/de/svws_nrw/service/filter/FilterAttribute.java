package de.svws_nrw.service.filter;

import de.svws_nrw.core.types.filter.FilterAttributeType;

/**
 * Ein Attribut, nach dem innerhalb einer Filterkategorie gefiltert werden kann.
 */
public interface FilterAttribute {

	/**
	 * Liefert den Schlüssel des Filterattributes zurück.
	 *
	 * @return Schlüssel
	 */
	FilterAttributeKey getKey();

	/**
	 * Liefert den Anzeigename des Filterattributes zurück.
	 *
	 * @return Anzeigename
	 */
	String getLabel();

	/**
	 * Liefert den Schlüssel der Kategorie zurück.
	 *
	 * @return Schlüssel
	 */
	FilterCategoryKey getKeyCategory();

	/**
	 * Liefert den Typ des Filterattributes zurück. Der Typ bestimmt, woher die auswählbaren Werte stammen.
	 *
	 * @return Typ
	 */
	FilterAttributeType getType();

	/**
	 * Liefert den Namen des Enums, CoreTypes oder Katalogs zurück, aus dem die Werte des Filterattributes stammen.
	 *
	 * @return Name der Quelle der Werte
	 */
	String getSource();

	/**
	 * Liefert die technische Zuordnung dieses Filterattributs zu dem Entity-Feld, aus dem der zu filternde Wert ermittelt wird.
	 *
	 * <p>Die Zuordnung ist ein internes Implementierungsdetail und nicht die stabile fachliche Identität des Filterattributs.
	 * Dafür ist der {@link FilterCategoryKey} vorgesehen.</p>
	 *
	 * @return die Entity-Zuordnung; darf nicht {@code null} sein
	 */
	AttributeTarget getTarget();
}
