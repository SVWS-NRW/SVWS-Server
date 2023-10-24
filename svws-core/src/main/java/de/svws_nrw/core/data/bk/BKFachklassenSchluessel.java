package de.svws_nrw.core.data.bk;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse spezifiziert einen Bildungsgangs anhand des Gliederungsindex und
 * des Fachklassenschlüssels, wobei der index schon Gliederungen zusammenfassen kann.
 * zum Beispiel entspricht der Index 10 den Gliederungen A01-A04
 */
@XmlRootElement
@Schema(description = "Identifizierung eines Bildungsgangs.")
@TranspilerDTO
public class BKFachklassenSchluessel {

	/** Der Index der Schulgliederung */
	@Schema(description = "index der Schulgliederung", example = "10")
	public @NotNull Integer index = -1;

	/** Der Schlüssel der Fachklasse */
	@Schema(description = "Schlüssel der Fachklasse", example = "10004")
	public @NotNull String schluessel = "";

}
