package de.svws_nrw.core.data.stundenplan;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem Kurs eines Stundenplans.
 */
@XmlRootElement
@Schema(description = "ein Kurs eines Stundenplans.")
@TranspilerDTO
public class StundenplanKurs {

	/** Die ID des Kurses. */
	@Schema(description = "die ID des Kurses", example = "4711")
	public long id = -1;

	/** Die Bezeichnung des Kurses. */
	@Schema(description = "Die Bezeichnung des Kurses", example = "M-GK1")
	public @NotNull String bezeichnung = "";

	/** Die Wochenstunden, welche dem Kurs zugeordnet sind */
	@Schema(description = "die Wochenstunden, welche dem Kurs zugeordnet sind.", example = "3")
	public int wochenstunden = 0;

	/** Die Liste der IDs der Schienen, denen der Kurs zugeordnet ist. */
	@Schema(description = "die Liste der IDs der Schienen, denen der Kurs zugeordnet ist")
	public @NotNull List<@NotNull Long> schienen = new ArrayList<>();

	/** Die Liste der IDs der Jahrgänge, denen der Kurs zugeordnet ist. */
	@Schema(description = "die Liste der IDs der Jahrgänge, denen der Kurs zugeordnet ist")
	public @NotNull List<@NotNull Long> jahrgaenge = new ArrayList<>();

	/** Die Liste der IDs der Schüler, die dem Kurs zugeordnet sind. */
	@Schema(description = "die Liste der IDs der Schüler, die dem Kurs zugeordnet sind")
	public @NotNull List<@NotNull Long> schueler = new ArrayList<>();

}
