package de.svws_nrw.schulen.v1.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt die Informationen zu einer Schulform, welche einer
 * Organisationseinheit im Rahmen der Schuldatei zugeordnet sein kann.
 */
@XmlRootElement
@Schema(description = "die Information zu einer Schulform einer Organisationseinheit.")
@TranspilerDTO
public class SchuldateiOrganisationseinheitSchulform extends SchuldateiEintrag {

	/** Die ID des Schulform-Eintrages. */
	@Schema(description = "die ID des Schulform-Eintrages", example = "4711")
	public String id = null;

	/** Die Schulnummer. */
	@Schema(description = "die Schulnummer", example = "100001")
	public @NotNull String schulnummer = "";

	/** Schulformcode */
	@Schema(description = "Schulformcode", example = "Schulform")
	public @NotNull String schulformcode = "";

	/** Schulformwert */
	@Schema(description = "Schulformwert", example = "08")
	public @NotNull String schulformwert = "";


	/**
	 * Erstellt eine neue Schulform für eine Organiationseinheit der Schuldatei
	 */
	public SchuldateiOrganisationseinheitSchulform() {
		// Die Initialisierung mit Defaults erfolgt direkt über die Attribute
	}

}
