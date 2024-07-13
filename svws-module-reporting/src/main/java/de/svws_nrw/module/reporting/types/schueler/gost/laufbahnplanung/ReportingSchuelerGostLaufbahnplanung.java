package de.svws_nrw.module.reporting.types.schueler.gost.laufbahnplanung;

import de.svws_nrw.module.reporting.types.gost.laufbahnplanung.ReportingGostLaufbahnplanungErgebnismeldung;
import de.svws_nrw.module.reporting.types.gost.laufbahnplanung.ReportingGostLaufbahnplanungFachwahl;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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
	protected int abiturjahr;

	/** Die aktuelle Klasse zum aktuellen Halbjahr der Oberstufenlaufbahn */
	protected String aktuelleKlasse;

	/** Das Halbjahr der Oberstufenlaufbahn gemäß Halbjahr der Schule */
	protected String aktuellesGOStHalbjahr;

	/** Die Klasse zum ausgewählten Halbjahr der Oberstufenlaufbahn */
	protected String auswahlKlasse;

	/** Das ausgewählte Halbjahr der Oberstufenlaufbahn, in dem die Beratung erfolgt  */
	protected String auswahlGOStHalbjahr;

	/** Der Text der Schule für den Beratungsbogen */
	protected String beratungsbogenText;

	/** Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt */
	protected List<ReportingLehrer> beratungslehrkraefte;

	/** Der Text der Schule für den E-Mail-Versand */
	protected String emailText;

	/** Eine Liste vom Typ Fachwahl, die alle Fachwahlen und deren Daten enthält. */
	protected List<ReportingGostLaufbahnplanungFachwahl> fachwahlen;

	/** Eine Liste vom Typ Fehler, die alle Fehler zur Laufbahn und deren Daten enthält. */
	protected List<ReportingGostLaufbahnplanungErgebnismeldung> fehler;

	/** Das folgende Halbjahr der Oberstufenlaufbahn, also in der Regel das Halbjahr, für das die Beratung erfolgt */
	protected String folgeAktuellesGOStHalbjahr;

	/** Das folgende Halbjahr der Oberstufenlaufbahn auf das ausgewählte Halbjahr, also in der Regel das Halbjahr, für das die Beratung erfolgt */
	protected String folgeAuswahlGOStHalbjahr;

	/** Eine Liste vom Typ Hinweise, die alle Hinweise zur Laufbahn und deren Daten enthält. */
	protected List<ReportingGostLaufbahnplanungErgebnismeldung> hinweise;

	/** Das Datum der letzten Beratung */
	protected String letzteBeratungDatum;

	/** Die Lehrkraft der letzten Beratung */
	protected ReportingLehrer letzteBeratungLehrkraft;

	/** Das Datum des Rücklaufes der letzten importierten Wahldatei */
	protected String letzterRuecklaufDatum;

	/** Kommentar der Schule zur Laufbahn */
	protected String kommentar;

	/** Kursanzahl in der EF.1 */
	protected int kursanzahlEF1;

	/** Kursanzahl in der EF.2 */
	protected int kursanzahlEF2;

	/** Kursanzahl in der Q1.1 */
	protected int kursanzahlQ11;

	/** Kursanzahl in der Q1.2 */
	protected int kursanzahlQ12;

	/** Kursanzahl in der Q2.1 */
	protected int kursanzahlQ21;

	/** Kursanzahl in der Q2.2 */
	protected int kursanzahlQ22;

	/** Kursanzahl in der Qualifikationsphase */
	protected int kursanzahlQPh;

	/** Kursanzahl der aus der Qualifikationsphase anrechenbaren Kurse für Block I */
	protected int kursanzahlAnrechenbarBlockI;

	/** Die Prüfungsordnung des Schülers aus dem aktuellen Lernabschnitt */
	protected String pruefungsordnung;

	/** Wochenstundensumme in der EF.1 */
	protected int wochenstundenEF1;

	/** Wochenstundensumme in der EF.2 */
	protected int wochenstundenEF2;

	/** Wochenstundensumme in der Q1.1 */
	protected int wochenstundenQ11;

	/** Wochenstundensumme in der Q1.2 */
	protected int wochenstundenQ12;

	/** Wochenstundensumme in der Q2.1 */
	protected int wochenstundenQ21;

	/** Wochenstundensumme in der Q2.2 */
	protected int wochenstundenQ22;

	/** Wochenstundendurchschnitt in der EF */
	protected double wochenstundenDurchschnittEF;

	/** Wochenstundendurchschnitt in der Q1 */
	protected double wochenstundenDurchschnittQ1;

	/** Wochenstundendurchschnitt in der Q2 */
	protected double wochenstundenDurchschnittQ2;

	/** Wochenstundendurchschnitt in der Qualifikationsphase */
	protected double wochenstundenDurchschnittQPh;

	/** Wochenstundensumme der gesamten Laufbahn */
	protected double wochenstundenGesamt;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param abiturjahr Das Kalenderjahr, in dem die Abiturprüfung stattfindet
	 * @param aktuelleKlasse Die aktuelle Klasse zum aktuellen Halbjahr der Oberstufenlaufbahn
	 * @param aktuellesGOStHalbjahr Das Halbjahr der Oberstufenlaufbahn gemäß Halbjahr der Schule
	 * @param auswahlKlasse Die Klasse zum ausgewählten Halbjahr der Oberstufenlaufbahn
	 * @param auswahlGOStHalbjahr Das ausgewählte Halbjahr der Oberstufenlaufbahn
	 * @param beratungsbogenText Der Text der Schule für den Beratungsbogen
	 * @param beratungslehrkraefte Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt
	 * @param folgeAktuellesGOStHalbjahr Das folgende Halbjahr der Oberstufenlaufbahn auf das aktuelle Halbjahr, also in der Regel das Halbjahr, für das die Beratung erfolgt
	 * @param folgeAuswahlGOStHalbjahr Das folgende Halbjahr der Oberstufenlaufbahn auf das ausgewählte Halbjahr, also in der Regel das Halbjahr, für das die Beratung erfolgt
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
	public ReportingSchuelerGostLaufbahnplanung(final int abiturjahr, final String aktuelleKlasse, final String aktuellesGOStHalbjahr,
			final String auswahlKlasse, final String auswahlGOStHalbjahr, final String beratungsbogenText, final List<ReportingLehrer> beratungslehrkraefte,
			final String emailText, final List<ReportingGostLaufbahnplanungFachwahl> fachwahlen, final List<ReportingGostLaufbahnplanungErgebnismeldung> fehler,
			final String folgeAktuellesGOStHalbjahr, final String folgeAuswahlGOStHalbjahr, final List<ReportingGostLaufbahnplanungErgebnismeldung> hinweise,
			final String letzteBeratungDatum, final ReportingLehrer letzteBeratungLehrkraft, final String letzterRuecklaufDatum, final String kommentar,
			final int kursanzahlEF1, final int kursanzahlEF2, final int kursanzahlQ11, final int kursanzahlQ12, final int kursanzahlQ21,
			final int kursanzahlQ22, final int kursanzahlQPh, final String pruefungsordnung, final int wochenstundenEF1, final int wochenstundenEF2,
			final int wochenstundenQ11, final int wochenstundenQ12, final int wochenstundenQ21, final int wochenstundenQ22,
			final double wochenstundenDurchschnittEF, final double wochenstundenDurchschnittQ1, final double wochenstundenDurchschnittQ2,
			final double wochenstundenDurchschnittQPh, final double wochenstundenGesamt) {
		this.abiturjahr = abiturjahr;
		this.aktuellesGOStHalbjahr = aktuellesGOStHalbjahr;
		this.aktuelleKlasse = aktuelleKlasse;
		this.auswahlGOStHalbjahr = auswahlGOStHalbjahr;
		this.auswahlKlasse = auswahlKlasse;
		this.beratungsbogenText = beratungsbogenText;
		this.beratungslehrkraefte = beratungslehrkraefte;
		this.emailText = emailText;
		this.fachwahlen = fachwahlen;
		this.fehler = fehler;
		this.folgeAktuellesGOStHalbjahr = folgeAktuellesGOStHalbjahr;
		this.folgeAuswahlGOStHalbjahr = folgeAuswahlGOStHalbjahr;
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
		if ((letzteBeratungDatum != null) && !letzteBeratungDatum.isBlank() && !letzteBeratungDatum.isEmpty()) {
			letzteBeratung = letzteBeratung + " am "
					+ LocalDate.parse(letzteBeratungDatum, DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			hatLetzteBeratung = true;
		}
		if (letzteBeratungLehrkraft != null) {
			letzteBeratung = letzteBeratung + " von " + letzteBeratungLehrkraft.unterschriftfeld();
			hatLetzteBeratung = true;
		}
		letzteBeratung = Boolean.TRUE.equals(hatLetzteBeratung) ? (letzteBeratung + ".") : "";
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




	// ##### Getter #####

	/**
	 * Das Kalenderjahr, in dem die Abiturprüfung stattfindet
	 * @return Inhalt des Feldes abiturjahr
	 */
	public int abiturjahr() {
		return abiturjahr;
	}

	/**
	 * Die aktuelle Klasse zum aktuellen Halbjahr der Oberstufenlaufbahn
	 * @return Inhalt des Feldes aktuelleKlasse
	 */
	public String aktuelleKlasse() {
		return aktuelleKlasse;
	}

	/**
	 * Das Halbjahr der Oberstufenlaufbahn gemäß Halbjahr der Schule
	 * @return Inhalt des Feldes aktuellesGOStHalbjahr
	 */
	public String aktuellesGOStHalbjahr() {
		return aktuellesGOStHalbjahr;
	}

	/**
	 * Die Klasse zum ausgewählten Halbjahr der Oberstufenlaufbahn
	 * @return Inhalt des Feldes auswahlKlasse
	 */
	public String auswahlKlasse() {
		return auswahlKlasse;
	}

	/**
	 * Das ausgewählte Halbjahr der Oberstufenlaufbahn
	 * @return Inhalt des Feldes auswahlGOStHalbjahr
	 */
	public String auswahlGOStHalbjahr() {
		return auswahlGOStHalbjahr;
	}

	/**
	 * Der Text der Schule für den Beratungsbogen
	 * @return Inhalt des Feldes beratungsbogenText
	 */
	public String beratungsbogenText() {
		return beratungsbogenText;
	}

	/**
	 * Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt
	 * @return Inhalt des Feldes beratungslehrkraefte
	 */
	public List<ReportingLehrer> beratungslehrkraefte() {
		this.beratungslehrkraefte.sort(Comparator.comparing(ReportingLehrer::nachname).thenComparing(ReportingLehrer::vorname));
		return beratungslehrkraefte;
	}

	/**
	 * Der Text der Schule für den E-Mail-Versand
	 * @return Inhalt des Feldes emailText
	 */
	public String emailText() {
		return emailText;
	}

	/**
	 * Eine Liste vom Typ Fachwahl, die alle Fachwahlen und deren Daten enthält.
	 * @return Inhalt des Feldes fachwahlen
	 */
	public List<ReportingGostLaufbahnplanungFachwahl> fachwahlen() {
		return fachwahlen;
	}

	/**
	 * Eine Liste vom Typ Fehler, die alle Fehler zur Laufbahn und deren Daten enthält.
	 * @return Inhalt des Feldes fehler
	 */
	public List<ReportingGostLaufbahnplanungErgebnismeldung> fehler() {
		return fehler;
	}

	/**
	 * Das folgende Halbjahr der Oberstufenlaufbahn auf das aktuelle Halbjahr, also in der Regel das Halbjahr, für das die Beratung erfolgt
	 * @return Inhalt des Feldes folgeAktuellesGOStHalbjahr
	 */
	public String folgeAktuellesGOStHalbjahr() {
		return folgeAktuellesGOStHalbjahr;
	}

	/**
	 * Das folgende Halbjahr der Oberstufenlaufbahn auf das ausgewählte Halbjahr, also in der Regel das Halbjahr, für das die Beratung erfolgt
	 * @return Inhalt des Feldes folgeAuswahlGOStHalbjahr
	 */
	public String folgeAuswahlGOStHalbjahr() {
		return folgeAuswahlGOStHalbjahr;
	}

	/**
	 * Eine Liste vom Typ Hinweise, die alle Hinweise zur Laufbahn und deren Daten enthält.
	 * @return Inhalt des Feldes hinweise
	 */
	public List<ReportingGostLaufbahnplanungErgebnismeldung> hinweise() {
		return hinweise;
	}

	/**
	 * Das Datum der letzten Beratung
	 * @return Inhalt des Feldes letzteBeratungDatum
	 */
	public String letzteBeratungDatum() {
		return letzteBeratungDatum;
	}

	/**
	 * Die Lehrkraft der letzten Beratung
	 * @return Inhalt des Feldes letzteBeratungLehrkraft
	 */
	public ReportingLehrer letzteBeratungLehrkraft() {
		return letzteBeratungLehrkraft;
	}

	/**
	 * Das Datum des Rücklaufes der letzten importierten Wahldatei
	 * @return Inhalt des Feldes letzterRuecklaufDatum
	 */
	public String letzterRuecklaufDatum() {
		return letzterRuecklaufDatum;
	}

	/**
	 * Kommentar der Schule zur Laufbahn
	 * @return Inhalt des Feldes kommentar
	 */
	public String kommentar() {
		return kommentar;
	}

	/**
	 * Kursanzahl in der EF.1
	 * @return Inhalt des Feldes kursanzahlEF1
	 */
	public int kursanzahlEF1() {
		return kursanzahlEF1;
	}

	/**
	 * Kursanzahl in der EF.2
	 * @return Inhalt des Feldes kursanzahlEF2
	 */
	public int kursanzahlEF2() {
		return kursanzahlEF2;
	}

	/**
	 * Kursanzahl in der Q1.1
	 * @return Inhalt des Feldes kursanzahlQ11
	 */
	public int kursanzahlQ11() {
		return kursanzahlQ11;
	}

	/**
	 * Kursanzahl in der Q1.2
	 * @return Inhalt des Feldes kursanzahlQ12
	 */
	public int kursanzahlQ12() {
		return kursanzahlQ12;
	}

	/**
	 * Kursanzahl in der Q2.1
	 * @return Inhalt des Feldes kursanzahlQ21
	 */
	public int kursanzahlQ21() {
		return kursanzahlQ21;
	}

	/**
	 * Kursanzahl in der Q2.2
	 * @return Inhalt des Feldes kursanzahlQ22
	 */
	public int kursanzahlQ22() {
		return kursanzahlQ22;
	}

	/**
	 * Kursanzahl in der Qualifikationsphase
	 * @return Inhalt des Feldes kursanzahlQPh
	 */
	public int kursanzahlQPh() {
		return kursanzahlQPh;
	}

	/**
	 * Kursanzahl der aus der Qualifikationsphase anrechenbaren Kurse für Block I
	 * @return Inhalt des Feldes kursanzahlAnrechenbarBlockI
	 */
	public int kursanzahlAnrechenbarBlockI() {
		return kursanzahlAnrechenbarBlockI;
	}

	/**
	 * Die Prüfungsordnung des Schülers aus dem aktuellen Lernabschnitt
	 * @return Inhalt des Feldes pruefungsordnung
	 */
	public String pruefungsordnung() {
		return pruefungsordnung;
	}

	/**
	 * Wochenstundensumme in der EF.1
	 * @return Inhalt des Feldes wochenstundenEF1
	 */
	public int wochenstundenEF1() {
		return wochenstundenEF1;
	}

	/**
	 * Wochenstundensumme in der EF.2
	 * @return Inhalt des Feldes wochenstundenEF2
	 */
	public int wochenstundenEF2() {
		return wochenstundenEF2;
	}

	/**
	 * Wochenstundensumme in der Q1.1
	 * @return Inhalt des Feldes wochenstundenQ11
	 */
	public int wochenstundenQ11() {
		return wochenstundenQ11;
	}

	/**
	 * Wochenstundensumme in der Q1.2
	 * @return Inhalt des Feldes wochenstundenQ12
	 */
	public int wochenstundenQ12() {
		return wochenstundenQ12;
	}

	/**
	 * Wochenstundensumme in der Q2.1
	 * @return Inhalt des Feldes wochenstundenQ21
	 */
	public int wochenstundenQ21() {
		return wochenstundenQ21;
	}

	/**
	 * Wochenstundensumme in der Q2.2
	 * @return Inhalt des Feldes wochenstundenQ22
	 */
	public int wochenstundenQ22() {
		return wochenstundenQ22;
	}

	/**
	 * Wochenstundendurchschnitt in der EF
	 * @return Inhalt des Feldes wochenstundenDurchschnittEF
	 */
	public double wochenstundenDurchschnittEF() {
		return wochenstundenDurchschnittEF;
	}

	/**
	 * Wochenstundendurchschnitt in der Q1
	 * @return Inhalt des Feldes wochenstundenDurchschnittQ1
	 */
	public double wochenstundenDurchschnittQ1() {
		return wochenstundenDurchschnittQ1;
	}

	/**
	 * Wochenstundendurchschnitt in der Q2
	 * @return Inhalt des Feldes wochenstundenDurchschnittQ2
	 */
	public double wochenstundenDurchschnittQ2() {
		return wochenstundenDurchschnittQ2;
	}

	/**
	 * Wochenstundendurchschnitt in der Qualifikationsphase
	 * @return Inhalt des Feldes wochenstundenDurchschnittQPh
	 */
	public double wochenstundenDurchschnittQPh() {
		return wochenstundenDurchschnittQPh;
	}

	/**
	 * Wochenstundensumme der gesamten Laufbahn
	 * @return Inhalt des Feldes wochenstundenGesamt
	 */
	public double wochenstundenGesamt() {
		return wochenstundenGesamt;
	}


}
