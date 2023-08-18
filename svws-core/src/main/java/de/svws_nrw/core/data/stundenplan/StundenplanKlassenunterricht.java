package de.svws_nrw.core.data.stundenplan;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem Klassenunterricht eines Stundenplans.
 */
@XmlRootElement
@Schema(description = "ein Klassenunterricht eines Stundenplans.")
@TranspilerDTO
public class StundenplanKlassenunterricht {

	/** Die ID der Klasse. */
	@Schema(description = "die ID der Klasse", example = "4711")
	public long idKlasse = -1;

	/** Die ID des Faches. */
	@Schema(description = "die ID des Faches", example = "4712")
	public long idFach = -1;

	/** Die Bezeichnung des Klassenunterrichtes. */
	@Schema(description = "Die Bezeichnung des Klassenunterrichtes", example = "M (9a)")
	public @NotNull String bezeichnung = "";

	/** Die Wochenstunden, welche dem Klassenunterricht zugeordnet sind */
	@Schema(description = "die Wochenstunden, welche dem Klassenunterricht zugeordnet sind.", example = "3")
	public int wochenstunden = 0;

	/** Die Liste der IDs der Schienen, denen der Klassenunterricht zugeordnet ist. */
	@Schema(description = "die Liste der IDs der Schienen, denen der Klassenunterricht zugeordnet ist")
	public @NotNull List<@NotNull Long> schienen = new ArrayList<>();

	/** Die Liste der IDs der Schüler, denen der Klassenunterricht zugeordnet ist. */
	@Schema(description = "die Liste der IDs der Schüler, denen der Klassenunterricht zugeordnet ist")
	public @NotNull List<@NotNull Long> schueler = new ArrayList<>();

	/** Die Liste der IDs der Lehrer, die dem Klassenunterricht zugeordnet sind. */
	@Schema(description = "die Liste der IDs der Lehrer, die dem Klassenunterricht zugeordnet sind")
	public @NotNull List<@NotNull Long> lehrer = new ArrayList<>();

}
