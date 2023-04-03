package de.svws_nrw.core.data.schild3;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für den Schild3-Katalog Filter Feldliste.
 */
@XmlRootElement
@Schema(description = "Schild3-Katalog Filter Feldliste")
@TranspilerDTO
public class Schild3KatalogEintragFilterFeldListe {

    /** ID für den Eintrag welche Felder im Attributsfilter zur Verfügung stehen. */
    @Schema(description = "ID für den Eintrag welche Felder im Attributsfilter zur Verfügung stehen")
    public Long ID;

    /** Bezeichnung im Attributsfilter */
    @Schema(description = "Bezeichnung im Attributsfilter")
    public String Bezeichnung;

    /** Datenbankfeld im Attributsfilter */
    @Schema(description = "Datenbankfeld im Attributsfilter")
    public String DBFeld;

    /** Typ des Feldes im Attributsfilter */
    @Schema(description = "Typ des Feldes im Attributsfilter")
    public String Typ;

    /** Mögliche Werte des Feldes im Attributsfilter */
    @Schema(description = "Mögliche Werte des Feldes im Attributsfilter")
    public String Werte;

    /** Standardwert im Attributsfilter */
    @Schema(description = "Standardwert im Attributsfilter")
    public String StdWert;

    /** Operator  im Attributsfilter (größer-kleiner) */
    @Schema(description = "Operator  im Attributsfilter (größer-kleiner)")
    public String Operator;

    /** Zusatzbedingung im Attributsfilter */
    @Schema(description = "Zusatzbedingung im Attributsfilter")
    public String Zusatzbedingung;

}
