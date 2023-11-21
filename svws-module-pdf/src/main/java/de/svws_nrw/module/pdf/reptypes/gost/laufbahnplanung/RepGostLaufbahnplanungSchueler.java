package de.svws_nrw.module.pdf.reptypes.gost.laufbahnplanung;

import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.module.pdf.reptypes.RepSchueler;

import java.util.List;

/**
 * Die Klasse enthält die Grunddaten eines Schülers und dessen Daten für die GOSt-Laufbahnplanung.
 * Sie ist abgeleitet von der Druckklasse DruckSchueler.
 */

public class RepGostLaufbahnplanungSchueler extends RepSchueler {

	/** Das Schuljahr, in welchem der Schuljahresabschnitt liegt */
    public int abiturjahr;

	/** Das Halbjahr der Oberstufenlaufbahn, in dem die Beratung erfolgt */
	public String aktuellesGOStHalbjahr;

	/** Die aktuelle Klasse zum aktuellen Halbjahr der Oberstufenlaufbahn */
	public String aktuelleKlasse;

	/** Die Prüfungsordnung des Schülers aus dem aktuellen Lernabschnitt */
	public String pruefungsordnung;

	/** Das Halbjahr der Oberstufenlaufbahn, für das die Beratung erfolgt */
	public String beratungsGOStHalbjahr;

	/** Der Text der Schule für den Beratungsbogen */
	public String beratungsbogenText;

	/** Der Text der Schule für den E-Mail-Versand */
	public String emailText;

	/** Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt */
	public String beratungslehrkraefte;

	/** Die Lehrkraft der letzten Beratung */
	public String beratungslehrkraft;

	/** Das Datum des Rücklaufes der letzten importierten Wahldatei */
	public String ruecklaufdatum;

	/** Das Datum der letzten Beratung */
	public String beratungsdatum;

	/** Text zur letzten Beratung */
	public String letzteBeratungText;

	/** Kommentar der Schule zur Laufbahn */
    public String kommentar;

	/** Kursanzahl in der EF.1 */
	public int kursanzahlEF1;

	/** Kursanzahl in der EF.2 */
	public int kursanzahlEF2;

	/** Kursanzahl in der Q1.1 */
	public int kursanzahlQ11;

	/** Kursanzahl in der Q1.2 */
	public int kursanzahlQ12;

	/** Kursanzahl in der Q2.1 */
	public int kursanzahlQ21;

	/** Kursanzahl in der Q2.2 */
	public int kursanzahlQ22;

	/** Kursanzahl in der Qualifikationsphase */
	public int kursanzahlQPh;

	/** Wochenstundensumme in der EF.1 */
	public int wochenstundenEF1;

	/** Wochenstundensumme in der EF.2 */
	public int wochenstundenEF2;

	/** Wochenstundensumme in der Q1.1 */
	public int wochenstundenQ11;

	/** Wochenstundensumme in der Q1.2 */
	public int wochenstundenQ12;

	/** Wochenstundensumme in der Q2.1 */
	public int wochenstundenQ21;

	/** Wochenstundensumme in der Q2.2 */
	public int wochenstundenQ22;

	/** Wochenstundendurchschnitt in der EF */
	public double wochenstundenDurchschnittEF;

	/** Wochenstundendurchschnitt in der Q1 */
	public double wochenstundenDurchschnittQ1;

	/** Wochenstundendurchschnitt in der Q2 */
	public double wochenstundenDurchschnittQ2;

	/** Wochenstundendurchschnitt in der Qualifikationsphase */
	public double wochenstundenDurchschnittQPh;

	/** Wochenstundensumme der gesamten Laufbahn */
	public double wochenstundenGesamt;

	/** Eine Liste vom Typ Fachwahl, die alle Fachwahlen und deren Daten enthält. */
	public List<RepGostLaufbahnplanungFachwahl> Fachwahlen;

	/** Eine Liste vom Typ Fehler, die alle Fehler zur Laufbahn und deren Daten enthält. */
	public List<RepGostLaufbahnplanungErgebnismeldung> Fehler;

	/** Eine Liste vom Typ Hinweise, die alle Hinweise zur Laufbahn und deren Daten enthält. */
	public List<RepGostLaufbahnplanungErgebnismeldung> Hinweise;


