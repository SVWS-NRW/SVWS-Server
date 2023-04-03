package de.svws_nrw.core.data.schild3;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für den Schild3-Katalog zur Konfiguration des CSV-Exportes von Schild.
 */
@XmlRootElement
@Schema(description="Schild3-Katalog zur Konfiguration des CSV-Exportes von Schild")
@TranspilerDTO
public class Schild3KatalogEintragExportCSV {

    /** Die Datenart */
    @Schema(description = "Die Datenart")
    public String DatenartKrz;

    /** Der Name des Feldes mit der ID */
    @Schema(description = "Der Name des Feldes mit der ID")
    public String Feldname;

    /** Der Text für die Anzeige */
    @Schema(description = "Der Text für die Anzeige")
    public String AnzeigeText;

    /** Der Feldtyp */
    @Schema(description = "Der Feldtyp")
    public String Feldtyp;

    /** Feldwerte */
    @Schema(description = "Feldwerte")
    public String Feldwerte;

    /** Ergebniswerte */
    @Schema(description = "Ergebniswerte")
    public String ErgebnisWerte;

    /** Der Name des Lookup-Feldes */
    @Schema(description = "Der Name des Lookup-Feldes")
    public String LookupFeldname;

    /** Der SQL-Befehl zum Bestimmen des Loopup-Feldwertes */
    @Schema(description = "Der SQL-Befehl zum Bestimmen des Loopup-Feldwertes")
    public String LookupSQLText;

    /** Die unterstützen Datenbank-Formate */
    @Schema(description = "Die unterstützen Datenbank-Formate")
    public String DBFormat;

}
