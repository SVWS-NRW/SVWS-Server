package de.svws_nrw.core.data.druck;

import de.svws_nrw.base.annotations.SchildReportingDate;
import de.svws_nrw.base.annotations.SchildReportingMemo;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse enthält den Core-DTO für die GOSt-Laufbahnplanung mit den Schüler-Grunddaten.
 */
@XmlRootElement
@Schema(description = "Die Daten zur GOSt-Laufbahnplanung eines Schülers")
@TranspilerDTO
public class DruckGostLaufbahnplanungSchueler {

    /** Die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören. */
    @Schema(description = "ID des Schülers, zu dem die Laufbahnplanungsdaten gehören", example = "4711")
    public long SchuelerID = -1;

	/** Nachname des Schülers. */
	@Schema(description = "Nachname des Schülers", example = "Mustermann")
	public @NotNull String Nachname = "";

	/** Vorname des Schülers. */
	@Schema(description = "Vorname des Schülers", example = "Max")
	public @NotNull String Vorname = "";

	/** Geschlecht des Schülers. */
	@Schema(description = "Geschlecht des Schülers", example = "m")
	public String Geschlecht = "";

	/** Geburtsdatum des Schülers. */
	@Schema(description = "Geburtsdatum des Schülers", example = "02.02.2007")
	public String Geburtsdatum = "";

	/** Externe Schulnummer des Schülers, wenn er den Status extern hat. */
	@Schema(description = "Externe Schulnummer des Schülers", example = "123456")
	public String ExterneSchulnummer = "";

	/** Das Schuljahr, in welchem der Schuljahresabschnitt liegt */
    @Schema(description = "Das Jahr, in welchem die Abiturprüfung stattfindet", example = "2024")
    public int Abiturjahr = -1;

	/** Das Halbjahr der Oberstufenlaufbahn, in dem die Beratung erfolgt */
	@Schema(description = "Das Halbjahr der Oberstufenlaufbahn, in dem sich der Schüler befindet", example = "Q1.1")
	public @NotNull String AktuellesGOStHalbjahr = "";

	/** Die aktuelle Klasse zum aktuellen Halbjahr der Oberstufenlaufbahn */
	@Schema(description = "Die aktuelle Klasse zum aktuellen Halbjahr der Oberstufenlaufbahn", example = "Q1")
	public @NotNull String AktuelleKlasse = "";

	/** Die Prüfungsordnung des Schülers aus dem aktuellen Lernabschnitt */
	@Schema(description = "Die Prüfungsordnung des Schülers aus dem aktuellen Lernabschnitt", example = "APO-GOSt")
	public @NotNull String Pruefungsordnung = "";

	/** Das Halbjahr der Oberstufenlaufbahn, für das die Beratung erfolgt */
	@Schema(description = "Das Halbjahr der Oberstufenlaufbahn, für das die Beratung erfolgt", example = "Q1.2")
	public @NotNull String BeratungsGOStHalbjahr = "";

	/** Der Text der Schule für den Beratungsbogen */
	@SchildReportingMemo
	@Schema(description = "Der Text der Schule für den Beratungsbogen", example = "Mit der Abgabe der folgenden Wahl ...")
	public @NotNull String Beratungsbogentext = "";

	/** Der Text der Schule für den E-Mail-Versand */
	@SchildReportingMemo
	@Schema(description = "Der Text der Schule für das versenden der Wahldateien mit einer E-Mail", example = "Mit dieser E-Mail ...")
	public @NotNull String Emailtext = "";

	/** Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt */
	@Schema(description = "Die Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt", example = "Klaus Müller; Erwin Meier")
	public @NotNull String Beratungslehrkraefte = "";

	/** Die Lehrkraft der letzten Beratung */
	@Schema(description = "Die Beratungslehrkraft der letzten Beratung oder bei dessen Fehlen die des Abiturjahrgangs durch ';' getrennt", example = "Klaus Müller; Erwin Meier")
	public @NotNull String Beratungslehrkraft = "";

	/** Das Datum des Rücklaufes der letzten importierten Wahldatei */
	@SchildReportingDate
	@Schema(description = "Das Datum der letzten Änderung", example = "2023-01-14")
	public @NotNull String Ruecklaufdatum = "";

	/** Das Datum der letzten Beratung */
	@SchildReportingDate
	@Schema(description = "Das Datum der letzten Beratung", example = "2023-01-15")
	public @NotNull String Beratungsdatum = "";

	/** Das Datum der letzten Beratung */
	@Schema(description = "Text mit dem Datum und der Lehrkraft der letzten Beratung", example = "Die letzte Beratung wurde durchgeführt am 16.05.2022 von ")
	public @NotNull String LetzteBeratungText = "";

