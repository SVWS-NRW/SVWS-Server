package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt den schulspezifischen Katalog der Einwilligungsarten.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der schulspezifischen Einwilligungsarten.")
@TranspilerDTO
public class Einwilligungsart {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id = -1;

	/** Die Bezeichnung der Einwilligungsart. */
	@Schema(description = "die Bezeichnung der Einwilligungsart", example = "Verwendung Foto")
	public @NotNull String bezeichnung = "";


	/** Der Schlüssel der Einwilligungsart. */
	@Schema(description = "der Schlüssel der Einwilligungsart", example = "FOTO")
	public String schluessel = "";


	/** Eine ausführliche Beschreibung der Einwilligungsart. */
	@Schema(description = "Eine ausführliche Beschreibung der Einwilligungsart", example = "Einwilligung zur Verwendung von Fotos")
	public String beschreibung = "";

	/** Die Id des PersonTyps der Einwilligungsart. */
	@Schema(description = "Die Id des PersonTyps der Einwilligungsart.", example = "2")
	public int idPersonTyp = -1;

	/** Gibt an, für welche Personengruppe die Einwilligungsart relevant ist. */
	@Schema(description = "gibt an wie viele Einwilligungen dem entsprechenden Einwilligungsart-Eintrag zugeordnet sind", example = "3")
	public int anzahlEinwilligungen;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public int sortierung = 32000;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar = true;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Einwilligungsart() {
		// leer
	}

}
