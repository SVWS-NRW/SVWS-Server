package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

	/** Ein Array mit den zu blockenden Schülerklausurterminen. */
	@ArraySchema(schema = @Schema(implementation = GostSchuelerklausurTermin.class, description = "Ein Array mit den zu blockenden Schülerklausurterminen."))
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurtermine = new ArrayList<>();

	/** Ein Array mit den GostKlausurterminen, in die geblockt werden soll. */
	@ArraySchema(schema = @Schema(implementation = GostKlausurtermin.class, description = "Ein Array mit den GostKlausurterminen, in die geblockt werden soll."))
	public @NotNull List<GostKlausurtermin> termine = new ArrayList<>();

	/** True, falls NachschreiberInnen der selben Klausur auf den selben Termin geblockt werden sollen. */
	public boolean _regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen = false;

	/** True, falls NachschreiberInnen mit der selben Fachart (Fach + Kursart) auf den selben Termin geblockt werden sollen. */
	public boolean _regel_gleiche_fachart_auf_selbe_termine_verteilen = false;

	/**
	 * Default-Konstruktor
	 */
	public GostNachschreibterminblockungKonfiguration() {
		super();
	}

}
