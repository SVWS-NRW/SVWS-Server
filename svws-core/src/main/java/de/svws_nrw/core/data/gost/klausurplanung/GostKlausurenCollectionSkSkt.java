package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Schülerklausuren mit den zugehörigen Schülerklausurterminen.
 */
@XmlRootElement
@Schema(description = "die Sammlung von GostSchülerklausuren und den zugehörigen Schülerklausurterminen.")
@TranspilerDTO
public class GostKlausurenCollectionSkSkt {

	/** Die ID der Schülerklausur. */
	@Schema(description = "die ID der Schülerklausur", example = "")
	public @NotNull List<@NotNull GostSchuelerklausur> schuelerklausuren = new ArrayList<>();

	/** Die ID der Schülerklausur. */
	@Schema(description = "die ID der Schülerklausur", example = "")
	public @NotNull List<@NotNull GostSchuelerklausurTermin> schuelerklausurtermine = new ArrayList<>();

}
