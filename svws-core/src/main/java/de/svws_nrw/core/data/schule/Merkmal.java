package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-Api-Schnittstelle verwendet.
 * Sie beschreibt wie die Daten des Merkmals übergeben werden.
 */
@XmlRootElement
@Schema(description = "Das Merkmal einer Schule")
@TranspilerDTO
public class Merkmal {

	/** Die ID des Merkmals */
	@Schema(description = "Die ID des Merkmals", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Gibt an, ob das Merkmal einer Schule zugewiesen werden kann */
	@Schema(description = "Gibt an, ob das Merkmal einer Schule zugewiesen werden kann", example = "true")
	public boolean istSchulmerkmal;

	/** Gibt an, ob das Merkmal einem Schueler zugewiesen werden kann */
	@Schema(description = "Gibt an, ob das Merkmal einem Schueler zugewiesen werden kann", example = "true")
	public boolean istSchuelermerkmal;

	/** Das Kuerzel des Merkmals */
	@Schema(description = "Das Kuerzel des Merkmals", example = "GANZTAG")
	public String kuerzel;

	/** Die Bezeichnung des Merkmals */
	@Schema(description = "Die Bezeichnung des Merkmals", example = "Ganztagsschule")
	public String bezeichnung;

}
