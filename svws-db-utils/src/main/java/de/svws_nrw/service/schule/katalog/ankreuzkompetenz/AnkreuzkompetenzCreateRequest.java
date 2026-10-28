package de.svws_nrw.service.schule.katalog.ankreuzkompetenz;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AnkreuzkompetenzCreateRequest {

	/** Gibt an, ob es sich bei der Ankreuzkompetenz um eine Floskel zum Arbeits- und Sozialverhalten handelt (true) oder nicht (false). */
	@Schema(description = "gibt an, ob es sich bei der Ankreuzkompetenz um eine Floskel zum Arbeits- und Sozialverhalten handelt (true) oder nicht (false)",
			example = "false")
	@NotNull
	public Boolean istASV;

	/** Die ID des Faches, die zur Ankreuzkompetenz gehört. Bei einer ASV-Ankreuzkompetenz darf kein Fach gesetzt sein, sonst ist es Pflicht. */
	@Schema(description = "die ID des Faches, die zur Ankreuzkompetenz gehört. Bei einer ASV-Ankreuzkompetenz darf kein Fach gesetzt sein, sonst ist es Pflicht",
			example = "null")
	public Long idFach;

	/** Der Text der Ankreuzkompetenz */
	@Schema(description = "der Text der Ankreuzkompetenz", example = "Testtext")
	@NotBlank
	@Size(max = 255)
	public String floskelText;

	/** Schulgliederung zu der die Ankreuzkompetenz gehört (nur wichtig bei BK) */
	@Schema(description = "schulgliederung zu der die Ankreuzkompetenz gehört (nur wichtig bei BK)", example = "A02")
	@Size(max = 3)
	public String schulgliederung;

	/** Gibt an in welchem Abschnitten (1. HJ, 2. HJ oder beide) die Ankreuzkompetenz benutzt wird. */
	@Schema(description = "gibt an in welchem Abschnitten (1. HJ(1), 2. HJ(2) oder beide(0) die Ankreuzkompetenz benutzt wird", example = "1")
	public Integer abschnitt;

	/** Gibt an, ob die Ankreuzkompetenz aktiv ist. */
	@Schema(description = "gibt an, ob die Ankreuzkompetenz aktiv ist", example = "true")
	public Boolean istAktiv;

	/** Gibt an, ob die Ankreuzkompetenz in der Anwendung sichtbar sein soll oder nicht */
	@Schema(description = "gibt an, ob die Ankreuzkompetenz in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public Boolean istSichtbar;

	/** Gibt einen Wert für die Sortierung des Faches der Ankreuzkompetenz an. */
	@Schema(description = "gibt einen Wert für die Sortierung des Faches der Ankreuzkompetenz an", example = "32000")
	public Integer fachSortierung;

	/** Gibt einen Wert für die Sortierung der Ankreuzkompetenz an. */
	@Schema(description = "gibt einen Wert für die Sortierung der Ankreuzkompetenz an", example = "32000")
	public Integer sortierung;

}
