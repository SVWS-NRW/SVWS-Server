package de.svws_nrw.module.reporting.types.person;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.module.reporting.types.ReportingBaseType;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Person, welche Super-Klasse für alle personenbezogenen Reporting-Types ist wie Schüler oder Lehrer.
 * Sie sollte nicht direkt als Reporting-Type verwendet werden, sondern nur als Super-Klasse der speziellen Personenklasse.
 */
public class ReportingPerson extends ReportingBaseType {

	/** Die Anrede. */
	protected String anrede;

	/** Die private E-Mail-Adresse. */
	protected String emailPrivat;

	/** Die schulische E-Mail-Adresse. */
	protected String emailSchule;

	/** Die schulische Fax-Nummer. */
	protected String faxSchule;

	/** Das Geburtsdatum. */
	protected String geburtsdatum;

	/** Das Geburtsland. */
	protected String geburtsland;

	/** Der Geburtsname. */
	protected String geburtsname;

	/** Der Geburtsort. */
	protected String geburtsort;

	/** Das Geschlecht. */
	protected Geschlecht geschlecht;

	/** Ggf. die Hausnummer zur Straße im Wohnort. */
	protected String hausnummer;

	/** Ggf. der Hausnummerzusatz zur Straße im Wohnort. */
	protected String hausnummerZusatz;

	/** Der Name. */
	protected String nachname;

	/** Die Staatsangehörigkeit. */
	protected Nationalitaeten staatsangehoerigkeit;

	/** Eine evtl. vorhandene zweite Staatsangehörigkeit. */
	protected Nationalitaeten staatsangehoerigkeit2;

	/** Ggf. der Straßenname im Wohnort. */
	protected String strassenname;

	/** Die private Telefonnummer. */
	protected String telefonPrivat;

	/** Die private Mobilfunk-Telefonnummer. */
	protected String telefonPrivatMobil;

	/** Die schulische Telefonnummer. */
	protected String telefonSchule;

	/** Die schulische Mobilifunk-Telefonnummer. */
	protected String telefonSchuleMobil;

	/** Die Titel. */
	protected String titel;

	/** Der Vorname (Rufname). */
	protected String vorname;

	/** Alle Vornamen, sofern es mehrere gibt. */
	protected String vornamen;

	/** Der Wohnort. */
	protected OrtKatalogEintrag wohnort;

