package de.svws_nrw.module.reporting.types.schule;

import java.util.List;

import de.svws_nrw.module.reporting.types.ReportingBaseType;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Schule.
 */
public class ReportingSchule extends ReportingBaseType {

	/** Der aktuelle Abschnitt des Schuljahres der Schule. */
	protected ReportingSchuljahresabschnitt aktuellerSchuljahresabschnitt;

	/** Die Anzahl der Jahrgangsstufen pro Jahr. */
	protected long anzahlJahrgangsstufenProJahr;

	/** Die Anzahl der Abschnitte pro Jahr */
	protected long anzahlSchuljahresabschnitteProJahr;

	/** Der über die API als ausgewählter Schuljahresabschnitt deklarierter Abschnitt der Schule. */
	protected ReportingSchuljahresabschnitt auswahlSchuljahresabschnitt;

	/** Der erste Teil der Bezeichnung der Schule */
	protected String bezeichnung1;

	/** Der zweite Teil der Bezeichnung der Schule */
	protected String bezeichnung2;

	/** Der dritte Teil der Bezeichnung der Schule */
	protected String bezeichnung3;

	/** Die allgemeine Bezeichnung der Abschnitte (z.B. Quartal oder Halbjahr) */
	protected String bezeichnungSchuljahresabschnitt;

	/** Eine Liste der einzelnen speziellen Bezeichnungen für die Abschnitte (z.B. 1. Halbjahr, 2. Halbjahr) */
	protected List<String> bezeichnungenSchuljahresabschnitte;

	/** Die Dauer einer Unterrichtseinheit in Minuten. */
	protected long dauerUnterrichtseinheit;

	/** Die Mailadresse der Schule. */
	protected String email;

	/** Die Faxnummer der Schule. */
	protected String fax;

	/** Die Hausnummer zur Straße in der die Schule liegt. */
	protected String hausnummer;

	/** Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt. */
	protected String hausnummerZusatz;

	/** Der Ort in dem die Schule liegt. */
	protected String ort;

	/** Die Postleitzahl des Gebietes in dem die Schule liegt. */
	protected String plz;

	/** Die Liste der Schuljahresabschnitte, welche an der Schule definiert sind. */
	protected List<ReportingSchuljahresabschnitt> schuljahresabschnitte;

	/** Die Schulform der Schule */
	protected String schulform;

	/** Das Logo der Schule im Base64-Format. */
	protected String schullogo;

	/** Die eindeutige Schulnummer der Schule */
	protected long schulnummer;

	/** Der Straßenname der Straße in der die Schule liegt. */
	protected String strassenname;

	/** Die Telefonnummer der Schule. */
	protected String telefon;

