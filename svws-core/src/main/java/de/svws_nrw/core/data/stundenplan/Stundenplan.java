package de.svws_nrw.core.data.stundenplan;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu den Grunddaten eines Stundenplans.
 */
@XmlRootElement
@Schema(description = "Die Grunddaten des Stundenplans.")
@TranspilerDTO
public class Stundenplan {

	/** Die ID des Stundenplans. */
	@Schema(description = "die ID des Stundenplans", example = "815")
	public long id = -1;

	/** Die ID des Schuljahresabschnitts des Stundenplans. */
	@Schema(description = "die ID des Schuljahresabschnitts des Stundenplans", example = "7")
	public long idSchuljahresabschnitt = -1;

	/** Das Datum, ab dem der Stundenplan gültig ist. */
	@Schema(description = "das Datum, ab dem der Stundenpland gültig ist", example = "1.1.1899")
	public @NotNull String gueltigAb = "";

	/** Das Datum, bis wann der Stundenplan gültig ist. */
	@Schema(description = "das Datum, bis wann der Stundenplan gültig ist", example = "31.7.3218")
	public @NotNull String gueltigBis = "";

	/** Die textuelle Beschreibung des Stundenplans. */
	@Schema(description = "die textuelle Beschreibung des Stundenplans", example = "Stundenplan zum Schuljahresanfang")
	public @NotNull String bezeichnungStundenplan = "";

	/** Das Modell für die Wochen des Stundenplans, d.h. ob es sich um einen Stundenplan für jede Woche handelt (0) oder ob es sich um einen unterschiedliche Stundenpläne in Abhängigkeit des Wochentyps handelt - z.B. A-/B-Wochen (2) handelt. Hier wird dann die maximale Anzahl der unterschiedlichen Wochentypen festgelegt. */
	@Schema(description = "das Modell für die Wochen an, d.h. ob es sich um einen Stundenplan für jede Woche handelt (0) oder ob es sich um einen unterschiedliche Stundenpläne in Abhängigkeit des Wochentyps handelt - z.B. A-/B-Wochen (2) handelt. Hier wird dann die maximale Anzahl der unterschiedlichen Wochentypen festgelegt.", example = "1")
	public int wochenTypModell = 0;

	/** Das Zeitraster des Stundenplans. */
	@Schema(description = "das Zeitraster des Stundenplans")
	public @NotNull List<@NotNull StundenplanZeitraster> zeitraster = new ArrayList<>();

	/** Die Liste der Räume, die für den Stundenplan zur Verfügung stehen. */
	@Schema(description = "die Liste der Räume, die für den Stundenplan zur Verfügung stehen")
	public @NotNull List<@NotNull StundenplanRaum> raeume = new ArrayList<>();

	/** Die Liste der Schienen, die für den Stundenplan angelegt sind. */
	@Schema(description = "die Liste der Schienen, die für den Stundenplan angelegt sind")
	public @NotNull List<@NotNull StundenplanSchiene> schienen = new ArrayList<>();

	/** Die Liste der Pausenzeiten, bei welchen Aufsichten eingeteilt werden müssen. */
	@Schema(description = "die Liste der Pausenzeiten, bei welchen Aufsichten eingeteilt werden müssen")
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeiten = new ArrayList<>();

	/** Die Liste der Aufsichtsbereiche in Pausen, für welche Aufsichten eingeteilt werden müssen. */
	@Schema(description = "die Liste der Aufsichtsbereiche in Pausen, für welche Aufsichten eingeteilt werden müssen")
	public @NotNull List<@NotNull StundenplanAufsichtsbereich> aufsichtsbereiche = new ArrayList<>();

	/** Die Liste der Kalenderwochen-Zuordnungen, sofern unterschiedliche Wochentypen in einer Woche genutzt werden. */
	@Schema(description = "die Liste der Kalenderwochen-Zuordnungen, sofern unterschiedliche Wochentypen in einer Woche genutzt werden")
	public @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> kalenderwochenZuordnung = new ArrayList<>();

	/** Die Liste der Jahrgänge, die für den Stundenplan zur Verfügung stehen. */
	@Schema(description = "die Liste der Jahrgänge, die für den Stundenplan zur Verfügung stehen")
	public @NotNull List<@NotNull StundenplanJahrgang> jahrgaenge = new ArrayList<>();

}
