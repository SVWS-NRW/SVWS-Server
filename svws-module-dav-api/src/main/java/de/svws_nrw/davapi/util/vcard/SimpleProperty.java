package de.svws_nrw.davapi.util.vcard;

/**
 * Diese Klasse repräsentiert ein einfaches VCard Property als Key-Value-Pair
 */
public final class SimpleProperty implements VCardProperty {

	/**
	 * der Typ des Properties
	 */
	private final String type;

	/**
	 * das Property
	 */
	private final String property;

	/**
	 * Ein Konstruktor zur Erzeugung eines einfachen VCard-Properties mit dem Typ
	 * und dem Property, bspw: fn:Max Mustermann
	 *
	 * @param type     der Typ, bspw: fn
	 *
	 * @param property das Property, bspw: Max Mustermann
	 */
	public SimpleProperty(final String type, final String property) {
		this.type = type;
		this.property = property;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void serializeType(final StringBuilder sb) {
		sb.append(type);
	}

	@Override
	public void serializeProperty(final StringBuilder sb) {
		sb.append(property);
	}

}
