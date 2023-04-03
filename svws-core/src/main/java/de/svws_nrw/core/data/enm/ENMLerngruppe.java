package de.svws_nrw.core.data.enm;

import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu Lerngruppen
 * in Bezug auf den Lernabschnitt für das Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description="Spezifiziert die Struktur von JSON-Daten zu Lerngruppen in Bezug auf den Lernabschnitt für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMLerngruppe {

	/** Die eindeutige ID der Lerngruppe - generiert, nicht (!) aus der SVWS-DB */
	@Schema(description = "Die eindeutige ID der Lerngruppe - generiert, nicht (!) aus der SVWS-DB.", example="12345")
	public long id;
	
	/** Die ID der Lerngruppe in der SVWS-DB (Die ID des Kurses oder die ID der Klasse in der Versetzungstabelle, siehe kursartID). */
	@Schema(description = "Die ID der Lerngruppe in der SVWS-DB (Die ID des Kurses oder die ID der Klasse in der "
			+ "Versetzungstabelle, siehe kursartID).", example="1234")
	public long kID; 

	/** Die ID des Faches der Lerngruppe. */
	@Schema(description = "Die ID des Faches der Lerngruppe.", example="123")
	public long fachID;

	/** Gibt die ID der Kursart an. Ist dieser Wert null, so handelt es sich um Klassen-Unterricht. */
	@Schema(description = "Gibt die ID der Kursart an. Ist dieser Wert null, so handelt es sich um Klassen-Unterricht.", example="PUT")
	public Integer kursartID;

	/** Die Bezeichnung der Lerngruppe (z.B. D-GK4) */
	@Schema(description = "Die Bezeichnung der Lerngruppe.", example="D-GK4")
	public String bezeichnung;

	/** Die Bezeichnung der (allgemeinen) Kursart (z.B. GK) */
	@Schema(description = "Das Kürzel der (allgemeinen) Kursart.", example="GK")
	public String kursartKuerzel;

	/** Das einstellige Kürzel der bilingualen Sprache, sofern es sich um eine bilinguale Lerngruppe handelt. (z.B. F) */
	@Schema(description = "Das einstellige Kürzel der bilingualen Sprache, "
			+ "sofern es sich um eine bilinguale Lerngruppe handelt.", example="F")
	public String bilingualeSprache;

	/** Die IDs der Lehrer, die der Lerngruppe zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den Informationen zu den "
			+ "IDs der Lehrer, die der Lerngruppe zugeordnet sind."))
	public @NotNull Vector<@NotNull Long> lehrerID = new Vector<>();
	
	/** Die Anzahl der Wochenstunden, falls es sich um einen Kurs handelt. */
	@Schema(description = "Die Anzahl der Wochenstunden, falls es sich um einen Kurs handelt.", example="3")
	public int wochenstunden;

}

