package de.svws_nrw.core.data.lernplattform.v1;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Datenstruktur einer Lerngruppe in Bezug auf den Lernabschnitt, innerhalb des Exports einer Lernplattform.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Datenstruktur einer Lerngruppe in Bezug auf den Lernabschnitt, innerhalb des Exports einer Lernplattform.")
@TranspilerDTO
public class LernplattformV1Lerngruppe {

	/** Die generierte eindeutige ID der Lerngruppe. */
	@Schema(description = "Die generierte eindeutige ID der Lerngruppe.", example = "123")
	public long id;

	/** Die ID der Lerngruppe in der SVWS-DB (Die ID des Kurses oder die ID der Klasse in der Versetzungstabelle, siehe kursartID). */
	@Schema(description = "Die ID der Lerngruppe in der SVWS-DB (Die ID des Kurses oder die ID der Klasse in der Versetzungstabelle, siehe kursartID).",
			example = "1234")
	public long idIntern;

	/** Die ID des Faches der Lerngruppe. */
	@Schema(description = "Die ID des Faches der Lerngruppe.", example = "123")
	public long idFach;

	/** Gibt die ID der Kursart an. Ist dieser Wert null, so handelt es sich um Klassenunterricht. */
	@Schema(description = "Gibt die ID der Kursart an. Ist dieser Wert null, so handelt es sich um Klassenunterricht.", example = "PUT")
	public Integer idKursart;

	/** Die Bezeichnung der Lerngruppe (z.B. D-GK4) */
	@Schema(description = "Die Bezeichnung der Lerngruppe.", example = "D-GK4")
	public String bezeichnung;

	/** Das Kürzel der (allgemeinen) Kursart (z.B. GK) */
	@Schema(description = "Das Kürzel der (allgemeinen) Kursart.", example = "GK")
	public String kursartKuerzel;

	/** Das einstellige Kürzel der bilingualen Sprache, sofern es sich um eine bilinguale Lerngruppe handelt. (z.B. F) */
	@Schema(description = "Das einstellige Kürzel der bilingualen Sprache, "
			+ "sofern es sich um eine bilinguale Lerngruppe handelt.", example = "F")
	public String bilingualeSprache;

	/** Die IDs der Lehrer, die der Lerngruppe zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den IDs der Lehrer, die der Lerngruppe zugeordnet sind."))
	public @NotNull List<Long> idsLehrer = new ArrayList<>();

	/** Die Anzahl der Wochenstunden, falls es sich um einen Kurs handelt. */
	@Schema(description = "Die Anzahl der Wochenstunden, falls es sich um einen Kurs handelt.", example = "3")
	public Integer wochenstunden;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LernplattformV1Lerngruppe() {
		// leer
	}
}
