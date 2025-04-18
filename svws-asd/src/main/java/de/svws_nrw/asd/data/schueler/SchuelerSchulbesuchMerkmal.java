package de.svws_nrw.asd.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt besondere Merkmale für die Statistik in Bezug auf den Schulbesuch eines Schülers.
 */
@XmlRootElement
@Schema(description = "Das besondere Merkmale für die Statistik in Bezug auf den Schulbesuch des Schülers.")
@TranspilerDTO
public class SchuelerSchulbesuchMerkmal {

	/** Die ID des besonderen Merkmals für die Statistik. */
	@Schema(description = "die ID des besonderen Merkmals für die Statistik", example = "3", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die ID des Merkmals */
	@Schema(description = "die ID des Merkmals", example = "1")
	public Long idMerkmal;

	/** Das Datum, ab dem das Merkmal vorliegt. */
	@Schema(description = "das Datum, ab dem das Merkmal vorliegt", example = "2007-08-01")
	public String datumVon;

	/** Das Datum, bis wann das Merkmal vorliegt. */
	@Schema(description = "das Datum, bis wann das Merkmal vorliegt", example = "2008-07-31")
	public String datumBis;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuelerSchulbesuchMerkmal() {
		// leer
	}

}
