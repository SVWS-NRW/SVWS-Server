package de.svws_nrw.davapi.util.vcard;

/**
 * Diese Klasse repräsentiert das Related Property einer VCard V4
 *
 */
public final class RelatedProperty implements VCardProperty {

	private final RelatedTypeValue type;
	private final String relatedVCardUUID;

	/**
	 * Konstruktor mit dem der Typ der Relation zu einer anderen VCard und der UUID
	 * der anderen VCard
	 *
	 * @param type             der Typ der Relation zu einer anderen VCard
	 * @param relatedVCardUUID die UUID der verbundenen VCard
	 */
	public RelatedProperty(final RelatedTypeValue type, final String relatedVCardUUID) {
		this.type = type;
		this.relatedVCardUUID = relatedVCardUUID;
	}

	@Override
	public String getType() {
		return "RELATED;TYPE=" + type.name().toLowerCase();
	}

	@Override
	public void serializeType(final StringBuilder sb) {
		sb.append(getType());

	}

	@Override
	public void serializeProperty(final StringBuilder sb) {
		sb.append("urn:uuid:");
		sb.append(relatedVCardUUID);
	}

	/**
	 * Typen von möglichen Relationen zwischen vCards gemäß
	 * https://www.rfc-editor.org/rfc/rfc6350#section-6.6.6
	 *
	 */
	public enum RelatedTypeValue {
		/** Kind dieser VCard */
		CHILD,
		/** Eltern dieser VCard */
		PARENT,
		/** Geschwister dieser VCARD */
		SIBLING
	}
}
