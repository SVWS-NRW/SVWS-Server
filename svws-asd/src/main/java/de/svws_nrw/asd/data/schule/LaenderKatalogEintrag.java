package de.svws_nrw.asd.data.schule;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieser DTO beinhaltet die Daten für den Katalog der Länder.
 */
@XmlRootElement
@Schema(description = "ein Eintrag im Katalog der Länder")
@TranspilerDTO
public class LaenderKatalogEintrag extends CoreTypeData {

	/** Der Amtliche Gemeindeschlüssel (AGS) des Landes. */
	@Schema(description = "der Amtliche Gemeindeschlüssel (AGS) des Landes", example = "01000000")
	public String ags = null;

	/** Die Postleitzahl des Standorts der Schule. */
	@Schema(description = "die Postleitzahl des Standorts der Schule", example = "12345")
	public String plz = null;

	/** Ein Zahlwert, welcher die Sortierreihenfolge angibt. */
	@Schema(description = "ein Zahlwert, welcher die Sortierreihenfolge angibt", example = "2")
	public @NotNull Integer sortierung = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LaenderKatalogEintrag() {
		// leer
	}

}

