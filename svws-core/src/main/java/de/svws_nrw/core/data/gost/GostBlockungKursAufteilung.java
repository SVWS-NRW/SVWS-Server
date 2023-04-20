package de.svws_nrw.core.data.gost;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird für die Rückmeldung beim Aufteilen eines Kurses einer Blockung der gymnasialen Oberstufe in zwei Kurse
 * verwendet.
 */
@XmlRootElement
@Schema(description = "Informationen zu der Aufteilung eines Kurses der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostBlockungKursAufteilung {

	/** Informationen zum ersten Kurs. */
	@Schema(description = "Informationen zum ersten Kurs.")
	public @NotNull GostBlockungKurs kurs1 = new GostBlockungKurs();

	/** Informationen zum zweiten Kurs. */
	@Schema(description = "Informationen zum zweiten Kurs - dem neuen Kurs.")
	public @NotNull GostBlockungKurs kurs2 = new GostBlockungKurs();

	/** Die IDs der Schüler, die dem ersten Kurs zugeordnet bleiben. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Die IDs der Schüler, die dem ersten Kurs zugeordnet bleiben."))
	public @NotNull List<@NotNull Long> schueler1 = new ArrayList<>();

	/** Die IDs der Schüler, die dem zweiten Kurs zugeordnet werden. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Die IDs der Schüler, die dem zweiten Kurs zugeordnet werden."))
	public @NotNull List<@NotNull Long> schueler2 = new ArrayList<>();

}
