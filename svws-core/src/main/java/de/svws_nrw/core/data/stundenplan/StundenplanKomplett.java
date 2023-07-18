package de.svws_nrw.core.data.stundenplan;

import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die aggregierten Informationen für den gesamten Stundenplan kombiniert
 * mit allen Informationen zu Unterrichten, Pausenaufsichten und der
 * Unterrichtsverteilung.
 * Ggf. sind die Daten auf einen Kontext für einen Schüler, einen Lehrer oder eine Klasse
 * reduziert.
 */
@XmlRootElement
@Schema(description = "die Daten für den Stundenplan.")
@TranspilerDTO
public class StundenplanKomplett {

	/** Die Grunddaten des Stundenplans. */
	@Schema(description = "die Grunddaten des Stundenplans")
	public @NotNull Stundenplan daten = new Stundenplan();

	/** Die Unterrichtsdaten des Stundenplans. */
	@Schema(description = "die Unterrichtsdaten des Stundenplans")
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichte = new ArrayList<>();

	/** Die Informationen zu den Pausenaufsichten. */
	@Schema(description = "die Informationen zu den Pausenaufsichten")
	public @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichten = new ArrayList<>();

	/** Die Zusatzinformationen zu der Unterrichtsverteilung. */
	@Schema(description = "die Zusatzinformationen zu der Unterrichtsverteilung")
	public @NotNull StundenplanUnterrichtsverteilung unterrichtsverteilung = new StundenplanUnterrichtsverteilung();

}
