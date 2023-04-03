package de.svws_nrw.core.data.schule;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.schule.Schulform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse spezifiziert den Core-DTO für einen Katalog-Eintrag im 
 * Katalog der Schulstufen.  
 */
@XmlRootElement
@Schema(description = "ein Eintrag im Katalog der Schulstufen.")
@TranspilerDTO
public class SchulstufeKatalogEintrag {

    /** Die ID des Katalog-Eintrags. */
    @Schema(description = "die ID des Katalog-Eintrags", example = "4711")
    public long id = -1;

	/** Das eindeutige Kürzel des Katalog-Eintrags. */
	@Schema(description = "das eindeutige Kürzel des Katalog-Eintrags", example = "SI")
	public @NotNull String kuerzel = "";

    /** Die Bezeichnung des Katalog-Eintrags. */
    @Schema(description = "die Bezeichnung des Katalog-Eintrags", example = "Sekundarstufe I")
    public @NotNull String bezeichnung = "";

    /** Die Kürzel der Schulformen die bei der Schulstufe vorkommen. */
    @Schema(description = "die Kürzel der Schulformen die bei der Schulstufe vorkommen")
    public @NotNull List<@NotNull String> schulformen = new Vector<>();
    
    /** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
    @Schema(description = "gibt an, in welchem schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
    public Integer gueltigVon = null;

    /** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
    @Schema(description = "gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "2025")
    public Integer gueltigBis = null;


    /**
     * Erstellt einen Schulstufen-Eintrag mit Standardwerten
     */
    public SchulstufeKatalogEintrag() {
    }


    /**
     * Erstellt einen Schulstufen-Eintrag mit den angegebenen Werten
     * 
     * @param id              die ID
     * @param kuerzel         das Kürzel 
     * @param bezeichnung     die Bezeichnung
     * @param schulformen     die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt
     * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
     * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
     */
    public SchulstufeKatalogEintrag(final long id, final @NotNull String kuerzel, final @NotNull String bezeichnung, 
            final @NotNull List<@NotNull Schulform> schulformen, final Integer gueltigVon, final Integer gueltigBis) {
        this.id = id;
        this.kuerzel = kuerzel;
        this.bezeichnung = bezeichnung;
        for (final @NotNull Schulform schulform : schulformen)
            this.schulformen.add(schulform.daten.kuerzel);
        this.gueltigVon = gueltigVon;
        this.gueltigBis = gueltigBis;
    }

}