	/**
	 * Erstellt einen neuen Schüler für die GOSt-Laufbahnplanung.
	 *
	 * @param schuelerStammdaten Das Schuljahr, in welchem der Schuljahresabschnitt liegt
	 * @param abiturjahr Das Halbjahr der Oberstufenlaufbahn, in dem die Beratung erfolgt
	 * @param aktuellesGOStHalbjahr Die aktuelle Klasse zum aktuellen Halbjahr der Oberstufenlaufbahn
	 * @param aktuelleKlasse Die Prüfungsordnung des Schülers aus dem aktuellen Lernabschnitt
	 * @param pruefungsordnung Das Halbjahr der Oberstufenlaufbahn, für das die Beratung erfolgt
	 * @param beratungsGOStHalbjahr Der Text der Schule für den Beratungsbogen
	 * @param beratungsbogenText Der Text der Schule für den E-Mail-Versand
	 * @param emailText Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt
	 * @param beratungslehrkraefte Die Lehrkraft der letzten Beratung
	 * @param beratungslehrkraft Das Datum des Rücklaufes der letzten importierten Wahldatei
	 * @param ruecklaufdatum Das Datum der letzten Beratung
	 * @param beratungsdatum Das Datum der letzten Beratung
	 * @param letzteBeratungText Text zur letzten Beratung
	 * @param kommentar Kommentar der Schule zur Laufbahn
	 * @param kursanzahlEF1 Kursanzahl in der EF.1
	 * @param kursanzahlEF2 Kursanzahl in der EF.2
	 * @param kursanzahlQ11 Kursanzahl in der Q1.1
	 * @param kursanzahlQ12 Kursanzahl in der Q1.2
	 * @param kursanzahlQ21 Kursanzahl in der Q2.1
	 * @param kursanzahlQ22 Kursanzahl in der Q2.2
	 * @param kursanzahlQPh Kursanzahl in der Qualifikationsphase
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
	 * @param fachwahlen Eine Liste vom Typ Fachwahl, die alle Fachwahlen und deren Daten enthält.
	 * @param fehler Eine Liste vom Typ Fehler, die alle Fehler zur Laufbahn und deren Daten enthält.
	 * @param hinweise Eine Liste vom Typ Hinweise, die alle Hinweise zur Laufbahn und deren Daten enthält.
	 */
	public RepGostLaufbahnplanungSchueler(final SchuelerStammdaten schuelerStammdaten,
										  final int abiturjahr,
										  final String aktuellesGOStHalbjahr,
										  final String aktuelleKlasse,
										  final String pruefungsordnung,
										  final String beratungsGOStHalbjahr,
										  final String beratungsbogenText,
										  final String emailText,
										  final String beratungslehrkraefte,
										  final String beratungslehrkraft,
										  final String ruecklaufdatum,
										  final String beratungsdatum,
										  final String letzteBeratungText,
										  final String kommentar,
										  final int kursanzahlEF1, final int kursanzahlEF2, final int kursanzahlQ11, final int kursanzahlQ12, final int kursanzahlQ21, final int kursanzahlQ22, final int kursanzahlQPh,
										  final int wochenstundenEF1, final int wochenstundenEF2, final int wochenstundenQ11, final int wochenstundenQ12, final int wochenstundenQ21, final int wochenstundenQ22,
										  final double wochenstundenDurchschnittEF, final double wochenstundenDurchschnittQ1, final double wochenstundenDurchschnittQ2, final double wochenstundenDurchschnittQPh, final double wochenstundenGesamt,
										  final List<RepGostLaufbahnplanungFachwahl> fachwahlen,
										  final List<RepGostLaufbahnplanungErgebnismeldung> fehler,
										  final List<RepGostLaufbahnplanungErgebnismeldung> hinweise) {
		super(schuelerStammdaten);
		this.abiturjahr = abiturjahr;
		this.aktuellesGOStHalbjahr = aktuellesGOStHalbjahr;
		this.aktuelleKlasse = aktuelleKlasse;
		this.pruefungsordnung = pruefungsordnung;
		this.beratungsGOStHalbjahr = beratungsGOStHalbjahr;
		this.beratungsbogenText = beratungsbogenText;
		this.emailText = emailText;
		this.beratungslehrkraefte = beratungslehrkraefte;
		this.beratungslehrkraft = beratungslehrkraft;
		this.ruecklaufdatum = ruecklaufdatum;
		this.beratungsdatum = beratungsdatum;
		this.letzteBeratungText = letzteBeratungText;
		this.kommentar = kommentar;
		this.kursanzahlEF1 = kursanzahlEF1;
		this.kursanzahlEF2 = kursanzahlEF2;
		this.kursanzahlQ11 = kursanzahlQ11;
		this.kursanzahlQ12 = kursanzahlQ12;
		this.kursanzahlQ21 = kursanzahlQ21;
		this.kursanzahlQ22 = kursanzahlQ22;
		this.kursanzahlQPh = kursanzahlQPh;
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
		Fachwahlen = fachwahlen;
		Fehler = fehler;
		Hinweise = hinweise;
	}
}
