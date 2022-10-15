package de.nrw.schule.svws.core.data.schild3;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
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
    @Schema(required = false, description = "ID des Eintrags die bei der Prüfung auf fehlende Einträge genutzt werden")
    public Long ID;

    /** Beschreibung des Eintrags die bei der Prüfung auf fehlende Einträge genutzt werden */
    @Schema(required = false, description = "Beschreibung des Eintrags die bei der Prüfung auf fehlende Einträge genutzt werden")
    public String Beschreibung;

    /** Feldname des zu prüfenden Feldes */
    @Schema(required = false, description = "Feldname des zu prüfenden Feldes")
    public String Feldname;

    /** Tabellenname des zu prüfenden Feldes */
    @Schema(required = false, description = "Tabellenname des zu prüfenden Feldes")
    public String Tabellen;

    /** Abfrage die zur Prüfung des Feldes führt. */
    @Schema(required = false, description = "Abfrage die zur Prüfung des Feldes führt")
    public String SQLText;

    /** ggf. Schulform für bestimmte Schulformen */
    @Schema(required = false, description = "ggf. Schulform für bestimmte Schulformen")
    public String Schulform;

    /** Feldtyp des zu prüfenden Feldes */
    @Schema(required = false, description = "Feldtyp des zu prüfenden Feldes")
    public String Feldtyp;

}
