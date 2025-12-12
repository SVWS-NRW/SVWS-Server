package de.svws_nrw.core.data.enm;
import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieses Core-DTO beinhaltet die Information zu einer Abteilung,
 * welche von einem Lehrer geleitet wird.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer Abteilung.")
@TranspilerDTO
public class ENMAbteilung {
	/** Die ID des Eintrags für die Abteilung */
	@Schema(description = "die ID des Eintrags für die Abteilung", example = "4711")
	public long id = -1;

	/** Die Lehrer-ID des Abteilungsleiters, sofern die Abteilung einen zugewiesen hat. */
	@Schema(description = "die Lehrer-ID des Abteilungsleiters, sofern die Abteilung einen zugewiesen hat", example = "null")
	public Long idAbteilungsleiter = null;

	/** Die Bezeichnung der Abteilung (max. 50 Zeichen) */
	@Schema(description = "die Bezeichnung der Abteilung (max. 50 Zeichen)", example = "4712")
	public @NotNull String bezeichnung = "";

	/** Gibt einen Wert für die Sortierung der Abteilungen an. */
	@Schema(description = "gibt einen Wert für die Sortierung der Abteilungen an", example = "32000")
	public int sortierung = 32000;

	/** Die Zuordnung der Klassen zu der Abteilung. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "die Zuordnung der Klassen-IDs zu der Abteilung."))
	public final @NotNull List<Long> klassenzuordnungen = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ENMAbteilung() {
		// leer
	}

}
