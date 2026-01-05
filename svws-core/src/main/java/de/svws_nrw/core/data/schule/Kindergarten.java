package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt den schulspezifischen Eintrag im Katalog der Kindergärten.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der schulspezifischen Kindergärten.")
@TranspilerDTO
public class Kindergarten {

	/** Die ID des Kindergartens. */
	@Schema(description = "Die ID des Kindergartens.", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die Bezeichnung des Kindergartens. */
	@Schema(description = "Die Bezeichnung des Kindergartens.", example = "Kita Sonnenschein")
	public @NotNull String bezeichnung = "";

	/** Bemerkung zum Kindergartens. */
	@Schema(description = "Die Bemerkung zum Kindergartens.", example = "Ist geschlossen")
	public String bemerkung;

	/** Die Telefonnummer des Kindergartens. */
	@Schema(description = "Die Telefonnummer des Kindergartens.", example = "00007-4711")
	public String tel;

	/** Die E-Mail des Kindergartens. */
	@Schema(description = "Die E-Mail des Kindergartens.", example = "kita@sonnenschein.de")
	public String email;

	/** Der Strassenname des Kindergartens. */
	@Schema(description = "Der Strassenname des Kindergartens.", example = "Musterweg")
	public String strassenname;

	/** Die Hausnummer des Kindergartens. */
	@Schema(description = "Die Hausnummer des Kindergartens.", example = "12")
	public String hausNr;

	/** der Hausnummerzusatz des Kindergartens. */
	@Schema(description = "Der Hausnummerzusatz des Kindergartens.", example = "a-d")
	public String hausNrZusatz;

	/** Die PLZ des Kindergartens. */
	@Schema(description = "Die PLZ des Kindergartens.", example = "42287")
	public String plz;

	/** Der Ort des Kindergartens. */
	@Schema(description = "Der Ort des Kindergartens.", example = "Düsseldorf")
	public String ort;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an.", example = "1")
	public int sortierung;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.", example = "true")
	public boolean istSichtbar;

	/** Gibt an, ob der Kindergarten in anderen Datenbanktabellen referenziert ist oder nicht. */
	@Schema(description = "Gibt an, ob der Kindergarten in anderen Datenbanktabellen referenziert ist oder nicht.",
			example = "true",
			accessMode = Schema.AccessMode.READ_ONLY)
	public boolean referenziertInAnderenTabellen;

}
