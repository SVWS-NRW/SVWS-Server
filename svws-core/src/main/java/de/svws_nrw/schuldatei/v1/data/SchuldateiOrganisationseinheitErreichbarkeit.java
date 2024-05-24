package de.svws_nrw.schuldatei.v1.data;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt eine Erreichbarkeit für eine Organisationseinheit
 * der Schuldatei.
 */
@XmlRootElement
@Schema(description = "eine Erreichbarkeit einer Organisationseinheit der Schuldatei.")
@TranspilerDTO
public class SchuldateiOrganisationseinheitErreichbarkeit extends SchuldateiEintrag {

	/** Die ID des Erreichbarkeits-Eintrags. */
	@Schema(description = "die ID des Erreichbarkeits-Eintrags", example = "4711")
	public Integer id;

	/** Die Schulnummer. */
	@Schema(description = "die Schulnummer", example = "100001")
	public @NotNull Integer schulnummer = 0;

	/** Die Nummer der Liegenschaft der Organisationseinheit */
	@Schema(description = "Die Nummer der Liegenschaft der Organisationseinheit", example = "1")
	public @NotNull Integer liegenschaft = 0;

    /** Kommgruppe des Eintrags*/
    @Schema(description = "Kommgruppe des Eintrags")
    public Integer kommgruppe;

    /** codekey des Eintrags */
    @Schema(description = "codekey des Eintrags", example = "01")
    public String codekey;

    /** codewert des Eintrags */
    @Schema(description = "codewert des Eintrags", example = "01234/1243501")
    public String codewert;


    /**
     * Erstellt einen neuen Eintrag zur Erreichbarkeit einer Organisationseinheit der Schuldatei
     */
    public SchuldateiOrganisationseinheitErreichbarkeit() {
        // Die Initialisierung mit Defaults erfolgt direkt über die Attribute
    }

}
