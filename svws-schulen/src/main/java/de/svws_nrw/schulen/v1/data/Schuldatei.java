package de.svws_nrw.schulen.v1.data;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Schuldatei.
 */
@XmlRootElement
@Schema(description = "die Schuldatei.")
@TranspilerDTO
public class Schuldatei {

	/** Die Version der Schuldatei. */
	@Schema(description = "die Version")
	public @NotNull SchuldateiVersion version = new SchuldateiVersion();

	/** Die Organisationseinheit des Eintrags */
	@ArraySchema(schema = @Schema(implementation = SchuldateiOrganisationseinheit.class))
	public @NotNull List<SchuldateiOrganisationseinheit> organisationseinheiten = new ArrayList<>();


	/**
	 * Erstellt eine Schuldatei
	 */
	public Schuldatei() {
		// Die Initialisierung mit Defaults erfolgt direkt über die Attribute
	}

}
