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
public class GostKlausurraumblockungKonfiguration {

	/** Die maximale Zeit, welche für die Blockung verwendet wird  */
	public long maxTimeMillis = 1000L;

	/** Die Liste der angereicherten Schülerklausurtermine. */
	@Schema(description = "die Liste der zu blockenden Schülerklausurtermine", example = "")
	public @NotNull List<@NotNull GostSchuelerklausurTerminRich> schuelerklausurtermine = new ArrayList<>();

	/** Die Liste der angereicherten Klausurräume. */
	@Schema(description = "die Liste der GostKlausurräume, in die geblockt werden soll", example = "")
	public @NotNull List<@NotNull GostKlausurraumRich> raeume = new ArrayList<>();

	/** Gewicht, um die Schülerklausuren auf möglichst wenig Räume zu verteilen. */
	public double _regel_blocke_in_moeglichst_wenig_raeume = 1;

	/** Gewicht, um alle Klausuren desselben Kurses in denselben Raum zu blocken. */
	public double _regel_selbe_kursklausur_in_selben_raum = 1;

	/** Gewicht, um in einem Klausurraum möglichst alle Klausuren mit ähnlichen Klausurdauern zu blocken. */
	public double _regel_aehnliche_klausurdauer_pro_raum = 0;

}
