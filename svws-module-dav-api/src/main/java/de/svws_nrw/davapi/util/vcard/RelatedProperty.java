package de.svws_nrw.davapi.util.vcard;

/**
 * Diese Klasse repräsentiert das Related Property einer VCard V4
 *
 */
public class RelatedProperty implements VCardProperty {

	private RelatedTypeValue type;
	private String relatedVCardUUID;

	/**
	 * Konstruktor mit dem der Typ der Relation zu einer anderen VCard und der UUID
	 * der anderen VCard
	 * 
	 * @param type             der Typ der Relation zu einer anderen VCard
	 * @param relatedVCardUUID die UUID der verbundenen VCard
	 */
	public RelatedProperty(RelatedTypeValue type, String relatedVCardUUID) {
		this.type = type;
		this.relatedVCardUUID = relatedVCardUUID;
	}

	@Override
	public String getType() {
		return "RELATED;TYPE=" + type.name().toLowerCase();
	}

	@Override
	public void serializeType(StringBuilder sb) {
		sb.append(getType());

	}

	@Override
	public void serializeProperty(StringBuilder sb) {
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
