package de.svws_nrw.module.pdf.types.schueler.gost.laufbahnplanung;

import de.svws_nrw.module.pdf.types.gost.laufbahnplanung.ReportingGostLaufbahnplanungFachwahl;
import de.svws_nrw.module.pdf.types.gost.laufbahnplanung.ReportingGostLaufbahnplanungErgebnismeldung;
import de.svws_nrw.module.pdf.types.lehrer.ReportingLehrer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Klasse.</p>
 *
 *  <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 *  einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingSchuelerGostLaufbahnplanung {

	/** Das Kalenderjahr, in dem die Abiturprüfung stattfindet */
    private int abiturjahr;

	/** Die aktuelle Klasse zum aktuellen Halbjahr der Oberstufenlaufbahn */
	private String aktuelleKlasse;

	/** Das Halbjahr der Oberstufenlaufbahn, in dem die Beratung erfolgt */
	private String aktuellesGOStHalbjahr;

	/** Der Text der Schule für den Beratungsbogen */
	private String beratungsbogenText;

	/** Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt */
	private List<ReportingLehrer> beratungslehrkraefte;

	/** Der Text der Schule für den E-Mail-Versand */
	private String emailText;

	/** Eine Liste vom Typ Fachwahl, die alle Fachwahlen und deren Daten enthält. */
	private List<ReportingGostLaufbahnplanungFachwahl> fachwahlen;

	/** Eine Liste vom Typ Fehler, die alle Fehler zur Laufbahn und deren Daten enthält. */
	private List<ReportingGostLaufbahnplanungErgebnismeldung> fehler;

	/** Das folgende Halbjahr der Oberstufenlaufbahn, also in der Regel das Halbjahr, für das die Beratung erfolgt */
	private String folgeGOStHalbjahr;

	/** Eine Liste vom Typ Hinweise, die alle Hinweise zur Laufbahn und deren Daten enthält. */
	private List<ReportingGostLaufbahnplanungErgebnismeldung> hinweise;

	/** Das Datum der letzten Beratung */
	private String letzteBeratungDatum;

	/** Die Lehrkraft der letzten Beratung */
	private ReportingLehrer letzteBeratungLehrkraft;

	/** Das Datum des Rücklaufes der letzten importierten Wahldatei */
	private String letzterRuecklaufDatum;

	/** Kommentar der Schule zur Laufbahn */
    private String kommentar;

	/** Kursanzahl in der EF.1 */
	private int kursanzahlEF1;

	/** Kursanzahl in der EF.2 */
	private int kursanzahlEF2;

	/** Kursanzahl in der Q1.1 */
	private int kursanzahlQ11;

	/** Kursanzahl in der Q1.2 */
	private int kursanzahlQ12;

	/** Kursanzahl in der Q2.1 */
	private int kursanzahlQ21;

	/** Kursanzahl in der Q2.2 */
	private int kursanzahlQ22;

	/** Kursanzahl in der Qualifikationsphase */
	private int kursanzahlQPh;

	/** Die Prüfungsordnung des Schülers aus dem aktuellen Lernabschnitt */
	private String pruefungsordnung;

	/** Wochenstundensumme in der EF.1 */
	private int wochenstundenEF1;

	/** Wochenstundensumme in der EF.2 */
	private int wochenstundenEF2;

	/** Wochenstundensumme in der Q1.1 */
	private int wochenstundenQ11;

	/** Wochenstundensumme in der Q1.2 */
	private int wochenstundenQ12;

	/** Wochenstundensumme in der Q2.1 */
	private int wochenstundenQ21;

	/** Wochenstundensumme in der Q2.2 */
	private int wochenstundenQ22;

	/** Wochenstundendurchschnitt in der EF */
	private double wochenstundenDurchschnittEF;

	/** Wochenstundendurchschnitt in der Q1 */
	private double wochenstundenDurchschnittQ1;

	/** Wochenstundendurchschnitt in der Q2 */
	private double wochenstundenDurchschnittQ2;

	/** Wochenstundendurchschnitt in der Qualifikationsphase */
	private double wochenstundenDurchschnittQPh;

	/** Wochenstundensumme der gesamten Laufbahn */
	private double wochenstundenGesamt;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param abiturjahr Das Kalenderjahr, in dem die Abiturprüfung stattfindet
	 * @param aktuelleKlasse Die aktuelle Klasse zum aktuellen Halbjahr der Oberstufenlaufbahn
	 * @param aktuellesGOStHalbjahr Das Halbjahr der Oberstufenlaufbahn, in dem die Beratung erfolgt
	 * @param beratungsbogenText Der Text der Schule für den Beratungsbogen
	 * @param beratungslehrkraefte Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt
	 * @param folgeGOStHalbjahr Das folgende Halbjahr der Oberstufenlaufbahn, also in der Regel das Halbjahr, für das die Beratung erfolgt
	 * @param emailText Der Text der Schule für den E-Mail-Versand
	 * @param fachwahlen Eine Liste vom Typ Fachwahl, die alle Fachwahlen und deren Daten enthält.
	 * @param fehler Eine Liste vom Typ Fehler, die alle Fehler zur Laufbahn und deren Daten enthält.
	 * @param hinweise Eine Liste vom Typ Hinweise, die alle Hinweise zur Laufbahn und deren Daten enthält.
	 * @param letzteBeratungDatum Das Datum der letzten Beratung
	 * @param letzteBeratungLehrkraft Die Lehrkraft der letzten Beratung
	 * @param letzterRuecklaufDatum Das Datum des Rücklaufes der letzten importierten Wahldatei
	 * @param kommentar Kommentar der Schule zur Laufbahn
	 * @param kursanzahlEF1 Kursanzahl in der EF.1
	 * @param kursanzahlEF2 Kursanzahl in der EF.2
	 * @param kursanzahlQ11 Kursanzahl in der Q1.1
	 * @param kursanzahlQ12 Kursanzahl in der Q1.2
	 * @param kursanzahlQ21 Kursanzahl in der Q2.1
	 * @param kursanzahlQ22 Kursanzahl in der Q2.2
	 * @param kursanzahlQPh Kursanzahl in der Qualifikationsphase
	 * @param pruefungsordnung Die Prüfungsordnung des Schülers aus dem aktuellen Lernabschnitt
	 * @param wochenstundenEF1 Wochenstundensumme in der EF.1
	 * @param wochenstundenEF2 Wochenstundensumme in der EF.2
	 * @param wochenstundenQ11 Wochenstundensumme in der Q1.1
	 * @param wochenstundenQ12 Wochenstundensumme in der Q1.2
	 * @param wochenstundenQ21 Wochenstundensumme in der Q2.1
	 * @param wochenstundenQ22 Wochenstundensumme in der Q2.2
	 * @param wochenstundenDurchschnittEF Wochenstundendurchschnitt in der EF
	 * @param wochenstundenDurchschnittQ1 Wochenstundendurchschnitt in der Q1
	 * @param wochenstundenDurchschnittQ2 Wochenstundendurchschnitt in der Q2
	 * @param wochenstundenDurchschnittQPh Wochenstundendurchschnitt in der Qualifikationsphase
	 * @param wochenstundenGesamt Wochenstundensumme der gesamten Laufbahn
	 */
	public ReportingSchuelerGostLaufbahnplanung(final int abiturjahr, final String aktuelleKlasse, final String aktuellesGOStHalbjahr, final String beratungsbogenText, final List<ReportingLehrer> beratungslehrkraefte, final String emailText, final List<ReportingGostLaufbahnplanungFachwahl> fachwahlen, final List<ReportingGostLaufbahnplanungErgebnismeldung> fehler, final String folgeGOStHalbjahr, final List<ReportingGostLaufbahnplanungErgebnismeldung> hinweise, final String letzteBeratungDatum, final ReportingLehrer letzteBeratungLehrkraft, final String letzterRuecklaufDatum, final String kommentar, final int kursanzahlEF1, final int kursanzahlEF2, final int kursanzahlQ11, final int kursanzahlQ12, final int kursanzahlQ21, final int kursanzahlQ22, final int kursanzahlQPh, final String pruefungsordnung, final int wochenstundenEF1, final int wochenstundenEF2, final int wochenstundenQ11, final int wochenstundenQ12, final int wochenstundenQ21, final int wochenstundenQ22, final double wochenstundenDurchschnittEF, final double wochenstundenDurchschnittQ1, final double wochenstundenDurchschnittQ2, final double wochenstundenDurchschnittQPh, final double wochenstundenGesamt) {
		this.abiturjahr = abiturjahr;
		this.aktuellesGOStHalbjahr = aktuellesGOStHalbjahr;
		this.aktuelleKlasse = aktuelleKlasse;
		this.beratungsbogenText = beratungsbogenText;
		this.beratungslehrkraefte = beratungslehrkraefte;
		this.emailText = emailText;
		this.fachwahlen = fachwahlen;
		this.fehler = fehler;
		this.folgeGOStHalbjahr = folgeGOStHalbjahr;
		this.hinweise = hinweise;
		this.letzteBeratungDatum = letzteBeratungDatum;
		this.letzteBeratungLehrkraft = letzteBeratungLehrkraft;
		this.letzterRuecklaufDatum = letzterRuecklaufDatum;
		this.kommentar = kommentar;
		this.kursanzahlEF1 = kursanzahlEF1;
		this.kursanzahlEF2 = kursanzahlEF2;
		this.kursanzahlQ11 = kursanzahlQ11;
		this.kursanzahlQ12 = kursanzahlQ12;
		this.kursanzahlQ21 = kursanzahlQ21;
		this.kursanzahlQ22 = kursanzahlQ22;
		this.kursanzahlQPh = kursanzahlQPh;
		this.pruefungsordnung = pruefungsordnung;
		this.wochenstundenEF1 = wochenstundenEF1;
		this.wochenstundenEF2 = wochenstundenEF2;
		this.wochenstundenQ11 = wochenstundenQ11;
		this.wochenstundenQ12 = wochenstundenQ12;
		this.wochenstundenQ21 = wochenstundenQ21;
		this.wochenstundenQ22 = wochenstundenQ22;
		this.wochenstundenDurchschnittEF = wochenstundenDurchschnittEF;
		this.wochenstundenDurchschnittQ1 = wochenstundenDurchschnittQ1;
		this.wochenstundenDurchschnittQ2 = wochenstundenDurchschnittQ2;
		this.wochenstundenDurchschnittQPh = wochenstundenDurchschnittQPh;
		this.wochenstundenGesamt = wochenstundenGesamt;
	}



	// ##### Berechnete Felder #####

	/**
	 * Erstellt einen Satz für die Angaben zur letzten Beratung in der Form: "Die letzte Beratung wurde durchgeführt am (Datum) von (Lehrkraft)."
	 * Nicht vorhandene Informationen werden entsprechend ausgelassen. Sind keinerlei Informationen vorhanden, so wird ein leerer String zurückgegeben.
	 * @return 	Satz mit den Informationen zur letzten Beratung oder ein leerer String.
	 */
	public String letzteBeratungText() {
		// Ausgabe der Informationen (Zeit und Person) der letzten Beratung zusammenstellen, je nachdem, welche Informationen hinterlegt sind.
		String letzteBeratung = "Die letzte Beratung wurde durchgeführt";
		boolean hatLetzteBeratung = false;
		if (letzteBeratungDatum != null && !letzteBeratungDatum.isBlank() && !letzteBeratungDatum.isEmpty()) {
			letzteBeratung = letzteBeratung + " am " + LocalDate.parse(letzteBeratungDatum, DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			hatLetzteBeratung = true;
		}
		if (letzteBeratungLehrkraft != null) {
			letzteBeratung = letzteBeratung + " von " + letzteBeratungLehrkraft.unterschriftfeld();
			hatLetzteBeratung = true;
		}
		letzteBeratung = Boolean.TRUE.equals(hatLetzteBeratung) ? letzteBeratung + "." : "";
		return letzteBeratung;
	}

	/**
	 * Erzeugt eine Beschriftung für ein Unterschriftsfeld mit allen Beratungslehrkräften.
	 * @return 	Beschriftung in der Form: Titel Vorname (erster Buchstabe). Nachname
	 */
	public String unterschriftfeldBeratungslehrkraefteMehrzeiligHtml() {
		return unterschriftfeldBeratungslehrkraefteMehrzeiligHtml(true, false);
	}

	/**
	 * Erzeugt eine Beschriftung für ein Unterschriftsfeld mit allen Beratungslehrkraeften.
	 * @param mitVornameKurz		Gibt an, ob nur der erste Buchstabe des Vornamens ausgegeben werden soll.
	 * @param mitAmtsbezeichnung	Gibt an, ob die Amtsbezeichnung hinzugefügt werden soll.
	 * @return 						Beschriftung gemäß Parametern: Titel Vorname Nachname, Amtsbezeichnung
	 */
	public String unterschriftfeldBeratungslehrkraefteMehrzeiligHtml(final boolean mitVornameKurz, final boolean mitAmtsbezeichnung) {
		return beratungslehrkraefte.stream()
			.map(l -> l.unterschriftfeld(mitVornameKurz, mitAmtsbezeichnung))
			.collect(Collectors.joining("<br/>"))
			.trim();
	}




	// ##### Getter und Setter #####

	/**
	 * Das Kalenderjahr, in dem die Abiturprüfung stattfindet
	 * @return Inhalt des Feldes abiturjahr
	 */
	public int abiturjahr() {
		return abiturjahr;
	}

	/**
	 * Das Kalenderjahr, in dem die Abiturprüfung stattfindet, wird gesetzt.
	 * @param abiturjahr Neuer Wert für das Feld abiturjahr
	 */
	public void setAbiturjahr(final int abiturjahr) {
		this.abiturjahr = abiturjahr;
	}

	/**
	 * Die aktuelle Klasse zum aktuellen Halbjahr der Oberstufenlaufbahn
	 * @return Inhalt des Feldes aktuelleKlasse
	 */
	public String aktuelleKlasse() {
		return aktuelleKlasse;
	}

	/**
	 * Die aktuelle Klasse zum aktuellen Halbjahr der Oberstufenlaufbahn wird gesetzt.
	 * @param aktuelleKlasse Neuer Wert für das Feld aktuelleKlasse
	 */
	public void setAktuelleKlasse(final String aktuelleKlasse) {
		this.aktuelleKlasse = aktuelleKlasse;
	}

	/**
	 * Das Halbjahr der Oberstufenlaufbahn, in dem die Beratung erfolgt
	 * @return Inhalt des Feldes aktuellesGOStHalbjahr
	 */
	public String aktuellesGOStHalbjahr() {
		return aktuellesGOStHalbjahr;
	}

	/**
	 * Das Halbjahr der Oberstufenlaufbahn, in dem die Beratung erfolgt, wird gesetzt.
	 * @param aktuellesGOStHalbjahr Neuer Wert für das Feld aktuellesGOStHalbjahr
	 */
	public void setAktuellesGOStHalbjahr(final String aktuellesGOStHalbjahr) {
		this.aktuellesGOStHalbjahr = aktuellesGOStHalbjahr;
	}

	/**
	 * Der Text der Schule für den Beratungsbogen
	 * @return Inhalt des Feldes beratungsbogenText
	 */
	public String beratungsbogenText() {
		return beratungsbogenText;
	}

	/**
	 * Der Text der Schule für den Beratungsbogen wird gesetzt.
	 * @param beratungsbogenText Neuer Wert für das Feld beratungsbogenText
	 */
	public void setBeratungsbogenText(final String beratungsbogenText) {
		this.beratungsbogenText = beratungsbogenText;
	}

	/**
	 * Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt
	 * @return Inhalt des Feldes beratungslehrkraefte
	 */
	public List<ReportingLehrer> beratungslehrkraefte() {
		return beratungslehrkraefte;
	}

	/**
	 * Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt wird gesetzt.
	 * @param beratungslehrkraefte Neuer Wert für das Feld beratungslehrkraefte
	 */
	public void setBeratungslehrkraefte(final List<ReportingLehrer> beratungslehrkraefte) {
		this.beratungslehrkraefte = beratungslehrkraefte;
	}

	/**
	 * Der Text der Schule für den E-Mail-Versand
	 * @return Inhalt des Feldes emailText
	 */
	public String emailText() {
		return emailText;
	}

	/**
	 * Der Text der Schule für den E-Mail-Versand wird gesetzt.
	 * @param emailText Neuer Wert für das Feld emailText
	 */
	public void setEmailText(final String emailText) {
		this.emailText = emailText;
	}

	/**
	 * Eine Liste vom Typ Fachwahl, die alle Fachwahlen und deren Daten enthält.
	 * @return Inhalt des Feldes fachwahlen
	 */
	public List<ReportingGostLaufbahnplanungFachwahl> fachwahlen() {
		return fachwahlen;
	}

	/**
	 * Eine Liste vom Typ Fachwahl, die alle Fachwahlen und deren Daten enthält, wird gesetzt.
	 * @param fachwahlen Neuer Wert für das Feld fachwahlen
	 */
	public void setFachwahlen(final List<ReportingGostLaufbahnplanungFachwahl> fachwahlen) {
		this.fachwahlen = fachwahlen;
	}

	/**
	 * Eine Liste vom Typ Fehler, die alle Fehler zur Laufbahn und deren Daten enthält.
	 * @return Inhalt des Feldes fehler
	 */
	public List<ReportingGostLaufbahnplanungErgebnismeldung> fehler() {
		return fehler;
	}

	/**
	 * Eine Liste vom Typ Fehler, die alle Fehler zur Laufbahn und deren Daten enthält, wird gesetzt.
	 * @param fehler Neuer Wert für das Feld fehler
	 */
	public void setFehler(final List<ReportingGostLaufbahnplanungErgebnismeldung> fehler) {
		this.fehler = fehler;
	}

	/**
	 * Das folgende Halbjahr der Oberstufenlaufbahn, also in der Regel das Halbjahr, für das die Beratung erfolgt
	 * @return Inhalt des Feldes folgeGOStHalbjahr
	 */
	public String folgeGOStHalbjahr() {
		return folgeGOStHalbjahr;
	}

	/**
	 * Das folgende Halbjahr der Oberstufenlaufbahn, also in der Regel das Halbjahr, für das die Beratung erfolgt, wird gesetzt.
	 * @param folgeGOStHalbjahr Neuer Wert für das Feld folgeGOStHalbjahr
	 */
	public void setFolgeGOStHalbjahr(final String folgeGOStHalbjahr) {
		this.folgeGOStHalbjahr = folgeGOStHalbjahr;
	}

	/**
	 * Eine Liste vom Typ Hinweise, die alle Hinweise zur Laufbahn und deren Daten enthält.
	 * @return Inhalt des Feldes hinweise
	 */
	public List<ReportingGostLaufbahnplanungErgebnismeldung> hinweise() {
		return hinweise;
	}

	/**
	 * Eine Liste vom Typ Hinweise, die alle Hinweise zur Laufbahn und deren Daten enthält, wird gesetzt.
	 * @param hinweise Neuer Wert für das Feld hinweise
	 */
	public void setHinweise(final List<ReportingGostLaufbahnplanungErgebnismeldung> hinweise) {
		this.hinweise = hinweise;
	}

	/**
	 * Das Datum der letzten Beratung
	 * @return Inhalt des Feldes letzteBeratungDatum
	 */
	public String letzteBeratungDatum() {
		return letzteBeratungDatum;
	}

	/**
	 * Das Datum der letzten Beratung wird gesetzt.
	 * @param letzteBeratungDatum Neuer Wert für das Feld letzteBeratungDatum
	 */
	public void setLetzteBeratungDatum(final String letzteBeratungDatum) {
		this.letzteBeratungDatum = letzteBeratungDatum;
	}

	/**
	 * Die Lehrkraft der letzten Beratung
	 * @return Inhalt des Feldes letzteBeratungLehrkraft
	 */
	public ReportingLehrer letzteBeratungLehrkraft() {
		return letzteBeratungLehrkraft;
	}

	/**
	 * Die Lehrkraft der letzten Beratung wird gesetzt.
	 * @param letzteBeratungLehrkraft Neuer Wert für das Feld letzteBeratungLehrkraft
	 */
	public void setLetzteBeratungLehrkraft(final ReportingLehrer letzteBeratungLehrkraft) {
		this.letzteBeratungLehrkraft = letzteBeratungLehrkraft;
	}

	/**
	 * Das Datum des Rücklaufes der letzten importierten Wahldatei
	 * @return Inhalt des Feldes letzterRuecklaufDatum
	 */
	public String letzterRuecklaufDatum() {
		return letzterRuecklaufDatum;
	}

	/**
	 * Das Datum des Rücklaufes der letzten importierten Wahldatei wird gesetzt.
	 * @param letzterRuecklaufDatum Neuer Wert für das Feld letzterRuecklaufDatum
	 */
	public void setLetzterRuecklaufDatum(final String letzterRuecklaufDatum) {
		this.letzterRuecklaufDatum = letzterRuecklaufDatum;
	}

	/**
	 * Kommentar der Schule zur Laufbahn
	 * @return Inhalt des Feldes kommentar
	 */
	public String kommentar() {
		return kommentar;
	}

	/**
	 * Kommentar der Schule zur Laufbahn wird gesetzt.
	 * @param kommentar Neuer Wert für das Feld kommentar
	 */
	public void setKommentar(final String kommentar) {
		this.kommentar = kommentar;
	}

	/**
	 * Kursanzahl in der EF.1
	 * @return Inhalt des Feldes kursanzahlEF1
	 */
	public int kursanzahlEF1() {
		return kursanzahlEF1;
	}

	/**
	 * Kursanzahl in der EF1 wird gesetzt.
	 * @param kursanzahlEF1 Neuer Wert für das Feld kursanzahlEF1
	 */
	public void setKursanzahlEF1(final int kursanzahlEF1) {
		this.kursanzahlEF1 = kursanzahlEF1;
	}

	/**
	 * Kursanzahl in der EF.2
	 * @return Inhalt des Feldes kursanzahlEF2
	 */
	public int kursanzahlEF2() {
		return kursanzahlEF2;
	}

	/**
	 * Kursanzahl in der EF2 wird gesetzt.
	 * @param kursanzahlEF2 Neuer Wert für das Feld kursanzahlEF2
	 */
	public void setKursanzahlEF2(final int kursanzahlEF2) {
		this.kursanzahlEF2 = kursanzahlEF2;
	}

	/**
	 * Kursanzahl in der Q1.1
	 * @return Inhalt des Feldes kursanzahlQ11
	 */
	public int kursanzahlQ11() {
		return kursanzahlQ11;
	}

	/**
	 * Kursanzahl in der Q11 wird gesetzt.
	 * @param kursanzahlQ11 Neuer Wert für das Feld kursanzahlQ11
	 */
	public void setKursanzahlQ11(final int kursanzahlQ11) {
		this.kursanzahlQ11 = kursanzahlQ11;
	}

	/**
	 * Kursanzahl in der Q1.2
	 * @return Inhalt des Feldes kursanzahlQ12
	 */
	public int kursanzahlQ12() {
		return kursanzahlQ12;
	}

	/**
	 * Kursanzahl in der Q12 wird gesetzt.
	 * @param kursanzahlQ12 Neuer Wert für das Feld kursanzahlQ12
	 */
	public void setKursanzahlQ12(final int kursanzahlQ12) {
		this.kursanzahlQ12 = kursanzahlQ12;
	}

	/**
	 * Kursanzahl in der Q2.1
	 * @return Inhalt des Feldes kursanzahlQ21
	 */
	public int kursanzahlQ21() {
		return kursanzahlQ21;
	}

	/**
	 * Kursanzahl in der Q21 wird gesetzt.
	 * @param kursanzahlQ21 Neuer Wert für das Feld kursanzahlQ21
	 */
	public void setKursanzahlQ21(final int kursanzahlQ21) {
		this.kursanzahlQ21 = kursanzahlQ21;
	}

	/**
	 * Kursanzahl in der Q2.2
	 * @return Inhalt des Feldes kursanzahlQ22
	 */
	public int kursanzahlQ22() {
		return kursanzahlQ22;
	}

	/**
	 * Kursanzahl in der Q22 wird gesetzt.
	 * @param kursanzahlQ22 Neuer Wert für das Feld kursanzahlQ22
	 */
	public void setKursanzahlQ22(final int kursanzahlQ22) {
		this.kursanzahlQ22 = kursanzahlQ22;
	}

	/**
	 * Kursanzahl in der Qualifikationsphase
	 * @return Inhalt des Feldes kursanzahlQPh
	 */
	public int kursanzahlQPh() {
		return kursanzahlQPh;
	}

	/**
	 * Kursanzahl in der Qualifikationsphase wird gesetzt.
	 * @param kursanzahlQPh Neuer Wert für das Feld kursanzahlQPh
	 */
	public void setKursanzahlQPh(final int kursanzahlQPh) {
		this.kursanzahlQPh = kursanzahlQPh;
	}

	/**
	 * Die Prüfungsordnung des Schülers aus dem aktuellen Lernabschnitt
	 * @return Inhalt des Feldes pruefungsordnung
	 */
	public String pruefungsordnung() {
		return pruefungsordnung;
	}

	/**
	 * Die Prüfungsordnung des Schülers aus dem aktuellen Lernabschnitt wird gesetzt.
	 * @param pruefungsordnung Neuer Wert für das Feld pruefungsordnung
	 */
	public void setPruefungsordnung(final String pruefungsordnung) {
		this.pruefungsordnung = pruefungsordnung;
	}

	/**
	 * Wochenstundensumme in der EF.1
	 * @return Inhalt des Feldes wochenstundenEF1
	 */
	public int wochenstundenEF1() {
		return wochenstundenEF1;
	}

	/**
	 * Wochenstundensumme in der EF1 wird gesetzt.
	 * @param wochenstundenEF1 Neuer Wert für das Feld wochenstundenEF1
	 */
	public void setWochenstundenEF1(final int wochenstundenEF1) {
		this.wochenstundenEF1 = wochenstundenEF1;
	}

	/**
	 * Wochenstundensumme in der EF.2
	 * @return Inhalt des Feldes wochenstundenEF2
	 */
	public int wochenstundenEF2() {
		return wochenstundenEF2;
	}

	/**
	 * Wochenstundensumme in der EF2 wird gesetzt.
	 * @param wochenstundenEF2 Neuer Wert für das Feld wochenstundenEF2
	 */
	public void setWochenstundenEF2(final int wochenstundenEF2) {
		this.wochenstundenEF2 = wochenstundenEF2;
	}

	/**
	 * Wochenstundensumme in der Q1.1
	 * @return Inhalt des Feldes wochenstundenQ11
	 */
	public int wochenstundenQ11() {
		return wochenstundenQ11;
	}

	/**
	 * Wochenstundensumme in der Q11 wird gesetzt.
	 * @param wochenstundenQ11 Neuer Wert für das Feld wochenstundenQ11
	 */
	public void setWochenstundenQ11(final int wochenstundenQ11) {
		this.wochenstundenQ11 = wochenstundenQ11;
	}

	/**
	 * Wochenstundensumme in der Q1.2
	 * @return Inhalt des Feldes wochenstundenQ12
	 */
	public int wochenstundenQ12() {
		return wochenstundenQ12;
	}

	/**
	 * Wochenstundensumme in der Q12 wird gesetzt.
	 * @param wochenstundenQ12 Neuer Wert für das Feld wochenstundenQ12
	 */
	public void setWochenstundenQ12(final int wochenstundenQ12) {
		this.wochenstundenQ12 = wochenstundenQ12;
	}

	/**
	 * Wochenstundensumme in der Q2.1
	 * @return Inhalt des Feldes wochenstundenQ21
	 */
	public int wochenstundenQ21() {
		return wochenstundenQ21;
	}

	/**
	 * Wochenstundensumme in der Q21 wird gesetzt.
	 * @param wochenstundenQ21 Neuer Wert für das Feld wochenstundenQ21
	 */
	public void setWochenstundenQ21(final int wochenstundenQ21) {
		this.wochenstundenQ21 = wochenstundenQ21;
	}

	/**
	 * Wochenstundensumme in der Q2.2
	 * @return Inhalt des Feldes wochenstundenQ22
	 */
	public int wochenstundenQ22() {
		return wochenstundenQ22;
	}

	/**
	 * Wochenstundensumme in der Q22 wird gesetzt.
	 * @param wochenstundenQ22 Neuer Wert für das Feld wochenstundenQ22
	 */
	public void setWochenstundenQ22(final int wochenstundenQ22) {
		this.wochenstundenQ22 = wochenstundenQ22;
	}

	/**
	 * Wochenstundendurchschnitt in der EF
	 * @return Inhalt des Feldes wochenstundenDurchschnittEF
	 */
	public double wochenstundenDurchschnittEF() {
		return wochenstundenDurchschnittEF;
	}

	/**
	 * Wochenstundendurchschnitt in der EF wird gesetzt.
	 * @param wochenstundenDurchschnittEF Neuer Wert für das Feld wochenstundenDurchschnittEF
	 */
	public void setWochenstundenDurchschnittEF(final double wochenstundenDurchschnittEF) {
		this.wochenstundenDurchschnittEF = wochenstundenDurchschnittEF;
	}

	/**
	 * Wochenstundendurchschnitt in der Q1
	 * @return Inhalt des Feldes wochenstundenDurchschnittQ1
	 */
	public double wochenstundenDurchschnittQ1() {
		return wochenstundenDurchschnittQ1;
	}

	/**
	 * Wochenstundendurchschnitt in der Q1 wird gesetzt.
	 * @param wochenstundenDurchschnittQ1 Neuer Wert für das Feld wochenstundenDurchschnittQ1
	 */
	public void setWochenstundenDurchschnittQ1(final double wochenstundenDurchschnittQ1) {
		this.wochenstundenDurchschnittQ1 = wochenstundenDurchschnittQ1;
	}

	/**
	 * Wochenstundendurchschnitt in der Q2
	 * @return Inhalt des Feldes wochenstundenDurchschnittQ2
	 */
	public double wochenstundenDurchschnittQ2() {
		return wochenstundenDurchschnittQ2;
	}

	/**
	 * Wochenstundendurchschnitt in der Q2 wird gesetzt.
	 * @param wochenstundenDurchschnittQ2 Neuer Wert für das Feld wochenstundenDurchschnittQ2
	 */
	public void setWochenstundenDurchschnittQ2(final double wochenstundenDurchschnittQ2) {
		this.wochenstundenDurchschnittQ2 = wochenstundenDurchschnittQ2;
	}

	/**
	 * Wochenstundendurchschnitt in der Qualifikationsphase
	 * @return Inhalt des Feldes wochenstundenDurchschnittQPh
	 */
	public double wochenstundenDurchschnittQPh() {
		return wochenstundenDurchschnittQPh;
	}

	/**
	 * Wochenstundendurchschnitt in der Qualifikationsphase wird gesetzt.
	 * @param wochenstundenDurchschnittQPh Neuer Wert für das Feld wochenstundenDurchschnittQPh
	 */
	public void setWochenstundenDurchschnittQPh(final double wochenstundenDurchschnittQPh) {
		this.wochenstundenDurchschnittQPh = wochenstundenDurchschnittQPh;
	}

	/**
	 * Wochenstundensumme der gesamten Laufbahn
	 * @return Inhalt des Feldes wochenstundenGesamt
	 */
	public double wochenstundenGesamt() {
		return wochenstundenGesamt;
	}

	/**
	 * Wochenstundensumme der gesamten Laufbahn wird gesetzt.
	 * @param wochenstundenGesamt Neuer Wert für das Feld wochenstundenGesamt
	 */
	public void setWochenstundenGesamt(final double wochenstundenGesamt) {
		this.wochenstundenGesamt = wochenstundenGesamt;
	}
}
