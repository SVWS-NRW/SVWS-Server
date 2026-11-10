package de.svws_nrw.service.schueler.leistungsdaten;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Enthält die Attribute, die beim Anlegen von Schülerleistungsdaten über den {@link SchuelerLeistungsdatenService}
 * angegeben werden können. Pflicht sind die ID des Lernabschnitts und die ID des Faches. Nicht angegebene
 * (also {@code null}) weitere Attribute werden mit ihren Standardwerten angelegt.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SchuelerLeistungsdatenCreateRequest {

	private static final int MAX_STUNDEN = 999;
	private static final int MAX_FEHLSTUNDEN = 99999;

	/** Die ID des Lernabschnitts, zu dem die Leistungsdaten angelegt werden. */
	@Schema(description = "Die ID des Lernabschnitts des Schülers, zu dem diese Leistungsdaten gehören.", example = "4785")
	@NotNull
	public Long lernabschnittID;

	/** Die ID des Faches, auf das sich die Leistungsdaten beziehen. */
	@Schema(description = "Die ID des Faches, auf welches sich die Leistungsdaten beziehen.", example = "46")
	@NotNull
	public Long fachID;

	/** Die ID des Kurses oder null bei Klassenunterricht. */
	@Schema(description = "Die ID des Kurses, auf den sich die Leistungsdaten beziehen, oder null bei Klassenunterricht.", example = "7732")
	public Long kursID;

	/** Das Kürzel der speziellen Kursart. */
	@Schema(description = "Das Kürzel der speziellen Kursart des Schülers, sofern Kursunterricht vorliegt.", example = "LK1")
	public String kursart;

	/** Die Nummer des Abiturfachs (1 bis 5) oder null. */
	@Schema(description = "Gibt an, ob es sich bei der Fachbelegung um ein Abiturfach handelt (1 bis 5), oder null.", example = "1")
	public Integer abifach;

	/** Gibt an, ob es sich um ein Fach der Zentralen Prüfungen 10 oder der Zentralen Klausuren in der EF handelt. */
	@Schema(description = "Gibt an, ob es sich um ein Fach der Zentralen Prüfungen 10 handelt oder um ein Fach der Zentralen Klausuren in der EF (G8).",
			example = "false")
	public Boolean istZP10oderZKEF;

	/** Die Schulnummer der kooperierenden Schule oder null. */
	@Schema(description = "Die Schulnummer, sofern es sich um Unterricht an einer kooperierenden Schule handelt, ansonsten null.", example = "100000")
	@Min(100000)
	@Max(999999)
	public Integer koopSchule;

	/** Die ID des Fachlehrers oder null. */
	@Schema(description = "Die ID des Fachlehrers oder null.", example = "23")
	public Long lehrerID;

	/** Die Anzahl der Wochenstunden. */
	@Schema(description = "Die Anzahl der Wochenstunden, welche das Fach unterrichtet wird.", example = "3")
	@Min(0)
	@Max(MAX_STUNDEN)
	public Integer wochenstunden;

	/** Die ID der Zusatzkraft oder null. */
	@Schema(description = "Die ID der Zusatzkraft oder null.", example = "23")
	public Long zusatzkraftID;

	/** Die Anzahl der Wochenstunden der Zusatzkraft. */
	@Schema(description = "Die Anzahl der Wochenstunden der Zusatzkraft.", example = "3")
	@Min(0)
	@Max(MAX_STUNDEN)
	public Integer zusatzkraftWochenstunden;

	/** Gibt an, ob das Fach auf dem Zeugnis erscheint. */
	@Schema(description = "Gibt an, ob das Fach auf dem Zeugnis erscheint oder nicht.", example = "true")
	public Boolean aufZeugnis;

	/** Das Kürzel der erteilten Note. */
	@Schema(description = "Das Kürzel der erteilten Note. Es können auch Pseudonoten eingetragen werden (z.B. AT).", example = "3-")
	public String note;

	/** Das Kürzel der erteilten Quartalsnote. */
	@Schema(description = "Das Kürzel der erteilten Quartalsnote. Es können auch Pseudonoten eingetragen werden (z.B. AT).", example = "3-")
	public String noteQuartal;

	/** Gibt an, ob die Leistung gemahnt wurde. */
	@Schema(description = "Gibt an, ob die Leistung gemahnt wurde oder nicht.", example = "false")
	public Boolean istGemahnt;

	/** Das Datum der Mahnung oder null. */
	@Schema(description = "Das Datum, wann die Leistung gemahnt wurde, oder null.", example = "2025-01-20")
	public String mahndatum;

	/** Gibt an, ob es sich um ein epochal unterrichtetes Fach handelt. */
	@Schema(description = "Gibt an, ob es sich um ein epochal unterrichtetes Fach handelt oder nicht.", example = "false")
	public Boolean istEpochal;

	/** Der Jahrgang, aus dem die Leistung als abgeschlossenes Fach geholt wurde, oder null. */
	@Schema(description = "Der Jahrgang, aus dem die Leistung über das Holen von abgeschlossenen Fächern übernommen wurde, oder null.", example = "EF")
	public String geholtJahrgangAbgeschlossen;

	/** Die Gewichtung für den allgemeinbildenden Teil am Berufskolleg. */
	@Schema(description = "Die Gewichtung für den allgemeinbildenden Teil (am Berufskolleg).", example = "1")
	@Min(0)
	@Max(9)
	public Integer gewichtungAllgemeinbildend;

	/** Der Text für die fachbezogene Lernentwicklung. */
	@Schema(description = "Der Text für die fachbezogene Lernentwicklung des Schülers.", example = "")
	public String textFachbezogeneLernentwicklung;

	/** Der Umfang des Lernstandsberichts (V oder R) oder leer. */
	@Schema(description = "Die Facheigenschaft für die Lernstandsberichte an Grundschulen (V = voller Umfang, R = reduzierter Umfang) oder leer.",
			example = "V")
	@Size(max = 1)
	public String umfangLernstandsbericht;

	/** Die Gesamtanzahl der Fehlstunden. */
	@Schema(description = "Die Gesamtanzahl der Fehlstunden für dieses Fach.", example = "2")
	@Min(0)
	@Max(MAX_FEHLSTUNDEN)
	public Integer fehlstundenGesamt;

	/** Die Anzahl der unentschuldigten Fehlstunden. */
	@Schema(description = "Die Anzahl der unentschuldigten Fehlstunden für dieses Fach.", example = "2")
	@Min(0)
	@Max(MAX_FEHLSTUNDEN)
	public Integer fehlstundenUnentschuldigt;

}
