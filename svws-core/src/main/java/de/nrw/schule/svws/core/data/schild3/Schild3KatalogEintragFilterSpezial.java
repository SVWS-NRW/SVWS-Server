package de.nrw.schule.svws.core.data.schild3;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für den Schild3-Katalog Spezialfilter.
 */
@XmlRootElement
@Schema(description="Schild3-Katalog Spezialfilter")
@TranspilerDTO
public class Schild3KatalogEintragFilterSpezial {

    /** ID der Felder die im Filter II zur Verfügung stehen. */
    @Schema(required = false, description = "ID der Felder die im Filter II zur Verfügung stehen")
    public Long ID;

    /** Gruppe in Filter II */
    @Schema(required = false, description = "Gruppe in Filter II")
    public String Gruppe;

    /** Kurzbezeichnung in Filter II */
    @Schema(required = false, description = "Kurzbezeichnung in Filter II")
    public String KurzBez;

    /** Bezeichnung in Filter II */
    @Schema(required = false, description = "Bezeichnung in Filter II")
    public String Bezeichnung;

    /** Filter II */
    @Schema(required = false, description = "Filter II")
    public String Grundschule;

    /** Tabelle für Filter II */
    @Schema(required = false, description = "Tabelle für Filter II")
    public String Tabelle;

    /** Datenbankfeld für Filter II */
    @Schema(required = false, description = "Datenbankfeld für Filter II")
    public String DBFeld;

    /** Feldtyp für Filter II */
    @Schema(required = false, description = "Feldtyp für Filter II")
    public String Typ;

    /** Filter II */
    @Schema(required = false, description = "Filter II")
    public String Control;

    /** Filter II */
    @Schema(required = false, description = "Filter II")
    public String WerteAnzeige;

    /** Filter II */
    @Schema(required = false, description = "Filter II")
    public String WerteSQL;

    /** Filter II */
    @Schema(required = false, description = "Filter II")
    public String LookupInfo;

    /** Filter II */
    @Schema(required = false, description = "Filter II")
    public String OperatorenAnzeige;

    /** Filter II */
    @Schema(required = false, description = "Filter II")
    public String OperatorenSQL;

    /** Filter II */
    @Schema(required = false, description = "Filter II")
    public String Zusatzbedingung;

    /** Filter II */
    @Schema(required = false, description = "Filter II")
    public String ZusatzTabellen;

}
