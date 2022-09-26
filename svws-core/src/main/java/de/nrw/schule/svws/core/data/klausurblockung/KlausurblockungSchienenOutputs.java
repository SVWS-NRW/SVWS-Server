package de.nrw.schule.svws.core.data.klausurblockung;

import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Spezifiziert die grundlegende Struktur von JSON-Daten für die Rückgabe mehrerer Ausgaben bezüglich der Eingabe
 * {@link KlausurblockungSchienenInput}. */
@XmlRootElement(name = "KlausurblockungSchienenInput")
@Schema(name = "KlausurblockungSchienenInput", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für die Rückgabe mehrerer Ausgaben bezüglich der Eingabe KlausurblockungSchienenInput.")
@TranspilerDTO
public class KlausurblockungSchienenOutputs {

	/** Rückgabe mehrerer {@link KlausurblockungSchienenOutput}. */
	@ArraySchema(schema = @Schema(implementation = KlausurblockungSchienenOutput.class))
	public @NotNull Vector<@NotNull KlausurblockungSchienenOutput> ergebnisse = new Vector<>();

}
