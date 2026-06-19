package de.svws_nrw.core.data.gost.laufbahnplanung.v2;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Datenaustauschformat für die Laufbahnplanung der gymnasialen Oberstufe:
 *
 * - Die Informationen den Wahlen der Gleichwertigen komplexen Lernleistungen zu einem Schüler
 */
@XmlRootElement
@Schema(description = "Enthält die Informationen zu den GKL-Wahlen eines Schülers.")
@TranspilerDTO
public class GostLaufbahnplanungExportV2GKL {

	/** Die ID der Definition der Gleichwertigen komplexen Lernleistung (GKL). */
	@Schema(description = "Die ID der Definition der Gleichwertigen komplexen Lernleistung (GKL).")
	public long id = -1;

	/** Gibt an, in welchem Fach die GKL gewählte wurde. */
	@Schema(description = "Gibt an, in welchem Fach die GKL gewählte wurde.")
	public long idFach = -1;

	/** Gibt das Halbjahr der gymnasialen Oberstufe an, wo die GKL stattfinden (0-4) - Q2.2 nicht möglich */
	@Schema(description = "Gibt das Halbjahr der gymnasialen Oberstufe an, wo die GKL stattfinden (0-4) - Q2.2 nicht möglich")
	public int idHalbjahr = -1;

	/** Gibt das Quartal an, in welchem die GKL in dem Halbjahr stattfinden soll. */
	@Schema(description = "Gibt das Quartal an, in welchem die GKL in dem Halbjahr stattfinden soll.")
	public int quartal = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostLaufbahnplanungExportV2GKL() {
		// leer
	}

}
