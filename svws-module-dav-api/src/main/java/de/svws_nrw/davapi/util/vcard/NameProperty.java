package de.svws_nrw.davapi.util.vcard;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * Name Property einer VCard.<br>
 * Beispiel:<br>
 * {@code N:Public;John;Quinlan;Mr.;Esq.} <br>
 * {@code N:Stevenson;John;Philip,Paul;Dr.;Jr.,M.D.,A.C.P.}
 *
 * @see <a href=
 *      "https://datatracker.ietf.org/doc/html/rfc2426#section-3.1.2">RFC N Type
 *      Definition</a>
 *
 */
public final class NameProperty implements VCardProperty {

	/**
	 * Default Konstruktor für ein NameProperty
	 */
	public NameProperty() {
		//empty constructor
	}

	/**
	 * Typ dieses VCard Properties
	 */
	private static final String TYPE = "N";
	/**
	 * Trennzeichen zwischen den HonorificPrefixes dieses Namens
	 */
	private static final String PFX_SEQUENCE_SEPERATOR = " ";
	/**
	 * Trennzeichen zwischen gegebenem Namen (Vorname) und Familiennamen dieses
	 * Namens
	 */
	private static final String GIVEN_FAMILY_NAME_SEPERATOR = " ";
	/**
	 * Der Familienname
	 */
	private String familyName;
	/**
	 * Der gegebene Rufname
	 */
	private String givenName;
	/**
	 * Liste von Ehrentiteln/Namensprefixen, bspw. {'Dr.','Dipl.-Ing.'}
	 */
	private final List<String> honorificPrefixes = new ArrayList<>();
	/**
	 * Liste von Ehrentiteln/Namenssuffixen, bspw: 'MBA'
	 */
	private final List<String> honorificSuffixes = new ArrayList<>();
	/**
	 * Liste weiterer Vornamen
	 */
	private final List<String> additionalNames = new ArrayList<>();

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public void serializeType(final StringBuilder sb) {
		sb.append(TYPE);
	}

	@Override
	public void serializeProperty(final StringBuilder sb) {
		sb.append(familyName);
		sb.append(SEQUENCE_ELEMENT_SEPARATOR);
		sb.append(givenName);
		sb.append(SEQUENCE_ELEMENT_SEPARATOR);
		appendMultiple(additionalNames, sb);
		sb.append(SEQUENCE_ELEMENT_SEPARATOR);
		appendMultiple(honorificPrefixes, sb);
		sb.append(SEQUENCE_ELEMENT_SEPARATOR);
		appendMultiple(honorificSuffixes, sb);
	}

	/**
	 * getter für den Familienname
	 *
	 * @return der Familienname
	 */
	public String getFamilyName() {
		return familyName;
	}

	/**
	 * setter für den Familienname
	 *
	 * @param familyName der Familienname
	 */
	public void setFamilyName(final String familyName) {
		this.familyName = familyName;
	}

	/**
	 * Getter für den Rufname
	 *
	 * @return der Rufname
	 */
	public String getGivenName() {
		return givenName;
	}

	/**
	 * setter für den Rufname
	 *
	 * @param givenName der Rufname
	 */
	public void setGivenName(final String givenName) {
		this.givenName = givenName;
	}

	/**
	 * getter für die Liste an Präfixtiteln (bspw Dr.)
	 *
	 * @return die Liste an Präfixtiteln
	 */
	public List<String> getHonorificPrefixes() {
		return honorificPrefixes;
	}

	/**
	 * setter für die Liste an Suffixtiteln (bspw MBA)
	 *
	 * @return die Liste an Suffixtiteln
	 */
	public List<String> getHonorificSuffixes() {
		return honorificSuffixes;
	}

	/**
	 * getter für die Liste mit zusätzlichen Vornamen
	 *
	 * @return die Liste mit zusätzlichen Vornamen
	 */
	public List<String> getAdditionalNames() {
		return additionalNames;
	}

	/**
	 * Hilfsmethode zum zufügen von Listen
	 *
	 * @param names
	 * @param sb
	 */
	private static void appendMultiple(final List<String> names, final StringBuilder sb) {
		final Iterator<String> iter = names.iterator();
		while (iter.hasNext()) {
			sb.append(iter.next());
			if (iter.hasNext()) {
				sb.append(PROPERTY_LIST_ENTRY_SEPARATOR);
			}
		}
	}

	/**
	 * Erzeugt den Fullname aus diesem Namen
	 *
	 * @return das Fullname Property passend zu diesem NameProperty
	 */
	public FullnameProperty toFullnameProperty() {
		final StringBuilder sb = new StringBuilder();
		final Iterator<String> iter = honorificPrefixes.iterator();
		while (iter.hasNext()) {
			sb.append(iter.next());
			sb.append(PFX_SEQUENCE_SEPERATOR);
		}
		sb.append(givenName);
		sb.append(GIVEN_FAMILY_NAME_SEPERATOR);
		sb.append(familyName);
		return new FullnameProperty(sb.toString());
	}

}
