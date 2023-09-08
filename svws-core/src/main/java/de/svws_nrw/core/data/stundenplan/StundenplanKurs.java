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

	/** Eine Nummer, welche die Sortierreihenfolge bei den Kursen angibt. */
	@Schema(description = "Eine Nummer, welche die Sortierreihenfolge bei den Kursen angibt", example = "32000")
	public int sortierung = 32000;

	/** Die Liste der IDs der {@link StundenplanSchiene}-Objekte, denen der Kurs zugeordnet ist. */
	@Schema(description = "die Liste der IDs der Schienen, denen der Kurs zugeordnet ist")
	public @NotNull List<@NotNull Long> schienen = new ArrayList<>();

	/** Die Liste der IDs der {@link StundenplanJahrgang}-Objekte, denen der Kurs zugeordnet ist. */
	@Schema(description = "die Liste der IDs der Jahrgänge, denen der Kurs zugeordnet ist")
	public @NotNull List<@NotNull Long> jahrgaenge = new ArrayList<>();

	/** Die Liste der IDs der {@link StundenplanSchueler}-Objekte, die dem Kurs zugeordnet sind. */
	@Schema(description = "die Liste der IDs der Schüler, die dem Kurs zugeordnet sind")
	public @NotNull List<@NotNull Long> schueler = new ArrayList<>();

	/** Die Liste der IDs der {@link StundenplanLehrer}-Objekte, die dem Kurs zugeordnet sind. */
	@Schema(description = "die Liste der IDs der Lehrer, die dem Kurs zugeordnet sind")
	public @NotNull List<@NotNull Long> lehrer = new ArrayList<>();

}
