package de.nrw.schule.svws.core.data.enm;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert Struktur von JSON-Daten zu den Floskeln 
 * für das Externe-Noten-Modul ENM. 
 */
@XmlRootElement
@Schema(description="Spezifiziert Struktur von JSON-Daten zu den Floskeln für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMFloskel {

	/** Das Kürzel der Floskel. */
	@Schema(required = true, description = "Das Kürzel der Floskel.", example="#D001")
	public String kuerzel;

	/** Der Text der Floskel. */
	@Schema(required = true, description = "Der Text der Floskel.", example="Vorname hat gut gelernt.")
	public String text;
	
	/** Die ID des Faches, dem die Floskel zugeordnet ist, sofern die Floskel einem Fach 
	 * zugeordnet wurde, ansonsten null. */
	@Schema(required = false, description = "Die ID des Faches, dem die Floskel zugeordnet ist, sofern die Floskel einem Fach zugeordnet "
			+ "wurde, ansonsten null.", example="12")
	public Long fachID;

	/** Eine den Notenstufen ähnliche Kategorisierung */
	@Schema(required = false, description = "Eine den Notenstufen ähnliche Kategorisierung.", example="3")
	public Long niveau;

	/** Die ID des Jahrganges, dem die Floskel zugeordnet ist, falls die Floskel einem Fach 
	 * zugeordnet wurde, ansonsten null, falls sie für alle Jahrgänge gilt. */
	@Schema(required = false, description = "Die ID des Jahrganges, dem die Floskel zugeordnet ist, falls die Floskel einem Fach "
			+ "zugeordnet wurde, ansonsten null, falls sie für alle Jahrgänge gilt.", example="4")
	public Long jahrgangID;
	
}
