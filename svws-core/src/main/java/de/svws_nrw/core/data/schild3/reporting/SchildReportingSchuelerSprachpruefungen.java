package de.svws_nrw.core.data.schild3.reporting;

import de.svws_nrw.base.annotations.SchildReportingDate;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse enthält den Core-DTO für die Schild-Reporting-Datenquelle SchuelerSprachpruefungen.
 */
@XmlRootElement
@Schema(description = "Datenquelle SchuelerSprachpruefungen")
@TranspilerDTO
public class SchildReportingSchuelerSprachpruefungen {

    /** Die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören. */
    @Schema(description = "die ID des Schülers, zu dem die Sprachprüfungen gehören", example = "4711")
    public long schuelerID;

	/** Das Sprachkürzel des geprüften Faches */
	@Schema(description = "Das Sprachkürzel des geprüften Faches", example = "R")
	public @NotNull String sprache = "";

	/** Gibt an, in welchem ASD-Jahrgang die Prüfung abgelegt wurde */
	@Schema(description = "ASD-Jahrgang, in dem die Prüfung abgelegt wurde", example = "10")
	public @NotNull String jahrgang = "";

	/** Bezeichnung des am Schulabschluss orientierte Anspruchsniveau der Sprachprüfung */
	@Schema(description = "Bezeichnung des am Schulabschluss orientierte Anspruchsniveau der Sprachprüfung", example = "MSA")
	public @NotNull String anspruchsniveau = "";

	/** Sprache, die durch die Prüfung ersetzt wird */
	@Schema(description = "Sprache, die durch die Prüfung ersetzt wird", example = "F")
	public @NotNull String ersetzteSprache = "";

	/** Prüfung ist eine Prüfung im herkunftssprachlichen Unterricht */
	@Schema(description = "Prüfung ist eine Prüfung im herkunftssprachlichen Unterricht", example = "true")
	public boolean istHSUPruefung = false;

	/** Prüfung ist eine Sprachfeststellungsprüfung */
	@Schema(description = "Prüfung ist eine Sprachfeststellungsprüfung", example = "true")
	public boolean istFeststellungspruefung = false;

	/** Durch die Prüfung kann die erste Pflichtfremdsprache ersetzt werden */
	@Schema(description = "Durch die Prüfung kann die erste Pflichtfremdsprache ersetzt werden", example = "true")
	public boolean kannErstePflichtfremdspracheErsetzen = false;

	/** Durch die Prüfung kann die zweite Pflichtfremdsprache ersetzt werden */
	@Schema(description = "Durch die Prüfung kann die zweite Pflichtfremdsprache ersetzt werden", example = "true")
	public boolean kannZweitePflichtfremdspracheErsetzen = false;

	/** Durch die Prüfung kann die Wahlpflichtfremdsprache ersetzt werden */
	@Schema(description = "Durch die Prüfung kann die Wahlpflichtfremdsprache ersetzt werden", example = "true")
	public boolean kannWahlpflichtfremdspracheErsetzen = false;

	/** Durch die Prüfung kann die Sprache als fortgeführte Fremdsprache in der GOSt belegt werden. */
	@Schema(description = "Durch die Prüfung kann die Sprache als fortgeführte Fremdsprache in der GOSt belegt werden", example = "true")
	public boolean kannBelegungAlsFortgefuehrteSpracheErlauben = false;

	/** Datum der Sprachprüfung. */
	@SchildReportingDate
	@Schema(description = "Datum der Sprachprüfung", example = "2023-08-02")
	public @NotNull String pruefungsdatum = "";

	/** Das Kürzel des GeR-Referenzniveaus, welches durch die Prüfung erreicht wurde */
	@Schema(description = "Das Kürzel des GeR-Referenzniveaus, welches durch die Prüfung erreicht wurde", example = "B2")
	public @NotNull String referenzniveau = "";

	/** Die Note, die in der Sprachprüfung erreicht wurde */
	@Schema(description = "Die Note, die in der Sprachprüfung erreicht wurde", example = "3")
	public @NotNull String note = "";
}
