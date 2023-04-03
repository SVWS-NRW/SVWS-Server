package de.svws_nrw.core.data.schueler;

import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse liefert die allgemeinen Angaben zu dem Lernabschnitt eines Schülers zurück.
 */
@XmlRootElement
@Schema(description = "Die allgemeinen Angaben zu dem Lernabschnitt eines Schülers.")
@TranspilerDTO
public class SchuelerLernabschnittsdaten {

	/** Die ID des Lernabschnitts in der Datenbank. */
	@Schema(description = "die ID des Lernabschnitts in der Datenbank", example = "126784")
	public long id;

	/** Die ID des Schülers, zu dem diese Lernabschnittdaten gehören. */
	@Schema(description = "die ID des Schülers, zu dem diese Lernabschnittdaten gehören", example = "4785")
	public long schuelerID;

	/** Die ID des Schuljahresabschnitts, zu welchem diese Lernabschnittdaten gehören. */
	@Schema(description = "die ID des Schuljahresabschnitts, zu welchem diese Lernabschnittdaten gehören", example = "42")
	public long schuljahresabschnitt;

	/** Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitt in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.). */
	@Schema(description = "eine Nr, zur Unterscheidung von Lernabschnissdaten, wenn beim Schüler mehrere Lernabschnitt in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.)", example = "NULL")
	public Integer wechselNr = null;

	// TODO Version 1.x: Prüfen ob die Datumsangaben des Lernabschnitts nicht besser in der Klassen- oder Jahrgangstabelle des Schuljahresabschnitts aufgehoben ist
	/** Das Datum, wann der Lernabschnitt beginnt */
	@Schema(description = "das Datum, wann der Lernabschnitt beginnt", example = "NULL")
	public String datumAnfang = null;

	/** Das Datum, wann der Lernabschnitt endet */
	@Schema(description = "das Datum, wann der Lernabschnitt endet", example = "NULL")
	public String datumEnde = null;

	/** Das Datum der Konferenz */
	@Schema(description = "das Datum der Konferenz", example = "NULL")
	public String datumKonferenz = null;

	/** Das Datum des Zeugnisses bzw. der Laufbahnbescheinigung */
	@Schema(description = "das Datum der Zeugnisses bzw. der Laufbahnbescheinigung", example = "NULL")
	public String datumZeugnis = null;

	// TODO Version 1.x: Prüfen, ob der Wert beim Lernabschnitt gespeichert wird oder sich besser automatisch ermitteln lässt
	/** Die Anzahl der Schulbesuchsjahre */
	@Schema(description = "die Anzahl der Schulbesuchsjahre", example = "NULL")
	public Integer anzahlSchulbesuchsjahre = null;

	/** Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht */
	@Schema(description = "gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht", example = "true")
	public boolean istGewertet = true;

	/** Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht */
	@Schema(description = "gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht", example = "false")
	public boolean istWiederholung = false;

	/** Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist. */
	@Schema(description = "die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist.", example = "APO-GOSt(B)10/G8")
	public @NotNull String pruefungsOrdnung = "";

	/** Die ID der Klasse des Schülers. */
	@Schema(description = "die ID der Klasse des Schülers", example = "46")
	public long klassenID = -1;

	/** Die ID eines Tutors, der den Schüler betreut, oder null, falls keiner zugewiesen ist */
	@Schema(description = "die ID eines Tutors, der den Schüler betreut, oder null, falls keiner zugewiesen ist", example = "null")
	public Long tutorID = null;

	/** Die ID der Folge-Klasse des Schülers, sofern dieser vom Standard der Klassentabelle abweicht. */
	@Schema(description = "die ID der Folge-Klasse des Schülers, sofern dieser vom Standard der Klassentabelle abweicht", example = "59")
	public Long folgeklassenID = null;

