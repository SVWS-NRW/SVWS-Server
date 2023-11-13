package de.svws_nrw.core.data.lehrer;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Anrechnungsstunden für die Lehrerabschnittsdaten mit der angebenen ID.
 * Dabei kann es sich sowohl um allgemeine Anrechnungsstunden, als auch um Abrechnungsstunden bezüglich
 * Mehr- oder Minderleistungen, handeln.
 */
@XmlRootElement
@Schema(description = "Anrechnungsstunden bei Lehrerabschnittsdaten.")
@TranspilerDTO
public class LehrerPersonalabschnittsdatenAnrechnungsstunden {

	/** Die ID für den Eintrag von Anrechnungsstunden. */
	@Schema(description = "Die ID für den Eintrag von Anrechnungsstunden.", example = "4711")
	public long id = -1;

	/** Die ID der Lehrerabschnittsdaten. */
	@Schema(description = "Die ID der Lehrerabschnittsdaten.", example = "4712")
	public long idAbschnittsdaten = -1;

	/** Die ID des Anrechnungsgrundes. */
	@Schema(description = "Die ID des Anrechnungsgrundes.", example = "4713")
	public Long idGrund = null;

	/** Die Anzahl der Anrechnungsstunden, welche dem Grund zugeordnet sind. */
	@Schema(description = "Die Anzahl der Anrechnungsstunden, welche dem Grund zugeordnet sind.", example = "0.5")
	public double idAnerkennungsgrund = 0.0;

}
