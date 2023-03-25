package de.svws_nrw.core.data.schild3;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für den Schild3-Katalog Prüfungsordnungen.
 */
@XmlRootElement
@Schema(description="Schild3-Katalog Prüfungsordnungen")
@TranspilerDTO
public class Schild3KatalogEintragPruefungsordnung {

    /** Zulässige Schulformen der Prüfungsordnungen */
    @Schema(required = false, description = "Zulässige Schulformen der Prüfungsordnungen")
    public String PO_Schulform;

    /** Erstes Kürzel */
    @Schema(required = false, description = "Erstes Kürzel")
    public String PO_Krz;

    /** Zweites Kürzel */
    @Schema(required = false, description = "Zweites Kürzel")
    public String PO_Name;

    /** Zulässige Gliederungen */
    @Schema(required = false, description = "Zulässige Gliederungen")
    public String PO_SGL;

    /** Deprecated  */
    @Schema(required = false, description = "Deprecated")
    public Integer PO_MinJahrgang;

    /** Deprecated */
    @Schema(required = false, description = "Deprecated")
    public Integer PO_MaxJahrgang;

    /** Zulässige Jahrgänge */
    @Schema(required = false, description = "Zulässige Jahrgänge")
    public String PO_Jahrgaenge;

    /** Gültig ab Schuljahr */
    @Schema(required = false, description = "Gültig ab Schuljahr")
    public Integer gueltigVon;

    /** Gültig bis Schuljahr */
    @Schema(required = false, description = "Gültig bis Schuljahr")
    public Integer gueltigBis;

}
