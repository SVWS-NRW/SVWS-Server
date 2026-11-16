package de.svws_nrw.service.filter;

import de.svws_nrw.core.types.filter.FilterAttributeType;

/**
 * Ein Filterattribut, dessen Werte aus einem CoreType stammen.
 */
public class CoreTypeFilterAttribute implements FilterAttribute {

	private final FilterAttributeKey key;
	private final String label;
	private final String coreTypeName;

	private final FilterCategoryKey keyCategory;

	private final AttributeTarget target;

	/**
	 * Erzeugt ein neues {@link CoreTypeFilterAttribute}.
	 *
	 * @param key Schlüssel des Attributes
	 * @param label Anzeigename des Attributes
	 * @param coreTypeName Name des CoreTypes
	 * @param keyCategory Schlüssel der Kategorie
	 * @param target Zuordnung zum Entity-Feld, aus dem der zu filternde Wert ermittelt wird
	 */
	public CoreTypeFilterAttribute(final FilterAttributeKey key, final String label, final String coreTypeName, final FilterCategoryKey keyCategory,
			final AttributeTarget target) {
		this.key = key;
		this.label = label;
		this.coreTypeName = coreTypeName;
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
	 * @return {@link FilterAttributeType#CORETYPE}
	 */
	@Override
	public FilterAttributeType getType() {
		return FilterAttributeType.CORETYPE;
	}

	/**
	 * Liefert den Namen des CoreTypes zurück, aus dem die Werte stammen.
	 *
	 * @return Name des CoreTypes
	 */
	@Override
	public String getSource() {
		return this.coreTypeName;
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
	 * Liefert den Namen des CoreTypes zurück.
	 *
	 * @return Name des CoreTypes
	 */
	public String getCoreTypeName() {
		return coreTypeName;
	}

}
