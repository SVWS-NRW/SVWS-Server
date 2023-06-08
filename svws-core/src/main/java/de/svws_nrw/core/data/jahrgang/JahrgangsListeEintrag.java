package de.svws_nrw.core.data.jahrgang;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für die Jahrgänge übergeben werden.
 */
@XmlRootElement
@Schema(description = "ein Eintrag eines Jahrgangs in der Jahrgangsliste.")
@TranspilerDTO
public class JahrgangsListeEintrag {

	/** Die ID des Jahrgangs. */
	@Schema(description = "die ID des Jahrgangs", example = "4711")
	public long id;

	/** Das Kürzel des Jahrgangs. */
	@Schema(description = "das Kürzel des Jahrgangs", example = "EF")
	public String kuerzel;

	/** Das dem Jahrgang zugeordnete Statistik-Kürzel. */
	@Schema(description = "das dem Jahrgang zugeordnete Statistik-Kürzel", example = "EF")
	public @NotNull String kuerzelStatistik = "";

	/** Der Name / die Bezeichnung des Jahrgangs. */
	@Schema(description = "der Name / die Bezeichnung des Jahrgangs", example = "Einführungsphase")
	public String bezeichnung;

	/** Die Sortierreihenfolge des Jahrgangslisten-Eintrags. */
	@Schema(description = "die Sortierreihenfolge des Jahrgangslisten-Eintrags", example = "1")
	public int sortierung;

	/** Die ID der Schulgliederung, der der Eintrag zugeordnet ist. */
	@Schema(description = "die ID der Schulgliederung, der der Eintrag zugeordnet ist", example = "***")
	public String kuerzelSchulgliederung;

	/** Die ID des Folgejahrgangs, sofern einer definiert ist, ansonsten null */
	@Schema(description = "die ID des Folgejahrgangs, sofern einer definiert ist", example = "4712")
	public Long idFolgejahrgang;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

}