	/** Die Adresse der Homepage der Schule (Domain-Name) */
	protected String webAdresse;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param aktuellerSchuljahresabschnitt Der aktuelle Abschnitt des Schuljahres der Schule.
	 * @param anzahlJahrgangsstufenProJahr Die Anzahl der Jahrgangsstufen pro Jahr.
	 * @param anzahlSchuljahresabschnitteProJahr Die Anzahl der Abschnitte pro Jahr
	 * @param auswahlSchuljahresabschnitt Der über die API als ausgewählter Schuljahresabschnitt deklarierter Abschnitt der Schule.
	 * @param bezeichnung1 Der erste Teil der Bezeichnung der Schule
	 * @param bezeichnung2 Der zweite Teil der Bezeichnung der Schule
	 * @param bezeichnung3 Der dritte Teil der Bezeichnung der Schule
	 * @param bezeichnungSchuljahresabschnitt Die allgemeine Bezeichnung der Abschnitte (z.B. Quartal oder Halbjahr)
	 * @param bezeichnungenSchuljahresabschnitte Eine Liste der einzelnen speziellen Bezeichnungen für die Abschnitte (z.B. 1. Halbjahr, 2. Halbjahr)
	 * @param dauerUnterrichtseinheit Die Dauer einer Unterrichtseinheit in Minuten.
	 * @param email Die Mailadresse der Schule.
	 * @param fax Die Faxnummer der Schule.
	 * @param hausnummer Die Hausnummer zur Straße in der die Schule liegt.
	 * @param hausnummerZusatz Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt.
	 * @param ort Der Ort in dem die Schule liegt.
	 * @param plz Die Postleitzahl des Gebietes in dem die Schule liegt.
	 * @param schuljahresabschnitte Die Liste der Schuljahresabschnitte, welche an der Schule definiert sind.
	 * @param schulform Die Schulform der Schule
	 * @param schullogo Das Logo der Schule im Base64-Format
	 * @param schulnummer Die eindeutige Schulnummer der Schule
	 * @param strassenname Der Straßenname der Straße in der die Schule liegt.
	 * @param telefon Die Telefonnummer der Schule.
	 * @param webAdresse Die Adresse der Homepage der Schule (Domain-Name)
	 */
	public ReportingSchule(final ReportingSchuljahresabschnitt aktuellerSchuljahresabschnitt, final long anzahlJahrgangsstufenProJahr,
			final long anzahlSchuljahresabschnitteProJahr, final ReportingSchuljahresabschnitt auswahlSchuljahresabschnitt, final String bezeichnung1,
			final String bezeichnung2, final String bezeichnung3,
			final String bezeichnungSchuljahresabschnitt, final List<String> bezeichnungenSchuljahresabschnitte, final long dauerUnterrichtseinheit,
			final String email, final String fax, final String hausnummer, final String hausnummerZusatz, final String ort, final String plz,
			final List<ReportingSchuljahresabschnitt> schuljahresabschnitte, final String schulform, final String schullogo, final long schulnummer,
			final String strassenname, final String telefon, final String webAdresse) {
		this.aktuellerSchuljahresabschnitt = aktuellerSchuljahresabschnitt;
		this.anzahlJahrgangsstufenProJahr = anzahlJahrgangsstufenProJahr;
		this.anzahlSchuljahresabschnitteProJahr = anzahlSchuljahresabschnitteProJahr;
		this.auswahlSchuljahresabschnitt = aktuellerSchuljahresabschnitt;
		this.bezeichnung1 = bezeichnung1;
		this.bezeichnung2 = bezeichnung2;
		this.bezeichnung3 = bezeichnung3;
		this.bezeichnungSchuljahresabschnitt = bezeichnungSchuljahresabschnitt;
		this.bezeichnungenSchuljahresabschnitte = bezeichnungenSchuljahresabschnitte;
		this.dauerUnterrichtseinheit = dauerUnterrichtseinheit;
		this.email = email;
		this.fax = fax;
		this.hausnummer = hausnummer;
		this.hausnummerZusatz = hausnummerZusatz;
		this.ort = ort;
		this.plz = plz;
		this.schuljahresabschnitte = schuljahresabschnitte;
		this.schulform = schulform;
		this.schullogo = schullogo;
		this.schulnummer = schulnummer;
		this.strassenname = strassenname;
		this.telefon = telefon;
		this.webAdresse = webAdresse;
	}



	// ##### Berechnete Felder #####

	/**
	 * Gibt das aktuelle Schuljahr der Schule in Textdarstellung der Form 2023/24 zurück
	 *
	 * @return Das aktuelle Schuljahr der Schule in Textdarstellung
	 */
	public String aktuellesSchuljahrText() {
		return "%d/%d".formatted(this.aktuellerSchuljahresabschnitt.schuljahr(), (this.aktuellerSchuljahresabschnitt.schuljahr() % 100) + 1);
	}

	/**
	 * Gibt das aktuelle Schuljahr der Schule mit aktuellem Abschnitt in Textdarstellung der Form 2023/24.1 zurück.
	 *
	 * @return Das aktuelle Schuljahr der Schule mit aktuellem Abschnitt
	 */
	public String aktuellesSchuljahrUndAbschnittText() {
		return aktuellesSchuljahrText() + "." + this.aktuellerSchuljahresabschnitt.abschnitt();
	}

	/**
	 * Stellt die Anschrift der Schule als ein mehrzeiliges Feld zur Verfügung.
	 *
	 * @return Anschrift der Schule
	 */
	public String anschrift() {
		return String.format("%s%s%s".formatted(
				((bezeichnung1 != null) && !bezeichnung1.isEmpty()) ? bezeichnung1.trim() : "",
				"%n" + this.strassennameHausnummer(),
				"%n" + this.plzOrt()));
	}

	/**
	 * Stellt die Anschrift der Schule als ein mehrzeiliges Feld im html-Format zur Verfügung.
	 *
	 * @return Anschrift der Schule im html-Format
	 */
	public String anschriftHtml() {
		return String.format("%s%s%s".formatted(
				((bezeichnung1 != null) && !bezeichnung1.isEmpty()) ? bezeichnung1.trim() : "",
				"<br/>" + this.strassennameHausnummer(),
				"<br/>" + this.plzOrt()));
	}

	/**
	 * Stellt die Anschrift mit vollständiger Bezeichnung der Schule als ein mehrzeiliges Feld zur Verfügung.
	 *
	 * @return Anschrift der Schule
	 */
	public String anschriftVollstaendig() {
		return String.format("%s%s%s".formatted(
				this.bezeichnungSchuleMehrzeilig(),
				"%n" + this.strassennameHausnummer(),
				"%n" + this.plzOrt()));
	}