	/** Das Kürzel der Schulgliederung bzw. des Bildungsgangs des Schülers. */
	@Schema(description = "das Kürzel der Schulgliederung bzw. des Bildungsgangs des Schülers", example = "B09")
	public String schulgliederung;

	/** Die ID des Jahrgangs des Schülers */
	@Schema(description = "die ID des Jahrgangs des Schülers", example = "78")
	public long jahrgangID = -1;

	/** Die ID der Fachklasse des Schülers an einem Berufskolleg */
	@Schema(description = "die ID der Fachklasse des Schülers an einem Berufskolleg", example = "null")
	public Long fachklasseID = null;

	/** Der Schwerpunkt eines Schülers laut dem Schwerpunkt-Katalog */
	@Schema(description = "der Schwerpunkt eines Schülers laut dem Schwerpunkt-Katalog", example = "null")
	public Long schwerpunktID = null;

	/** Das Kürzel der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag - siehe Core-Type) */
	@Schema(description = "das Kürzel der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag)", example = "null")
	public String organisationsform = null;

	/** Das Kürzel der Klassenart in Bezug auf den Schüler (z.B. Regelklasse - siehe Core-Type) */
	@Schema(description = "das Kürzel der Klassenart in Bezug auf den Schüler (z.B. Regelklasse)", example = "null")
	public String Klassenart = "RK";

	/** Die Summe der Gesamtfehlstunden für den gesamten Lernabschnitt */
	@Schema(description = "die Summe der Gesamtfehlstunden für den gesamten Lernabschnitt", example = "0")
	public int fehlstundenGesamt = 0;

	/** Die Summe der unentschuldigten Fehlstunden für den gesamten Lernabschnitt */
	@Schema(description = "die Summe der unentschuldigten Fehlstunden für den gesamten Lernabschnitt", example = "0")
	public int fehlstundenUnentschuldigt = 0;

	/** Der Grenzwert für die Fehlstunden, ab dem am Berufskolleg Warnbriefe zur Entlassung verschickt werden */
	@Schema(description = "der Grenzwert für die Fehlstunden, ab dem am Berufskolleg Warnbriefe zur Entlassung verschickt werden", example = "null")
	public Integer fehlstundenGrenzwert = null;

	/** Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht */
	@Schema(description = "gibt an, ob eine Schwerbehinderung nachgwiesen ist oder nicht", example = "false")
	public boolean hatSchwerbehinderungsNachweis = false;

	/** Gibt an, ob eine Förderung nach der Ausbildungsordnung Sonderpädagogischer Förderung (AOSF) vorliegt */
	@Schema(description = "gibt an, ob eine Förderung nach der Ausbildungsordnung Sonderpädagogischer Förderung (AOSF) vorliegt", example = "false")
	public boolean hatAOSF = false;

	/** Gibt an, ob eine Diagnose zu Autismus vorliegt oder nicht */
	@Schema(description = "gibt an, ob eine Diagnose zu Autismus vorliegt oder nicht", example = "false")
	public boolean hatAutismus = false;

	/** Gibt an, ob zieldifferent unterrichet wird oder nicht */
	@Schema(description = "gibt an, ob zieldifferent unterrichet wird oder nicht", example = "false")
	public boolean hatZieldifferentenUnterricht = false;

	/** Die ID des Haupförderschwerpunktes des Schülers */
	@Schema(description = "die ID des Haupförderschwerpunktes des Schülers", example = "null")
	public Long foerderschwerpunkt1ID = null;

	/** Die ID des weiteren Förderschwerpunktes des Schülers */
	@Schema(description = "die ID des weiteren Förderschwerpunktes des Schülers", example = "null")
	public Long foerderschwerpunkt2ID = null;

	/** Die ID eines Sonderpädagogen, der den Schüler betreut und auch im Notenmodul hat */
	@Schema(description = "die ID eines Sonderpädagogen, der den Schüler betreut und auch im Notenmodul hat", example = "null")
	public Long sonderpaedagogeID = null;

