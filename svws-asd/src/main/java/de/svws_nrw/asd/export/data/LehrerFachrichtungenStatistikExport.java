package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Fachrichtungsdaten der Lehrer (L63).
 */
@XmlRootElement
@Schema(description = "die Fachrichtungsdaten der Lehrer (L63)")
@TranspilerDTO
public class LehrerFachrichtungenStatistikExport {

	/** Satzschlüssel: Eine Fachrichtung eines Lehrers. */
	@Schema(description = "satzschlüssel: ein Fachrichtung eines Lehrers", example = "GB")
	public @NotNull String fachrichtung = "";

	/** Die Qualifikation zu der Fachrichtung. */
	@Schema(description = "die Qualifikation zu der Fachrichtung", example = "1")
	public @NotNull String qualifikation = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerFachrichtungenStatistikExport() {
	}

}
