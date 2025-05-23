package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt den schulspezifischen Katalog der Kindergräten.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der schulspezifischen Kindergräten.")
@TranspilerDTO
public class Kindergarten {

	/** Die ID des Kindergartens. */
	@Schema(description = "die ID des Kindergartens", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Die Bezeichnung des Kindergartens. */
	@Schema(description = "die Bezeichnung des Kindergartens", example = "Kita Sonnenschein")
	public @NotNull String bezeichnung = "";

	/** Die PLZ des Kindergartens. */
	@Schema(description = "die PLZ des Kindergartens", example = "42287")
	public String plz = "";

	/** Der Ort des Kindergartens. */
	@Schema(description = "der Ort des Kindergartens", example = "Düsseldorf")
	public String ort = "";

	/** Der Strassenname des Kindergartens. */
	@Schema(description = "der Strassenname des Kindergartens", example = "Musterweg")
	public String strassenname = "";

	/** Die Hausnummer des Kindergartens. */
	@Schema(description = "die Hausnummer des Kindergartens", example = "12")
	public String hausNr = "";

	/** der Hausnummerzusatz des Kindergartens. */
	@Schema(description = "der Hausnummerzusatz des Kindergartens", example = "a-d")
	public String hausNrZusatz = "";

	/** Die Telefonnummer des Kindergartens. */
	@Schema(description = "die Telefonnummer des Kindergartens", example = "00007-4711")
	public String tel = "";

	/** Die E-Mail des Kindergartens. */
	@Schema(description = "die E-Mail des Kindergartens", example = "kita@sonnenschein.de")
	public String email = "";

	/** Bemerkung zum Kindergartens. */
	@Schema(description = "bemerkung zum Kindergartens", example = "Ist geschlossen")
	public String bemerkung = "";

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar = true;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public int sortierung = 1;

}
