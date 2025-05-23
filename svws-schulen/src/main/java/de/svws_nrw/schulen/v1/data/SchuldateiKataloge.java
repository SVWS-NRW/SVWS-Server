package de.svws_nrw.schulen.v1.data;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Kataloge zu der Schuldatei.
 */
@XmlRootElement
@Schema(description = "die Kataloge zu der Schuldatei.")
@TranspilerDTO
public class SchuldateiKataloge {

	/** Die Version der Schuldatei. */
	@Schema(description = "die Version")
	public @NotNull SchuldateiVersion version = new SchuldateiVersion();

	/** Die Katalog-Einträge */
	@ArraySchema(schema = @Schema(implementation = SchuldateiKatalogeintrag.class))
	public @NotNull List<SchuldateiKatalogeintrag> kataloge = new ArrayList<>();


	/**
	 * Erstellt Kataloge zu der Schuldatei
	 */
	public SchuldateiKataloge() {
		// Die Initialisierung mit Defaults erfolgt direkt über die Attribute
	}

}
