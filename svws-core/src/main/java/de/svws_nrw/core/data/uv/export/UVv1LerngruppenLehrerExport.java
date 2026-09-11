package de.svws_nrw.core.data.uv.export;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse beschreibt die Zuordnung einer Lehrkraft zu einer Lerngruppe.
 */
@Schema(description = "Diese Klasse beschreibt die Zuordnung einer Lehrkraft zu einer Lerngruppe.")
@TranspilerDTO
public class UVv1LerngruppenLehrerExport {

	/** Die UV-ID des Lehrers, der der Lerngruppe zugeordnet ist. */
	@Schema(description = "die UV-ID des Lehrers, der der Lerngruppe zugeordnet ist", example = "3001")
	public long lehrerUvId = -1;

	/** Die Reihenfolge der Zuordnung. */
	@Schema(description = "die Reihenfolge der Zuordnung", example = "1")
	public int reihenfolge = 1;

	/** Die Anzahl der Wochenstunden in dieser Lerngruppe. */
	@Schema(description = "die Anzahl der Wochenstunden in dieser Lerngruppe", example = "4.0")
	public double wochenstunden = 0.0;

	/** Die Anzahl der Wochenstunden, die auf das Deputat angerechnet werden. */
	@Schema(description = "die Anzahl der Wochenstunden, die auf das Deputat angerechnet werden", example = "2.0")
	public double wochenstundenAngerechnet = 0.0;

}
