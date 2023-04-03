package de.svws_nrw.core.data.schild3;

import de.svws_nrw.core.transpiler.TranspilerDTO;
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
	@Schema(description = "Kürzel der Datenart")
    public String DatenartKrz;

    /** Datenart  */
    @Schema(description = "Datenart")
    public String Datenart;

    /** Name der Tabelle */
    @Schema(description = "Name der Tabelle")
    public String Tabellenname;

    /** Reihenfolge */
    @Schema(description = "Reihenfolge")
    public Integer Reihenfolge;

    /** Gültig ab Schuljahr */
    @Schema(description = "Gültig ab Schuljahr")
    public Integer gueltigVon;

    /** Gültig bis Schuljahr */
    @Schema(description = "Gültig bis Schuljahr")
    public Integer gueltigBis;

}
