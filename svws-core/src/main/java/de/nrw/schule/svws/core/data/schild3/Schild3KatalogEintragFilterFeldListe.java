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
public class Schild3KatalogEintragFilterFeldListe {

    /** ID für den Eintrag welche Felder im Attributsfilter zur Verfügung stehen. */
    @Schema(required = false, description = "ID für den Eintrag welche Felder im Attributsfilter zur Verfügung stehen")
    public Long ID;

    /** Bezeichnung im Attributsfilter */
    @Schema(required = false, description = "Bezeichnung im Attributsfilter")
    public String Bezeichnung;

    /** Datenbankfeld im Attributsfilter */
    @Schema(required = false, description = "Datenbankfeld im Attributsfilter")
    public String DBFeld;

    /** Typ des Feldes im Attributsfilter */
    @Schema(required = false, description = "Typ des Feldes im Attributsfilter")
    public String Typ;

    /** Mögliche Werte des Feldes im Attributsfilter */
    @Schema(required = false, description = "Mögliche Werte des Feldes im Attributsfilter")
    public String Werte;

    /** Standardwert im Attributsfilter */
    @Schema(required = false, description = "Standardwert im Attributsfilter")
    public String StdWert;

    /** Operator  im Attributsfilter (größer-kleiner) */
    @Schema(required = false, description = "Operator  im Attributsfilter (größer-kleiner)")
    public String Operator;

    /** Zusatzbedingung im Attributsfilter */
    @Schema(required = false, description = "Zusatzbedingung im Attributsfilter")
    public String Zusatzbedingung;

}
