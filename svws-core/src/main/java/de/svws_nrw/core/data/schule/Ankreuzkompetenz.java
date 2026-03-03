package de.svws_nrw.core.data.schule;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieses Core-DTO beinhaltet die Information zu einer Ankreuzkompetenz.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer Ankreuzkompetenz.")
@TranspilerDTO
public class Ankreuzkompetenz {

	/** Die ID des Eintrags für die Ankreuzkompetenz */
	@Schema(description = "die ID des Eintrags für die Ankreuzkompetenz", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Die ID des Faches, die zur Ankreuzkompetenz gehört, sofern IstASV den Wert false hat. Ansonsten wird diese ID des Faches ein null übergeben */
	@Schema(description = "die ID des Faches, die zur Ankreuzkompetenz gehört, sofern IstASV den Wert 0 hat. Ansonsten wird diese ID des Faches ein null übergeben", example = "null")
	public Long idFach;

	/** Gibt an, falls die Fach_ID null ist, ob es sich bei der Ankreuzkompetenz um eine Floskel zum Arbeits- und Sozialverhalten handelt (true) oder nicht (false). */
	@Schema(description = "gibt an, falls die Fach_ID null ist, ob es sich bei der Ankreuzkompetenz um eine Floskel zum Arbeits- und Sozialverhalten handelt (true) oder nicht (false)", example = "32000")
	public boolean istASV = false;

	/** Schulgliederung zu der die Ankreuzkompetenz gehört (nur wichtig bei BK) */
	@Schema(description = "schulgliederung zu der die Ankreuzkompetenz gehört (nur wichtig bei BK)", example = "A02")
	public String schulgliederung;

	/** Der Text der Ankreuzkompetenz */
	@Schema(description = "der Text der Ankreuzkompetenz", example = "Testtext")
	public @NotNull String floskelText = "";

	/** Gibt an in welchem Abschnitten (1. HJ, 2. HJ oder beide) die Ankreuzkompetenz benutzt wird. */
	@Schema(description = "gibt an in welchem Abschnitten (1. HJ(1), 2. HJ(2) oder beide(3) die Ankreuzkompetenz benutzt wird", example = "1")
	public int abschnitt;

	/** Gibt an, ob die Ankreuzkompetenz aktiv ist. */
	@Schema(description = "gibt an, ob die Ankreuzkompetenz aktiv ist", example = "true")
	public boolean istAktiv;

	/** Gibt an, ob die Ankreuzkompetenz in der Anwendung sichtbar sein soll oder nicht */
	@Schema(description = "gibt an, ob die Ankreuzkompetenz in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

	/** Gibt einen Wert für die Sortierung des Faches der Ankreuzkompetenz an. */
	@Schema(description = "gibt einen Wert für die Sortierung des Faches der Ankreuzkompetenz an", example = "32000")
	public int fachSortierung;

	/** Gibt einen Wert für die Sortierung der Ankreuzkompetenz an. */
	@Schema(description = "gibt einen Wert für die Sortierung der Ankreuzkompetenz an", example = "32000")
	public int sortierung;

	/** Die Zuordnung der Jahrgänge zu der Ankreuzkompetenzen. */
	@ArraySchema(
			schema = @Schema(implementation = AnkreuzkompetenzJahrgangszuordnung.class,
					description = "die Zuordnung der Jahrgänge zu der Ankreuzkompetenz.",
					accessMode = Schema.AccessMode.READ_ONLY)
	)
	public final @NotNull List<AnkreuzkompetenzJahrgangszuordnung> jahrgaengezuordnung = new ArrayList<>();

}
