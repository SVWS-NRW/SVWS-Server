package de.nrw.schule.svws.core.data.schild3;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für die Schema-Definition 
 * der JSON-Daten einer Datenquelle für das Schild-Reporting.
 * Sie enthält die Definition eines Attributs und dessen Typs. 
 */
@XmlRootElement
@Schema(description="Das Attribut und dessen Typ für die Schema-Definition der Datenquelle für das Schild-Reporting")
@TranspilerDTO
public class SchildReportingDatenquelleAttribut {

    /** Der Name des Attributs */
    @Schema(required = true, description = "der Name des Attributs", example ="Vorname")
    public @NotNull String name = "";

    /** Der Typ des Attributs */
    @Schema(required = true, description = "der Typ des Attributs", example = "string")
    public @NotNull String typ = "";

    /** Die Beschreibung des Attributs */
    @Schema(required = true, description = "die Beschreibung des Attributs", example = "Der Vorname des Schülers")
    public @NotNull String beschreibung = "";

}
