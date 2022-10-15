package de.nrw.schule.svws.core.data.schild3;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für den Schild3-Katalog AbiturInfos.
 */
@XmlRootElement
@Schema(description="Schild3-Katalog AbiturInfos")
@TranspilerDTO
public class Schild3KatalogEintragAbiturInfos {

    /** Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
	@Schema(required = false, description = "Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin")
    public String PrfOrdnung;

    /** Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
    @Schema(required = false, description = "Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin")
    public String AbiFach;

    /** Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
    @Schema(required = false, description = "Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin")
    public String Bedingung;

    /** Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
    @Schema(required = false, description = "Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin")
    public String AbiInfoKrz;

    /** Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
    @Schema(required = false, description = "Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin")
    public String AbiInfoBeschreibung;

    /** Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
    @Schema(required = false, description = "Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin")
    public String AbiInfoText;

    /** Gültig ab Schuljahr */
    @Schema(required = false, description = "Gültig ab Schuljahr")
    public Integer gueltigVon;

    /** Gültig bis Schuljahr */
    @Schema(required = false, description = "Gültig bis Schuljahr")
    public Integer gueltigBis;

}
