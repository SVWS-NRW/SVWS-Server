package de.nrw.schule.svws.core.data.schule;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für den Katalog Schüler-Status.
 */
@XmlRootElement
@Schema(description="Katalog Schüler-Status")
@TranspilerDTO
public class SchuelerstatusKatalogEintrag {

    /** Die numerische ID des Schüler-Status. */
    @Schema(required = false, description = "Die numerische ID des Schüler-Status")
    public Integer StatusNr;

    /** Klartext des Schülerstatus */
    @Schema(required = false, description = "Klartext des Schülerstatus")
    public String Bezeichnung;

    /** Sortierung des Schülerstatus */
    @Schema(required = false, description = "Sortierung des Schülerstatus")
    public Integer Sortierung;

}
