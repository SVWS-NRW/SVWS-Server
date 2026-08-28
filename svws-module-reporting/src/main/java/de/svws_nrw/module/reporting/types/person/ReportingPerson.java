package de.svws_nrw.module.reporting.types.person;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.utils.ReportingBildquelle;
import de.svws_nrw.module.reporting.utils.ReportingStrings;

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

	/** Das Foto der Person im Base64-Format. Gepflegt wird es allein für Schüler und Lehrkräfte. */
	protected String foto;

	/** Die Bildquelle des Fotos, beim ersten Zugriff aus den Base64-Daten abgeleitet. */
	protected String fotoHtmlSource;

	/** Gibt an, ob das Foto vorliegt. */
	private boolean fotoGeladen;

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
	 * @param foto					Das Foto der Person im Base64-Format.
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
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ReportingPerson(final String anrede, final String emailPrivat, final String emailSchule, final String faxSchule, final String foto, final String geburtsdatum,
			final String geburtsland, final String geburtsname, final String geburtsort, final Geschlecht geschlecht, final String hausnummer,
			final String hausnummerZusatz, final String nachname, final Nationalitaeten staatsangehoerigkeit, final Nationalitaeten staatsangehoerigkeit2,
			final String strassenname, final String telefonPrivat, final String telefonPrivatMobil, final String telefonSchule,
			final String telefonSchuleMobil, final String titel, final String vorname, final String vornamen, final OrtKatalogEintrag wohnort,
			final OrtsteilKatalogEintrag wohnortsteil) {
		super();
		this.anrede = ersetzeNullBlankTrim(anrede);
		this.emailPrivat = ersetzeNullBlankTrim(emailPrivat);
		this.emailSchule = ersetzeNullBlankTrim(emailSchule);
		this.faxSchule = ersetzeNullBlankTrim(faxSchule);
		this.foto = ersetzeNullBlankTrim(foto);
		this.fotoGeladen = (foto != null);
		this.geburtsdatum = ersetzeNullBlankTrim(geburtsdatum);
		this.geburtsland = ersetzeNullBlankTrim(geburtsland);
		this.geburtsname = ersetzeNullBlankTrim(geburtsname);
		this.geburtsort = ersetzeNullBlankTrim(geburtsort);
		this.geschlecht = geschlecht;
		this.hausnummer = ersetzeNullBlankTrim(hausnummer);
		this.hausnummerZusatz = ersetzeNullBlankTrim(hausnummerZusatz);
		this.nachname = ersetzeNullBlankTrim(nachname);
		this.staatsangehoerigkeit = staatsangehoerigkeit;
		this.staatsangehoerigkeit2 = staatsangehoerigkeit2;
		this.strassenname = ersetzeNullBlankTrim(strassenname);
		this.telefonPrivat = ersetzeNullBlankTrim(telefonPrivat);
		this.telefonPrivatMobil = ersetzeNullBlankTrim(telefonPrivatMobil);
		this.telefonSchule = ersetzeNullBlankTrim(telefonSchule);
		this.telefonSchuleMobil = ersetzeNullBlankTrim(telefonSchuleMobil);
		this.titel = ersetzeNullBlankTrim(titel);
		this.vorname = ersetzeNullBlankTrim(vorname);
		this.vornamen = ersetzeNullBlankTrim(vornamen);
		this.wohnort = wohnort;
		this.wohnortsteil = wohnortsteil;
	}