	/** Ggf. der Ortsteil des Wohnortes. */
	protected OrtsteilKatalogEintrag wohnortsteil;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param anrede				Die Anrede.
	 * @param emailPrivat			Die private E-Mail-Adresse.
	 * @param emailSchule			Die schulische E-Mail-Adresse.
	 * @param faxSchule 			Die schulische Fax-Nummer.
	 * @param geburtsdatum			Das Geburtsdatum.
	 * @param geburtsland			Das Geburtsland.
	 * @param geburtsname			Der Geburtsname.
	 * @param geburtsort			Der Geburtsort.
	 * @param geschlecht			Das Geschlecht.
	 * @param hausnummer			Ggf. die Hausnummer zur Straße im Wohnort.
	 * @param hausnummerZusatz		Ggf. der Hausnummerzusatz zur Straße im Wohnort.
	 * @param nachname				Der Name.
	 * @param staatsangehoerigkeit	Die Staatsangehörigkeit.
	 * @param staatsangehoerigkeit2	Eine evtl. vorhandene zweite Staatsangehörigkeit.
	 * @param strassenname			Ggf. der Straßenname im Wohnort.
	 * @param telefonPrivat			Die private Telefonnummer.
	 * @param telefonPrivatMobil	Die private Mobilfunk-Telefonnummer.
	 * @param telefonSchule			Die schulische Telefonnummer.
	 * @param telefonSchuleMobil	Die schulische Mobilfunk-Telefonnummer.
	 * @param titel					Die Titel.
	 * @param vorname				Der Vorname (Rufname).
	 * @param vornamen				Alle Vornamen, sofern es mehrere gibt.
	 * @param wohnort				Der Wohnort.
	 * @param wohnortsteil			Ggf. der Ortsteil des Wohnortes.
	 */
	public ReportingPerson(final String anrede, final String emailPrivat, final String emailSchule, final String faxSchule, final String geburtsdatum,
			final String geburtsland, final String geburtsname, final String geburtsort, final Geschlecht geschlecht, final String hausnummer,
			final String hausnummerZusatz, final String nachname, final Nationalitaeten staatsangehoerigkeit, final Nationalitaeten staatsangehoerigkeit2,
			final String strassenname, final String telefonPrivat, final String telefonPrivatMobil, final String telefonSchule,
			final String telefonSchuleMobil, final String titel, final String vorname, final String vornamen, final OrtKatalogEintrag wohnort,
			final OrtsteilKatalogEintrag wohnortsteil) {
		super();
		this.anrede = anrede;
		this.emailPrivat = emailPrivat;
		this.emailSchule = emailSchule;
		this.faxSchule = faxSchule;
		this.geburtsdatum = geburtsdatum;
		this.geburtsland = geburtsland;
		this.geburtsname = geburtsname;
		this.geburtsort = geburtsort;
		this.geschlecht = geschlecht;
		this.hausnummer = hausnummer;
		this.hausnummerZusatz = hausnummerZusatz;
		this.nachname = nachname;
		this.staatsangehoerigkeit = staatsangehoerigkeit;
		this.staatsangehoerigkeit2 = staatsangehoerigkeit2;
		this.strassenname = strassenname;
		this.telefonPrivat = telefonPrivat;
		this.telefonPrivatMobil = telefonPrivatMobil;
		this.telefonSchule = telefonSchule;
		this.telefonSchuleMobil = telefonSchuleMobil;
		this.titel = titel;
		this.vorname = vorname;
		this.vornamen = vornamen;
		this.wohnort = wohnort;
		this.wohnortsteil = wohnortsteil;
	}


// ##### Berechnete Felder #####

	/**
	 * Erzeugt die mehrzeilige Briefanschrift im html-Format.
	 *
	 * @return Briefanschrift (html)
	 */
	public String anschrift() {
		String result;
		switch (anrede) {
			case "Frau" -> result = "Frau " + this.vornameNachname() + "<br/>";
			case "Herr" -> result = "Herrn " + this.vornameNachname() + "<br/>";
			case "Familie" -> result = "Familie" + this.nachname() + "<br/>";
			case null, default -> result = this.vornameNachname() + "<br/>";
		}
		result += !this.wohnortsteilname().isEmpty() ? ("OT " + this.wohnortsteilname() + "<br/>") : "";
		result += this.strassennameHausnummer() + "<br/>";
		result += this.plzOrt();

		return result.trim();
	}

	/**
	 * Erzeugt die mehrzeilige Briefanschrift mit allen Vornamen im html-Format.
	 *
	 * @return Briefanschrift (html)
	 */
	public String anschriftMitAllenVornamen() {
		String result;
		switch (anrede) {
			case "Frau" -> result = "Frau " + this.vornamenNachnameMitTitel() + "<br/>";
			case "Herr" -> result = "Herrn " + this.vornamenNachnameMitTitel() + "<br/>";
			case "Familie" -> result = "Familie" + this.nachname() + "<br/>";
			case null, default -> result = this.vornamenNachnameMitTitel() + "<br/>";
		}
		result += !this.wohnortsteilname().isEmpty() ? ("OT " + this.wohnortsteilname() + "<br/>") : "";
		result += this.strassennameHausnummer() + "<br/>";
		result += this.plzOrt();

		return result.trim();
	}

