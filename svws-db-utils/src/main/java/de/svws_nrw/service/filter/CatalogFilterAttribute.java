package de.svws_nrw.service.filter;

import java.util.Objects;

import de.svws_nrw.core.types.filter.FilterAttributeType;

/**
 * Ein Filterattribut, dessen Werte aus einem Katalog stammen.
 */
public class CatalogFilterAttribute implements FilterAttribute {

	private final FilterAttributeKey key;
	private final String label;
	private final FilterCatalog catalog;

	private final FilterCategoryKey keyCategory;

	private final AttributeTarget target;

	/**
	 * Erzeugt ein neues {@link CatalogFilterAttribute}.
	 *
	 * @param key Schlüssel des Attributes
	 * @param label Anzeigename des Attributes
	 * @param catalog der Katalog, aus dem die Werte stammen
	 * @param keyCategory Schlüssel der Kategorie
	 * @param target Zuordnung zum Entity-Feld, aus dem der zu filternde Wert ermittelt wird
	 *
	 * @throws NullPointerException wenn der Katalog {@code null} ist
	 */
	public CatalogFilterAttribute(
			final FilterAttributeKey key,
			final String label,
			final FilterCatalog catalog,
			final FilterCategoryKey keyCategory,
			final AttributeTarget target) {
		this.key = key;
		this.label = label;
		this.catalog = Objects.requireNonNull(catalog, "Der Katalog darf nicht null sein.");
		this.keyCategory = keyCategory;
		this.target = target;
	}

	/**
	 * Liefert den Schlüssel des Filterattributes zurück.
	 *
	 * @return Schlüssel
	 */
	@Override
	public FilterAttributeKey getKey() {
		return this.key;
	}

	/**
	 * Liefert den Anzeigename des Filterattributes zurück.
	 *
	 * @return Anzeigename
	 */
	@Override
	public String getLabel() {
		return this.label;
	}

	/**
	 * Liefert den Schlüssel der Kategorie zurück.
	 *
	 * @return Schlüssel
	 */
	@Override
	public FilterCategoryKey getKeyCategory() {
		return this.keyCategory;
	}

	/**
	 * Liefert den Typ des Filterattributes zurück.
	 *
	 * @return {@link FilterAttributeType#CATALOG}
	 */
	@Override
	public FilterAttributeType getType() {
		return FilterAttributeType.CATALOG;
	}

	/**
	 * Liefert den Namen des Katalogs zurück, aus dem die Werte stammen.
	 *
	 * @return Name des Katalogs
	 */
	@Override
	public String getSource() {
		return this.catalog.getName();
	}

	/**
	 * Liefert die technische Zuordnung dieses Filterattributs zu dem Entity-Feld, aus dem der zu filternde Wert ermittelt wird.
	 *
	 * <p>Die Zuordnung ist ein internes Implementierungsdetail und nicht die stabile fachliche Identität des Filterattributs.
	 * Dafür ist der {@link FilterCategoryKey} vorgesehen.</p>
	 *
	 * @return die Entity-Zuordnung; darf nicht {@code null} sein
	 */
	@Override
	public AttributeTarget getTarget() {
		return this.target;
	}

	/**
	 * Liefert den Katalog zurück, aus dem die Werte stammen.
	 *
	 * @return der Katalog
	 */
	public FilterCatalog getCatalog() {
		return catalog;
	}
}
