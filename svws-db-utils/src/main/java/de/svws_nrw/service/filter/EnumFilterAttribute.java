package de.svws_nrw.service.filter;

/**
 * Ein Filterattribut, dessen Werte aus einem Enum stammen.
 */
public class EnumFilterAttribute implements FilterAttribute {

	private final FilterAttributeKey key;
	private final String label;
	private final String enumName;

	private final FilterCategoryKey keyCategory;

	private final AttributeTarget target;

	/**
	 * Erzeugt ein neues {@link EnumFilterAttribute}.
	 *
	 * @param key Schlüssel des Attributes
	 * @param label Anzeigename des Attributes
	 * @param enumName Name des Enums
	 * @param keyCategory Schlüssel der Kategorie
	 * @param target Zuordnung zum Entity-Feld, aus dem der zu filternde Wert ermittelt wird
	 */
	public EnumFilterAttribute(final FilterAttributeKey key, final String label, final String enumName, final FilterCategoryKey keyCategory,
			final AttributeTarget target) {
		this.key = key;
		this.label = label;
		this.enumName = enumName;
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
	 * Liefert den Namen des Enums zurück.
	 *
	 * @return Name des Enums
	 */
	public String getEnumName() {
		return enumName;
	}

}
