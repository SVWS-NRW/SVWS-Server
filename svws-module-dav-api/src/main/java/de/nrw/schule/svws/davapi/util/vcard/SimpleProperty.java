package de.nrw.schule.svws.davapi.util.vcard;

/**
 * Diese Klasse repräsentiert ein einfaches VCard Property als Key-Value-Pair
 * 
 */
public class SimpleProperty implements VCardProperty {

	/**
	 * der Typ des Properties
	 */
	private String type;
	/**
	 * das Property
	 */
	private String property;

	/**
	 * ein Konstruktor zur Erzeugung eines einfachen VCard-Properties mit dem Typ
	 * und dem Property, bspw: fn:Max Mustermann
	 * 
	 * @param type     der Typ, bspw: fn
	 * 
	 * @param property das Property, bspw: Max Mustermann
	 */
	public SimpleProperty(String type, String property) {
		this.type = type;
		this.property = property;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void serializeType(StringBuilder sb) {
		sb.append(type);
	}

	@Override
	public void serializeProperty(StringBuilder sb) {
		sb.append(property);
	}

}
