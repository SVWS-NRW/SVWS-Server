package de.svws_nrw.module.reporting.types.lehrer;

import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.module.reporting.types.person.ReportingPerson;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Lehrer.
 */
public class ReportingLehrer extends ReportingPerson {

	/** Ggf. die Amtsbezeichnung des Lehrers. */
	protected String amtsbezeichnung;

	/** Ggf. das Foto des Lehrers (jpg, Base64-kodiert des Lehrers.) */
	protected String foto;

	/** Die ID des Lehrers. */
	protected long id;

	/** Das Kürzel des Lehrers. */
	protected String kuerzel;

	/** Die Bezeichnung des Personals-Typs des Lehrers. */
	protected PersonalTyp personalTyp;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param amtsbezeichnung Ggf. die Amtsbezeichnung des Lehrers.
	 * @param anrede Die Anrede des Lehrers.
	 * @param emailPrivat Private E-Mail-Adresse des Lehrers.
	 * @param emailSchule Schulische E-Mail-Adresse des Lehrers.
	 * @param faxSchule Die schulische Faxnummer des Lehrers.
	 * @param foto Ggf. das Foto des Lehrers (jpg, Base64-kodiert.)
	 * @param geburtsdatum Das Geburtsdatum des Lehrers.
	 * @param geburtsland Das Geburtsland des Lehrers.
	 * @param geburtsname Der Geburtsname des Lehrers.
	 * @param geburtsort Der Geburtsort des Lehrers.
	 * @param geschlecht Das Geschlecht des Lehrers.
	 * @param hausnummer Ggf. die Hausnummer zur Straße im Wohnort des Lehrers.
	 * @param hausnummerZusatz Ggf. der Hausnummerzusatz zur Straße im Wohnort des Lehrers.
	 * @param id Die ID des Lehrers.
	 * @param kuerzel Das Kürzel des Lehrers.
	 * @param nachname Der Name des Lehrers.
	 * @param personalTyp Die Bezeichnung des Personals-Typs des Lehrers.
	 * @param staatsangehoerigkeit Die Staatsangehörigkeit des Lehrers.
	 * @param staatsangehoerigkeit2 Die ggf. zweite Staatsangehörigkeit des Lehrers.
	 * @param strassenname Ggf. der Straßenname im Wohnort des Lehrers.
	 * @param telefonPrivat Die private Telefonnummer des Lehrers.
	 * @param telefonPrivatMobil Die private Mobilfunk-Telefonnummer des Lehrers.
	 * @param telefonSchule Die schulische Telefonnummer des Lehrers.
	 * @param telefonSchuleMobil Die schulische Mobilfunk-Telefonnummer des Lehrers.
	 * @param titel Die Titel des Lehrers.
	 * @param vorname Der Vorname des Lehrers.
	 * @param vornamen Alle Vornamen des Lehrers
	 * @param wohnort Der Wohnort des Lehrers.
	 * @param wohnortsteil Ggf. der Ortsteil des Wohnortes des Lehrers.
	 */
	public ReportingLehrer(final String amtsbezeichnung, final String anrede, final String emailPrivat, final String emailSchule, final String faxSchule,
			final String foto, final String geburtsdatum, final String geburtsland, final String geburtsname, final String geburtsort,
			final Geschlecht geschlecht, final String hausnummer, final String hausnummerZusatz, final long id, final String kuerzel, final String nachname,
			final PersonalTyp personalTyp, final Nationalitaeten staatsangehoerigkeit, final Nationalitaeten staatsangehoerigkeit2, final String strassenname,
			final String telefonPrivat, final String telefonPrivatMobil, final String telefonSchule, final String telefonSchuleMobil, final String titel,
			final String vorname, final String vornamen, final OrtKatalogEintrag wohnort, final OrtsteilKatalogEintrag wohnortsteil) {
		super(anrede, emailPrivat, emailSchule, faxSchule, geburtsdatum, geburtsland, geburtsname, geburtsort, geschlecht, hausnummer, hausnummerZusatz,
				nachname, staatsangehoerigkeit, staatsangehoerigkeit2, strassenname, telefonPrivat, telefonPrivatMobil, telefonSchule, telefonSchuleMobil,
				titel, vorname,	vornamen, wohnort, wohnortsteil);
		this.amtsbezeichnung = amtsbezeichnung;
		this.foto = foto;
		this.id = id;
		this.kuerzel = kuerzel;
		this.personalTyp = personalTyp;
	}



	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	public int hashCode() {
		return 31 + Long.hashCode(id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof final ReportingLehrer other))
			return false;
		return (id == other.id);
	}


	// ##### Berechnete Felder #####

	/**
	 * Erzeugt eine Beschriftung für ein Unterschriftsfeld.
	 *
	 * @return Beschriftung in der Form: Titel Vorname (erster Buchstabe). Nachname
	 */
	public String unterschriftfeld() {
		return unterschriftfeld(true, false);
	}

	/**
	 * Erzeugt eine Beschriftung für ein Unterschriftsfeld.
	 * @param mitVornameKurz     Gibt an, ob nur der erste Buchstabe des Vornamens ausgegeben werden soll.
	 * @param mitAmtsbezeichnung Gibt an, ob die Amtsbezeichnung hinzugefügt werden soll.
	 *
	 * @return Beschriftung gemäß Parametern: Titel Vorname Nachname, Amtsbezeichnung
	 */
	public String unterschriftfeld(final boolean mitVornameKurz, final boolean mitAmtsbezeichnung) {
		return ("%s %s %s%s")
				.formatted(
						this.titel(),
						mitVornameKurz ? (this.vorname().charAt(0) + ".") : this.vorname(),
						this.nachname(),
						mitAmtsbezeichnung ? (", " + this.amtsbezeichnung()) : "")
				.trim().translateEscapes();
	}

	/**
	 * Erzeugt eine Beschriftung für ein Unterschriftsfeld.
	 *
	 * @param mitVornameKurz     Gibt an, ob nur der erste Buchstabe des Vornamens ausgegeben werden soll.
	 * @param mitAmtsbezeichnung Gibt an, ob die Amtsbezeichnung hinzugefügt werden soll.
	 * @param zusatzUnterschrift Zusatz, der an die Unterschrift angehängt werden soll. Evtl. Zeilenumbruch kann eingefügt werden.
	 *
	 * @return Beschriftung gemäß Parametern: Titel Vorname Nachname, Amtsbezeichnung_ZusatzUnterschrift
	 */
	public String unterschriftfeld(final boolean mitVornameKurz, final boolean mitAmtsbezeichnung, final String zusatzUnterschrift) {
		return (unterschriftfeld(mitVornameKurz, mitAmtsbezeichnung) + zusatzUnterschrift).trim().translateEscapes();
	}



	// ##### Getter #####

	/**
	 * Ggf. die Amtsbezeichnung des Lehrers.
	 *
	 * @return Inhalt des Feldes amtsbezeichnung
	 */
	public String amtsbezeichnung() {
		return amtsbezeichnung;
	}

	/**
	 * Ggf. das Foto des Lehrers (jpg, Base64-kodiert des Lehrers.)
	 *
	 * @return Inhalt des Feldes foto
	 */
	public String foto() {
		return foto;
	}

	/**
	 * Die ID des Lehrers.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Das Kürzel des Lehrers.
	 *
	 * @return Inhalt des Feldes kuerzel
	 */
	public String kuerzel() {
		return kuerzel;
	}

	/**
	 * Die Bezeichnung des Personals-Typs des Lehrers.
	 *
	 * @return Inhalt des Feldes personalTyp
	 */
	public PersonalTyp personalTyp() {
		return personalTyp;
	}
}
