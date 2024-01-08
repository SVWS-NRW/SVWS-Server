package de.svws_nrw.module.reporting.types.lehrer;

import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.core.types.schule.Nationalitaeten;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Lehrer.</p>
 * <p>Sie enthält im Wesentlichen die Stammdaten des Lehrers.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingLehrer {

	/** Ggf. die Amtsbezeichnung des Lehrers. */
	private String amtsbezeichnung;

	/** Ggf. die Anrede des Lehrers. */
	private String anrede;

	/** Ggf. die dienstliche E-Mail-Adresse des Lehrers. */
	private String emailDienstlich;

	/** Ggf. die private Email-Adresse des Lehrers. */
	private String emailPrivat;

	/** Ggf. das Foto des Lehrers (jpg, Base64-kodiert des Lehrers.) */
	private String foto;

	/** Das Geburtsdatum des Lehrers. */
	private String geburtsdatum;

	/** Das Geschlecht des Lehrers */
	private Geschlecht geschlecht;

	/** Ggf. die Hausnummer zur Straße im Wohnort des Lehrers. */
	private String hausnummer;

	/** Ggf. der Hausnummerzusatz zur Straße im Wohnort des Lehrers. */
	private String hausnummerZusatz;

	/** Die ID des Lehrers. */
	private long id;

	/** Das Kürzel des Lehrers. */
	private String kuerzel;

	/** Der Nachname des Lehrers. */
	private String nachname;

	/** Die Bezeichnung des Personals-Typs des Lehrers. */
	private String personalTyp;

	/** Ggf. die Staatsangehörigkeit des Lehrers. */
	private Nationalitaeten staatsangehoerigkeit;

	/** Ggf. der Straßenname im Wohnort des Lehrers. */
	private String strassenname;

	/** Ggf. die Telefonnummer des Lehrers. */
	private String telefon;

	/** Ggf. die Mobilnummer des Lehrers. */
	private String telefonMobil;

	/** Ggf. ein akademischer Grad des Lehrers. */
	private String titel;

	/** Der Vorname des Lehrers. */
	private String vorname;

	/** Der Wohnort des Lehrers. */
	private OrtKatalogEintrag wohnort;

	/** Der Wohnortsname des Lehrers. */
	private String wohnortname;

	/** Ggf. der Ortsteil des Wohnortes des Lehrers. */
	private OrtsteilKatalogEintrag wohnortsteil;

	/** Ggf. der Ortsteilname des Wohnortes des Lehrers. */
	private String wohnortsteilname;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param amtsbezeichnung Ggf. die Amtsbezeichnung des Lehrers.
	 * @param anrede Ggf. die Anrede des Lehrers.
	 * @param emailDienstlich Ggf. die dienstliche E-Mail-Adresse des Lehrers.
	 * @param emailPrivat Ggf. die private Email-Adresse des Lehrers.
	 * @param foto Ggf. das Foto des Lehrers (jpg, Base64-kodiert des Lehrers.)
	 * @param geburtsdatum Das Geburtsdatum des Lehrers.
	 * @param geschlecht Das Geschlecht des Lehrers
	 * @param hausnummer Ggf. die Hausnummer zur Straße im Wohnort des Lehrers.
	 * @param hausnummerZusatz Ggf. der Hausnummerzusatz zur Straße im Wohnort des Lehrers.
	 * @param id Die ID des Lehrers.
	 * @param kuerzel Das Kürzel des Lehrers.
	 * @param nachname Der Nachname des Lehrers.
	 * @param personalTyp Die Bezeichnung des Personals-Typs des Lehrers.
	 * @param staatsangehoerigkeit Ggf. die Staatsangehörigkeit des Lehrers.
	 * @param strassenname Ggf. der Straßenname im Wohnort des Lehrers.
	 * @param telefon Ggf. die Telefonnummer des Lehrers.
	 * @param telefonMobil Ggf. die Mobilnummer des Lehrers.
	 * @param titel Ggf. ein akademischer Grad des Lehrers.
	 * @param vorname Der Vorname des Lehrers.
	 * @param wohnort Der Wohnort des Lehrers.
	 * @param wohnortname Der Wohnortsname des Lehrers.
	 * @param wohnortsteil Ggf. der Ortsteil des Wohnortes des Lehrers.
	 * @param wohnortsteilname Ggf. der Ortsteilname des Wohnortes des Lehrers.
	 */
	public ReportingLehrer(final String amtsbezeichnung, final String anrede, final String emailDienstlich, final String emailPrivat, final String foto, final String geburtsdatum, final Geschlecht geschlecht, final String hausnummer, final String hausnummerZusatz, final long id, final String kuerzel, final String nachname, final String personalTyp, final Nationalitaeten staatsangehoerigkeit, final String strassenname, final String telefon, final String telefonMobil, final String titel, final String vorname, final OrtKatalogEintrag wohnort, final String wohnortname,  final OrtsteilKatalogEintrag wohnortsteil, final String wohnortsteilname) {
		this.amtsbezeichnung = amtsbezeichnung;
		this.anrede = anrede;
		this.emailDienstlich = emailDienstlich;
		this.emailPrivat = emailPrivat;
		this.foto = foto;
		this.geburtsdatum = geburtsdatum;
		this.geschlecht = geschlecht;
		this.hausnummer = hausnummer;
		this.hausnummerZusatz = hausnummerZusatz;
		this.id = id;
		this.kuerzel = kuerzel;
		this.nachname = nachname;
		this.personalTyp = personalTyp;
		this.staatsangehoerigkeit = staatsangehoerigkeit;
		this.strassenname = strassenname;
		this.telefon = telefon;
		this.telefonMobil = telefonMobil;
		this.titel = titel;
		this.vorname = vorname;
		this.wohnort = wohnort;
		this.wohnortname = wohnortname;
		this.wohnortsteil = wohnortsteil;
		this.wohnortsteilname = wohnortsteilname;
	}



	// ##### Berechnete Felder #####

	/**
	 * Erzeugt eine Beschriftung für ein Unterschriftsfeld.
	 * @return Beschriftung in der Form: Titel Vorname (erster Buchstabe). Nachname
	 */
	public String unterschriftfeld() {
		return unterschriftfeld(true, false);
	}

	/**
	 * Erzeugt eine Beschriftung für ein Unterschriftsfeld.
	 * @param mitVornameKurz     Gibt an, ob nur der erste Buchstabe des Vornamens ausgegeben werden soll.
	 * @param mitAmtsbezeichnung Gibt an, ob die Amtsbezeichnung hinzugefügt werden soll.
	 * @return Beschriftung gemäß Parametern: Titel Vorname Nachname, Amtsbezeichnung
	 */
	public String unterschriftfeld(final boolean mitVornameKurz, final boolean mitAmtsbezeichnung) {
		return ("%s %s %s%s")
			.formatted(
				titel,
				mitVornameKurz ? vorname.charAt(0) + "." : vorname,
				nachname,
				mitAmtsbezeichnung ? ", " + amtsbezeichnung : "")
			.trim().translateEscapes();
	}

	/**
	 * Erzeugt eine Beschriftung für ein Unterschriftsfeld.
	 * @param mitVornameKurz     Gibt an, ob nur der erste Buchstabe des Vornamens ausgegeben werden soll.
	 * @param mitAmtsbezeichnung Gibt an, ob die Amtsbezeichnung hinzugefügt werden soll.
	 * @param zusatzUnterschrift Zusatz, der an die Unterschrift angehängt werden soll. Evtl. Zeilenumbruch kann eingefügt werden.
	 * @return Beschriftung gemäß Parametern: Titel Vorname Nachname, Amtsbezeichnung_ZusatzUnterschrift
	 */
	public String unterschriftfeld(final boolean mitVornameKurz, final boolean mitAmtsbezeichnung, final String zusatzUnterschrift) {
		return (unterschriftfeld(mitVornameKurz, mitAmtsbezeichnung) + zusatzUnterschrift).trim().translateEscapes();
	}



	// ##### Getter und Setter #####

	/**
	 * Ggf. die Amtsbezeichnung des Lehrers.
	 * @return Inhalt des Feldes amtsbezeichnung
	 */
	public String amtsbezeichnung() {
		return amtsbezeichnung;
	}

	/**
	 * Ggf die Amtsbezeichnung des Lehrers wird gesetzt.
	 * @param amtsbezeichnung Neuer Wert für das Feld amtsbezeichnung
	 */
	public void setAmtsbezeichnung(final String amtsbezeichnung) {
		this.amtsbezeichnung = amtsbezeichnung;
	}

	/**
	 * Ggf. die Anrede des Lehrers.
	 * @return Inhalt des Feldes anrede
	 */
	public String anrede() {
		return anrede;
	}

	/**
	 * Ggf die Anrede des Lehrers wird gesetzt.
	 * @param anrede Neuer Wert für das Feld anrede
	 */
	public void setAnrede(final String anrede) {
		this.anrede = anrede;
	}

	/**
	 * Ggf. die dienstliche E-Mail-Adresse des Lehrers.
	 * @return Inhalt des Feldes emailDienstlich
	 */
	public String emailDienstlich() {
		return emailDienstlich;
	}

	/**
	 * Ggf die dienstliche E-Mail-Adresse des Lehrers wird gesetzt.
	 * @param emailDienstlich Neuer Wert für das Feld emailDienstlich
	 */
	public void setEmailDienstlich(final String emailDienstlich) {
		this.emailDienstlich = emailDienstlich;
	}

	/**
	 * Ggf. die private Email-Adresse des Lehrers.
	 * @return Inhalt des Feldes emailPrivat
	 */
	public String emailPrivat() {
		return emailPrivat;
	}

	/**
	 * Ggf die private E-Mail-Adresse des Lehrers wird gesetzt.
	 * @param emailPrivat Neuer Wert für das Feld emailPrivat
	 */
	public void setEmailPrivat(final String emailPrivat) {
		this.emailPrivat = emailPrivat;
	}

	/**
	 * Ggf. das Foto des Lehrers (jpg, Base64-kodiert des Lehrers.)
	 * @return Inhalt des Feldes foto
	 */
	public String foto() {
		return foto;
	}

	/**
	 * Ggf das Foto des Lehrers (jpg, Base64-kodiert des Lehrers) wird gesetzt.
	 * @param foto Neuer Wert für das Feld foto
	 */
	public void setFoto(final String foto) {
		this.foto = foto;
	}

	/**
	 * Das Geburtsdatum des Lehrers.
	 * @return Inhalt des Feldes geburtsdatum
	 */
	public String geburtsdatum() {
		return geburtsdatum;
	}

	/**
	 * Das Geburtsdatum des Lehrers wird gesetzt.
	 * @param geburtsdatum Neuer Wert für das Feld geburtsdatum
	 */
	public void setGeburtsdatum(final String geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}

	/**
	 * Das Geschlecht des Lehrers
	 * @return Inhalt des Feldes geschlecht
	 */
	public Geschlecht geschlecht() {
		return geschlecht;
	}

	/**
	 * Das Geschlecht des Lehrers wird gesetzt.
	 * @param geschlecht Neuer Wert für das Feld geschlecht
	 */
	public void setGeschlecht(final Geschlecht geschlecht) {
		this.geschlecht = geschlecht;
	}

	/**
	 * Ggf. die Hausnummer zur Straße im Wohnort des Lehrers.
	 * @return Inhalt des Feldes hausnummer
	 */
	public String hausnummer() {
		return hausnummer;
	}

	/**
	 * Ggf die Hausnummer zur Straße im Wohnort des Lehrers wird gesetzt.
	 * @param hausnummer Neuer Wert für das Feld hausnummer
	 */
	public void setHausnummer(final String hausnummer) {
		this.hausnummer = hausnummer;
	}

	/**
	 * Ggf. der Hausnummerzusatz zur Straße im Wohnort des Lehrers.
	 * @return Inhalt des Feldes hausnummerZusatz
	 */
	public String hausnummerZusatz() {
		return hausnummerZusatz;
	}

	/**
	 * Ggf der Hausnummerzusatz zur Straße im Wohnort des Lehrers wird gesetzt.
	 * @param hausnummerZusatz Neuer Wert für das Feld hausnummerZusatz
	 */
	public void setHausnummerZusatz(final String hausnummerZusatz) {
		this.hausnummerZusatz = hausnummerZusatz;
	}

	/**
	 * Die ID des Lehrers.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Die ID des Lehrers wird gesetzt.
	 * @param id Neuer Wert für das Feld id
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Das Kürzel des Lehrers.
	 * @return Inhalt des Feldes kuerzel
	 */
	public String kuerzel() {
		return kuerzel;
	}

	/**
	 * Das Kürzel des Lehrers wird gesetzt.
	 * @param kuerzel Neuer Wert für das Feld kuerzel
	 */
	public void setKuerzel(final String kuerzel) {
		this.kuerzel = kuerzel;
	}

	/**
	 * Der Nachname des Lehrers.
	 * @return Inhalt des Feldes nachname
	 */
	public String nachname() {
		return nachname;
	}

	/**
	 * Der Nachname des Lehrers wird gesetzt.
	 * @param nachname Neuer Wert für das Feld nachname
	 */
	public void setNachname(final String nachname) {
		this.nachname = nachname;
	}

	/**
	 * Die Bezeichnung des Personals-Typs des Lehrers.
	 * @return Inhalt des Feldes personalTyp
	 */
	public String personalTyp() {
		return personalTyp;
	}

	/**
	 * Die Bezeichnung des Personals-Typs des Lehrers wird gesetzt.
	 * @param personalTyp Neuer Wert für das Feld personalTyp
	 */
	public void setPersonalTyp(final String personalTyp) {
		this.personalTyp = personalTyp;
	}

	/**
	 * Ggf. die Staatsangehörigkeit des Lehrers.
	 * @return Inhalt des Feldes staatsangehoerigkeit
	 */
	public Nationalitaeten staatsangehoerigkeit() {
		return staatsangehoerigkeit;
	}

	/**
	 * Ggf die Staatsangehörigkeit des Lehrers wird gesetzt.
	 * @param staatsangehoerigkeit Neuer Wert für das Feld staatsangehoerigkeit
	 */
	public void setStaatsangehoerigkeit(final Nationalitaeten staatsangehoerigkeit) {
		this.staatsangehoerigkeit = staatsangehoerigkeit;
	}

	/**
	 * Ggf. der Straßenname im Wohnort des Lehrers.
	 * @return Inhalt des Feldes strassenname
	 */
	public String strassenname() {
		return strassenname;
	}

	/**
	 * Ggf der Straßenname im Wohnort des Lehrers wird gesetzt.
	 * @param strassenname Neuer Wert für das Feld strassenname
	 */
	public void setStrassenname(final String strassenname) {
		this.strassenname = strassenname;
	}

	/**
	 * Ggf. die Telefonnummer des Lehrers.
	 * @return Inhalt des Feldes telefon
	 */
	public String telefon() {
		return telefon;
	}

	/**
	 * Ggf die Telefonnummer des Lehrers wird gesetzt.
	 * @param telefon Neuer Wert für das Feld telefon
	 */
	public void setTelefon(final String telefon) {
		this.telefon = telefon;
	}

	/**
	 * Ggf. die Mobilnummer des Lehrers.
	 * @return Inhalt des Feldes telefonMobil
	 */
	public String telefonMobil() {
		return telefonMobil;
	}

	/**
	 * Ggf die Mobilnummer des Lehrers wird gesetzt.
	 * @param telefonMobil Neuer Wert für das Feld telefonMobil
	 */
	public void setTelefonMobil(final String telefonMobil) {
		this.telefonMobil = telefonMobil;
	}

	/**
	 * Ggf. ein akademischer Grad des Lehrers.
	 * @return Inhalt des Feldes titel
	 */
	public String titel() {
		return titel;
	}

	/**
	 * Ggf ein akademischer Grad des Lehrers wird gesetzt.
	 * @param titel Neuer Wert für das Feld titel
	 */
	public void setTitel(final String titel) {
		this.titel = titel;
	}

	/**
	 * Der Vorname des Lehrers.
	 * @return Inhalt des Feldes vorname
	 */
	public String vorname() {
		return vorname;
	}

	/**
	 * Der Vorname des Lehrers wird gesetzt.
	 * @param vorname Neuer Wert für das Feld vorname
	 */
	public void setVorname(final String vorname) {
		this.vorname = vorname;
	}

	/**
	 * Der Wohnort des Lehrers.
	 * @return Inhalt des Feldes wohnort
	 */
	public OrtKatalogEintrag wohnort() {
		return wohnort;
	}

	/**
	 * Der Wohnort des Lehrers wird gesetzt.
	 * @param wohnort Neuer Wert für das Feld wohnort
	 */
	public void setWohnort(final OrtKatalogEintrag wohnort) {
		this.wohnort = wohnort;
	}

	/**
	 * Der Wohnortsname des Lehrers.
	 * @return Inhalt des Feldes wohnortname
	 */
	public String wohnortname() {
		return wohnortname;
	}

	/**
	 * Der Wohnortsname des Lehrers wird gesetzt.
	 * @param wohnortname Neuer Wert für das Feld wohnortname
	 */
	public void setWohnortname(final String wohnortname) {
		this.wohnortname = wohnortname;
	}

	/**
	 * Ggf. der Ortsteil des Wohnortes des Lehrers.
	 * @return Inhalt des Feldes wohnortsteil
	 */
	public OrtsteilKatalogEintrag wohnortsteil() {
		return wohnortsteil;
	}

	/**
	 * Ggf der Ortsteil des Wohnortes des Lehrers wird gesetzt.
	 * @param wohnortsteil Neuer Wert für das Feld wohnortsteil
	 */
	public void setWohnortsteil(final OrtsteilKatalogEintrag wohnortsteil) {
		this.wohnortsteil = wohnortsteil;
	}

	/**
	 * Ggf. der Ortsteilname des Wohnortes des Lehrers.
	 * @return Inhalt des Feldes wohnortsteilname
	 */
	public String wohnortsteilname() {
		return wohnortsteilname;
	}

	/**
	 * Ggf der Ortsteilname des Wohnortes des Lehrers wird gesetzt.
	 * @param wohnortsteilname Neuer Wert für das Feld wohnortsteilname
	 */
	public void setWohnortsteilname(final String wohnortsteilname) {
		this.wohnortsteilname = wohnortsteilname;
	}
}
