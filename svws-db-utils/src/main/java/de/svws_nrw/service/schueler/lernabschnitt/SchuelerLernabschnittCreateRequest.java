package de.svws_nrw.service.schueler.lernabschnitt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import de.svws_nrw.validation.constraints.NotBlankIfPresent;
import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Enthält die Attribute, die beim Anlegen eines Schülerlernabschnitts über den {@link SchuelerLernabschnittService}
 * angegeben werden können. Pflicht sind die ID des Schülers und die ID des Schuljahresabschnitts. Alle weiteren
 * Attribute sind optional. Nicht angegebene Attribute werden mit ihren Standardwerten angelegt, ein Wert {@code null}
 * wird dabei wie ein nicht angegebenes Attribut behandelt. Ausnahme ist {@code idBetreuung}: Das Attribut ist nicht
 * nullable, der Standardwert wird direkt im Request gesetzt und ein expliziter Wert {@code null} wird abgelehnt.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SchuelerLernabschnittCreateRequest {

	/** Die ID des Schülers, zu dem der Lernabschnitt angelegt wird. */
	@Schema(description = "Die ID des Schülers, zu dem der Lernabschnitt angelegt wird.", example = "4711")
	@NotNull
	public Long schuelerID;

	/** Die ID des Schuljahresabschnitts, zu dem der Lernabschnitt angelegt wird. */
	@Schema(description = "Die ID des Schuljahresabschnitts, zu dem der Lernabschnitt angelegt wird.", example = "42")
	@NotNull
	public Long schuljahresabschnitt;

	/** Das Datum, wann der Lernabschnitt beginnt. */
	@Schema(description = "Das Datum, wann der Lernabschnitt beginnt.", example = "2024-08-01")
	@ValidDateFormat
	public String datumAnfang;

	/** Das Datum, wann der Lernabschnitt endet. */
	@Schema(description = "Das Datum, wann der Lernabschnitt endet.", example = "2025-01-31")
	@ValidDateFormat
	public String datumEnde;

	/** Das Datum der Konferenz. */
	@Schema(description = "Das Datum der Konferenz.", example = "2025-01-20")
	@ValidDateFormat
	public String datumKonferenz;

	/** Das Datum des Zeugnisses bzw. der Laufbahnbescheinigung. */
	@Schema(description = "Das Datum des Zeugnisses bzw. der Laufbahnbescheinigung.", example = "2025-01-31")
	@ValidDateFormat
	public String datumZeugnis;

	/** Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht. Standard, wenn nicht angegeben: true. */
	@Schema(description = "Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht. Standard, wenn nicht angegeben: true.", example = "true")
	public Boolean istGewertet;

	/** Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht. Standard, wenn nicht angegeben: false. */
	@Schema(description = "Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht. Standard, wenn nicht angegeben: false.", example = "false")
	public Boolean istWiederholung;

	/** Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist. */
	@Schema(description = "Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist.", example = "APO-GOSt(B)10/G8")
	@NotBlankIfPresent
	@NoLeadingOrTrailingWhitespaces
	@Size(max = 20)
	public String pruefungsOrdnung;

	/** Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist. */
	@Schema(description = "Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist.", example = "46")
	public Long klassenID;

	/** Die ID eines Tutors, der den Schüler betreut, oder null, falls keiner zugewiesen ist. */
	@Schema(description = "Die ID eines Tutors, der den Schüler betreut, oder null, falls keiner zugewiesen ist.", example = "12")
	public Long tutorID;

	/** Die ID der Folgeklasse des Schülers, sofern diese vom Standard der Klassentabelle abweicht. */
	@Schema(description = "Die ID der Folgeklasse des Schülers, sofern diese vom Standard der Klassentabelle abweicht.", example = "59")
	public Long folgeklassenID;

	/** Die ID der Schulgliederung bzw. des Bildungsgangs des Schülers. */
	@Schema(description = "Die ID der Schulgliederung bzw. des Bildungsgangs des Schülers.", example = "1000")
	public Long idSchulgliederung;

	/** Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist. */
	@Schema(description = "Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist.", example = "78")
	public Long jahrgangID;

	/** Die ID der bisherigen Anzahl der Jahre in der Schuleingangsphase. */
	@Schema(description = "Die ID der bisherigen Anzahl der Jahre in der Schuleingangsphase.", example = "2")
	public Long idEpJahre;

	/** Die ID der Fachklasse des Schülers an einem Berufskolleg. */
	@Schema(description = "Die ID der Fachklasse des Schülers an einem Berufskolleg.", example = "1")
	public Long fachklasseID;

	/** Die ID des Schwerpunkts des Schülers laut dem Schwerpunkt-Katalog. */
	@Schema(description = "Die ID des Schwerpunkts des Schülers laut dem Schwerpunkt-Katalog.", example = "1")
	public Long schwerpunktID;

	/** Die ID der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag). */
	@Schema(description = "Die ID der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag).", example = "1000")
	public Long idOrganisationsform;

	/** Die ID der Klassenart in Bezug auf den Schüler (z.B. Regelklasse). */
	@Schema(description = "Die ID der Klassenart in Bezug auf den Schüler (z.B. Regelklasse).", example = "7000")
	public Long idKlassenart;

	/** Die ID der Art der Teilnahme an Ganztagsangeboten und/oder Übermittagsbetreuung. Standard, wenn nicht angegeben: 1. */
	@Schema(description = "Die ID der Art der Teilnahme an Ganztagsangeboten und/oder Übermittagsbetreuung. Standard, wenn nicht angegeben: 1.", example = "1")
	@NotNull
	public Long idBetreuung = 1L;

	/** Die Summe der Gesamtfehlstunden für den gesamten Lernabschnitt. */
	@Schema(description = "Die Summe der Gesamtfehlstunden für den gesamten Lernabschnitt.", example = "0")
	@Min(0)
	@Max(99999)
	public Integer fehlstundenGesamt;

	/** Die Summe der unentschuldigten Fehlstunden für den gesamten Lernabschnitt. */
	@Schema(description = "Die Summe der unentschuldigten Fehlstunden für den gesamten Lernabschnitt.", example = "0")
	@Min(0)
	@Max(99999)
	public Integer fehlstundenUnentschuldigt;

	/** Der Grenzwert für die Fehlstunden, ab dem am Berufskolleg Warnbriefe zur Entlassung verschickt werden. */
	@Schema(description = "Der Grenzwert für die Fehlstunden, ab dem am Berufskolleg Warnbriefe zur Entlassung verschickt werden.", example = "20")
	@Min(0)
	@Max(99999)
	public Integer fehlstundenGrenzwert;

	/** Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht. Standard, wenn nicht angegeben: false. */
	@Schema(description = "Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht. Standard, wenn nicht angegeben: false.", example = "false")
	public Boolean hatSchwerbehinderungsNachweis;

	/** Gibt an, ob eine Förderung nach der Ausbildungsordnung Sonderpädagogischer Förderung (AOSF) vorliegt. Standard, wenn nicht angegeben: false. */
	@Schema(description = "Gibt an, ob eine Förderung nach der Ausbildungsordnung Sonderpädagogischer Förderung (AOSF) vorliegt. Standard, wenn nicht angegeben: false.", example = "false")
	public Boolean hatAOSF;

	/** Gibt an, ob eine Diagnose zu Autismus vorliegt oder nicht. Standard, wenn nicht angegeben: false. */
	@Schema(description = "Gibt an, ob eine Diagnose zu Autismus vorliegt oder nicht. Standard, wenn nicht angegeben: false.", example = "false")
	public Boolean hatAutismus;

	/** Gibt an, ob zieldifferent unterrichtet wird oder nicht. Standard, wenn nicht angegeben: false. */
	@Schema(description = "Gibt an, ob zieldifferent unterrichtet wird oder nicht. Standard, wenn nicht angegeben: false.", example = "false")
	public Boolean hatZieldifferentenUnterricht;

	/** Die ID des Hauptförderschwerpunkts des Schülers. */
	@Schema(description = "Die ID des Hauptförderschwerpunkts des Schülers.", example = "3")
	public Long foerderschwerpunkt1ID;

	/** Die ID des weiteren Förderschwerpunkts des Schülers. */
	@Schema(description = "Die ID des weiteren Förderschwerpunkts des Schülers.", example = "5")
	public Long foerderschwerpunkt2ID;

	/** Die ID eines Sonderpädagogen, der den Schüler betreut. */
	@Schema(description = "Die ID eines Sonderpädagogen, der den Schüler betreut.", example = "12")
	public Long sonderpaedagogeID;

	/** Die Sprache des bilingualen Zweigs, falls der Schüler im bilingualen Zweig unterrichtet wird. */
	@Schema(description = "Die Sprache des bilingualen Zweigs, falls der Schüler im bilingualen Zweig unterrichtet wird.", example = "E")
	public String bilingualerZweig;

	/** Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 für die Versetzung ausreichend ist. Standard, wenn nicht angegeben: true. */
	@Schema(description = "Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 für die Versetzung ausreichend ist. Standard, wenn nicht angegeben: true.", example = "true")
	public Boolean istFachpraktischerAnteilAusreichend;

	/** Die ID des Versetzungsvermerks. */
	@Schema(description = "Die ID des Versetzungsvermerks.", example = "1000")
	public Long idVersetzungsvermerk;

	/** Die Durchschnittsnote in diesem Lernabschnitt. */
	@Schema(description = "Die Durchschnittsnote in diesem Lernabschnitt.", example = "2,3")
	@NotBlankIfPresent
	@Size(max = 4)
	public String noteDurchschnitt;

	/** Die Lernbereichsnote Gesellschaftswissenschaft oder Arbeitslehre für den Hauptschulabschluss nach Klasse 10. */
	@Schema(description = "Die Lernbereichsnote Gesellschaftswissenschaft oder Arbeitslehre für den Hauptschulabschluss nach Klasse 10.", example = "3")
	@Min(1)
	@Max(6)
	public Integer noteLernbereichGSbzwAL;

	/** Die Lernbereichsnote Naturwissenschaft für den Hauptschulabschluss nach Klasse 10. */
	@Schema(description = "Die Lernbereichsnote Naturwissenschaft für den Hauptschulabschluss nach Klasse 10.", example = "3")
	@Min(1)
	@Max(6)
	public Integer noteLernbereichNW;

	/** Die Art des Abschlusses. */
	@Schema(description = "Die Art des Abschlusses.", example = "1")
	public Integer abschlussart;

	/** Gibt an, ob der berechnete Abschluss eine Prognose ist oder nicht. */
	@Schema(description = "Gibt an, ob der berechnete Abschluss eine Prognose ist oder nicht.", example = "false")
	public Boolean istAbschlussPrognose;

	/** Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs- bzw. Abschlussberechnung. */
	@Schema(description = "Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs- bzw. Abschlussberechnung.", example = "versetzt")
	@NotBlankIfPresent
	public String textErgebnisPruefungsalgorithmus;

	/** Die Art des Zeugnisses. */
	@Schema(description = "Die Art des Zeugnisses.", example = "HJ")
	@NotBlankIfPresent
	@Size(max = 5)
	public String zeugnisart;

}
