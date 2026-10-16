package de.svws_nrw.service.schueler.lernabschnitt;

import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Enthält die Attribute, die beim Patchen von Schülerlernabschnittsdaten über den
 * {@link SchuelerLernabschnittService} geändert werden können.
 */
public class SchuelerLernabschnittsdatenPatchRequest {

	/** Das Datum, wann der Lernabschnitt beginnt. */
	@Schema(description = "Das Datum, wann der Lernabschnitt beginnt, oder null, um es zu entfernen.", example = "2024-08-01")
	public JsonNullable<@ValidDateFormat String> datumAnfang = JsonNullable.undefined();

	/** Das Datum, wann der Lernabschnitt endet. */
	@Schema(description = "Das Datum, wann der Lernabschnitt endet, oder null, um es zu entfernen.", example = "2025-01-31")
	public JsonNullable<@ValidDateFormat String> datumEnde = JsonNullable.undefined();

	/** Das Datum der Konferenz. */
	@Schema(description = "Das Datum der Konferenz oder null, um es zu entfernen.", example = "2025-01-20")
	public JsonNullable<@ValidDateFormat String> datumKonferenz = JsonNullable.undefined();

	/** Das Datum des Zeugnisses bzw. der Laufbahnbescheinigung. */
	@Schema(description = "Das Datum des Zeugnisses bzw. der Laufbahnbescheinigung oder null, um es zu entfernen.", example = "2025-01-31")
	public JsonNullable<@ValidDateFormat String> datumZeugnis = JsonNullable.undefined();

	/** Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht. */
	@Schema(description = "Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht.", example = "true")
	public JsonNullable<@NotNull Boolean> istGewertet = JsonNullable.undefined();

	/** Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht. */
	@Schema(description = "Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht.", example = "false")
	public JsonNullable<@NotNull Boolean> istWiederholung = JsonNullable.undefined();

	/** Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist. */
	@Schema(description = "Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist.", example = "APO-GOSt(B)10/G8")
	public JsonNullable<@NotBlank @Size(max = 20) String> pruefungsOrdnung = JsonNullable.undefined();

	/** Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist. */
	@Schema(description = "Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist.", example = "46")
	public JsonNullable<Long> klassenID = JsonNullable.undefined();

	/** Die ID eines Tutors, der den Schüler betreut, oder null, falls keiner zugewiesen ist. */
	@Schema(description = "Die ID eines Tutors, der den Schüler betreut, oder null, falls keiner zugewiesen ist.", example = "12")
	public JsonNullable<Long> tutorID = JsonNullable.undefined();

	/** Die ID der Folgeklasse des Schülers, sofern diese vom Standard der Klassentabelle abweicht. */
	@Schema(description = "Die ID der Folgeklasse des Schülers, sofern diese vom Standard der Klassentabelle abweicht.", example = "59")
	public JsonNullable<Long> folgeklassenID = JsonNullable.undefined();

	/** Die ID der Schulgliederung bzw. des Bildungsgangs des Schülers. */
	@Schema(description = "Die ID der Schulgliederung bzw. des Bildungsgangs des Schülers.", example = "1000")
	public JsonNullable<Long> idSchulgliederung = JsonNullable.undefined();

	/** Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist. */
	@Schema(description = "Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist.", example = "78")
	public JsonNullable<Long> jahrgangID = JsonNullable.undefined();

	/** Die ID der bisherigen Anzahl der Jahre in der Schuleingangsphase. */
	@Schema(description = "Die ID der bisherigen Anzahl der Jahre in der Schuleingangsphase.", example = "2")
	public JsonNullable<Long> idEpJahre = JsonNullable.undefined();

	/** Die ID der Fachklasse des Schülers an einem Berufskolleg. */
	@Schema(description = "Die ID der Fachklasse des Schülers an einem Berufskolleg.", example = "1")
	public JsonNullable<Long> fachklasseID = JsonNullable.undefined();

	/** Die ID des Schwerpunkts des Schülers laut dem Schwerpunkt-Katalog. */
	@Schema(description = "Die ID des Schwerpunkts des Schülers laut dem Schwerpunkt-Katalog.", example = "1")
	public JsonNullable<Long> schwerpunktID = JsonNullable.undefined();

	/** Die ID der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag). */
	@Schema(description = "Die ID der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag).", example = "1000")
	public JsonNullable<Long> idOrganisationsform = JsonNullable.undefined();

	/** Die ID der Klassenart in Bezug auf den Schüler (z.B. Regelklasse). */
	@Schema(description = "Die ID der Klassenart in Bezug auf den Schüler (z.B. Regelklasse).", example = "7000")
	public JsonNullable<Long> idKlassenart = JsonNullable.undefined();

	/** Die Summe der Gesamtfehlstunden für den gesamten Lernabschnitt. */
	@Schema(description = "Die Summe der Gesamtfehlstunden für den gesamten Lernabschnitt.", example = "0")
	public JsonNullable<@Min(0) @Max(99999) Integer> fehlstundenGesamt = JsonNullable.undefined();

	/** Die Summe der unentschuldigten Fehlstunden für den gesamten Lernabschnitt. */
	@Schema(description = "Die Summe der unentschuldigten Fehlstunden für den gesamten Lernabschnitt.", example = "0")
	public JsonNullable<@Min(0) @Max(99999) Integer> fehlstundenUnentschuldigt = JsonNullable.undefined();

