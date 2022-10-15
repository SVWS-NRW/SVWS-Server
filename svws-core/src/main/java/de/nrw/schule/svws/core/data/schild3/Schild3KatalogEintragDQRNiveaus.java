package de.nrw.schule.svws.core.data.schild3;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für den Schild3-Katalog DQR-Niveaus.
 */
@XmlRootElement
@Schema(description="Schild3-Katalog DQR-Niveaus")
@TranspilerDTO
public class Schild3KatalogEintragDQRNiveaus {

    /** DQR-Niveau für Gliederung */
    @Schema(required = false, description = "DQR-Niveau für Gliederung")
    public String Gliederung;

    /** DQR-Niveau für die Fachklasse */
    @Schema(required = false, description = "DQR-Niveau für die Fachklasse")
    public String FKS;

    /** DQR-Niveau als Nummer */
    @Schema(required = false, description = "DQR-Niveau als Nummer")
    public Integer DQR_Niveau;

    /** Gültig ab Schuljahr */
    @Schema(required = false, description = "Gültig ab Schuljahr")
    public Integer gueltigVon;

    /** Gültig bis Schuljahr */
    @Schema(required = false, description = "Gültig bis Schuljahr")
    public Integer gueltigBis;

}
