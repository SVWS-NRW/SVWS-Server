package de.svws_nrw.core.data.schild3;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für den Schild3-Katalog Prüfungsordnung-Optionen.
 */
@XmlRootElement
@Schema(description="Schild3-Katalog Prüfungsordnung-Optionen")
@TranspilerDTO
public class Schild3KatalogEintragPruefungsordnungOption {

    /** Schulformen für die die Optionen gelten */
    @Schema(required = false, description = "Schulformen für die die Optionen gelten")
    public String OP_Schulformen;

    /** Kürzel der Prüfungsordung */
    @Schema(required = false, description = "Kürzel der Prüfungsordung")
    public String OP_POKrz;

    /** Angezeigter Kurztext in Schild-NRW */
    @Schema(required = false, description = "Angezeigter Kurztext in Schild-NRW")
    public String OP_Krz;

    /** Abgangsart in der Statistik B */
    @Schema(required = false, description = "Abgangsart in der Statistik B")
    public String OP_Abgangsart_B;

    /** Abgangsart in der Statistik NB */
    @Schema(required = false, description = "Abgangsart in der Statistik NB")
    public String OP_Abgangsart_NB;

    /** A=Allgemein B=Berufsbildend */
    @Schema(required = false, description = "A=Allgemein B=Berufsbildend")
    public String OP_Art;

    /** Abschlusskürzel in Schild-NRW */
    @Schema(required = false, description = "Abschlusskürzel in Schild-NRW")
    public String OP_Typ;

    /** Bildungsgang A oder B */
    @Schema(required = false, description = "Bildungsgang A oder B")
    public String OP_Bildungsgang;

    /** Text des Abschlusses */
    @Schema(required = false, description = "Text des Abschlusses")
    public String OP_Name;

    /** Paragraph in der BASS (veraltet?) */
    @Schema(required = false, description = "Paragraph in der BASS (veraltet?)")
    public String OP_Kommentar;

    /** Zulässig für diese Jahrgänge */
    @Schema(required = false, description = "Zulässig für diese Jahrgänge")
    public String OP_Jahrgaenge;

    /** Zulässig für BKIndex */
    @Schema(required = false, description = "Zulässig für BKIndex")
    public String OP_BKIndex;

    /** Zulässig für diese Gliederungen */
    @Schema(required = false, description = "Zulässig für diese Gliederungen")
    public String OP_BKAnl_Typ;

    /** Reihenfolge */
    @Schema(required = false, description = "Reihenfolge")
    public Integer OP_Reihenfolge;

    /** Gültig ab Schuljahr */
    @Schema(required = false, description = "Gültig ab Schuljahr")
    public Integer gueltigVon;

    /** Gültig bis Schuljahr */
    @Schema(required = false, description = "Gültig bis Schuljahr")
    public Integer gueltigBis;

}
