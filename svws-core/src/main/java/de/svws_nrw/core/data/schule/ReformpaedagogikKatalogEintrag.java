package de.svws_nrw.core.data.schule;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.schule.Schulform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Core-DTO definiert einen Katalog-Eintrag für
 * den Raformpädagogik-Katalog.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Reformpädagogik Katalog.")
@TranspilerDTO
public class ReformpaedagogikKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id;

	/** Das Kürzel. */
	@Schema(required = true, description = "das Kürzel", example="M")
	public @NotNull String kuerzel = "";
	
	/** Die Bezeichnung. */
	@Schema(required = true, description = "die Bezeichnung", example="Montessori")
	public @NotNull String bezeichnung = "";

    /** Die Kürzel der Schulformen, bei welchen der Eintrag vorkommen darf. */
    @Schema(required = true, description = "die Kürzel der Schulformen, bei welchen der Eintrag vorkommen darf")
    public @NotNull List<@NotNull String> schulformen = new Vector<>();

    /** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
    @Schema(required = false, description = "gibt an, in welchem der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
    public Integer gueltigVon = null;

    /** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schulgliederung bekannt, so ist null gesetzt. */
    @Schema(required = false, description = "gibt an, bis zu welchem der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
    public Integer gueltigBis = null;


    /**
     * Erstellt einen Reformpädagogik-Eintrag mit Standardwerten
     */
    public ReformpaedagogikKatalogEintrag() {
    }


    /**
     * Erstellt einen Reformpädagogik-Eintrag mit den angegebenen Werten
     * 
     * @param id              die ID
     * @param kuerzel         das Kürzel 
     * @param bezeichnung     die Bezeichnung 
     * @param schulformen     die Kürzel der Schulformen, bei welchen die Art der Reformpädagogik vorkommen kann
     * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
     * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
     */
    public ReformpaedagogikKatalogEintrag(final long id, final @NotNull String kuerzel, final @NotNull String bezeichnung,  
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
