package de.svws_nrw.davapi.util.vcard;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Vector;

import de.svws_nrw.core.data.adressbuch.AdressbuchEintrag;
import de.svws_nrw.core.data.adressbuch.AdressbuchKontakt;
import de.svws_nrw.core.data.adressbuch.AdressbuchKontaktListe;
import de.svws_nrw.core.data.adressbuch.Telefonnummer;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse repräsentiert eine VCard und bietet Methoden, Eigenschaften
 * hinzuzufügen, zu lesen sowie die VCard zu serialisieren.
 *
 */
public class VCard {

	/**
	 * formatString für UID Property
	 */
	private static final String URN_UUID_STR = "urn:uuid:%s";

	/**
	 * VCard-Trennzeichen zwischen type und property
	 */
	private static final String TYPE_PROPERTY_SEPARATOR = ":";

	/**
	 * VCard-Trennzeichen zwischen verschiedenen PropertyTypes
	 */
	private static final String PROPERTY_SEPARATOR = "\r\n";

	/**
	 * VCardProperty zum Start einer VCard
	 */
	private static final VCardProperty BEGIN_PROPERTY = new SimpleProperty("BEGIN", "VCARD");

	/**
	 * VCardProperty für das Ende einer VCard
	 */
	private static final VCardProperty END_PROPERTY = new SimpleProperty("END", "VCARD");

	/**
	 * Version der VCard
	 */
	private Version version = Version.V3;
	/**
	 * das benötige Fullname Property dieser VCard
	 */
	private VCardProperty fnProperty;
	/**
	 * Die Art der VCard (für Vcard V4)
	 */
	private Kind kind = Kind.INDIVIDUAL;

	/**
	 * Die Liste der übrigen Properties dieser VCard
	 */
	private List<VCardProperty> properties = new Vector<>();

	/**
	 * der UID String für diese VCard
	 */
	private String uid;

	/**
	 * Member dieser VCard (bei Version 4.0 und KIND:GROUP)
	 */
	private List<String> members = new Vector<>();

	/**
	 * Konstruktor für eine VCard mit einem gegebenen Fullname Property Dies erfüllt
	 * die Minimalen Anforderungen zum Serialisieren einer VCard
	 * 
	 * @param fnProperty das Fullname Property der VCard
	 */
	public VCard(FullnameProperty fnProperty) {
		this.fnProperty = fnProperty;
	}

	/**
	 * Konstruktor für eine VCard mit einem gegebenen Name Property. Das benötigte
	 * FullnameProperty wird daraus erzeugt
	 *
	 * @param nameProperty das nameProperty der VCard
	 */
	public VCard(NameProperty nameProperty) {
		this.fnProperty = nameProperty.toFullnameProperty();
		addProperty(nameProperty);
	}

	/**
	 * privater Konstruktor für eine VCard auf Basis eines
	 * {@link AdressbuchKontakt}. Die dem Kontakt gegebenen Eigenschaften werden in
	 * Properties übertragen. Das Fullname Property wird aus dem Kontakt erzeugt.
	 * 
	 * @param kontakt der Kontakt, den diese VCard repräsentiert
	 */
	private VCard(AdressbuchKontakt kontakt) {
		this((AdressbuchEintrag) kontakt);
		NameProperty np = new NameProperty();

		np.setFamilyName(kontakt.nachname);
		np.setGivenName(kontakt.vorname);
		if (kontakt.zusatzNachname != null) {
			np.getHonorificSuffixes().add(kontakt.zusatzNachname);
		}
		addProperty(np);
		this.fnProperty = np.toFullnameProperty();
		if (kontakt.organisation != null) {
			addProperty(new SimpleProperty("ORG", kontakt.organisation));
		}
		if (kontakt.rolle != null) {
			addProperty(new SimpleProperty("ROLE", kontakt.rolle));
		}
		if (kontakt.idEltern != null) {
			addProperty(new RelatedProperty(RelatedProperty.RelatedTypeValue.PARENT, stringToUUId(kontakt.idEltern)));
		}
		if (kontakt.idKind != null) {
			addProperty(new RelatedProperty(RelatedProperty.RelatedTypeValue.CHILD, stringToUUId(kontakt.idKind)));
		}
		AddressProperty ap = new AddressProperty();
		ap.setCity(kontakt.ort);
		ap.setPostalCode(kontakt.plz);
		ap.setStreet(kontakt.strassenname);
		ap.setHouseNumber(kontakt.hausnummer);
		ap.setHouseNumberAddition(kontakt.hausnummerZusatz);
		addProperty(ap);

		addSimplePropertyIfNotNull("EMAIL", kontakt.email);
		addSimplePropertyIfNotNull("URL", kontakt.webAdresse);
		VCardProperty categoriesProperty;
		if (kontakt.kategorien.isEmpty())
			categoriesProperty = new CategoriesProperty(kontakt.adressbuchId);
		else {

			categoriesProperty = new CategoriesProperty(kontakt.kategorien);
			addProperty(categoriesProperty);
		}
		for (Telefonnummer t : kontakt.telefonnummern) {
			addProperty(new PhoneProperty(t.type, t.number));
		}
	}

