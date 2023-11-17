package de.svws_nrw.module.pdf.drucktypes;

import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerFachwahlen;
import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerFehler;
import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerHinweise;
import de.svws_nrw.core.data.schueler.SchuelerStammdaten;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse enthält die Grunddaten eines Schülers und dessen Daten für die GOSt-Laufbahnplanung.
 * Sie ist abgeleitet von der Druckklasse DruckSchueler.
 */

public class DruckGostLaufbahnplanungSchueler extends DruckSchueler {

	/**
	 * Weist die Daten zu den Feldern der Elternklasse zu, wenn ein Elternklassenobjekt übergeben wird.
	 *
	 * @param schuelerStammdaten Daten in Form eines Objektes der Elternklasse
	 */
	public DruckGostLaufbahnplanungSchueler(final SchuelerStammdaten schuelerStammdaten) {
		super(schuelerStammdaten);
	}


	/** Das Schuljahr, in welchem der Schuljahresabschnitt liegt */
    public int abiturjahr = -1;

	/** Das Halbjahr der Oberstufenlaufbahn, in dem die Beratung erfolgt */
	public String aktuellesGOStHalbjahr = "";

	/** Die aktuelle Klasse zum aktuellen Halbjahr der Oberstufenlaufbahn */
	public String aktuelleKlasse = "";

	/** Die Prüfungsordnung des Schülers aus dem aktuellen Lernabschnitt */
	public String pruefungsordnung = "";

	/** Das Halbjahr der Oberstufenlaufbahn, für das die Beratung erfolgt */
	public String beratungsGOStHalbjahr = "";

	/** Der Text der Schule für den Beratungsbogen */
	public String beratungsbogenText = "";

	/** Der Text der Schule für den E-Mail-Versand */
	public String emailText = "";

	/** Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt */
	public String beratungslehrkraefte = "";

	/** Die Lehrkraft der letzten Beratung */
	public String beratungslehrkraft = "";

	/** Das Datum des Rücklaufes der letzten importierten Wahldatei */
	public String ruecklaufdatum = "";

	/** Das Datum der letzten Beratung */
	public String beratungsdatum = "";

	/** Das Datum der letzten Beratung */
	public String letzteBeratungText = "";

	/** Kommentar der Schule zur Laufbahn */
    public String kommentar = "";

	/** Kursanzahl in der EF.1 */
	public int kursanzahlEF1 = 0;

	/** Kursanzahl in der EF.2 */
	public int kursanzahlEF2 = 0;

	/** Kursanzahl in der Q1.1 */
	public int kursanzahlQ11 = 0;

	/** Kursanzahl in der Q1.2 */
	public int kursanzahlQ12 = 0;

	/** Kursanzahl in der Q2.1 */
	public int kursanzahlQ21 = 0;

	/** Kursanzahl in der Q2.2 */
	public int kursanzahlQ22 = 0;

	/** Kursanzahl in der Qualifikationsphase */
	public int kursanzahlQPh = 0;

	/** Wochenstundensumme in der EF.1 */
	public int wochenstundenEF1 = 0;

	/** Wochenstundensumme in der EF.2 */
	public int wochenstundenEF2 = 0;

	/** Wochenstundensumme in der Q1.1 */
	public int wochenstundenQ11 = 0;

	/** Wochenstundensumme in der Q1.2 */
	public int wochenstundenQ12 = 0;

	/** Wochenstundensumme in der Q2.1 */
	public int wochenstundenQ21 = 0;

	/** Wochenstundensumme in der Q2.2 */
	public int wochenstundenQ22 = 0;

	/** Wochenstundendurchschnitt in der EF */
	public double wochenstundenDurchschnittEF = 0;

	/** Wochenstundendurchschnitt in der EF */
	public double wochenstundenDurchschnittQ1 = 0;

	/** Wochenstundendurchschnitt in der EF */
	public double wochenstundenDurchschnittQ2 = 0;

	/** Wochenstundendurchschnitt in der Qualifikationsphase */
	public double wochenstundenDurchschnittQPh = 0;

	/** Wochenstundensumme der gesamten Laufbahn */
	public double wochenstundenGesamt = 0;

	/** Eine Liste vom Typ Fachwahlen, die alle Fachwahlen und deren Daten enthält. */
	public List<DruckGostLaufbahnplanungSchuelerFachwahlen> Fachwahlen = new ArrayList<>();

	/** Eine Liste vom Typ Fehler, die alle Fehler zur Laufbahn und deren Daten enthält. */
	public List<DruckGostLaufbahnplanungSchuelerFehler> Fehler = new ArrayList<>();

	/** Eine Liste vom Typ Hinweise, die alle Hinweise zur Laufbahn und deren Daten enthält. */
	public List<DruckGostLaufbahnplanungSchuelerHinweise> Hinweise = new ArrayList<>();

}
