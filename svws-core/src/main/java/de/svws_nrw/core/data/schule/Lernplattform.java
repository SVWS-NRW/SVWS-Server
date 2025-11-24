package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt den schulspezifischen Katalog der Einwilligungsarten.
 */
@XmlRootElement
@Schema(description = "Ein Eintrag in dem Katalog der schulspezifischen Lernplattformen.")
@TranspilerDTO
public class Lernplattform {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Die Bezeichnung der Lernplattform. */
	@Schema(description = "Die Bezeichnung der Lernplattform", example = "IServ")
	public @NotNull String bezeichnung = "";

	/** Gibt an, ob die Lernplattform in anderen Datenbanktabellen referenziert ist oder nicht. */
	@Schema(description = "Gibt an, ob die Lernplattform in anderen Datenbanktabellen referenziert ist oder nicht.", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
	public Boolean referenziertInAnderenTabellen = false;

}
