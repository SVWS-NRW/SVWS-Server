package de.svws_nrw.core.data.email;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Ein Dateianhang für den E-Mail-Versand über die API. Der MIME-Type wird nicht übergeben, weil der Server ihn aus den Daten selbst bestimmt.
 */
@XmlRootElement
@Schema(description = "ein Dateianhang für den E-Mail-Versand")
@TranspilerDTO
public class EmailAttachment {

	/** Der Dateiname des Anhangs mit Endung. */
	@Schema(description = "der Dateiname des Anhangs mit Endung", example = "Einladung.pdf")
	public @NotNull String filename = "";

	/** Der Inhalt der Datei, Base64-kodiert. */
	@Schema(description = "der Inhalt der Datei, Base64-kodiert", example = "JVBERi0xLjcK")
	public @NotNull String data = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public EmailAttachment() {
		// leer
	}

}
