package de.svws_nrw.core.data.schule;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu dem allgemeinen Katalog der Räume.
 */
@XmlRootElement
@Schema(description = "ein Raum")
@TranspilerDTO
public class Raum {

	/** Die ID des Raumes. */
	@Schema(description = "die ID des Raumes", example = "4711")
	public long id = -1;

	/** Das Raumkürzel. */
	@Schema(description = "das Raumkürzel", example = "R042")
	public @NotNull String kuerzel = "";

	/** Die Beschreibung des Raumes. */
	@Schema(description = "die Beschreibung des Raumes", example = "Klassenraum der Klasse 07b")
	public @NotNull String beschreibung = "";

	/** Die Grösse des Raumes an Arbeitsplätzen für Schüler. */
	@Schema(description = "die Grösse des Raumes an Arbeitsplätzen für Schüler", example = "30")
	public int groesse = -1;

}