	/**
	 * Stellt die Anschrift mit vollständiger Bezeichnung der Schule als ein mehrzeiliges Feld im html-Format zur Verfügung.
	 *
	 * @return Anschrift der Schule im html-Format
	 */
	public String anschriftVollstaendigHtml() {
		return String.format("%s%s%s".formatted(
				this.bezeichnungSchuleMehrzeiligHtml(),
				"<br/>" + this.strassennameHausnummer(),
				"<br/>" + this.plzOrt()));
	}

	/**
	 * Stellt die drei einzelnen Bezeichnungsfelder der Schule als ein mehrzeiliges Feld zur Verfügung.
	 *
	 * @return Mehrzeiliges Bezeichnungsfeld
	 */
	public String bezeichnungSchuleMehrzeilig() {
		return String.format("%s%s%s".formatted(
				((bezeichnung1 != null) && !bezeichnung1.isEmpty()) ? bezeichnung1.trim() : "",
				((bezeichnung2 != null) && !bezeichnung2.isEmpty()) ? ("%n" + bezeichnung2.trim()) : "",
				((bezeichnung3 != null) && !bezeichnung3.isEmpty()) ? ("%n" + bezeichnung3.trim()) : ""));
	}

	/**
	 * Stellt die drei einzelnen Bezeichnungsfelder der Schule als ein mehrzeiliges Feld im html-Format zur Verfügung.
	 *
	 * @return Mehrzeiliges Bezeichnungsfeld im html-Format
	 */
	public String bezeichnungSchuleMehrzeiligHtml() {
		return "%s%s%s".formatted(
				((bezeichnung1 != null) && !bezeichnung1.isEmpty()) ? bezeichnung1.trim() : "",
				((bezeichnung2 != null) && !bezeichnung2.isEmpty()) ? ("<br/>" + bezeichnung2.trim()) : "",
				((bezeichnung3 != null) && !bezeichnung3.isEmpty()) ? ("<br/>" + bezeichnung3.trim()) : "");
	}

	/**
	 * Erzeugt die Angabe von Postleitzahl und Wohnort.
	 *
	 * @return Postleitzahl und Wohnort
	 */
	public String plzOrt() {
		if ((this.plz == null))
			return "";

		String result = this.plz;
		result += " " + this.ort;

		return result.trim();
	}

	/**
	 * Stellt die Rücksendeinformation im Fensterumschlag der Schule zur Verfügung.
	 *
	 * @return Rücksendeinformation
	 */
	public String ruecksendeinformation() {
		return String.format("%s%s%s%s%s".formatted(
				((bezeichnung1 != null) && !bezeichnung1.isEmpty()) ? bezeichnung1.trim() : "",
				(((bezeichnung1 != null) && !bezeichnung1.isEmpty()) && (!this.strassennameHausnummer().isEmpty() || !this.plzOrt().isEmpty())) ? " - " : "",
				this.strassennameHausnummer(),
				(!this.strassennameHausnummer().isEmpty() && !this.plzOrt().isEmpty()) ? " - " : "",
				this.plzOrt()));
	}

	/**
	 * Erzeugt die Angabe von Straße und Hausnummer.
	 *
	 * @return Straße und Hausnummer
	 */
	public String strassennameHausnummer() {
		if (this.strassenname.isEmpty())
			return "";

		String result = this.strassenname;
		result += !this.hausnummer.isEmpty() ? (" " + this.hausnummer) : "";
		result += (!this.hausnummer.isEmpty() && !this.hausnummerZusatz.isEmpty()) ? (" " + this.hausnummerZusatz) : "";

		return result.trim();
	}


	// ##### Getter #####

	/**
	 * Der aktuelle Abschnitt des Schuljahres der Schule.
	 *
	 * @return Inhalt des Feldes aktuellerSchuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt aktuellerSchuljahresabschnitt() {
		return aktuellerSchuljahresabschnitt;
	}

	/**
	 * Die Anzahl der Jahrgangsstufen pro Jahr.
	 *
	 * @return Inhalt des Feldes anzahlJahrgangsstufenProJahr
	 */
	public long anzahlJahrgangsstufenProJahr() {
		return anzahlJahrgangsstufenProJahr;
	}

	/**
	 * Die Anzahl der Abschnitte pro Jahr
	 *
	 * @return Inhalt des Feldes anzahlSchuljahresabschnitteProJahr
	 */
	public long anzahlSchuljahresabschnitteProJahr() {
		return anzahlSchuljahresabschnitteProJahr;
	}

