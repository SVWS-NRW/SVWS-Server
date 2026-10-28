package de.svws_nrw.service.schule.katalog.ankreuzkompetenz;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public class AnkreuzkompetenzPatchRequest {

	/** Gibt an, ob es sich bei der Ankreuzkompetenz um eine Floskel zum Arbeits- und Sozialverhalten handelt (true) oder nicht (false). */
	@Schema(description = "gibt an, ob es sich bei der Ankreuzkompetenz um eine Floskel zum Arbeits- und Sozialverhalten handelt (true) oder nicht (false)",
			example = "false")
	public JsonNullable<@NotNull Boolean> istASV = JsonNullable.undefined();

	/** Die ID des Faches, die zur Ankreuzkompetenz gehört. Bei einer ASV-Ankreuzkompetenz darf kein Fach gesetzt sein, sonst ist es Pflicht. */
	@Schema(description = "die ID des Faches, die zur Ankreuzkompetenz gehört. Bei einer ASV-Ankreuzkompetenz darf kein Fach gesetzt sein, sonst ist es Pflicht",
			example = "null")
	public JsonNullable<Long> idFach = JsonNullable.undefined();

	/** Der Text der Ankreuzkompetenz */
	@Schema(description = "der Text der Ankreuzkompetenz", example = "Testtext")
	public JsonNullable<@NotBlank @Size(max = 255) String> floskelText = JsonNullable.undefined();

	/** Schulgliederung zu der die Ankreuzkompetenz gehört (nur wichtig bei BK) */
	@Schema(description = "schulgliederung zu der die Ankreuzkompetenz gehört (nur wichtig bei BK)", example = "A02")
	public JsonNullable<@Size(max = 3) String> schulgliederung = JsonNullable.undefined();

	/** Gibt an in welchem Abschnitten (1. HJ, 2. HJ oder beide) die Ankreuzkompetenz benutzt wird. */
	@Schema(description = "gibt an in welchem Abschnitten (1. HJ(1), 2. HJ(2) oder beide(0) die Ankreuzkompetenz benutzt wird", example = "1")
	public JsonNullable<Integer> abschnitt = JsonNullable.undefined();

	/** Gibt an, ob die Ankreuzkompetenz aktiv ist. */
	@Schema(description = "gibt an, ob die Ankreuzkompetenz aktiv ist", example = "true")
	public JsonNullable<Boolean> istAktiv = JsonNullable.undefined();

	/** Gibt an, ob die Ankreuzkompetenz in der Anwendung sichtbar sein soll oder nicht */
	@Schema(description = "gibt an, ob die Ankreuzkompetenz in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public JsonNullable<@NotNull Boolean> istSichtbar = JsonNullable.undefined();

	/** Gibt einen Wert für die Sortierung des Faches der Ankreuzkompetenz an. */
	@Schema(description = "gibt einen Wert für die Sortierung des Faches der Ankreuzkompetenz an", example = "32000")
	public JsonNullable<Integer> fachSortierung = JsonNullable.undefined();

	/** Gibt einen Wert für die Sortierung der Ankreuzkompetenz an. */
	@Schema(description = "gibt einen Wert für die Sortierung der Ankreuzkompetenz an", example = "32000")
	public JsonNullable<@NotNull Integer> sortierung = JsonNullable.undefined();

}
