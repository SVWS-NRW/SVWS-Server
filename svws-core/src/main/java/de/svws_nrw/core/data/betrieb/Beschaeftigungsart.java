package de.svws_nrw.core.data.betrieb;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation äber die Open-Api-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten der Beschäftigungsart übergeben werden.
 */
@XmlRootElement
@Schema(description = "Ein Eintrag im Katalog der schulspezifischen Beschäftigungsarten")
@TranspilerDTO
public class Beschaeftigungsart {

	/** Die ID der Beschäftigungsart */
	@Schema(description = "Die ID der Beschäftigungsart", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die Bezeichnung der Beschäftigungsart */
	@Schema(description = "Die Bezeichnung der Beschäftigungsart", example = "Azubi")
	public String bezeichnung;

	/** Die Position in der Sortierreihenfolge der Beschäftigungsarten */
	@Schema(description = "Die Position in der Sortierreihenfolge der Beschäftigungsarten", example = "1")
	public int sortierung;

	/** Die Sichtbarkeit der Beschäftigungsart */
	@Schema(description = "Die Sichtbarkeit der Beschäftigungsart", example = "true")
	public boolean istSichtbar;

	/** Die Änderbarkeit der Beschäftigungsart */
	@Schema(description = "Die Änderbarkeit der Beschäftigungsart", example = "true")
	public boolean istAenderbar;
}