	/**
	 * privater Konstruktor für eine VCard auf Basis einer
	 * {@link AdressbuchKontaktListe}. Die der Liste gegebenen Eigenschaften werden
	 * in Properties übertragen. Das Fullname Property wird aus der ID erzeugt.
	 * 
	 * @param kl die Liste, die diese VCard repräsentiert
	 */
	private VCard(AdressbuchKontaktListe kl) {
		this((AdressbuchEintrag) kl);
		setKind(Kind.GROUP);
		for (AdressbuchKontakt k : kl.kontakte) {
			this.addMember(stringToUUId(k.id));
		}
	}

	/**
	 * privater Konstruktor für AdressbuchEinträge. setzt die UID und das
	 * FullnameProperty auf Basis der {@link AdressbuchEintrag#id}
	 * 
	 * @param e der Adressbucheintrag
	 */
	private VCard(AdressbuchEintrag e) {
		setUID(stringToUUId(e.id));
		this.fnProperty = new FullnameProperty(e.id);
	}

	/**
	 * Hilfsmethode um aus einem gegebenen String eine GUID zu erzeugen, dazu wird
	 * {@linkplain UUID#nameUUIDFromBytes(byte[])} verwendet
	 * 
	 * @param input der String, dessen GUID-HASH erzeugt werden soll
	 * @return ein GUID-Hash der eingabe
	 */
	public static String stringToUUId(String input) {
		return UUID.nameUUIDFromBytes(input.getBytes()).toString();
	}

	/**
	 * Hilfsmethode zum hinzufügen von {@link SimpleProperty} zu dieser VCard
	 * 
	 * @param type     der Type des {@link SimpleProperty}
	 * @param property das Property des {@link SimpleProperty}
	 */
	private void addSimplePropertyIfNotNull(String type, String property) {
		if (property != null) {
			SimpleProperty sp = new SimpleProperty(type, property);
			addProperty(sp);
		}
	}

	/**
	 * Methode zum Serialisieren dieser VCard
	 * 
	 * @return die VCard als Zeichenkette
	 */
	public String serialize() {
		StringBuilder sb = new StringBuilder();
		serializeProperty(BEGIN_PROPERTY, sb);
		sb.append(PROPERTY_SEPARATOR);
		serializeProperty(version.property, sb);
		sb.append(PROPERTY_SEPARATOR);
		if (this.version == Version.V4) {
			serializeProperty(kind.property, sb);
			sb.append(PROPERTY_SEPARATOR);
		}
		serializeProperty(fnProperty, sb);
		sb.append(PROPERTY_SEPARATOR);
		for (VCardProperty vCardProperty : properties) {
			serializeProperty(vCardProperty, sb);
			sb.append(PROPERTY_SEPARATOR);
		}
		if (this.uid != null) {
			serializeProperty(new SimpleProperty("UID", this.uid/* String.format(URN_UUID_STR, this.uid) */), sb);
			sb.append(PROPERTY_SEPARATOR);
		}
		if (this.kind == Kind.GROUP) {
			for (String memberUID : members) {
				serializeProperty(new SimpleProperty("MEMBER", String.format(URN_UUID_STR, memberUID)), sb);
				sb.append(PROPERTY_SEPARATOR);
			}
		}
		serializeProperty(END_PROPERTY, sb);
		return sb.toString();
	}

	/**
	 * Methode zum hinzufügen eines {@link VCardProperty}
	 * 
	 * @param property das zuzufügende Property
	 */
	public void addProperty(VCardProperty property) {
		properties.add(property);
	}