	/**
	 * Erzeugt die Anrede zusammen mit dem Nachnamen.
	 *
	 * @return Anrede mit Nachname.
	 */
	public String anredeNachname() {
		switch (anrede) {
			case "Frau" -> {
				return ("Frau " + this.nachnameMitTitel()).trim();
			}
			case "Herr" -> {
				return ("Herr " + this.nachnameMitTitel()).trim();
			}
			case "Familie" -> {
				return ("Familie " + this.nachname()).trim();
			}
			case null, default -> {
				switch (geschlecht) {
					case Geschlecht.W -> {
						return ("Frau " + this.nachnameMitTitel()).trim();
					}
					case Geschlecht.M -> {
						return ("Herr " + this.nachnameMitTitel()).trim();
					}
					case null, default -> {
						return this.vornameNachnameMitTitel();
					}
				}
			}
		}
	}

	/**
	 * Erzeugt die Anrede zusammen mit dem Vornamen und dem Nachnamen.
	 *
	 * @return Anrede mit Vorname und Nachname.
	 */
	public String anredeVornameNachname() {
		switch (anrede) {
			case "Frau" -> {
				return ("Frau " + this.vornameNachnameMitTitel()).trim();
			}
			case "Herr" -> {
				return ("Herr " + this.vornameNachnameMitTitel()).trim();
			}
			case "Familie" -> {
				return ("Familie " + this.nachname()).trim();
			}
			case null, default -> {
				switch (geschlecht) {
					case Geschlecht.W -> {
						return ("Frau " + this.vornameNachnameMitTitel()).trim();
					}
					case Geschlecht.M -> {
						return ("Herr " + this.vornameNachnameMitTitel()).trim();
					}
					case null, default -> {
						return this.vornameNachnameMitTitel();
					}
				}
			}
		}
	}

	/**
	 * Erzeugt die Anrede zusammen mit den Vornamen und dem Nachnamen.
	 *
	 * @return Anrede mit Vornamen und Nachname.
	 */
	public String anredeVornamenNachname() {
		switch (anrede) {
			case "Frau" -> {
				return ("Frau " + this.vornamenNachnameMitTitel()).trim();
			}
			case "Herr" -> {
				return ("Herr " + this.vornamenNachnameMitTitel()).trim();
			}
			case "Familie" -> {
				return ("Familie " + this.nachname()).trim();
			}
			case null, default -> {
				switch (geschlecht) {
					case Geschlecht.W -> {
						return ("Frau " + this.vornamenNachnameMitTitel()).trim();
					}
					case Geschlecht.M -> {
						return ("Herr " + this.vornamenNachnameMitTitel()).trim();
					}
					case null, default -> {
						return this.vornamenNachnameMitTitel();
					}
				}
			}
		}
	}

	/**
	 * Erzeugt für einen Brief die formale Anrede ("Sehr geehrte").
	 *
	 * @return Formale Anrede
	 */
	public String briefanredeFormal() {
		return (this.sehrGeehrteGeehrter() + " " + this.anredeNachname()).trim();
	}

	/**
	 * Erzeugt für einen Brief die persönliche Anrede ("Liebe").
	 *
	 * @return Persönliche Anrede
	 */
	public String briefanredePersoenlich() {
		return (this.liebeLieber() + " " + this.anredeNachname()).trim();
	}

	/**
	 * Erzeugt das Anredewort "Liebe" oder "Lieber" oder "Hallo".
	 *
	 * @return "Liebe" oder "Lieber" oder "Hallo"
	 */
	public String liebeLieber() {
		switch (anrede) {
			case "Frau", "Familie" -> {
				return "Liebe";
			}
			case "Herr" -> {
				return "Lieber";
			}
			case null, default -> {
				switch (geschlecht) {
					case Geschlecht.W -> {
						return "Liebe";
					}
					case Geschlecht.M -> {
						return "Lieber";
					}
					case null, default -> {
						return "Hallo";
					}
				}
			}
		}
	}

