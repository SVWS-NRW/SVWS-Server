package de.svws_nrw.core.data.klassen;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für die Klassen übergeben werden.
 */
@XmlRootElement
@Schema(description = "ein Eintrag einer Klasse in der Liste der Klassen.")
@TranspilerDTO
public class KlassenListeEintrag {

	/** Die ID der Klasse. */
	@Schema(description = "die ID der Klasse", example = "4711")
	public long id;

	/** Die ID des Schuljahresabschnittes des Kurses. */
	@Schema(description = "die ID des Schuljahresabschnittes des Kurses", example = "14")
	public long idSchuljahresabschnitt;

	/** Das Kürzel der Klasse. */
	@Schema(description = "das Kürzel der Klasse", example = "06b")
	public String kuerzel;

	/** Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist */
	@Schema(description = "die ID des zugeordneten Jahrgangs", example = "815")
	public Long idJahrgang;

	/** Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z). */
	@Schema(description = "das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z)", example = "B")
	public String parallelitaet;

	/** Die Sortierreihenfolge des Jahrgangslisten-Eintrags. */
	@Schema(description = "die Sortierreihenfolge des Jahrgangslisten-Eintrags", example = "1")
	public int sortierung;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

	/** Die Liste der IDs der Klassen-Lehrer der Klasse. */
	@ArraySchema(schema = @Schema(implementation = Long.class))
	public @NotNull List<@NotNull Long> klassenLehrer = new ArrayList<>();

	/** Die Schüler der Klasse. */
	public @NotNull List<@NotNull Schueler> schueler = new ArrayList<>();

}
