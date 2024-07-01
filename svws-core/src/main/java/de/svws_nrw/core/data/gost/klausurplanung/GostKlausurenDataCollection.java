package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert eine Sammlung von Gost-Klausurvorgaben, Kursklausuren und Schülerklausuren.
 */
@XmlRootElement
@Schema(description = "die Sammlung von Gost-Klausurvorgaben, Kursklausuren und Schülerklausuren")
@TranspilerDTO
public class GostKlausurenDataCollection {

	/** Die Liste der Klausurvorgaben. */
	@Schema(description = "die ID der Schülerklausur", example = "")
	public @NotNull List<GostKlausurvorgabe> vorgaben = new ArrayList<>();

	/** Die Liste der Kursklausuren. */
	@Schema(description = "die ID der Schülerklausur", example = "")
	public @NotNull List<GostKursklausur> kursklausuren = new ArrayList<>();

	/** Die Liste der Schülerklausuren. */
	@Schema(description = "die ID der Klausurraumstunde", example = "")
	public @NotNull List<GostSchuelerklausur> schuelerklausuren = new ArrayList<>();

	/** Die Liste der Schülerklausuren. */
	@Schema(description = "die ID der Klausurraumstunde", example = "")
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurtermine = new ArrayList<>();

	/** Die Liste der Schülerklausuren. */
	@Schema(description = "die ID der Klausurraumstunde", example = "")
	public @NotNull List<GostKlausurtermin> termine = new ArrayList<>();

}
