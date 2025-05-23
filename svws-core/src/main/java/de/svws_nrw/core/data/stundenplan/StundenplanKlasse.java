package de.svws_nrw.core.data.stundenplan;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einer Klasse eines Stundenplans.
 */
@XmlRootElement
@Schema(description = "eine Klasse eines Stundenplans.")
@TranspilerDTO
public class StundenplanKlasse {

	/** Die ID der Klasse. */
	@Schema(description = "die ID der Klasse", example = "4711")
	public long id = -1;

	/** Das Kürzel der Klasse. Darf nicht "blank" sein. */
	@Schema(description = "das Kürzel der Klasse", example = "01b")
	public @NotNull String kuerzel = "";

	/** Die Bezeichnung der Klasse, z.B. "Eichhörnchen". Darf "blank" sein. */
	@Schema(description = "Die Bezeichnung der Klasse", example = "Eichhörnchen")
	public @NotNull String bezeichnung = "";

	/** Die Sortierreihenfolge des Klassenlisten-Eintrags. */
	@Schema(description = "die Sortierreihenfolge des Klassenlisten-Eintrags", example = "1")
	public int sortierung = 32000;

	/** Die Liste der {@link StundenplanJahrgang}-IDs denen die Klasse zugeordnet ist. */
	@Schema(description = "die Liste der IDs der Jahrgänge, denen die Klasse zugeordnet ist")
	public @NotNull List<Long> jahrgaenge = new ArrayList<>();

	/** Die Liste der IDs der  {@link StundenplanSchueler}, die der Klasse zugeordnet sind. */
	@Schema(description = "die Liste der IDs der Schüler, die der Klasse zugeordnet sind")
	public @NotNull List<Long> schueler = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public StundenplanKlasse() {
		// leer
	}

}