	/** Die Sprache des bilngualen Zweigs, falls der Schüler im bilingualen Zweig unterrichtet wird */
	@Schema(description = "die Sprache des bilngualen Zweigs, falls der Schüler im bilingualen Zweig unterrichtet wird", example = "null")
	public String bilingualerZweig = null;

	/** Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 ausreichend sind für Versetzung */
	@Schema(description = "gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 ausreichend sind für Versetzung", example = "true")
	public boolean istFachpraktischerAnteilAusreichend = true;

	/** Das Kürzel des Versetzungsvermerks */
	@Schema(description = "das Kürzel des Versetzungsvermerks", example = "null")
	public String versetzungsvermerk = null;

	/** Die Durchschnittsnote in diesem Lernabschnitt - wird ggf. von einem Prüfungsalgorithmus gesetzt und kann dann ausgelesen werden */
	@Schema(description = "die Durchschnittsnote in diesem Lernabschnitt - wird ggf. von einem Prüfungsalgorithmus gesetzt und kann dann ausgelesen werden", example = "null")
	public String noteDurchschnitt = null;

	/** Die Lernbereichnote Gesellschaftswissenschaft oder Arbeitlehre für den Hauptschulabschluss nach Klassen 10 */
	@Schema(description = "die Lernbereichnote Gesellschaftswissenschaft oder Arbeitlehre für den Hauptschulabschluss nach Klassen 10", example = "null")
	public Integer noteLernbereichGSbzwAL = null;

	/** Die Lernbereichnote Naturwissenschaft für den Hauptschulabschluss nach Klassen 10 */
	@Schema(description = "die Lernbereichnote Naturwissenschaft für den Hauptschulabschluss nach Klassen 10", example = "null")
	public Integer noteLernbereichNW = null;

	/** Die Art des Abschlusses (siehe Katalog) */
	@Schema(description = "die Art des Abschlusses", example = "null")
	public Integer abschlussart = null;

	/** Gibt an, ob der berechnete Abschluss eine Prognose ist oder nicht (siehe Katalog) */
	@Schema(description = "gibt an, ob der berechnete Abschluss eine Prognose ist oder nicht", example = "false")
	public boolean istAbschlussPrognose = false;

	/** Der erreichte allgemeinbildende Abschluss */
	@Schema(description = "der erreichte allgemeinbildende Abschluss", example = "null")
	public String abschluss = null;

	/** Der erreichte berufsbezogene Abschluss am Berufskolleg */
	@Schema(description = "der erreichte berufsbezogene Abschluss am Berufskolleg", example = "null")
	public String abschlussBerufsbildend = null;

	/** Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung */
	@Schema(description = "die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung", example = "null")
	public String textErgebnisPruefungsalgorithmus = null;

	/** Die Art des Zeugnisses */
	@Schema(description = "die Art des Zeugnisses", example = "null")
	public String zeugnisart = null;


	/** Die Informationen den Nachprüfungen in diesem Lernabschnitt oder null, falls keine vorhanden sind. */
	@Schema(description = "die Informationen den Nachprüfungen in diesem Lernabschnitt oder null, falls keine vorhanden sind.", example = "null")
	public SchuelerLernabschnittNachpruefungsdaten nachpruefungen = null;

	/** Die Bemerkungen in diesem Lernabschnitt. */
	@Schema(description = "die Bemerkungen in diesem Lernabschnitt")
	public @NotNull SchuelerLernabschnittBemerkungen bemerkungen = new SchuelerLernabschnittBemerkungen();

	/** Die Leistungsdaten des Schülers in diesem Lernabschnitt. */
	@ArraySchema(schema = @Schema(implementation = SchuelerLeistungsdaten.class, description = "Ein Array mit den Leistungsdaten des Schülers in diesem Lernabschnitt."))
	public @NotNull Vector<@NotNull SchuelerLeistungsdaten> leistungsdaten = new Vector<>();

}
