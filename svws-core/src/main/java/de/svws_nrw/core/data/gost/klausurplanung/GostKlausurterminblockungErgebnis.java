package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieses Core-DTO enthält das Ergebnis einer Klausurtermin-Blockung
 */
@XmlRootElement
@Schema(description = "Das Ergebnis einer Klausurtermin-Blockung der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostKlausurterminblockungErgebnis {

	/** Ein Array mit den GostKlausurterminblockungErgebnisTerminen. */
	@ArraySchema(schema = @Schema(implementation = GostKlausurterminblockungErgebnisTermin.class, description = "Ein Array mit den GostKlausurterminblockungErgebnisTerminen."))
	public @NotNull List<GostKlausurterminblockungErgebnisTermin> termine = new ArrayList<>();

	/**
	 * Default-Konstruktor
	 */
	public GostKlausurterminblockungErgebnis() {
		super();
	}

}
