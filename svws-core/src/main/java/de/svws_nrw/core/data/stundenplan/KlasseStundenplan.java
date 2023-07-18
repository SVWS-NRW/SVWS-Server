package de.svws_nrw.core.data.stundenplan;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu dem Stundenplan einer Klasse.
 */
@XmlRootElement
@Schema(description = "der Unterricht im Stundenplan einer Klasse.")
@TranspilerDTO
public class KlasseStundenplan {

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

	/** Das Datum, ab dem der Stundenpland gültig ist. */
	@Schema(description = "das Datum, ab dem der Stundenpland gültig ist", example = "1.1.1899")
	public @NotNull String gueltigAb = "";

	/** Das Datum, bis wann der Stundenplan gültig ist. */
	@Schema(description = "das Datum, bis wann der Stundenplan gültig ist", example = "31.7.3218")
	public @NotNull String gueltigBis = "";

	/** Die ID der Klasse. */
	@Schema(description = "die ID der Klasse", example = "4711")
	public @NotNull Long idKlasse = -1L;

	/** Die Kurzbezeichnung der Klasse */
	@Schema(description = "Die Kurzbezeichnung der Klasse", example = "QF")
	public @NotNull String klasseName = "";

	/** Der Unterricht der Klasse. */
	@Schema(description = "der Unterricht der Klasse")
	public @NotNull List<@NotNull KlasseStundenplanUnterricht> unterricht = new ArrayList<>();

}
