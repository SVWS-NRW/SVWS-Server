package de.svws_nrw.core.data.enm;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zur Konfiguration
 * für die im Client anzuzeigenden Spalten.
 */
@XmlRootElement
@Schema(description = "Die Konfiguration für die im Client anzuzeigenden Spalten.")
@TranspilerDTO
public class ENMConfigSpalte {

	/** Die ID dieser Teilleistung in der SVWS-DB, sofern es sich um eine Teilleistung handelt. */
	@Schema(description = "Die ID der Teilleistung in der SVWS-DB, sofern es sich um eine Teilleistung handelt.", example = "307956")
	public Long idTeilleistung = null;

	/** Der Name der Spalte */
	@Schema(description = "Der Name der Spalte", example = "ZB")
	public @NotNull String name = "";

	/** Gibt an, ob die Spalte angezeigt werden soll oder nicht. */
	@Schema(description = "Gibt an, ob die Spalte angezeigt werden soll oder nicht.", example = "true")
	public boolean anzeigen = true;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ENMConfigSpalte() {
		// leer
	}

}
