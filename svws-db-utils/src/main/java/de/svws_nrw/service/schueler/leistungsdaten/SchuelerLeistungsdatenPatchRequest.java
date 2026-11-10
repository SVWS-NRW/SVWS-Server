package de.svws_nrw.service.schueler.leistungsdaten;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Enthält die Attribute, die beim Patchen von Schülerleistungsdaten über den {@link SchuelerLeistungsdatenService}
 * geändert werden können. Nicht enthalten sind die ID und die Berufsabschlussnote, da diese auch bisher
 * nicht patchbar waren.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SchuelerLeistungsdatenPatchRequest {

	private static final int MAX_STUNDEN = 999;
	private static final int MAX_FEHLSTUNDEN = 99999;

	/** Die ID des Lernabschnitts, zu dem die Leistungsdaten gehören. */
	@Schema(description = "Die ID des Lernabschnitts des Schülers, zu dem diese Leistungsdaten gehören.", example = "4785")
	public JsonNullable<@NotNull Long> lernabschnittID = JsonNullable.undefined();

	/** Die ID des Faches, auf das sich die Leistungsdaten beziehen. */
	@Schema(description = "Die ID des Faches, auf welches sich die Leistungsdaten beziehen.", example = "46")
	public JsonNullable<@NotNull Long> fachID = JsonNullable.undefined();

	/** Die ID des Kurses oder null bei Klassenunterricht. */
	@Schema(description = "Die ID des Kurses, auf den sich die Leistungsdaten beziehen, oder null bei Klassenunterricht.", example = "7732")
	public JsonNullable<Long> kursID = JsonNullable.undefined();

	/** Das Kürzel der speziellen Kursart. */
	@Schema(description = "Das Kürzel der speziellen Kursart des Schülers, sofern Kursunterricht vorliegt. Bei null wird PUK verwendet.", example = "LK1")
	public JsonNullable<String> kursart = JsonNullable.undefined();

	/** Die Nummer des Abiturfachs (1 bis 5) oder null. */
	@Schema(description = "Gibt an, ob es sich bei der Fachbelegung um ein Abiturfach handelt (1 bis 5), oder null.", example = "1")
	public JsonNullable<Integer> abifach = JsonNullable.undefined();

	/** Gibt an, ob es sich um ein Fach der Zentralen Prüfungen 10 oder der Zentralen Klausuren in der EF handelt. */
	@Schema(description = "Gibt an, ob es sich um ein Fach der Zentralen Prüfungen 10 handelt oder um ein Fach der Zentralen Klausuren in der EF (G8).",
			example = "false")
	public JsonNullable<@NotNull Boolean> istZP10oderZKEF = JsonNullable.undefined();

	/** Die Schulnummer der kooperierenden Schule oder null. */
	@Schema(description = "Die Schulnummer, sofern es sich um Unterricht an einer kooperierenden Schule handelt, ansonsten null.", example = "100000")
	public JsonNullable<@Min(100000) @Max(999999) Integer> koopSchule = JsonNullable.undefined();

	/** Die ID des Fachlehrers oder null. */
	@Schema(description = "Die ID des Fachlehrers oder null.", example = "23")
	public JsonNullable<Long> lehrerID = JsonNullable.undefined();

	/** Die Anzahl der Wochenstunden. */
	@Schema(description = "Die Anzahl der Wochenstunden, welche das Fach unterrichtet wird.", example = "3")
	public JsonNullable<@Min(0) @Max(MAX_STUNDEN) Integer> wochenstunden = JsonNullable.undefined();

	/** Die ID der Zusatzkraft oder null. */
	@Schema(description = "Die ID der Zusatzkraft oder null.", example = "23")
	public JsonNullable<Long> zusatzkraftID = JsonNullable.undefined();

	/** Die Anzahl der Wochenstunden der Zusatzkraft. */
	@Schema(description = "Die Anzahl der Wochenstunden der Zusatzkraft.", example = "3")
	public JsonNullable<@Min(0) @Max(MAX_STUNDEN) Integer> zusatzkraftWochenstunden = JsonNullable.undefined();

	/** Gibt an, ob das Fach auf dem Zeugnis erscheint. */
	@Schema(description = "Gibt an, ob das Fach auf dem Zeugnis erscheint oder nicht.", example = "true")
	public JsonNullable<@NotNull Boolean> aufZeugnis = JsonNullable.undefined();

	/** Das Kürzel der erteilten Note. */
	@Schema(description = "Das Kürzel der erteilten Note. Es können auch Pseudonoten eingetragen werden (z.B. AT).", example = "3-")
	public JsonNullable<String> note = JsonNullable.undefined();

	/** Das Kürzel der erteilten Quartalsnote. */
	@Schema(description = "Das Kürzel der erteilten Quartalsnote. Es können auch Pseudonoten eingetragen werden (z.B. AT).", example = "3-")
	public JsonNullable<String> noteQuartal = JsonNullable.undefined();

	/** Gibt an, ob die Leistung gemahnt wurde. */
	@Schema(description = "Gibt an, ob die Leistung gemahnt wurde oder nicht.", example = "false")
	public JsonNullable<@NotNull Boolean> istGemahnt = JsonNullable.undefined();

	/** Das Datum der Mahnung oder null. */
	@Schema(description = "Das Datum, wann die Leistung gemahnt wurde, oder null.", example = "2025-01-20")
	public JsonNullable<String> mahndatum = JsonNullable.undefined();

	/** Gibt an, ob es sich um ein epochal unterrichtetes Fach handelt. */
	@Schema(description = "Gibt an, ob es sich um ein epochal unterrichtetes Fach handelt oder nicht.", example = "false")
	public JsonNullable<@NotNull Boolean> istEpochal = JsonNullable.undefined();

	/** Der Jahrgang, aus dem die Leistung als abgeschlossenes Fach geholt wurde, oder null. */
	@Schema(description = "Der Jahrgang, aus dem die Leistung über das Holen von abgeschlossenen Fächern übernommen wurde, oder null.", example = "EF")
	public JsonNullable<String> geholtJahrgangAbgeschlossen = JsonNullable.undefined();

	/** Die Gewichtung für den allgemeinbildenden Teil am Berufskolleg. */
	@Schema(description = "Die Gewichtung für den allgemeinbildenden Teil (am Berufskolleg).", example = "1")
	public JsonNullable<@Min(0) @Max(9) Integer> gewichtungAllgemeinbildend = JsonNullable.undefined();

	/** Der Text für die fachbezogene Lernentwicklung. */
	@Schema(description = "Der Text für die fachbezogene Lernentwicklung des Schülers.", example = "")
	public JsonNullable<String> textFachbezogeneLernentwicklung = JsonNullable.undefined();

	/** Der Umfang des Lernstandsberichts (V oder R) oder leer. */
	@Schema(description = "Die Facheigenschaft für die Lernstandsberichte an Grundschulen (V = voller Umfang, R = reduzierter Umfang) oder leer.",
			example = "V")
	public JsonNullable<@Size(max = 1) String> umfangLernstandsbericht = JsonNullable.undefined();

	/** Die Gesamtanzahl der Fehlstunden. */
	@Schema(description = "Die Gesamtanzahl der Fehlstunden für dieses Fach.", example = "2")
	public JsonNullable<@Min(0) @Max(MAX_FEHLSTUNDEN) Integer> fehlstundenGesamt = JsonNullable.undefined();

	/** Die Anzahl der unentschuldigten Fehlstunden. */
	@Schema(description = "Die Anzahl der unentschuldigten Fehlstunden für dieses Fach.", example = "2")
	public JsonNullable<@Min(0) @Max(MAX_FEHLSTUNDEN) Integer> fehlstundenUnentschuldigt = JsonNullable.undefined();

}
