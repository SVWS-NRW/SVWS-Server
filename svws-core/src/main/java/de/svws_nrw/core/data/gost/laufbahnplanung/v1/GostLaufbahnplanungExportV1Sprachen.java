package de.svws_nrw.core.data.gost.laufbahnplanung.v1;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import jakarta.validation.constraints.NotNull;

/**
 * Datenaustauschformat für die Laufbahnplanung der gymnasialen Oberstufe:
 *
 * - DTO für die Sprachbelegungen und die Sprachprüfungen eines Schülers.
 */
@TranspilerDTO
public class GostLaufbahnplanungExportV1Sprachen {

	/** Die ID des Schülers. */
	public long schuelerID;

	/** Die Liste der Sprachbelegungen. */
	public @NotNull List<GostLaufbahnplanungExportV1Sprachbelegung> belegungen = new ArrayList<>();

	/** Die Liste der Sprachprüfungen. */
	public @NotNull List<GostLaufbahnplanungExportV1Sprachpruefung> pruefungen = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostLaufbahnplanungExportV1Sprachen() {
		// leer
	}

}