	/**
	 * Erzeugt den Nachnamen mit Titel, Titel zuerst.
	 *
	 * @return Titel und Nachname.
	 */
	public String nachnameMitTitel() {
		return ((!this.titel().isEmpty() ? this.titel() + " " : "") + this.nachname()).trim();
	}

	/**
	 * Erzeugt den vollständigen Namen, Nachname zuerst.
	 *
	 * @return Vollständiger Name.
	 */
	public String nachnameVorname() {
		return (this.nachname() + ", " + this.vorname()).trim();
	}

	/**
	 * Erzeugt den vollständigen Namen mit allen Vornamen, Nachname zuerst.
	 *
	 * @return Vollständiger Name.
	 */
	public String nachnameVornamen() {
		return (this.nachname() + ", " + (this.vornamen().isEmpty() ? this.vorname() : this.vornamen())).trim();
	}

	/**
	 * Erzeugt den vollständigen Namen mit Titel, Nachname zuerst
	 *
	 * @return Vollständiger Name.
	 */
	public String nachnameVornameMitTitel() {
		return (this.nachname() + ", " + this.vorname() + (!this.titel().isEmpty() ? ", " + this.titel() : "")).trim();
	}

	/**
	 * Erzeugt den vollständigen Namen mit allen Vornamen und Titel, Nachname zuerst
	 *
	 * @return Vollständiger Name.
	 */
	public String nachnameVornamenMitTitel() {
		return (this.nachname() + ", " + (this.vornamen().isEmpty() ? this.vorname() : this.vornamen()) + (!this.titel().isEmpty() ? ", " + this.titel() : ""))
				.trim();
	}

	/**
	 * Erzeugt die Angabe der Postleitzahl.
	 *
	 * @return Postleitzahl
	 */
	public String plz() {
		if ((this.wohnort() == null))
			return "";

		return this.wohnort().plz;
	}

	/**
	 * Erzeugt die Angabe von Postleitzahl und Wohnort.
	 *
	 * @return Postleitzahl und Wohnort
	 */
	public String plzOrt() {
		if ((this.wohnort() == null))
			return "";

		String result = this.wohnort().plz;
		result += " " + this.wohnort().ortsname;

		return result.trim();
	}

	/**
	 * Erzeugt das Anredewort "Sehr geehrte" oder "Sehr geehrter" oder "Guten Tag"
	 *
	 * @return "Sehr geehrte" oder "Sehr geehrter" oder "Guten Tag"
	 */
	public String sehrGeehrteGeehrter() {
		switch (anrede) {
			case "Frau", "Familie" -> {
				return "Sehr geehrte";
			}
			case "Herr" -> {
				return "Sehr geehrter";
			}
			case null, default -> {
				switch (geschlecht) {
					case Geschlecht.W -> {
						return "Sehr geehrte";
					}
					case Geschlecht.M -> {
						return "Sehr geehrter";
					}
					case null, default -> {
						return "Guten Tag";
					}
				}
			}
		}
	}

	/**
	 * Erzeugt die Angabe von Straße und Hausnummer.
	 *
	 * @return Straße und Hausnummer
	 */
	public String strassennameHausnummer() {
		if (this.strassenname().isEmpty())
			return "";

		String result = this.strassenname();
		result += !this.hausnummer().isEmpty() ? (" " + this.hausnummer()) : "";
		result += (!this.hausnummer().isEmpty() && !this.hausnummerZusatz().isEmpty()) ? (" " + this.hausnummerZusatz()) : "";

		return result.trim();
	}

	/**
	 * Erzeugt den vollständigen Namen, Vorname zuerst,
	 *
	 * @return Vollständiger Name.
	 */
	public String vornameNachname() {
		return (this.vorname() + " " + this.nachname()).trim();
	}

	/**
	 * Erzeugt den vollständigen Namen, Vornamen zuerst,
	 *
	 * @return Vollständiger Name.
	 */
	public String vornamenNachname() {
		return ((this.vornamen().isEmpty() ? this.vorname() : this.vornamen()) + " " + this.nachname()).trim();
	}

