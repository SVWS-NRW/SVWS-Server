package de.svws_nrw.core.data.stundenplan;

import de.svws_nrw.core.data.RGBFarbe;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu den Fächern eines Stundenplans.
 */
@XmlRootElement
@Schema(description = "ein Fach eines Stundenplans.")
@TranspilerDTO
public class StundenplanFach {

	/** Die ID des Faches. */
	@Schema(description = "die ID des Faches", example = "4711")
	public long id = -1;

	/** Das Kürzel des Faches. */
	@Schema(description = "Das Kürzel des Faches", example = "D")
	public @NotNull String kuerzel = "";

	/** Die Bezeichnung des Faches. */
	@Schema(description = "Die Bezeichnung des Faches", example = "Deutsch")
	public @NotNull String bezeichnung = "";

	/** Eine Nummer, welche die Sortierreihenfolge bei den Fächern angibt. */
	@Schema(description = "Eine Nummer, welche die Sortierreihenfolge bei den Fächern angibt", example = "32000")
	public int sortierung = 32000;

	/** Die Farbe, die zur Darstellung des Faches genutzt werden soll - sofern vom Standard abgewichen werden soll. */
	@Schema(description = "die Farbe, die zur Darstellung des Faches genutzt werden soll - sofern vom Standard abgewichen werden soll")
	public RGBFarbe farbe = null;

}
