package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beinhaltet die Konfiguration für den Blockungs-Algorithmus von Nachschreibterminen.
 */
@XmlRootElement
@Schema(description = "Die Konfiguration für den Blockungs-Algorithmus von Nachschreibterminen der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostNachschreibterminblockungKonfiguration {

	/** Die maximale Zeit, welche für die Blockung verwendet wird  */
	public long maxTimeMillis = 1000L;

	/** Die Liste der Schülerklausuren. */
	@Schema(description = "die ID der Klausurraumstunde", example = "")
	public @NotNull List<@NotNull GostSchuelerklausurTermin> schuelerklausurtermine = new ArrayList<>();

	/** Die Liste der Schülerklausuren. */
	@Schema(description = "die ID der Klausurraumstunde", example = "")
	public @NotNull List<@NotNull GostKlausurtermin> termine = new ArrayList<>();

	/** True, falls NachschreiberInnen der selben Klausur auf den selben Termin geblockt werden sollen. */
	public boolean _regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen = false;

	/** True, falls NachschreiberInnen mit der selben Fachart (Fach + Kursart) auf den selben Termin geblockt werden sollen. */
	public boolean _regel_gleiche_fachart_auf_selbe_termine_verteilen = false;

}
