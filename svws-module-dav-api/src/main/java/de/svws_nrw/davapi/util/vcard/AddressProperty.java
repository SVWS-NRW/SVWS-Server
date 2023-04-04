package de.svws_nrw.davapi.util.vcard;

/**
 * VCard Property für Adressen.<br>
 * Beispiel:<br>
 * {@code ADR;TYPE=dom,home,postal,parcel:;;123 Main Street;Any Town;CA;91921-1234}
 *
 * @see <a href=
 *      "https://datatracker.ietf.org/doc/html/rfc2426#section-3.2.1">RFC
 *      Dokumentation für ADR</a>
 */
public final class AddressProperty implements VCardProperty {
	/**
	 * Trennzeichen zwischen Straße und Hausnummer
	 */
	private static final String STREET_NUMBER_SPACER = " ";

	/**
	 * Konstante für den Property Type
	 */
	private static final String TYPE = "ADR";

	/**
	 * Trennzeichen zwischen Hausnummer und Hausnummerzusatz
	 */
	private static final Object HOUSENUMBER_ADDITION_SPACER = "";

	/**
	 * Postleitzahl
	 */
	private String postalCode;

	/**
	 * Postfach
	 */
	private String postOfficeBox;

	/**
	 * Erweiterte Adresse, laut <a href=
	 * "https://www.w3.org/TR/vcard-rdf/">https://www.w3.org/TR/vcard-rdf/</a>
	 * deprecated in V4.
	 */
	private String extendedAdress;

	/**
	 * Straßenname
	 */
	private String street;

	/**
	 * Stadt, genauer: Locality
	 */
	private String city;

	/**
	 * Region
	 */
	private String region;

	/**
	 * Land
	 */
	private String country;

	/**
	 * Hausnummer
	 */
	private String houseNumber;

	/**
	 * Hausnummerzusatz
	 */
	private String houseNumberAddition;

	/**
	 * Adressart, bspw "home"
	 */
	private String addressType;

	@Override
	public String getType() {
		if (this.addressType == null)
			return TYPE;
		return TYPE + ";TYPE=" + addressType;
	}

	@Override
	public void serializeType(final StringBuilder sb) {
		sb.append(getType());
	}

	@Override
	public void serializeProperty(final StringBuilder sb) {
		addPropertySequenceElement(this.postOfficeBox, sb);
		addPropertySequenceElement(this.extendedAdress, sb);
		addStreetPropertyPart(this.street, this.houseNumber, this.houseNumberAddition, sb);
		addPropertySequenceElement(this.city, sb);
		addPropertySequenceElement(this.region, sb);
		addPropertySequenceElement(this.postalCode, sb);
		addPropertySequenceElement(this.country, sb, true);
	}

	/**
	 * Hilfsmethode zum Serialisieren des Straßenelements einer Adresse an einem
	 * gegebenen StringBuilder
	 *
	 * @param street
	 * @param houseNumber
	 * @param houseNumberAddition
	 * @param sb
	 */
	private static void addStreetPropertyPart(final String street, final String houseNumber, final String houseNumberAddition,
			final StringBuilder sb) {
		if (street != null) {
			sb.append(street);
		}
		if (houseNumber != null) {
			sb.append(STREET_NUMBER_SPACER);
			sb.append(houseNumber);
		}
		if (houseNumberAddition != null) {
			sb.append(HOUSENUMBER_ADDITION_SPACER);
			sb.append(houseNumberAddition);
		}
		sb.append(SEQUENCE_ELEMENT_SEPARATOR);
	}

	/**
	 * Hilfsmethode zum Serialisieren eines Adress-Sequenz-Elements an einem
	 * gegebenen StringBuilder. das Sequenz-Element wird mit Trailing
	 * {@link VCardProperty#SEQUENCE_ELEMENT_SEPARATOR} zugefügt.
	 *
	 * @param propertySequenceElement
	 * @param sb
	 */
	private static void addPropertySequenceElement(final String propertySequenceElement, final StringBuilder sb) {
		addPropertySequenceElement(propertySequenceElement, sb, false);
	}

	/**
	 * Hilfsmethode zum Serialisieren eines Adress-Sequenz-Elements an einem
	 * gegebenen StringBuilder.
	 *
	 * @param propertySequenceElement
	 * @param sb
	 * @param isLast                  Angabe ob es das letzte Sequenz-Element ist,
	 *                                so dass kein Trailing
	 *                                {@link CardProperty#SEQUENCE_ELEMENT_SEPARATOR}
	 *                                gesetzt wird
	 */
	private static void addPropertySequenceElement(final String propertySequenceElement, final StringBuilder sb, final boolean isLast) {
		if (propertySequenceElement != null) {
			sb.append(propertySequenceElement);
		}
		if (!isLast) {
			sb.append(SEQUENCE_ELEMENT_SEPARATOR);
		}
	}

	/**
	 * getter for postalCode
	 *
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * setter for postalCode
	 *
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(final String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * getter for postOfficeBox
	 *
	 * @return the postOfficeBox
	 */
	public String getPostOfficeBox() {
		return postOfficeBox;
	}

	/**
	 * setter for postOfficeBox
	 *
	 * @param postOfficeBox the postOfficeBox to set
	 */
	public void setPostOfficeBox(final String postOfficeBox) {
		this.postOfficeBox = postOfficeBox;
	}

	/**
	 * getter for extendedAdress field
	 *
	 * in vCard V 4.0 nicht mehr zu verwenden
	 * @return the extendedAdress

	 */
	public String getExtendedAdress() {
		return extendedAdress;
	}

	/**
	 * setter for extendedAddress field
	 * in vCard V 4.0 nicht mehr zu verwenden
	 * @param extendedAdress the extendedAdress to set
	 */
	public void setExtendedAdress(final String extendedAdress) {
		this.extendedAdress = extendedAdress;
	}

	/**
	 * getter for street
	 *
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * setter for street
	 *
	 * @param street the street to set
	 */
	public void setStreet(final String street) {
		this.street = street;
	}

	/**
	 * getter for city
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * setter for city
	 *
	 * @param city the city to set
	 */
	public void setCity(final String city) {
		this.city = city;
	}

	/**
	 * getter for region
	 *
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * setter for region
	 *
	 * @param region the region to set
	 */
	public void setRegion(final String region) {
		this.region = region;
	}

	/**
	 * getter for country
	 *
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * setter for country
	 *
	 * @param country the country to set
	 */
	public void setCountry(final String country) {
		this.country = country;
	}

	/**
	 * getter for house number
	 *
	 * @return the houseNumber
	 */
	public String getHouseNumber() {
		return houseNumber;
	}

	/**
	 * setter for house number
	 *
	 * @param houseNumber the houseNumber to set
	 */
	public void setHouseNumber(final String houseNumber) {
		this.houseNumber = houseNumber;
	}

	/**
	 * getter for houseNumber addition
	 *
	 * @return the houseNumberAddition
	 */
	public String getHouseNumberAddition() {
		return houseNumberAddition;
	}

	/**
	 * setter for house number addition
	 *
	 * @param houseNumberAddition the houseNumberAddition to set
	 */
	public void setHouseNumberAddition(final String houseNumberAddition) {
		this.houseNumberAddition = houseNumberAddition;
	}

	/**
	 * getter for address type
	 *
	 * @return the addressType
	 */
	public String getAddressType() {
		return addressType;
	}

	/**
	 * setter for address type
	 *
	 * @param addressType the addressType to set
	 */
	public void setAddressType(final String addressType) {
		this.addressType = addressType;
	}
}
