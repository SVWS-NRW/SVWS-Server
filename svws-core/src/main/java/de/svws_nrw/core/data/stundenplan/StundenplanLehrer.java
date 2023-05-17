package de.svws_nrw.core.data.stundenplan;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu den Lehrern eines Stundenplans.
 */
@XmlRootElement
@Schema(description = "ein Lehrer eines Stundenplans.")
@TranspilerDTO
public class StundenplanLehrer {

	/** Die ID des Lehrers. */
	@Schema(description = "die ID des Lehrers", example = "4711")
	public long id = -1;

	/** Das Kürzel des Lehrers. */
	@Schema(description = "das Kürzel des Lehrers", example = "MUS")
	public @NotNull String kuerzel = "";

	/** Der Nachname des Lehrers. */
	@Schema(description = "Der Nachname des Lehrers", example = "Mustermann")
	public @NotNull String nachname = "";

	/** Der Vorname des Lehrers. */
	@Schema(description = "Der Vorname des Lehrers", example = "Max")
	public @NotNull String vorname = "";

	/** Die Liste der IDs der Unterrichsfächer, die der Lehrer unterrichten kann. */
	@Schema(description = "die Liste der IDs der Unterrichsfächer, die der Lehrer unterrichten kann")
	public @NotNull List<@NotNull Long> faecher = new ArrayList<>();

}
