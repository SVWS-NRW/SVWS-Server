package de.svws_nrw.core.data.schild3.reporting;

import de.svws_nrw.base.annotations.SchildReportingDate;
import de.svws_nrw.base.annotations.SchildReportingMemo;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse enthält den Core-DTO für die Schild-Reporting-Datenquelle Schuelerlernabschnitte.
 */
@XmlRootElement
@Schema(description = "Datenquelle Schuelerlernabschnitte.")
@TranspilerDTO
public class SchildReportingSchuelerLernabschnitt {

    /** Die ID des Lernabschnitts */
    @Schema(description = "die ID des Lernabschnitts", example = "126784")
    public long id;

    /** Die ID des Schülers, zu dem die Lernabschnittsdaten gehören. */
    @Schema(description = "die ID des Schülers, zu dem die Lernabschnittsdaten gehören", example = "4785")
    public long schuelerID;

    /** Das Schuljahr, in welchem der Schuljahresabschnitt liegt */
    @Schema(description = "Das Schuljahr, in welchem der Abschnitt liegt", example = "2024")
    public int schuljahr;

    /** Die Nummer des Abschnitts im Schuljahr */
    @Schema(description = "Die Nummer des Abschnitts im Schuljahr", example = "2")
    public int abschnitt;

    /** Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitte in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.). */
    @Schema(description = "eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitt in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, 0=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.)", example = "NULL")
    public int wechselNr = 0;

    /** Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht */
    @Schema(description = "gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht", example = "true")
    public boolean istGewertet = true;

    /** Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht */
    @Schema(description = "gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht", example = "false")
    public boolean istWiederholung = false;

    /** Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist. */
    @Schema(description = "die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist.", example = "APO-GOSt(B)10/G8")
    public @NotNull String pruefungsOrdnung = "";

    /** Die Bezeichnung der Klasse des Schülers */
    @Schema(description = "die Bezeichnung der Klasse des Schülers", example = "7a")
    public @NotNull String klasse = "";

    /** Die Statistik-Bezeichnung der Klasse des Schülers */
    @Schema(description = "die Statistik-Bezeichnung der Klasse des Schülers", example = "07A")
    public @NotNull String klasseStatistik = "";

    /** Die Bezeichnung des Jahrgangs */
    @Schema(description = "die Bezeichnung des Jahrgangs", example = "EF")
    public @NotNull String jahrgang = "";

    /** Die Statistik-Bezeichnung des Jahrgangs */
    @Schema(description = "die Statistik-Bezeichnung des Jahrgangs", example = "EF")
    public @NotNull String jahrgangStatistik = "";
    /** Das Datum der Zeugniskonferenz */
    @SchildReportingDate
    @Schema(description = "Das Datum der Zeugniskonferenz", example = "2021-06-26")
    public @NotNull String datumZeugniskonferenz = "";

    /** Das Datum des Zeugnisses */
    @SchildReportingDate
    @Schema(description = "Das Datum der Zeugnisses", example = "2021-06-28")
    public @NotNull String datumZeugnis = "";

    /** Das Ergebnis des Prüfungsalgorithmus */
    @SchildReportingMemo
    @Schema(description = "Das Ergebnis des Prüfungsalgorithmus", example = "Hier steht das Ergebnis des angewendeten Prüfungsalgorithmus")
    public @NotNull String logPruefungsalgorithmus = "";

    // TODO weitere Attribute

}
