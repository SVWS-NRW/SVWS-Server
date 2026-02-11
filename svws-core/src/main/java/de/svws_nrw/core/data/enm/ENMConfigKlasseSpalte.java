package de.svws_nrw.core.data.enm;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zur Konfiguration
 * für eine Klasse bei der Zusammenstellung der im Client anzuzeigenden Daten.
 */
@XmlRootElement
@Schema(description = "Die Konfiguration für eine Klasse bei der Zusammenstellung der im Client anzuzeigenden Daten.")
@TranspilerDTO
public class ENMConfigKlasseSpalte {

	/** Die ID dieser Teilleistung in der SVWS-DB, sofern es sich um eine Teilleistung handelt. */
	@Schema(description = "Die ID der Teilleistung in der SVWS-DB, sofern es sich um eine Teilleistung handelt.", example = "307956")
	public Long idTeilleistung = null;

	/** Der Name der Spalte */
	@Schema(description = "Der Name der Spalte", example = "ZB")
	public @NotNull String name = "";

	/** Gibt an, ob die Spalte gesperrt werden soll oder nicht. */
	@Schema(description = "Gibt an, ob die Spalte gesperrt werden soll oder nicht.", example = "false")
	public boolean gesperrt = true;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ENMConfigKlasseSpalte() {
		// leer
	}

}
