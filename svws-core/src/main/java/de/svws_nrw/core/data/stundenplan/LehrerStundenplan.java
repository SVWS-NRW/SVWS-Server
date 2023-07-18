package de.svws_nrw.core.data.stundenplan;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle
 * verwendet. Sie liefert die Informationen zu dem Stundenplan eines Lehrers.
 */
@XmlRootElement
@Schema(description = "der Stundenplan eines Lehrers.")
@TranspilerDTO
public class LehrerStundenplan {

	/** Die ID des Stundenplans. */
	@Schema(description = "die ID des Stundenplans", example = "815")
	public long idStundenplan = -1;

	/** Die textuelle Beschreibung des Stundenplans. */
	@Schema(description = "die textuelle Beschreibung des Stundenplans", example = "Stundenplan zum Schuljahresanfang")
	public @NotNull String bezeichnungStundenplan = "";

	/** Die ID des Schuljahresabschnitts des Stundenplans. */
	@Schema(description = "die ID des Schuljahresabschnitts des Stundenplans", example = "7")
	public long idSchuljahresabschnitt = -1;

	/** Das Zeitraster des Stundenplans. */
	@Schema(description = "das Zeitraster des Stundenplans")
	public @NotNull List<@NotNull StundenplanZeitraster> zeitraster = new ArrayList<>();

	/** Die Pausenzeiten des Stundenplans */
	@Schema(description = "Die Pausenzeiten des Stundenplans")
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeiten = new ArrayList<>();

	/** Das Datum, ab dem der Stundenpland gültig ist. */
	@Schema(description = "das Datum, ab dem der Stundenpland gültig ist", example = "1.1.1899")
	public @NotNull String gueltigAb = "";

	/** Das Datum, bis wann der Stundenplan gültig ist. */
	@Schema(description = "das Datum, bis wann der Stundenplan gültig ist", example = "31.7.3218")
	public @NotNull String gueltigBis = "";

	/** Die ID des Lehrers. */
	@Schema(description = "die ID des Lehrers", example = "4711")
	public long idLehrer = -1;

	/** Der Nachname des Lehrers. */
	@Schema(description = "der Nachname des Lehrers", example = "Mustermann")
	public @NotNull String nachname = "";

	/** Der Vorname des Lehrers. */
	@Schema(description = "der Vorname des Lehrers", example = "Max")
	public @NotNull String vorname = "";

	/** Der Unterricht des Lehrers. */
	@Schema(description = "der Unterricht des Lehrers")
	public @NotNull List<@NotNull LehrerStundenplanUnterricht> unterricht = new ArrayList<>();

	/** Die Pausenaufsichten des Lehrers. */
	@Schema(description = "Die Pausenaufsichten des Lehrers")
	public @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichten = new ArrayList<>();

}
