package de.svws_nrw.schuldatei.v1.data;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt eine zusätzliche Eigenschaft einer Organisationseinheit
 * der Schuldatei.
 */
@XmlRootElement
@Schema(description = "die zusätzliche Eigenschaft einer Organisationseinheit der Schuldatei.")
@TranspilerDTO
public class SchuldateiOrganisationseinheitEigenschaft {

	/** Die ID des Eigenschafts-Eintrags. */
	@Schema(description = "Die ID des Eigenschafts-Eintrags", example = "4711")
	public Integer id;

	/** Die Schulnummer. */
	@Schema(description = "die Schulnummer", example = "100001")
	public @NotNull Integer schulnummer = 0;

	/** Die Eigenschaftsnummer */
	@Schema(description = "Die Eigenschaftsnummer", example = "2")
	public @NotNull String eigenschaft = "";

	/** Beschreibung*/
	@Schema(description = "Beschreibung", example = "Schule nimmt nicht an ASD teil")
	public String Beschreibung;

    /** Detail */
    @Schema(description = "Detail")
    public String detail;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
    @Schema(description = "Gibt die Gültigkeit ab welchem Schuljahr an")
    public String gueltigab;

    /** Gibt die Gültigkeit bis zu welchem Schuljahr an */
    @Schema(description = "Gibt die Gültigkeit bis zu welchem Schuljahr an")
    public String gueltigbis;

    /** Gibt das Änderungsdatum des Eintrags an*/
    @Schema(description = "Gibt das Änderungsdatum des Eintrags an")
    public String geaendertam;


    /**
     * Erstellt eine neue weitere Eigenschaft einer Organiationseinheit der Schuldatei
     */
    public SchuldateiOrganisationseinheitEigenschaft() {
        // Die Initialisierung mit Defaults erfolgt direkt über die Attribute
    }

}
