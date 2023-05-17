package de.svws_nrw.core.data.stundenplan;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu den Schülern eines Stundenplans.
 */
@XmlRootElement
@Schema(description = "ein Schüler eines Stundenplans.")
@TranspilerDTO
public class StundenplanSchueler {

	/** Die ID des Lehrers. */
	@Schema(description = "die ID des Schülers", example = "4711")
	public long id = -1;

	/** Der Nachname des Schülers. */
	@Schema(description = "Der Nachname des Schülers", example = "Mustermann")
	public @NotNull String nachname = "";

	/** Der Vorname des Schülers. */
	@Schema(description = "Der Vorname des Schülers", example = "Max")
	public @NotNull String vorname = "";

	/** Die ID der Klasse in der sich der Schüler befindet. */
	@Schema(description = "die ID der Klasse in der sich der Schüler befindet")
	public long idKlasse = -1;

}
