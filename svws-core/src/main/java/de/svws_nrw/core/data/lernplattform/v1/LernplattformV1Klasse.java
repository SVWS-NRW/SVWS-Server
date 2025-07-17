package de.svws_nrw.core.data.lernplattform.v1;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Datenstruktur einer Klasse, innerhalb des Exports einer Lernplattform.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Datenstruktur einer Klasse, innerhalb des Exports einer Lernplattform.")
@TranspilerDTO
public class LernplattformV1Klasse {
	/** Die ID der Klasse aus der SVWS-DB (z.B. 12) */
	@Schema(description = "Die ID der Klasse aus der SVWS-DB.", example = "12")
	public long id;

	/** Das Kürzel der Klasse, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z. B. EFA) */
	@Schema(description = "Das Kürzel der Klasse, wie es im Rahmen der amtlichen Schulstatistik verwendet wird.", example = "EFA")
	public String kuerzel;

	/** Das Kürzel der Klasse, wie es im Rahmen der Schule benannt wird und angezeigt werden soll. (z. B. EF) */
	@Schema(description = "Das Kürzel der Klasse, wie es im Rahmen der Schule benannt wird und angezeigt werden soll.", example = "EF")
	public String kuerzelAnzeige;

	/** Die ID des Jahrgangs aus der SVWS-DB zu der die Klasse gehört. Bei einer jahrgangsübergreifenden Klasse ist der Wert null. */
	@Schema(description = "Die ID des Jahrgangs aus der SVWS-DB zu der die Klasse gehört. Bei einer jahrgangsübergreifenden Klasse ist der Wert null.",
			example = "11")
	public Long idJahrgang;

	/** Die IDs der zugeordneten Klassenlehrer. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit IDs der zugeordneten Klassenlehrer."))
	public @NotNull List<Long> idsKlassenlehrer = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LernplattformV1Klasse() {
		// leer
	}
}
