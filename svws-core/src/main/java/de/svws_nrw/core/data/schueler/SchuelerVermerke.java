package de.svws_nrw.core.data.schueler;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten eines Vermerkes bei einem Schüler.
 */
@XmlRootElement
@Schema(description = "Die Vermerkdaten eines Schülers.")
@TranspilerDTO
public class SchuelerVermerke {

	/** Die ID des Schülervermerks. */
	@Schema(description = "die ID des Schülervermerks", example = "4711")
	public long id;

	/** Die ID des zugehörigen Schülers. */
	@Schema(description = "die ID des zugehörigen Schülers.", example = "4713")
	public long idSchueler;

	/** Die ID der Vermerkart des Vermerks. */
	@Schema(description = "die ID der Vermerkart des Vermerks", example = "4")
	public Long idVermerkart;

	/** Das Datum der Erstellung oder letzten Bearbeitung. */
	@Schema(description = "Das Datum der Erstellung oder letzten Bearbeitung.", example = "12.04.2022")
	public String datum;

	/** Der Vermerk als Text. */
	@Schema(description = "Der Vermerk als Text. ", example = "Eltern haben nicht zugestimmt")
	public @NotNull String bemerkung = "";

	/** Name des Benutzers welcher den Vermerk angelegt hat. */
	@Schema(description = "Name des Benutzers welcher den Vermerk angelegt hat", example = "Max Mustermann")
	public String angelegtVon;

	/** Name des Benutzers welcher den Vermerk als letzten bearbeitet hat. */
	@Schema(description = "Name des Benutzers welcher den Vermerk als letzten bearbeitet hat.", example = "Dr. Manuela Musterfrau")
	public String geaendertVon;

}
