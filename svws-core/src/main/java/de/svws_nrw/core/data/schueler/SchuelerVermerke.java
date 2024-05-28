package de.svws_nrw.core.data.schueler;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten eines Vermerkes eines Schüelers.
 */
@XmlRootElement
@Schema(description = "Die Vermerkdaten eines Schülers.")
@TranspilerDTO
public class SchuelerVermerke {

	/** ID des Datensatzes */
	@Schema(description = "TODO", example = "4711")
	public long id;

	/** ID des Schülers */
	@Schema(description = "TODO", example = "4713")
	public long schueler_id;

	/** TODO */
	@Schema(description = "TODO", example = "4")
	public long VermerkArt_ID;

	/** TODO */
	@Schema(description = "TODO", example = "2")
	public String Datum;

	/** TODO */
	@Schema(description = "TODO", example = "20.04.2021")
	public String Bemerkung;

	/** TODO */
	@Schema(description = "TODOr", example = "12.02.2023")
	public String AngelegtVon;

	/** TODO */
	@Schema(description = "TODOr", example = "12.02.2023")
	public String GeaendertVon;
}