	/** Der Grenzwert für die Fehlstunden, ab dem am Berufskolleg Warnbriefe zur Entlassung verschickt werden. */
	@Schema(description = "Der Grenzwert für die Fehlstunden, ab dem am Berufskolleg Warnbriefe zur Entlassung verschickt werden.", example = "20")
	public JsonNullable<@Min(0) @Max(99999) Integer> fehlstundenGrenzwert = JsonNullable.undefined();

	/** Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht. */
	@Schema(description = "Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht.", example = "false")
	public JsonNullable<@NotNull Boolean> hatSchwerbehinderungsNachweis = JsonNullable.undefined();

	/** Gibt an, ob eine Förderung nach der Ausbildungsordnung Sonderpädagogischer Förderung (AOSF) vorliegt. */
	@Schema(description = "Gibt an, ob eine Förderung nach der Ausbildungsordnung Sonderpädagogischer Förderung (AOSF) vorliegt.", example = "false")
	public JsonNullable<@NotNull Boolean> hatAOSF = JsonNullable.undefined();

	/** Gibt an, ob eine Diagnose zu Autismus vorliegt oder nicht. */
	@Schema(description = "Gibt an, ob eine Diagnose zu Autismus vorliegt oder nicht.", example = "false")
	public JsonNullable<@NotNull Boolean> hatAutismus = JsonNullable.undefined();

	/** Gibt an, ob zieldifferent unterrichtet wird oder nicht. */
	@Schema(description = "Gibt an, ob zieldifferent unterrichtet wird oder nicht.", example = "false")
	public JsonNullable<@NotNull Boolean> hatZieldifferentenUnterricht = JsonNullable.undefined();

	/** Die ID des Hauptförderschwerpunkts des Schülers. */
	@Schema(description = "Die ID des Hauptförderschwerpunkts des Schülers.", example = "3")
	public JsonNullable<Long> foerderschwerpunkt1ID = JsonNullable.undefined();

	/** Die ID des weiteren Förderschwerpunkts des Schülers. */
	@Schema(description = "Die ID des weiteren Förderschwerpunkts des Schülers.", example = "5")
	public JsonNullable<Long> foerderschwerpunkt2ID = JsonNullable.undefined();

	/** Die ID eines Sonderpädagogen, der den Schüler betreut. */
	@Schema(description = "Die ID eines Sonderpädagogen, der den Schüler betreut.", example = "12")
	public JsonNullable<Long> sonderpaedagogeID = JsonNullable.undefined();

	/** Die Sprache des bilingualen Zweigs, falls der Schüler im bilingualen Zweig unterrichtet wird. */
	@Schema(description = "Die Sprache des bilingualen Zweigs, falls der Schüler im bilingualen Zweig unterrichtet wird.", example = "E")
	public JsonNullable<String> bilingualerZweig = JsonNullable.undefined();

	/** Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 für die Versetzung ausreichend ist. */
	@Schema(description = "Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 für die Versetzung ausreichend ist.",
			example = "true")
	public JsonNullable<@NotNull Boolean> istFachpraktischerAnteilAusreichend = JsonNullable.undefined();

	/** Die ID des Versetzungsvermerks. */
	@Schema(description = "Die ID des Versetzungsvermerks.", example = "1000")
	public JsonNullable<Long> idVersetzungsvermerk = JsonNullable.undefined();

	/** Die Durchschnittsnote in diesem Lernabschnitt. */
	@Schema(description = "Die Durchschnittsnote in diesem Lernabschnitt, oder null, um sie zu entfernen.", example = "2,3")
	public JsonNullable<@Size(min = 1, max = 4) String> noteDurchschnitt = JsonNullable.undefined();

	/** Die Lernbereichsnote Gesellschaftswissenschaft oder Arbeitslehre für den Hauptschulabschluss nach Klasse 10. */
	@Schema(description = "Die Lernbereichsnote Gesellschaftswissenschaft oder Arbeitslehre für den Hauptschulabschluss nach Klasse 10.", example = "3")
	public JsonNullable<@Min(1) @Max(6) Integer> noteLernbereichGSbzwAL = JsonNullable.undefined();

	/** Die Lernbereichsnote Naturwissenschaft für den Hauptschulabschluss nach Klasse 10. */
	@Schema(description = "Die Lernbereichsnote Naturwissenschaft für den Hauptschulabschluss nach Klasse 10.", example = "3")
	public JsonNullable<@Min(1) @Max(6) Integer> noteLernbereichNW = JsonNullable.undefined();

	/** Die Art des Abschlusses. */
	@Schema(description = "Die Art des Abschlusses.", example = "1")
	public JsonNullable<Integer> abschlussart = JsonNullable.undefined();

	/** Gibt an, ob der berechnete Abschluss eine Prognose ist oder nicht. */
	@Schema(description = "Gibt an, ob der berechnete Abschluss eine Prognose ist oder nicht.", example = "false")
	public JsonNullable<@NotNull Boolean> istAbschlussPrognose = JsonNullable.undefined();

	/** Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs- bzw. Abschlussberechnung. */
	@Schema(description = "Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs- bzw. Abschlussberechnung, oder null, um sie zu entfernen.", example = "versetzt")
	public JsonNullable<@Size(min = 1) String> textErgebnisPruefungsalgorithmus = JsonNullable.undefined();

	/** Die Art des Zeugnisses. */
	@Schema(description = "Die Art des Zeugnisses, oder null, um sie zu entfernen.", example = "HJ")
	public JsonNullable<@Size(min = 1, max = 5) String> zeugnisart = JsonNullable.undefined();

}
