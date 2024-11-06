package de.svws_nrw.core.data.enm;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten zu
 * einem Eintrag bei einem Schüler zu einer Ankreuzkompetenz für das Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die grundlegende Struktur von JSON-Daten einem Eintrag bei einem Schüler zu einer Ankreuzkompetenz für das"
		+ " Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMSchuelerAnkreuzkompetenz {

	/** Die ID des Eintrages aus der SVWS-DB */
	@Schema(description = "die ID des Eintrages aus der SVWS-DB", example = "12345")
	public long id = -1;

	/** Die ID der der Ankreuzkompetenz, auf welches sich der Eintrag bezieht */
	@Schema(description = "die ID der der Ankreuzkompetenz, auf welches sich der Eintrag bezieht", example = "42")
	public Long kompetenzID = null;

	/** Gibt für die einzelnen Stufen 1-5 der Ankreuzkompetenzen an, ob diese zugewiesen ist oder nicht (hier mit einer Verschiebung von 1 zum Array-Index). */
	@ArraySchema(schema = @Schema(implementation = Boolean.class,
			description = "gibt für die einzelnen Stufen 1-5 der Ankreuzkompetenzen an, ob diese zugewiesen ist oder nicht (hier mit einer Verschiebung von 1 zum Array-Index)."))
	public @NotNull boolean[] stufen = new boolean[5];

	/** Der Zeitstempel der letzten Änderung an den zugewiesenen Stufen */
	@Schema(description = "Der Zeitstempel der letzten Änderung an den zugewiesenen Stufen.", example = "2013-11-14 13:12:48.774")
	public String tsStufe;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ENMSchuelerAnkreuzkompetenz() {
		// leer
	}

}
