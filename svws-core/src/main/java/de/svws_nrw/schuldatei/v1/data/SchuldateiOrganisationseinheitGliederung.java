package de.svws_nrw.schuldatei.v1.data;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt eine Gliederung einer Organisationseinheit
 * der Schuldatei.
 */
@XmlRootElement
@Schema(description = "die Grunddaten einer Organisationseinheit der Schuldatei.")
@TranspilerDTO
public class SchuldateiOrganisationseinheitGliederung {

	/** Die ID des Gliederungs-Eintrages. */
	@Schema(description = "die ID des Gliederungs-Eintrages", example = "4711")
	public Integer id;

	/** Die Schulnummer. */
	@Schema(description = "die Schulnummer", example = "100001")
	public @NotNull Integer schulnummer = 0;

	/** Die Gliederung */
	@Schema(description = "die Gliederung", example = "G01")
	public String gliederung;

	/** Der Förderschwerpunkt */
	@Schema(description = "der Förderschwerpunkt", example = "LB")
	public String foerderschwerpunkt;

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
     * Erstellt eine neue Gliederung für eine Organisationseinheit der Schuldatei
     */
    public SchuldateiOrganisationseinheitGliederung() {
        // Die Initialisierung mit Defaults erfolgt direkt über die Attribute
    }

}
