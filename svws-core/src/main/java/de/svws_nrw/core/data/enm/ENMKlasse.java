package de.svws_nrw.core.data.enm;

import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu den Klasse der 
 * Schüler für das Externe-Noten-Modul ENM. 
 */
@XmlRootElement
@Schema(description="Spezifiziert die Struktur von JSON-Daten zu den Klassen der Schüler für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMKlasse {

	/** Die ID der Klasse Jahrgangs aus der SVWS-DB (z.B. 16) */
	@Schema(required = true, description = "Die ID der Klasse aus der SVWS-DB.", example="12")
	public long id;

	/** Das Kürzel der Klasse, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. EF) */
	@Schema(required = true, description = "Das Kürzel der Klasse, wie es im Rahmen der amtlichen Schulstatistik verwendet wird.", example="EF")
	public String kuerzel;

	/** Das Kürzel ser Klasse, wie er im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. EF) */
	@Schema(required = true, description = "Das Kürzel der Klasse, wie er im Rahmen der Schule benannt wird und angezeigt werden soll.", example="EF")
	public String kuerzelAnzeige;

	/** Die Reihenfolge der Klasse bei der Sortierung der Klasse. (z.B. 8) */
	@Schema(required = false, description = "Die Reihenfolge der Klasse bei der Sortierung der Klasse.", example="20")
	public int sortierung;

	/** Die IDs der zugeordneten Klassenlehrer. */
	@ArraySchema(schema = @Schema(required = true, implementation = Long.class, description = "Ein Array mit den Informationen der IDs der zugeordneten Klassenlehrer."))
	public @NotNull Vector<@NotNull Long> klassenlehrer = new Vector<>();

}
