package de.svws_nrw.core.data.schule;


import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten der schulspezifischen Betriebsart übergeben werden.
 */
@XmlRootElement
@Schema(description = "Ein Eintrag im Katalog der schulspezifischen Betriebsart.")
@TranspilerDTO
public class Betriebsart {

	/** Die ID der Betriebsart. */
	@Schema(description = "Die ID der Betriebsart.", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die Bezeichnung der Betriebsart. */
	@Schema(description = "Die Bezeichnung der Betriebsart.", example = "Einzelhandel")
	public @NotNull String bezeichnung = "";

	/** Gibt an, ob die Betriebsart in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "Gibt an, ob die Betriebsart in der Anwendung sichtbar sein soll oder nicht.", example = "true")
	public boolean istSichtbar;

	/** Die Sortierreihenfolge der Betriebsart. */
	@Schema(description = "Die Sortierreihenfolge der Betriebsart.", example = "1")
	public int sortierung;

	/** Gibt an, ob die Betriebsart in anderen Datenbanktabellen referenziert ist oder nicht. */
	@Schema(description = "Gibt an, ob die Betriebsart in anderen Datenbanktabellen referenziert ist oder nicht.", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
	public boolean referenziertInAnderenTabellen;
}
