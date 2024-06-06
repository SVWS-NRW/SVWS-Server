package de.svws_nrw.core.data.schueler;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten eines Vermerkes.
 */
@XmlRootElement
@Schema(description = "Die Vermerkdaten eines Schülers.")
@TranspilerDTO
public class SchuelerVermerke {

	/** Die ID des Schülervermerks. */
	@Schema(description = "Die ID des Schülervermerks", example = "4711")
	public long id;

	/** ID des entsprechenden Schülers. */
	@Schema(description = "ID des entsprechenden Schülers.", example = "4713")
	public long schueler_id;

	/** ID der entsprechenden Vermerkart. */
	@Schema(description = "ID der entsprechenden Vermerkart.", example = "4")
	public long VermerkArt_ID;

	/** Das Datum der Erstellung oder letzten Bearbeitung. */
	@Schema(description = "Das Datum der Erstellung oder letzten Bearbeitung.", example = "2")
	public String Datum;

	/** Der Vermerk als Text. */
	@Schema(description = "Der Vermerk als Text. ", example = "20.04.2021")
	public String Bemerkung;

	/** Name des Benutzers welcher den Vermerk angelegt hat. */
	@Schema(description = "Name des Benutzers welcher den Vermerk angelegt hat", example = "12.02.2023")
	public String AngelegtVon;

	/** Name des Benutzers welcher den Vermerk als letzten bearbeitet hat. */
	@Schema(description = "Name des Benutzers welcher den Vermerk als letzten bearbeitet hat.", example = "12.02.2023")
	public String GeaendertVon;
}
