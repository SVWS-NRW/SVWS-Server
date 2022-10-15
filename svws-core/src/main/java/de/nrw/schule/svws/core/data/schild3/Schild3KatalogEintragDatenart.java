package de.nrw.schule.svws.core.data.schild3;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für den Schild3-Katalog Datenart.
 */
@XmlRootElement
@Schema(description="Schild3-Katalog Datenart")
@TranspilerDTO
public class Schild3KatalogEintragDatenart {

    /** Kürzel der Datenart */
	@Schema(required = false, description = "Kürzel der Datenart")
    public String DatenartKrz;

    /** Datenart  */
    @Schema(required = false, description = "Datenart")
    public String Datenart;

    /** Name der Tabelle */
    @Schema(required = false, description = "Name der Tabelle")
    public String Tabellenname;

    /** Reihenfolge */
    @Schema(required = false, description = "Reihenfolge")
    public Integer Reihenfolge;

    /** Gültig ab Schuljahr */
    @Schema(required = false, description = "Gültig ab Schuljahr")
    public Integer gueltigVon;

    /** Gültig bis Schuljahr */
    @Schema(required = false, description = "Gültig bis Schuljahr")
    public Integer gueltigBis;

}
