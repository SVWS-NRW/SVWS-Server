package de.svws_nrw.davapi.util.vcard;

/**
 * VCard Property für Telefondaten.<br>
 * Beispiel: <br>
 * {@code TEL;TYPE=work,voice,pref,msg:+1-213-555-1234}
 * 
 * @see <a href=
 *      "https://datatracker.ietf.org/doc/html/rfc2426#section-3.3.1">RFC
 *      Dokumentation zum TEL Type</a>
 *
 */
public class PhoneProperty implements VCardProperty {

	/**
	 * Konstante für den Typ des VCard Properties
	 */
	private static final String TEL_TYPE = "TEL;TYPE=";
	/**
	 * Die Art der Telefonnummer, bspw 'work,voice'
	 */
	private String phoneType;
	/**
	 * die Telefonnummer
	 */
	private String number;

	/**
	 * Defaultkonstrukotr mit Art der Telefonnummer und der Telefonnummer.
	 * 
	 * @param phoneType die Art der Telefonnummer
	 * @param number    die Telefonnummer
	 */
	public PhoneProperty(String phoneType, String number) {
		this.phoneType = phoneType;
		this.number = number;
	}

	/**
	 * getter für die Telefonnummer
	 * 
	 * @return die Telefonnummer
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * setter für die Telefonnummer
	 * 
	 * @param number die Telefonnummer
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * getter für die Telefonart, bspw. 'pref,work'
	 * 
	 * @return die Telefonart
	 */
	public String getPhoneType() {
		return phoneType;
	}

	/**
	 * setter für die Telefonart, bspw. 'pref,work'
	 * 
	 * @param phoneType die Telefonart
	 */
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	@Override
	public String getType() {
		return TEL_TYPE + phoneType;
	}

	@Override
	public void serializeType(StringBuilder sb) {
		sb.append(getType());
	}

	@Override
	public void serializeProperty(StringBuilder sb) {
		sb.append(number);
	}
}