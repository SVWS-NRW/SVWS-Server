package de.svws_nrw.core.data.schild3;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für den Schild3-Katalog Bundesländer/Nachbarländer.
 */
@XmlRootElement
@Schema(description="Schild3-Katalog Bundesländer/Nachbarländer")
@TranspilerDTO
public class Schild3KatalogEintragLaender {

    /** Bundesländer/Nachbarländer Kurztext */
    @Schema(description = "Bundesländer/Nachbarländer Kurztext")
    public String Kurztext;

    /** Bundesländer/Nachbarländer Langtext */
    @Schema(description = "Bundesländer/Nachbarländer Langtext")
    public String Langtext;

    /** Bundesländer/Nachbarländer Sortierung */
    @Schema(description = "Bundesländer/Nachbarländer Sortierung")
    public Integer Sortierung;

    /** Gültig ab Schuljahr */
    @Schema(description = "Gültig ab Schuljahr")
    public Integer gueltigVon;

    /** Gültig bis Schuljahr */
    @Schema(description = "Gültig bis Schuljahr")
    public Integer gueltigBis;

}
