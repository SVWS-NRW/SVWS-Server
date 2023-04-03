package de.svws_nrw.core.data.schild3;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für den Schild3-Katalog Filter Fehlende Einträge.
 */
@XmlRootElement
@Schema(description="Schild3-Katalog Filter Fehlende Einträge")
@TranspilerDTO
public class Schild3KatalogEintragFilterFehlendeEintraege {

    /** ID des Eintrags die bei der Prüfung auf fehlende Einträge genutzt werden */
    @Schema(description = "ID des Eintrags die bei der Prüfung auf fehlende Einträge genutzt werden")
    public Long ID;

    /** Beschreibung des Eintrags die bei der Prüfung auf fehlende Einträge genutzt werden */
    @Schema(description = "Beschreibung des Eintrags die bei der Prüfung auf fehlende Einträge genutzt werden")
    public String Beschreibung;

    /** Feldname des zu prüfenden Feldes */
    @Schema(description = "Feldname des zu prüfenden Feldes")
    public String Feldname;

    /** Tabellenname des zu prüfenden Feldes */
    @Schema(description = "Tabellenname des zu prüfenden Feldes")
    public String Tabellen;

    /** Abfrage die zur Prüfung des Feldes führt. */
    @Schema(description = "Abfrage die zur Prüfung des Feldes führt")
    public String SQLText;

    /** ggf. Schulform für bestimmte Schulformen */
    @Schema(description = "ggf. Schulform für bestimmte Schulformen")
    public String Schulform;

    /** Feldtyp des zu prüfenden Feldes */
    @Schema(description = "Feldtyp des zu prüfenden Feldes")
    public String Feldtyp;

}