	/**
	 * Erzeugt den vollständigen Namen mit Titel, Vorname zuerst.
	 *
	 * @return Vollständiger Name.
	 */
	public String vornameNachnameMitTitel() {
		return ((!this.titel().isEmpty() ? this.titel() + " " : "") + this.vorname() + " " + this.nachname()).trim();
	}

	/**
	 * Erzeugt den vollständigen Namen mit Titel, Vornamen zuerst.
	 *
	 * @return Vollständiger Name.
	 */
	public String vornamenNachnameMitTitel() {
		return ((!this.titel().isEmpty() ? this.titel() + " " : "")
				+ (this.vornamen().isEmpty() ? this.vorname() : this.vornamen()) + " " + this.nachname()).trim();
	}

	/**
	 * Erzeugt den Namen des Wohnorts.
	 *
	 * @return Wohnortname
	 */
	public String wohnortname() {
		if ((this.wohnort() == null))
			return "";

		return this.wohnort().ortsname;
	}

	/**
	 * Erzeugt den Namen des Wohnortsteils.
	 *
	 * @return Wohnortsteilname
	 */
	public String wohnortsteilname() {
		if ((this.wohnortsteil() == null))
			return "";

		return this.wohnortsteil().ortsteil;
	}



	// ##### Getter #####

	/**
	 * Die Anrede.
	 *
	 * @return Inhalt des Feldes anrede
	 */
	public String anrede() {
		return anrede;
	}

	/**
	 * Die private E-Mail-Adresse.
	 *
	 * @return Inhalt des Feldes emailPrivat
	 */
	public String emailPrivat() {
		return emailPrivat;
	}

	/**
	 * Die schulische E-Mail-Adresse.
	 *
	 * @return Inhalt des Feldes emailSchule
	 */
	public String emailSchule() {
		return emailSchule;
	}

	/**
	 * Die schulische Fax-Nummer.
	 *
	 * @return Inhalt des Feldes faxSchule
	 */
	public String faxSchule() {
		return faxSchule;
	}

	/**
	 * Das Geburtsdatum.
	 *
	 * @return Inhalt des Feldes geburtsdatum
	 */
	public String geburtsdatum() {
		return geburtsdatum;
	}

	/**
	 * Das Geburtsland.
	 *
	 * @return Inhalt des Feldes geburtsland
	 */
	public String geburtsland() {
		return geburtsland;
	}

	/**
	 * Der Geburtsname.
	 *
	 * @return Inhalt des Feldes geburtsname
	 */
	public String geburtsname() {
		return geburtsname;
	}

	/**
	 * Der Geburtsort.
	 *
	 * @return Inhalt des Feldes geburtsort
	 */
	public String geburtsort() {
		return geburtsort;
	}

	/**
	 * Das Geschlecht.
	 *
	 * @return Inhalt des Feldes geschlecht
	 */
	public Geschlecht geschlecht() {
		return geschlecht;
	}

	/**
	 * Das Geschlecht als Kürzel in Klammern.
	 *
	 * @return Inhalt des Feldes geschlecht als druckbares Kürzel.
	 */
	public String geschlechtDruckKuerzelInKlammern() {
		switch (geschlecht) {
			case Geschlecht.M -> {
				return "(" + Geschlecht.M.kuerzel + ")";
			}
			case Geschlecht.W -> {
				return "(" + Geschlecht.W.kuerzel + ")";
			}
			case Geschlecht.D -> {
				return "(" + Geschlecht.D.kuerzel + ")";
			}
			case Geschlecht.X -> {
				return "(-)";
			}
			case null, default -> {
				return "";
			}
		}
	}

