package de.svws_nrw.core.data.schild3;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für den Schild3-Katalog Import und Export von Schülerdaten.
 */
@XmlRootElement
@Schema(description="Schild3-Katalog Import und Export von Schülerdaten")
@TranspilerDTO
public class Schild3KatalogEintragSchuelerImportExport {

    /** Tabelle mit den Daten für den Import-Export */
    @Schema(description = "TMP")
    public String Tabelle;

    /** Anzeigename */
    @Schema(description = "TMP")
    public String TabellenAnzeige;

    /** Master-Tabelle */
    @Schema(description = "TMP")
    public String MasterTable;

    /** SQL-Befehl für dem Export */
    @Schema(description = "TMP")
    public String ExpCmd;

    /** SQL-Befehl zum Ermitteln der Feldnamen */
    @Schema(description = "TMP")
    public String SrcGetFieldsSQL;

    /** SQL-Befehl zum Enternen der Daten */
    @Schema(description = "TMP")
    public String DeleteSQL;

    /** SQL-Befehl zum Ermitteln der IDs */
    @Schema(description = "TMP")
    public String DstGetIDSQL;

    /** Hauptfeld */
    @Schema(description = "TMP")
    public String HauptFeld;

    /** Detail-Feld */
    @Schema(description = "TMP")
    public String DetailFeld;

    /** Reihenfolge */
    @Schema(description = "TMP")
    public Integer Reihenfolge;

}
