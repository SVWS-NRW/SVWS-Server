package de.svws_nrw.module.pdf.types.schule;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Schule.</p>
 * <p>Sie enthält alle Daten zur Schule, insbesondere deren Schuljahresabschnitte und Stammdaten.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingSchule {

	/** Der aktuelle Abschnitt des Schuljahres der Schule. */
	private ReportingSchuljahresabschnitt aktuellerSchuljahresabschnitt;

	/** Die Anzahl der Jahrgangsstufen pro Jahr. */
	private long anzahlJahrgangsstufenProJahr;

	/** Die Anzahl der Abschnitte pro Jahr */
	private long anzahlSchuljahresabschnitteProJahr;

	/** Der erste Teil der Bezeichnung der Schule */
	private String bezeichnung1;

	/** Der zweite Teil der Bezeichnung der Schule */
	private String bezeichnung2;

	/** Der dritte Teil der Bezeichnung der Schule */
	private String bezeichnung3;

	/** Die allgemeine Bezeichnung der Abschnitte (z.B. Quartal oder Halbjahr) */
	private String bezeichnungSchuljahresabschnitt;

	/** Eine Liste der einzelnen speziellen Bezeichnungen für die Abschnitte (z.B. 1. Halbjahr, 2. Halbjahr) */
	private List<String> bezeichnungenSchuljahresabschnitte = new ArrayList<>();

	/** Die Dauer einer Unterrichtseinheit in Minuten. */
	private long dauerUnterrichtseinheit;

	/** Die Mailadresse der Schule. */
	private String email;

	/** Die Faxnummer der Schule. */
	private String fax;

	/** Die Hausnummer zur Straße in der die Schule liegt. */
	private String hausnummer;

	/** Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt. */
	private String hausnummerZusatz;

	/** Der Ort in dem die Schule liegt. */
	private String ort;

	/** Die Postleitzahl des Gebietes in dem die Schule liegt. */
	private String plz;

	/** Die Liste der Schuljahresabschnitte, welche an der Schule definiert sind. */
	private List<ReportingSchuljahresabschnitt> schuljahresabschnitte = new ArrayList<>();

	/** Die Schulform der Schule */
	private String schulform;

	/** Die eindeutige Schulnummer der Schule */
	private long schulnummer;

	/** Der Straßenname der Straße in der die Schule liegt. */
	private String strassenname;

	/** Die Telefonnummer der Schule. */
	private String telefon;

	/** Die Adresse der Homepage der Schule (Domain-Name) */
	private String webAdresse;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param aktuellerSchuljahresabschnitt Der aktuelle Abschnitt des Schuljahres der Schule.
	 * @param anzahlJahrgangsstufenProJahr Die Anzahl der Jahrgangsstufen pro Jahr.
	 * @param anzahlSchuljahresabschnitteProJahr Die Anzahl der Abschnitte pro Jahr
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
	 * @param schulnummer Die eindeutige Schulnummer der Schule
	 * @param strassenname Der Straßenname der Straße in der die Schule liegt.
	 * @param telefon Die Telefonnummer der Schule.
	 * @param webAdresse Die Adresse der Homepage der Schule (Domain-Name)
	 */
	public ReportingSchule(final ReportingSchuljahresabschnitt aktuellerSchuljahresabschnitt, final long anzahlJahrgangsstufenProJahr, final long anzahlSchuljahresabschnitteProJahr, final String bezeichnung1, final String bezeichnung2, final String bezeichnung3, final String bezeichnungSchuljahresabschnitt, final List<String> bezeichnungenSchuljahresabschnitte, final long dauerUnterrichtseinheit, final String email, final String fax, final String hausnummer, final String hausnummerZusatz, final String ort, final String plz, final List<ReportingSchuljahresabschnitt> schuljahresabschnitte, final String schulform, final long schulnummer, final String strassenname, final String telefon, final String webAdresse) {
		this.aktuellerSchuljahresabschnitt = aktuellerSchuljahresabschnitt;
		this.anzahlJahrgangsstufenProJahr = anzahlJahrgangsstufenProJahr;
		this.anzahlSchuljahresabschnitteProJahr = anzahlSchuljahresabschnitteProJahr;
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
		this.schulnummer = schulnummer;
		this.strassenname = strassenname;
		this.telefon = telefon;
		this.webAdresse = webAdresse;
	}



	// ##### Berechnete Felder #####

	/**
	 * Gibt das aktuelle Schuljahr der Schule in Textdarstellung der Form 2023/24 zurück
	 * @return Das aktuelle Schuljahr der Schule in Textdarstellung
	 */
	public String aktuellesSchuljahrText() {
		return "%d/%d".formatted(this.aktuellerSchuljahresabschnitt.schuljahr(), (this.aktuellerSchuljahresabschnitt.schuljahr() % 100) + 1);
	}

	/**
	 * Gibt das aktuelle Schuljahr der Schule mit aktuellem Abschnitt in Textdarstellung der Form 2023/24.1 zurück.
	 * @return Das aktuelle Schuljahr der Schule mit aktuellem Abschnitt
	 */
	public String aktuellesSchuljahrUndAbschnittText() {
		return aktuellesSchuljahrText() + "." + this.aktuellerSchuljahresabschnitt.abschnitt();
	}

	/**
	 * Stellt die drei einzelnen Bezeichnungsfelder der Schule als ein mehrzeiliges Feld zur Verfügung.
	 * @return Mehrzeiliges Bezeichnungsfeld
	 */
	public String bezeichnungSchuleMehrzeilig() {
		return String.format("%s%s%s".formatted(
			(bezeichnung1 != null && !bezeichnung1.isEmpty()) ? bezeichnung1.trim() : "",
			(bezeichnung2 != null && !bezeichnung2.isEmpty()) ? "%n" + bezeichnung2.trim() : "",
			(bezeichnung3 != null && !bezeichnung3.isEmpty()) ? "%n" + bezeichnung3.trim() : ""));
	}

	/**
	 * Stellt die drei einzelnen Bezeichnungsfelder der Schule als ein mehrzeiliges Feld im html-Format zur Verfügung.
	 * @return Mehrzeiliges Bezeichnungsfeld im html-Format
	 */
	public String bezeichnungSchuleMehrzeiligHtml() {
		return "%s%s%s".formatted(
			(bezeichnung1 != null && !bezeichnung1.isEmpty()) ? bezeichnung1.trim() : "",
			(bezeichnung2 != null && !bezeichnung2.isEmpty()) ? "<br/>" + bezeichnung2.trim() : "",
			(bezeichnung3 != null && !bezeichnung3.isEmpty()) ? "<br/>" + bezeichnung3.trim() : "");
	}



	// ##### Getter und Setter #####

	/**
	 * Der aktuelle Abschnitt des Schuljahres der Schule.
	 * @return Inhalt des Feldes aktuellerSchuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt aktuellerSchuljahresabschnitt() {
		return aktuellerSchuljahresabschnitt;
	}

	/**
	 * Der aktuelle Abschnitt des Schuljahres der Schule wird gesetzt.
	 * @param aktuellerSchuljahresabschnitt Neuer Wert für das Feld aktuellerSchuljahresabschnitt
	 */
	public void setAktuellerSchuljahresabschnitt(final ReportingSchuljahresabschnitt aktuellerSchuljahresabschnitt) {
		this.aktuellerSchuljahresabschnitt = aktuellerSchuljahresabschnitt;
	}

	/**
	 * Die Anzahl der Jahrgangsstufen pro Jahr.
	 * @return Inhalt des Feldes anzahlJahrgangsstufenProJahr
	 */
	public long anzahlJahrgangsstufenProJahr() {
		return anzahlJahrgangsstufenProJahr;
	}

	/**
	 * Die Anzahl der Jahrgangsstufen pro Jahr wird gesetzt.
	 * @param anzahlJahrgangsstufenProJahr Neuer Wert für das Feld anzahlJahrgangsstufenProJahr
	 */
	public void setAnzahlJahrgangsstufenProJahr(final long anzahlJahrgangsstufenProJahr) {
		this.anzahlJahrgangsstufenProJahr = anzahlJahrgangsstufenProJahr;
	}

	/**
	 * Die Anzahl der Abschnitte pro Jahr
	 * @return Inhalt des Feldes anzahlSchuljahresabschnitteProJahr
	 */
	public long anzahlSchuljahresabschnitteProJahr() {
		return anzahlSchuljahresabschnitteProJahr;
	}

	/**
	 * Die Anzahl der Abschnitte pro Jahr wird gesetzt.
	 * @param anzahlSchuljahresabschnitteProJahr Neuer Wert für das Feld anzahlSchuljahresabschnitteProJahr
	 */
	public void setAnzahlSchuljahresabschnitteProJahr(final long anzahlSchuljahresabschnitteProJahr) {
		this.anzahlSchuljahresabschnitteProJahr = anzahlSchuljahresabschnitteProJahr;
	}

	/**
	 * Der erste Teil der Bezeichnung der Schule
	 * @return Inhalt des Feldes bezeichnung1
	 */
	public String bezeichnung1() {
		return bezeichnung1;
	}

	/**
	 * Der erste Teil der Bezeichnung der Schule wird gesetzt.
	 * @param bezeichnung1 Neuer Wert für das Feld bezeichnung1
	 */
	public void setBezeichnung1(final String bezeichnung1) {
		this.bezeichnung1 = bezeichnung1;
	}

	/**
	 * Der zweite Teil der Bezeichnung der Schule
	 * @return Inhalt des Feldes bezeichnung2
	 */
	public String bezeichnung2() {
		return bezeichnung2;
	}

	/**
	 * Der zweite Teil der Bezeichnung der Schule wird gesetzt.
	 * @param bezeichnung2 Neuer Wert für das Feld bezeichnung2
	 */
	public void setBezeichnung2(final String bezeichnung2) {
		this.bezeichnung2 = bezeichnung2;
	}

	/**
	 * Der dritte Teil der Bezeichnung der Schule
	 * @return Inhalt des Feldes bezeichnung3
	 */
	public String bezeichnung3() {
		return bezeichnung3;
	}

	/**
	 * Der dritte Teil der Bezeichnung der Schule wird gesetzt.
	 * @param bezeichnung3 Neuer Wert für das Feld bezeichnung3
	 */
	public void setBezeichnung3(final String bezeichnung3) {
		this.bezeichnung3 = bezeichnung3;
	}

	/**
	 * Die allgemeine Bezeichnung der Abschnitte (z.B. Quartal oder Halbjahr)
	 * @return Inhalt des Feldes bezeichnungSchuljahresabschnitt
	 */
	public String bezeichnungSchuljahresabschnitt() {
		return bezeichnungSchuljahresabschnitt;
	}

	/**
	 * Die allgemeine Bezeichnung der Abschnitte (z. B. Quartal oder Halbjahr) wird gesetzt.
	 * @param bezeichnungSchuljahresabschnitt Neuer Wert für das Feld bezeichnungSchuljahresabschnitt
	 */
	public void setBezeichnungSchuljahresabschnitt(final String bezeichnungSchuljahresabschnitt) {
		this.bezeichnungSchuljahresabschnitt = bezeichnungSchuljahresabschnitt;
	}

	/**
	 * Eine Liste der einzelnen speziellen Bezeichnungen für die Abschnitte (z.B. 1. Halbjahr, 2. Halbjahr)
	 * @return Inhalt des Feldes bezeichnungenSchuljahresabschnitte
	 */
	public List<String> bezeichnungenSchuljahresabschnitte() {
		return bezeichnungenSchuljahresabschnitte;
	}

	/**
	 * Eine Liste der einzelnen speziellen Bezeichnungen für die Abschnitte (z.B. 1. Halbjahr, 2. Halbjahr) wird gesetzt.
	 * @param bezeichnungenSchuljahresabschnitte Neuer Wert für das Feld bezeichnungenSchuljahresabschnitte
	 */
	public void setBezeichnungenSchuljahresabschnitte(final List<String> bezeichnungenSchuljahresabschnitte) {
		this.bezeichnungenSchuljahresabschnitte = bezeichnungenSchuljahresabschnitte;
	}

	/**
	 * Die Dauer einer Unterrichtseinheit in Minuten.
	 * @return Inhalt des Feldes dauerUnterrichtseinheit
	 */
	public long dauerUnterrichtseinheit() {
		return dauerUnterrichtseinheit;
	}

	/**
	 * Die Dauer einer Unterrichtseinheit in Minuten wird gesetzt.
	 * @param dauerUnterrichtseinheit Neuer Wert für das Feld dauerUnterrichtseinheit
	 */
	public void setDauerUnterrichtseinheit(final long dauerUnterrichtseinheit) {
		this.dauerUnterrichtseinheit = dauerUnterrichtseinheit;
	}

	/**
	 * Die Mailadresse der Schule.
	 * @return Inhalt des Feldes email
	 */
	public String email() {
		return email;
	}

	/**
	 * Die Mailadresse der Schule wird gesetzt.
	 * @param email Neuer Wert für das Feld email
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * Die Faxnummer der Schule.
	 * @return Inhalt des Feldes fax
	 */
	public String fax() {
		return fax;
	}

	/**
	 * Die Faxnummer der Schule wird gesetzt.
	 * @param fax Neuer Wert für das Feld fax
	 */
	public void setFax(final String fax) {
		this.fax = fax;
	}

	/**
	 * Die Hausnummer zur Straße in der die Schule liegt.
	 * @return Inhalt des Feldes hausnummer
	 */
	public String hausnummer() {
		return hausnummer;
	}

	/**
	 * Die Hausnummer zur Straße, in der die Schule liegt, wird gesetzt.
	 * @param hausnummer Neuer Wert für das Feld hausnummer
	 */
	public void setHausnummer(final String hausnummer) {
		this.hausnummer = hausnummer;
	}

	/**
	 * Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt.
	 * @return Inhalt des Feldes hausnummerZusatz
	 */
	public String hausnummerZusatz() {
		return hausnummerZusatz;
	}

	/**
	 * Ggf der Hausnummerzusatz zur Straße, in der die Schule liegt, wird gesetzt.
	 * @param hausnummerZusatz Neuer Wert für das Feld hausnummerZusatz
	 */
	public void setHausnummerZusatz(final String hausnummerZusatz) {
		this.hausnummerZusatz = hausnummerZusatz;
	}

	/**
	 * Der Ort in dem die Schule liegt.
	 * @return Inhalt des Feldes ort
	 */
	public String ort() {
		return ort;
	}

	/**
	 * Der Ort, in dem die Schule liegt, wird gesetzt.
	 * @param ort Neuer Wert für das Feld ort
	 */
	public void setOrt(final String ort) {
		this.ort = ort;
	}

	/**
	 * Die Postleitzahl des Gebietes in dem die Schule liegt.
	 * @return Inhalt des Feldes plz
	 */
	public String plz() {
		return plz;
	}

	/**
	 * Die Postleitzahl des Gebietes, in dem die Schule liegt, wird gesetzt.
	 * @param plz Neuer Wert für das Feld plz
	 */
	public void setPlz(final String plz) {
		this.plz = plz;
	}

	/**
	 * Die Liste der Schuljahresabschnitte, welche an der Schule definiert sind.
	 * @return Inhalt des Feldes schuljahresabschnitte
	 */
	public List<ReportingSchuljahresabschnitt> schuljahresabschnitte() {
		return schuljahresabschnitte;
	}

	/**
	 * Die Liste der Schuljahresabschnitte, welche an der Schule definiert sind, wird gesetzt.
	 * @param schuljahresabschnitte Neuer Wert für das Feld schuljahresabschnitte
	 */
	public void setSchuljahresabschnitte(final List<ReportingSchuljahresabschnitt> schuljahresabschnitte) {
		this.schuljahresabschnitte = schuljahresabschnitte;
	}

	/**
	 * Die Schulform der Schule
	 * @return Inhalt des Feldes schulform
	 */
	public String schulform() {
		return schulform;
	}

	/**
	 * Die Schulform der Schule wird gesetzt.
	 * @param schulform Neuer Wert für das Feld schulform
	 */
	public void setSchulform(final String schulform) {
		this.schulform = schulform;
	}

	/**
	 * Die eindeutige Schulnummer der Schule
	 * @return Inhalt des Feldes schulnummer
	 */
	public long schulnummer() {
		return schulnummer;
	}

	/**
	 * Die eindeutige Schulnummer der Schule wird gesetzt.
	 * @param schulnummer Neuer Wert für das Feld schulnummer
	 */
	public void setSchulnummer(final long schulnummer) {
		this.schulnummer = schulnummer;
	}

	/**
	 * Der Straßenname der Straße in der die Schule liegt.
	 * @return Inhalt des Feldes strassenname
	 */
	public String strassenname() {
		return strassenname;
	}

	/**
	 * Der Straßenname der Straße, in der die Schule liegt, wird gesetzt.
	 * @param strassenname Neuer Wert für das Feld strassenname
	 */
	public void setStrassenname(final String strassenname) {
		this.strassenname = strassenname;
	}

	/**
	 * Die Telefonnummer der Schule.
	 * @return Inhalt des Feldes telefon
	 */
	public String telefon() {
		return telefon;
	}

	/**
	 * Die Telefonnummer der Schule wird gesetzt.
	 * @param telefon Neuer Wert für das Feld telefon
	 */
	public void setTelefon(final String telefon) {
		this.telefon = telefon;
	}

	/**
	 * Die Adresse der Homepage der Schule (Domain-Name)
	 * @return Inhalt des Feldes webAdresse
	 */
	public String webAdresse() {
		return webAdresse;
	}

	/**
	 * Die Adresse der Homepage der Schule (Domain-Name) wird gesetzt.
	 * @param webAdresse Neuer Wert für das Feld webAdresse
	 */
	public void setWebAdresse(final String webAdresse) {
		this.webAdresse = webAdresse;
	}
}
