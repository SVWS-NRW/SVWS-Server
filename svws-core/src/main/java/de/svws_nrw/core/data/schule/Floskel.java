package de.svws_nrw.core.data.schule;


import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-Api-Schnittstelle verwendet.
 * Sie beschreibt wie die Daten der Floskel übergeben werden.
 */
@XmlRootElement
@Schema(description = "Ein Eintrag im Katalog der schulspezifischen Floskeln")
@TranspilerDTO
public class Floskel {

	/** Die ID der Floskel */
	@Schema(description = "Die ID der Floskel", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Das Kürzel der Floskel */
	@Schema(description = "Das Kürzel der Floskel", example = "#103")
	public String kuerzel;

	/** Der Text */
	@Schema(description = "Der Text", example = "#$Vorname$ bringt sich bei Teamarbeit sehr produktiv in die Gruppe ein.")
	public String text;

	/** Die ID der Floskelgruppe */
	@Schema(description = "Die ID der Floskelgruppe", example = "1")
	public Long idFloskelgruppe;

	/** Die ID des Fachs */
	@Schema(description = "Die ID des Fachs", example = "8")
	public Long idFach;

	/** Das Niveau */
	@Schema(description = "Das Niveau", example = "1")
	public Integer niveau;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public int sortierung;

	/** Die IDs der Jahrgänge, falls die Floskel auf bestimmte Jahrgänge eingeschränkt ist. Liegt keine Einschränkung vor so ist die Liste leer */
	@ArraySchema(schema = @Schema(implementation = Long.class,
			description = "Die IDs der Jahrgänge, falls die Floskel auf bestimmte Jahrgänge eingeschränkt ist. Liegt keine Einschränkung vor so ist die Liste leer"))
	public List<Long> idsJahrgaenge = new ArrayList<>();

}
