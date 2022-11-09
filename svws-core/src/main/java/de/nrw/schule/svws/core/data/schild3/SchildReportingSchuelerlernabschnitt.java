package de.nrw.schule.svws.core.data.schild3;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse enthält den Core-DTO für die Schild-Reporting-Datenquelle Schuelerlernabschnitte.  
 */
@XmlRootElement
@Schema(description="Datenquelle Schuelerlernabschnitte.")
@TranspilerDTO
public class SchildReportingSchuelerlernabschnitt {

    /** Die ID des Lernabschnitts */
    @Schema(required = true, description = "die ID des Lernabschnitts", example="126784")
    public long id;

    /** Die ID des Schülers, zu dem die Lernabschnittdaten gehören. */
    @Schema(required = true, description = "die ID des Schülers, zu dem die Lernabschnittdaten gehören", example="4785")
    public long schuelerID;

    /** Das Schuljahr, in welchem der Schuljahresabschnitt liegt */
    @Schema(required = true, description = "Das Schuljahr, in welchem der Abschnitt liegt", example="2024")
    public int schuljahr;   

    /** Die Nummer des Abschnitts im Schuljahr */
    @Schema(required = true, description = "Die Nummer des Abschnitts im Schuljahr", example="2")
    public int abschnitt;   

    /** Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitt in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.). */
    @Schema(required = false, description = "eine Nr, zur Unterscheidung von Lernabschnissdaten, wenn beim Schüler mehrere Lernabschnitt in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.)", example="NULL") 
    public Integer wechselNr = null;

    /** Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht */
    @Schema(required = true, description = "gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht", example="true")  
    public boolean istGewertet = true;

    /** Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht */
    @Schema(required = true, description = "gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht", example="false") 
    public boolean istWiederholung = false;

    /** Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist. */
    @Schema(required = true, description = "die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist.", example="APO-GOSt(B)10/G8")
    public @NotNull String pruefungsOrdnung = "";

    /** Die Bezeichnung der Klasse des Schülers */
    @Schema(required = true, description = "die Bezeichnung der Klasse des Schülers", example="7a") 
    public @NotNull String klasse = "";

    /** Die Statistik-Bezeichnung der Klasse des Schülers */
    @Schema(required = true, description = "die Statistik-Bezeichnung der Klasse des Schülers", example="07A")  
    public @NotNull String klasseStatistik = "";

    /** Die Bezeichnung des Jahrgangs */
    @Schema(required = true, description = "die Bezeichnung des Jahrgangs", example="EF")    
    public @NotNull String jahrgang = "";

    /** Die Statistik-Bezeichnung des Jahrgangs */
    @Schema(required = true, description = "die Statistik-Bezeichnung des Jahrgangs", example="EF")    
    public @NotNull String jahrgangStatistik = "";
    // TODO weitere Attribute

}