	/**
	 * Das Geschlecht als Symbol.
	 *
	 * @return Inhalt des Feldes geschlecht als druckbares Symbol.
	 */
	public String geschlechtDruckSymbolHtml() {
		switch (geschlecht) {
			case Geschlecht.M -> {
				return "&#9792;";
			}
			case Geschlecht.W -> {
				return "&#9794;";
			}
			case Geschlecht.D -> {
				return Geschlecht.D.kuerzel;
			}
			case Geschlecht.X -> {
				return "-";
			}
			case null, default -> {
				return "";
			}
		}
	}

	/**
	 * Das Geschlecht als Symbol in Klammern.
	 *
	 * @return Inhalt des Feldes geschlecht als druckbares Symbol in Klammern.
	 */
	public String geschlechtDruckSymbolInKlammernHtml() {
		return "(" + this.geschlechtDruckSymbolHtml() + ")";
	}

	/**
	 * Ggf. die Hausnummer zur Straße im Wohnort.
	 *
	 * @return Inhalt des Feldes hausnummer
	 */
	public String hausnummer() {
		return hausnummer;
	}

	/**
	 * Ggf. der Hausnummerzusatz zur Straße im Wohnort.
	 *
	 * @return Inhalt des Feldes hausnummerZusatz
	 */
	public String hausnummerZusatz() {
		return hausnummerZusatz;
	}

	/**
	 * Der Name.
	 *
	 * @return Inhalt des Feldes nachname
	 */
	public String nachname() {
		return nachname;
	}

	/**
	 * Eine evtl. vorhandene zweite Staatsangehörigkeit.
	 *
	 * @return Inhalt des Feldes staatsangehoerigkeit2
	 */
	public Nationalitaeten staatsangehoerigkeit2() {
		return staatsangehoerigkeit2;
	}

	/**
	 * Die Staatsangehörigkeit.
	 *
	 * @return Inhalt des Feldes staatsangehoerigkeit
	 */
	public Nationalitaeten staatsangehoerigkeit() {
		return staatsangehoerigkeit;
	}

	/**
	 * Ggf. der Straßenname im Wohnort.
	 *
	 * @return Inhalt des Feldes strassenname
	 */
	public String strassenname() {
		return strassenname;
	}

	/**
	 * Die private Telefonnummer.
	 *
	 * @return Inhalt des Feldes telefonPrivat
	 */
	public String telefonPrivat() {
		return telefonPrivat;
	}

	/**
	 * Die private Mobilfunk-Telefonnummer.
	 *
	 * @return Inhalt des Feldes telefonPrivatMobil
	 */
	public String telefonPrivatMobil() {
		return telefonPrivatMobil;
	}

	/**
	 * Die schulische Telefonnummer.
	 *
	 * @return Inhalt des Feldes telefonSchule
	 */
	public String telefonSchule() {
		return telefonSchule;
	}

	/**
	 * Die schulische Mobilifunk-Telefonnummer.
	 *
	 * @return Inhalt des Feldes telefonSchuleMobil
	 */
	public String telefonSchuleMobil() {
		return telefonSchuleMobil;
	}

	/**
	 * Die Titel.
	 *
	 * @return Inhalt des Feldes titel
	 */
	public String titel() {
		return titel;
	}

	/**
	 * Der Vorname (Rufname).
	 *
	 * @return Inhalt des Feldes vorname
	 */
	public String vorname() {
		return vorname;
	}

	/**
	 * Alle Vornamen, sofern es mehrere gibt.
	 *
	 * @return Inhalt des Feldes vornamen
	 */
	public String vornamen() {
		if (!vornamen.isEmpty())
			return vornamen;
		else
			return vorname;
	}

	/**
	 * Der Wohnort.
	 *
	 * @return Inhalt des Feldes wohnort
	 */
	public OrtKatalogEintrag wohnort() {
		return wohnort;
	}

	/**
	 * Ggf. der Ortsteil des Wohnortes.
	 *
	 * @return Inhalt des Feldes wohnortsteil
	 */
	public OrtsteilKatalogEintrag wohnortsteil() {
		return wohnortsteil;
	}
}