// ##### Berechnete Felder #####

	/**
	 * Erzeugt die mehrzeilige Adresse im html-Format mit Ortsteil, Straße, Hausnummer, Postleitzahl und Ort.
	 *
	 * @return Adresse (html)
	 */
	public String adresse() {
		return (!this.wohnortsteilname().isEmpty() ? ("OT " + this.wohnortsteilname() + ReportingStrings.BR) : "")
				+ this.strassennameHausnummer() + ReportingStrings.BR
				+ this.plzOrt();
	}

	/**
	 * Erzeugt die mehrzeilige Anschrift für eine Briefanschrift im html-Format.
	 *
	 * @return Briefanschrift (html)
	 */
	public String anschriftNachname() {
		return anredeAnschriftNachname() + ReportingStrings.BR + adresse();
	}

	/**
	 * Erzeugt die mehrzeilige Anschrift für eine Briefanschrift im html-Format.
	 *
	 * @return Briefanschrift (html)
	 */
	public String anschriftVornameNachname() {
		return anredeAnschriftVornameNachname() + ReportingStrings.BR + adresse();
	}

	/**
	 * Erzeugt die mehrzeilige Anschrift für eine Briefanschrift mit allen Vornamen im html-Format.
	 *
	 * @return Briefanschrift (html)
	 */
	public String anschriftVornamenNachname() {
		return anredeAnschriftVornamenNachname() + ReportingStrings.BR + adresse();
	}

	/**
	 * Erzeugt die Anrede für eine Anschrift mit dem Nachnamen (mit 'Herrn' statt 'Herr').
	 *
	 * @return Anrede mit Nachname.
	 */
	public String anredeAnschriftNachname() {
		return erstelleAnrede(this.nachnameMitTitel(), this.vornameNachnameMitTitel(), this.nachname(), true);
	}

	/**
	 * Erzeugt die Anrede für eine Anschrift mit einem Vornamen und dem Nachnamen (mit 'Herrn' statt 'Herr').
	 *
	 * @return Anrede mit Vorname und Nachname.
	 */
	public String anredeAnschriftVornameNachname() {
		return erstelleAnrede(this.vornameNachnameMitTitel(), this.vornameNachnameMitTitel(), this.nachname(), true);
	}

	/**
	 * Erzeugt die Anrede für eine Anschrift mit allen Vornamen und dem Nachnamen (mit 'Herrn' statt 'Herr').
	 *
	 * @return Anrede mit Vornamen und Nachname.
	 */
	public String anredeAnschriftVornamenNachname() {
		return erstelleAnrede(this.vornamenNachnameMitTitel(), this.vornamenNachnameMitTitel(), this.nachname(), true);
	}


	/**
	 * Erzeugt die Anrede zusammen mit dem Nachnamen.
	 *
	 * @return Anrede mit Nachname.
	 */
	public String anredeNachname() {
		return erstelleAnrede(this.nachnameMitTitel(), this.vornameNachnameMitTitel(), this.nachname(), false);
	}

	/**
	 * Erzeugt die Anrede zusammen mit dem Vornamen und dem Nachnamen.
	 *
	 * @return Anrede mit Vorname und Nachname.
	 */
	public String anredeVornameNachname() {
		return erstelleAnrede(this.vornameNachnameMitTitel(), this.vornameNachnameMitTitel(), this.nachname(), false);
	}

	/**
	 * Erzeugt die Anrede zusammen mit den Vornamen und dem Nachnamen.
	 *
	 * @return Anrede mit Vornamen und Nachname.
	 */
	public String anredeVornamenNachname() {
		return erstelleAnrede(this.vornamenNachnameMitTitel(), this.vornamenNachnameMitTitel(), this.nachname(), false);
	}

	/**
	 * Erzeugt die Anrede anhand der eingetragenen Anrede oder des Geschlechts mit den übergebenen Namen.
	 *
	 * @param nameWennHerrOderFrau Text für Frau/Herr bzw. die geschlechtsabhängige Standard-Anrede.
	 * @param nameWennOhneAnrede   Text für die Standard-Anrede ohne spezifische Anrede.
	 * @param nameWennFamilie	   Text für Familie.
	 * @param istFuerAnschrift     Legt fest, ob die Anrede für das Anschriftfeld eines Briefes erstellt werden soll ('Herrn' statt 'Herr').
	 *
	 * @return Erzeugte Anrede.
	 */
	private String erstelleAnrede(final String nameWennHerrOderFrau, final String nameWennOhneAnrede, final String nameWennFamilie,
			final boolean istFuerAnschrift) {
		return switch (anrede) {
			case ReportingStrings.FRAU -> (ReportingStrings.FRAU_SPACE + nameWennHerrOderFrau).trim();
			case ReportingStrings.HERR -> ((istFuerAnschrift ? ReportingStrings.HERRN_SPACE : ReportingStrings.HERR_SPACE) + nameWennHerrOderFrau).trim();
			case ReportingStrings.FAMILIE -> (ReportingStrings.FAMILIE_SPACE + nameWennFamilie).trim();
			case null, default -> switch (geschlecht) {
				case Geschlecht.W -> (ReportingStrings.FRAU_SPACE + nameWennOhneAnrede).trim();
				case Geschlecht.M -> ((istFuerAnschrift ? ReportingStrings.HERRN_SPACE : ReportingStrings.HERR_SPACE) + nameWennOhneAnrede).trim();
				case null, default -> nameWennOhneAnrede.trim();
			};
		};
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
		return switch (anrede) {
			case ReportingStrings.FRAU, ReportingStrings.FAMILIE -> "Liebe";
			case ReportingStrings.HERR -> "Lieber";
			case null, default -> switch (geschlecht) {
				case Geschlecht.W -> "Liebe";
				case Geschlecht.M -> "Lieber";
				case null, default -> "Hallo";
			};
		};
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
		if ((this.wohnort() == null)) {
			return "";
		}

		return this.wohnort().plz;
	}

	/**
	 * Erzeugt die Angabe von Postleitzahl und Wohnort.
	 *
	 * @return Postleitzahl und Wohnort
	 */
	public String plzOrt() {
		if ((this.wohnort() == null)) {
			return "";
		}

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
		return switch (anrede) {
			case ReportingStrings.FRAU, ReportingStrings.FAMILIE -> "Sehr geehrte";
			case ReportingStrings.HERR -> "Sehr geehrter";
			case null, default -> switch (geschlecht) {
				case Geschlecht.W -> "Sehr geehrte";
				case Geschlecht.M -> "Sehr geehrter";
				case null, default -> "Guten Tag";
			};
		};
	}

	/**
	 * Erzeugt die Angabe von Straße und Hausnummer.
	 *
	 * @return Straße und Hausnummer
	 */
	public String strassennameHausnummer() {
		if (this.strassenname().isEmpty()) {
			return "";
		}

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
		if ((this.wohnort() == null)) {
			return "";
		}

		return this.wohnort().ortsname;
	}

	/**
	 * Erzeugt den Namen des Wohnortsteils.
	 *
	 * @return Wohnortsteilname
	 */
	public String wohnortsteilname() {
		if ((this.wohnortsteil() == null)) {
			return "";
		}

		return this.wohnortsteil().ortsteil;
	}



	// ##### Getter #####

	/**
	 * Die Anrede.
	 *
	 * @return Inhalt des Feldes anrede; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String anrede() {
		return anrede;
	}

	/**
	 * Die private E-Mail-Adresse.
	 *
	 * @return Inhalt des Feldes emailPrivat; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String emailPrivat() {
		return emailPrivat;
	}

	/**
	 * Die schulische E-Mail-Adresse.
	 *
	 * @return Inhalt des Feldes emailSchule; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String emailSchule() {
		return emailSchule;
	}

	/**
	 * Die schulische Fax-Nummer.
	 *
	 * @return Inhalt des Feldes faxSchule; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String faxSchule() {
		return faxSchule;
	}

	/**
	 * Das Foto der Person im Base64-Format. Liegt es noch nicht vor, wird es einmalig über {@link #ladeFoto()} nachgefordert; jeder Zugriff auf das Foto
	 * muss deshalb über diesen Getter laufen und nicht über das Feld.
	 *
	 * @return Inhalt des Feldes foto; nie {@code null}, bei fehlendem Foto ein leerer String.
	 */
	public String foto() {
		if (!fotoGeladen) {
			foto = ersetzeNullBlankTrim(ladeFoto());
			fotoGeladen = true;
		}
		return foto;
	}

	/**
	 * Fordert das Foto aus der Datenquelle an. Die Basisklasse kennt keine und behält, was sie beim Erzeugen bekommen hat; die Proxy-Klassen holen es hier
	 * aus ihrem Repository.
	 *
	 * @return Das Foto im Base64-Format oder {@code null}, wenn keines vorliegt. Der Getter normalisiert das Ergebnis, so dass nach außen wie beim
	 *         Konstruktor ein leerer String steht.
	 */
	protected String ladeFoto() {
		return foto;
	}

	/**
	 * Die Bildquelle des Fotos als Data-URL inklusive MIME-Type. Den Typ bestimmt {@link ReportingBildquelle} aus den Bilddaten.
	 * Die Zeichenkette entsteht erst beim ersten Zugriff: Personendaten werden auch für Ausgaben geladen, die keine Fotos zeigen.
	 *
	 * @return Die Bildquelle oder ein leerer String, wenn kein oder ein nicht darstellbares Foto vorliegt.
	 */
	public String fotoHtmlSource() {
		if (fotoHtmlSource == null) {
			fotoHtmlSource = ReportingBildquelle.ausBase64(foto());
		}
		return fotoHtmlSource;
	}

	/**
	 * Das Geburtsdatum.
	 *
	 * @return Inhalt des Feldes geburtsdatum; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String geburtsdatum() {
		return geburtsdatum;
	}

	/**
	 * Das Geburtsland.
	 *
	 * @return Inhalt des Feldes geburtsland; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String geburtsland() {
		return geburtsland;
	}

	/**
	 * Der Geburtsname.
	 *
	 * @return Inhalt des Feldes geburtsname; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String geburtsname() {
		return geburtsname;
	}

	/**
	 * Der Geburtsort.
	 *
	 * @return Inhalt des Feldes geburtsort; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String geburtsort() {
		return geburtsort;
	}

	/**
	 * Das Geschlecht.
	 *
	 * @return Inhalt des Feldes geschlecht; kann {@code null} sein, wenn kein Geschlecht zugeordnet ist.
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
		return switch (geschlecht) {
			case Geschlecht.M -> "(" + Geschlecht.M.kuerzel + ")";
			case Geschlecht.W -> "(" + Geschlecht.W.kuerzel + ")";
			case Geschlecht.D -> "(" + Geschlecht.D.kuerzel + ")";
			case Geschlecht.X -> "(-)";
			case null, default -> "";
		};
	}

	/**
	 * Das Geschlecht als Symbol.
	 *
	 * @return Inhalt des Feldes geschlecht als druckbares Symbol.
	 */
	public String geschlechtDruckSymbolHtml() {
		return switch (geschlecht) {
			case Geschlecht.M -> "&#9792;";
			case Geschlecht.W -> "&#9794;";
			case Geschlecht.D -> Geschlecht.D.kuerzel;
			case Geschlecht.X -> "-";
			case null -> "";
		};
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
	 * @return Inhalt des Feldes hausnummer; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String hausnummer() {
		return hausnummer;
	}

	/**
	 * Ggf. der Hausnummerzusatz zur Straße im Wohnort.
	 *
	 * @return Inhalt des Feldes hausnummerZusatz; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String hausnummerZusatz() {
		return hausnummerZusatz;
	}

	/**
	 * Der Name.
	 *
	 * @return Inhalt des Feldes nachname; nie {@code null}, bei fehlendem Wert ein leerer String.
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
	 * @return Inhalt des Feldes strassenname; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String strassenname() {
		return strassenname;
	}

	/**
	 * Die private Telefonnummer.
	 *
	 * @return Inhalt des Feldes telefonPrivat; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String telefonPrivat() {
		return telefonPrivat;
	}

	/**
	 * Die private Mobilfunk-Telefonnummer.
	 *
	 * @return Inhalt des Feldes telefonPrivatMobil; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String telefonPrivatMobil() {
		return telefonPrivatMobil;
	}

	/**
	 * Die schulische Telefonnummer.
	 *
	 * @return Inhalt des Feldes telefonSchule; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String telefonSchule() {
		return telefonSchule;
	}

	/**
	 * Die schulische Mobilifunk-Telefonnummer.
	 *
	 * @return Inhalt des Feldes telefonSchuleMobil; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String telefonSchuleMobil() {
		return telefonSchuleMobil;
	}

	/**
	 * Die Titel.
	 *
	 * @return Inhalt des Feldes titel; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String titel() {
		return titel;
	}

	/**
	 * Der Vorname (Rufname).
	 *
	 * @return Inhalt des Feldes vorname; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String vorname() {
		return vorname;
	}

	/**
	 * Alle Vornamen, sofern es mehrere gibt.
	 *
	 * @return Inhalt des Feldes vornamen; nie {@code null}, bei fehlendem Wert ein leerer String. Sind keine weiteren Vornamen vorhanden, wird der Rufname zurückgegeben.
	 */
	public String vornamen() {
		if (!vornamen.isEmpty()) {
			return vornamen;
		} else {
			return vorname;
		}
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