	/**
	 * Der über die API als ausgewählter Schuljahresabschnitt deklarierter Abschnitt der Schule.
	 *
	 * @return Inhalt des Feldes auswahlSchuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt auswahlSchuljahresabschnitt() {
		return auswahlSchuljahresabschnitt;
	}

	/**
	 * Der erste Teil der Bezeichnung der Schule
	 *
	 * @return Inhalt des Feldes bezeichnung1
	 */
	public String bezeichnung1() {
		return bezeichnung1;
	}

	/**
	 * Der zweite Teil der Bezeichnung der Schule
	 *
	 * @return Inhalt des Feldes bezeichnung2
	 */
	public String bezeichnung2() {
		return bezeichnung2;
	}

	/**
	 * Der dritte Teil der Bezeichnung der Schule
	 *
	 * @return Inhalt des Feldes bezeichnung3
	 */
	public String bezeichnung3() {
		return bezeichnung3;
	}

	/**
	 * Die allgemeine Bezeichnung der Abschnitte (z.B. Quartal oder Halbjahr)
	 *
	 * @return Inhalt des Feldes bezeichnungSchuljahresabschnitt
	 */
	public String bezeichnungSchuljahresabschnitt() {
		return bezeichnungSchuljahresabschnitt;
	}

	/**
	 * Eine Liste der einzelnen speziellen Bezeichnungen für die Abschnitte (z.B. 1. Halbjahr, 2. Halbjahr)
	 *
	 * @return Inhalt des Feldes bezeichnungenSchuljahresabschnitte
	 */
	public List<String> bezeichnungenSchuljahresabschnitte() {
		return bezeichnungenSchuljahresabschnitte;
	}

	/**
	 * Die Dauer einer Unterrichtseinheit in Minuten.
	 *
	 * @return Inhalt des Feldes dauerUnterrichtseinheit
	 */
	public long dauerUnterrichtseinheit() {
		return dauerUnterrichtseinheit;
	}

	/**
	 * Die Mailadresse der Schule.
	 *
	 * @return Inhalt des Feldes email
	 */
	public String email() {
		return email;
	}

	/**
	 * Die Faxnummer der Schule.
	 *
	 * @return Inhalt des Feldes fax
	 */
	public String fax() {
		return fax;
	}

	/**
	 * Die Hausnummer zur Straße in der die Schule liegt.
	 *
	 * @return Inhalt des Feldes hausnummer
	 */
	public String hausnummer() {
		return hausnummer;
	}

	/**
	 * Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt.
	 *
	 * @return Inhalt des Feldes hausnummerZusatz
	 */
	public String hausnummerZusatz() {
		return hausnummerZusatz;
	}

	/**
	 * Der Ort in dem die Schule liegt.
	 *
	 * @return Inhalt des Feldes ort
	 */
	public String ort() {
		return ort;
	}

	/**
	 * Die Postleitzahl des Gebietes in dem die Schule liegt.
	 *
	 * @return Inhalt des Feldes plz
	 */
	public String plz() {
		return plz;
	}

	/**
	 * Die Liste der Schuljahresabschnitte, welche an der Schule definiert sind.
	 *
	 * @return Inhalt des Feldes schuljahresabschnitte
	 */
	public List<ReportingSchuljahresabschnitt> schuljahresabschnitte() {
		return schuljahresabschnitte;
	}

	/**
	 * Die Schulform der Schule
	 *
	 * @return Inhalt des Feldes schulform
	 */
	public String schulform() {
		return schulform;
	}

	/**
	 * Das Schullogo der Schule im Base64-Format
	 *
	 * @return Inhalt des Feldes schullogo
	 */
	public String schullogo() {
		return schullogo;
	}

	/**
	 * Die eindeutige Schulnummer der Schule
	 *
	 * @return Inhalt des Feldes schulnummer
	 */
	public long schulnummer() {
		return schulnummer;
	}

	/**
	 * Der Straßenname der Straße in der die Schule liegt.
	 *
	 * @return Inhalt des Feldes strassenname
	 */
	public String strassenname() {
		return strassenname;
	}

	/**
	 * Die Telefonnummer der Schule.
	 *
	 * @return Inhalt des Feldes telefon
	 */
	public String telefon() {
		return telefon;
	}

	/**
	 * Die Adresse der Homepage der Schule (Domain-Name)
	 *
	 * @return Inhalt des Feldes webAdresse
	 */
	public String webAdresse() {
		return webAdresse;
	}

}