	/** Kommentar der Schule zur Laufbahn */
	@SchildReportingMemo
	@Schema(description = "Kommentar der Schule zur Laufbahnplanung", example = "Wir empfehlen ...")
    public @NotNull String Kommentar = "";

	/** Kursanzahl in der EF.1 */
	@Schema(description = "Kursanzahl in der EF.1", example = "12")
	public int KursanzahlEF1 = 0;

	/** Kursanzahl in der EF.2 */
	@Schema(description = "Kursanzahl in der EF.2", example = "11")
	public int KursanzahlEF2 = 0;

	/** Kursanzahl in der Q1.1 */
	@Schema(description = "Kursanzahl in der Q1.1", example = "10")
	public int KursanzahlQ11 = 0;

	/** Kursanzahl in der Q1.2 */
	@Schema(description = "Kursanzahl in der Q1.2", example = "10")
	public int KursanzahlQ12 = 0;

	/** Kursanzahl in der Q2.1 */
	@Schema(description = "Kursanzahl in der Q2.1", example = "10")
	public int KursanzahlQ21 = 0;

	/** Kursanzahl in der Q2.2 */
	@Schema(description = "Kursanzahl in der Q2.2", example = "10")
	public int KursanzahlQ22 = 0;

	/** Kursanzahl in der Qualifikationsphase */
	@Schema(description = "Kursanzahl in der Qualifikationsphase", example = "40")
	public int KursanzahlQPh = 0;

	/** Wochenstundensumme in der EF.1 */
	@Schema(description = "Wochenstundensumme aller Fächer in der EF.1", example = "36")
	public int WochenstundenEF1 = 0;

	/** Wochenstundensumme in der EF.2 */
	@Schema(description = "Wochenstundensumme aller Fächer in der EF.2", example = "33")
	public int WochenstundenEF2 = 0;

	/** Wochenstundensumme in der Q1.1 */
	@Schema(description = "Wochenstundensumme aller Fächer in der Q1.1", example = "34")
	public int WochenstundenQ11 = 0;

	/** Wochenstundensumme in der Q1.2 */
	@Schema(description = "Wochenstundensumme aller Fächer in der Q1.2", example = "34")
	public int WochenstundenQ12 = 0;

	/** Wochenstundensumme in der Q2.1 */
	@Schema(description = "Wochenstundensumme aller Fächer in der Q2.1", example = "34")
	public int WochenstundenQ21 = 0;

	/** Wochenstundensumme in der Q2.2 */
	@Schema(description = "Wochenstundensumme aller Fächer in der Q2.2", example = "34")
	public int WochenstundenQ22 = 0;

	/** Wochenstundendurchschnitt in der EF */
	@Schema(description = "Wochenstundendurchschnitt in der EF", example = "34.5")
	public double WochenstundenDurchschnittEF = 0;

	/** Wochenstundendurchschnitt in der EF */
	@Schema(description = "Wochenstundendurchschnitt in der Q1", example = "37")
	public double WochenstundenDurchschnittQ1 = 0;

	/** Wochenstundendurchschnitt in der EF */
	@Schema(description = "Wochenstundendurchschnitt in der Q2", example = "34")
	public double WochenstundenDurchschnittQ2 = 0;

	/** Wochenstundendurchschnitt in der Qualifikationsphase */
	@Schema(description = "Wochenstundendurchschnitt in der Qualifikationsphase", example = "34")
	public double WochenstundenDurchschnittQPh = 0;

	/** Wochenstundensumme der gesamten Laufbahn */
	@Schema(description = "Wochenstundensumme aller Fächer der gesamten Laufbahn", example = "103.5")
	public double WochenstundenGesamt = 0;

	/** Eine Liste vom Typ Fachwahlen, die alle Fachwahlen und deren Daten enthält. */
	@Schema(description = "Liste der Fachwahl der Schülerin bzw des Schülers", example = "Kuerzel, ...")
	public @NotNull List<@NotNull DruckGostLaufbahnplanungSchuelerFachwahlen> Fachwahlen = new ArrayList<>();

	/** Eine Liste vom Typ Fehler, die alle Fehler zur Laufbahn und deren Daten enthält. */
	@Schema(description = "Liste der Belegungsfehler der Schülerinnen und Schüler", example = "Fehler, ...")
	public @NotNull List<@NotNull DruckGostLaufbahnplanungSchuelerFehler> Fehler = new ArrayList<>();

	/** Eine Liste vom Typ Hinweise, die alle Hinweise zur Laufbahn und deren Daten enthält. */
	@Schema(description = "Liste der Belegungshinweise der Schülerinnen und Schüler", example = "Hinweis, ...")
	public @NotNull List<@NotNull DruckGostLaufbahnplanungSchuelerHinweise> Hinweise = new ArrayList<>();
}
