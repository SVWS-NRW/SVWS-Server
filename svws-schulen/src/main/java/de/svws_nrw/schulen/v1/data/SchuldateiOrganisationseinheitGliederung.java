package de.svws_nrw.schulen.v1.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt eine Gliederung einer Organisationseinheit
 * der Schuldatei.
 */
@XmlRootElement
@Schema(description = "eine Gliederung einer Organisationseinheit der Schuldatei.")
@TranspilerDTO
public class SchuldateiOrganisationseinheitGliederung extends SchuldateiEintrag {

	/** Die Schulnummer. */
	@Schema(description = "die Schulnummer", example = "100001")
	public @NotNull String schulnummer = "";

	/** Die ID des Gliederungs-Eintrages. */
	@Schema(description = "die ID des Gliederungs-Eintrages", example = "4711")
	public Integer id;

	/** Die Gliederung */
	@Schema(description = "die Gliederung", example = "G01")
	public @NotNull String gliederung = "";

	/** Der Förderschwerpunkt */
	@Schema(description = "der Förderschwerpunkt", example = "LB")
	public @NotNull String foerderschwerpunkt = "";


	/**
	 * Erstellt eine neue Gliederung für eine Organisationseinheit der Schuldatei
	 */
	public SchuldateiOrganisationseinheitGliederung() {
		// Die Initialisierung mit Defaults erfolgt direkt über die Attribute
	}

}
