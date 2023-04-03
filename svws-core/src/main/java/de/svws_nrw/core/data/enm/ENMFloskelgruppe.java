package de.svws_nrw.core.data.enm;


import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu den Floskelgruppen
 * für das Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Struktur von JSON-Daten zu den Floskelgruppen für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMFloskelgruppe {

	/** Das Kürzel der Floskelgruppe, z. B. AL1, AL2 oder ASV. */
	@Schema(description = "Das Kürzel der Floskelgruppe.", example = "AL1")
	public String kuerzel;

	/** Die textuelle Bezeichnung der Floskelgruppe, z. B. Allgemeine Floskeln oder Floskeln zum Arbeits- und Sozialverhalten. */
	@Schema(description = "Die textuelle Bezeichnung der Floskelgruppe, z. B. Allgemeine Floskeln "
			+ "oder Floskeln zum Arbeits- und Sozialverhalten.", example = "Allgemeine Floskeln")
	public String bezeichnung;

	/** Die Hauptgruppe für Floskeln. Diese kann bei mehreren Floskelgruppen auftreten und fasst diese ggf. nochmals zusammen (z.B. ALLG) */
	@Schema(description = "Die Hauptgruppe für Floskeln. Diese kann bei mehreren Floskelgruppen auftreten "
			+ "und fasst diese ggf. nochmals zusammen.", example = "ALLG")
	public String hauptgruppe;

	/** Die Liste der Floskeln, die dieser Floskelgruppe zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = ENMFloskel.class, description = "Ein Array mit den Informationen "
			+ "der Floskeln, die dieser Floskelgruppe zugeordnet sind."))
	public final @NotNull Vector<@NotNull ENMFloskel> floskeln = new Vector<>();

}
