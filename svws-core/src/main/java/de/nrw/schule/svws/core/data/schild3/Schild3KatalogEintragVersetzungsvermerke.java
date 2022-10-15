package de.nrw.schule.svws.core.data.schild3;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für den Schild3-Katalog Versetzungsvermerke / PrfSemAbschl.
 */
@XmlRootElement
@Schema(description="Schild3-Katalog Versetzungsvermerke / PrfSemAbschl")
@TranspilerDTO
public class Schild3KatalogEintragVersetzungsvermerke {

    /** Nummer des Versetzungsvermerks */
    @Schema(required = false, description = "Nummer des Versetzungsvermerks")
    public String Nr;

    /** Klartext des Versetzungsvermerks */
    @Schema(required = false, description = "Klartext des Versetzungsvermerks")
    public String Klartext;

    /** Statistikkürzel des Versetzungsvermerks (DEPRECATED) */
    @Schema(required = false, description = "Statistikkürzel des Versetzungsvermerks (DEPRECATED)")
    public String StatistikKrz;

    /** Sortierung des Versetzungsvermerks */
    @Schema(required = false, description = "Sortierung des Versetzungsvermerks")
    public Integer Sortierung;

    /** Schulform des Versetzungsvermerks */
    @Schema(required = false, description = "Schulform des Versetzungsvermerks")
    public String Schulform;

    /** Neues Statistikkürzel des Versetzungsvermerks */
    @Schema(required = false, description = "Neues Statistikkürzel des Versetzungsvermerks")
    public String StatistikKrzNeu;

    /** Gültig ab Schuljahr */
    @Schema(required = false, description = "Gültig ab Schuljahr")
    public Integer gueltigVon;

    /** Gültig bis Schuljahr */
    @Schema(required = false, description = "Gültig bis Schuljahr")
    public Integer gueltigBis;

}
