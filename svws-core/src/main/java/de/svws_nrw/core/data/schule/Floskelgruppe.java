package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-Api-Schnittstelle verwendet.
 * Sie beschreibt wie die Daten der Floskelgruppe übergeben werden.
 */
@XmlRootElement
@Schema(description = "Ein Eintrag im Katalog der schulspezifischen Floskelgruppen")
@TranspilerDTO
public class Floskelgruppe {

	/** Die ID der Floskelgruppe */
	@Schema(description = "Die ID der Floskelgruppe", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Das Kürzel der Floskelgruppe */
	@Schema(description = "Das Kürzel der Floskelgruppe", example = "ALLG")
	public @NotNull  String kuerzel = "";

	/** Die Bezeichnung der Floskelgruppe */
	@Schema(description = "Die Bezeichnung der Floskelgruppe", example = "Allgemeine Floskeln")
	public @NotNull  String bezeichnung = "";

	/** Die ID der Floskelgruppenart */
	@Schema(description = "Die ID der Floskelgruppenart", example = "1")
	public Long idFloskelgruppenart;

	/** Gibt an, ob die Telefonart in anderen Datenbanktabellen referenziert ist oder nicht. */
	@Schema(description = "Gibt an, ob die Telefonart in anderen Datenbanktabellen referenziert ist oder nicht.", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
	public boolean referenziertInAnderenTabellen;
}
