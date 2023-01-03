package de.nrw.schule.svws.core.data.klassen;

import java.util.Vector;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für die Klassen übergeben werden.  
 */
@XmlRootElement
@Schema(description="ein Eintrag einer Klasse in der Liste der Klassen.")
@TranspilerDTO
public class KlassenListeEintrag {

	/** Die ID der Klasse. */
	@Schema(required = true, description = "die ID der Klasse", example="4711")
	public long id;
	
	/** Das Kürzel der Klasse. */
	@Schema(required = true, description = "das Kürzel der Klasse", example="06b")
	public String kuerzel;

	/** Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist */
	@Schema(required = false, description = "die ID des zugeordneten Jahrgangs", example="815")
	public Long idJahrgang;

	/** Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z). */
	@Schema(required = true, description = "das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z)", example="B")
	public String parallelitaet; 

	/** Die Sortierreihenfolge des Jahrgangslisten-Eintrags. */
	@Schema(required = true, description = "die Sortierreihenfolge des Jahrgangslisten-Eintrags", example="1")
	public int sortierung;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(required = true, description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example="true")
	public boolean istSichtbar;
	
	/** Die Liste der IDs der Klassen-Lehrer der Klasse. */
	@ArraySchema(schema = @Schema(implementation = Long.class))
	public Vector<@NotNull Long> klassenLehrer = new Vector<>();
		
}
