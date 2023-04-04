package de.svws_nrw.davapi.util.vcard;

/**
 * Das Fullname Property einer VCard.<br>
 *
 * Type special notes: This type is based on the semantics of the X.520 Common
 * Name attribute. <b>The property MUST be present in the vCard object.</b> <br>
 * Beispiel<br>
 * {@code FN:Mr. John Q. Public\, Esq.}
 *
 * @see <a href=
 *      "https://datatracker.ietf.org/doc/html/rfc2426#section-3.1.1">RFC FN
 *      TYPE DEFINITION</a>
 *
 *
 */
public final class FullnameProperty implements VCardProperty {

	/**
	 * Konstante für den Typ des VCard Properties
	 */
	private static final String FN = "FN";
	/**
	 * Fullname dieser VCard
	 */
	private final String fullname;

	/**
	 * erstellt ein FullnameProperty für eine VCard mit dem Namen als Parameter
	 *
	 * @param fullname der Name einer VCard
	 */
	public FullnameProperty(final String fullname) {
		this.fullname = fullname;
	}

	@Override
	public String getType() {
		return FN;
	}

	@Override
	public void serializeType(final StringBuilder sb) {
		sb.append(FN);
	}

	@Override
	public void serializeProperty(final StringBuilder sb) {
		sb.append(fullname);
	}

}