	/**
	 * Methode zum Lesen eines {@link VCardProperty}
	 * 
	 * @param type der Type, der gesucht werden soll
	 * @return das Property, wenn es bei dem gegebenen Typ bereits eines gibt
	 */
	public Optional<VCardProperty> getProperty(String type) {
		return properties.stream().filter(p -> p.getType().equalsIgnoreCase(type)).findFirst();
	}

	/**
	 * setzt die Art der VCard, nur für VCard Version 4.0 nützlich
	 * 
	 * @param k die Art der VCard
	 */
	public void setKind(@NotNull Kind k) {
		this.kind = k;
	}

	/**
	 * setzt die UID der VCard. Nützlich aber nicht nötig, ist
	 * {@link #stringToUUId(String)} aufzurufen, um den angegeben String vorher ins
	 * GUID-Format zu bringen.
	 * 
	 * @param uid die UID
	 */
	public void setUID(String uid) {
		this.uid = uid;
	}

	/**
	 * fügt ein Mitglied zu dieser VCard hinzu
	 * 
	 * @param uid die UID der VCard
	 */
	public void addMember(String uid) {
		this.members.add(uid);
	}

	/**
	 * setzt die Version der VCard
	 * 
	 * @param version die Version
	 */
	public void setVersion(@NotNull Version version) {
		this.version = version;
	}

	/**
	 * Hilfsmethode zum hinzufügen eines {@link VCardProperty} zu einem gegebenen
	 * {@link StringBuilder} bei der Serialisierung. Nutzt die Methoden
	 * {@link VCardProperty#serializeType(StringBuilder)} und
	 * {@link VCardProperty#serializeProperty(StringBuilder)}
	 * 
	 * @param property
	 * @param sb
	 */
	private void serializeProperty(VCardProperty property, StringBuilder sb) {
		if (property instanceof RelatedProperty && this.version != Version.V4) {
			return;
		}
		property.serializeType(sb);
		sb.append(TYPE_PROPERTY_SEPARATOR);
		property.serializeProperty(sb);
	}

	/**
	 * Enum für unterstützte Versionen von VCards
	 *
	 */
	public enum Version {
		/** Version:3.0 */
		V3("3.0"),
		/** Version:4.0 */
		V4("4.0");

		/**
		 * das VersionProperty, welches diese Version repräsentiert
		 */
		private final VCardProperty property;
		/**
		 * Konstante für den Type String
		 */
		private static final String TYPE_STR = "VERSION";

		/**
		 * erstellt eine neue Version
		 * 
		 * @param version die Version
		 */
		Version(String version) {
			this.property = new SimpleProperty(TYPE_STR, version);
		}
	}

	/**
	 * Enum für das KIND Property einer VCard (Art der VCard) in Version 4.0.
	 * Unterstützt werden Individual und GROUP
	 *
	 */
	public enum Kind {
		/** KIND:individual, standard typ der VCard für Indivduen */
		INDIVIDUAL("individual"),
		/**
		 * KIND:group für Gruppen, bspw Gruppen von VCards oder auch Gruppen von
		 * Mailadressen
		 */
		GROUP("group"),
		/** KIND:org für Organsitation */
		ORG("org"),
		/** KIND:location für Orte */
		LOCATION("location");

		/**
		 * das KIND Property für die Art der VCard
		 */
		private VCardProperty property;
		/**
		 * Konstante für den Type String
		 */
		private static final String TYPE_STR = "KIND";

		/**
		 * erstellt eine neue Art für das KIND-Property der VCard
		 * 
		 * @param kind die Art 
		 */
		private Kind(String kind) {
			this.property = new SimpleProperty(TYPE_STR, kind);
		}
	}

	/**
	 * Statische Methode zum erstellen einer VCard auf Basis eines
	 * AdressbuchEintrags.
	 * 
	 * @param eintrag der AdressbuchEintrag
	 * @return die VCard, die diesen Adressbucheintrag repräsentiert.
	 */
	public static VCard createVCard(AdressbuchEintrag eintrag) {
		VCard result;
		if (eintrag instanceof AdressbuchKontakt k) {
			result = new VCard(k);
		} else if (eintrag instanceof AdressbuchKontaktListe kl) {
			result = new VCard(kl);
		} else {
			result = new VCard(new FullnameProperty(eintrag.id));
		}
		result.setVersion(Version.V4);
		return result;
	}
}
