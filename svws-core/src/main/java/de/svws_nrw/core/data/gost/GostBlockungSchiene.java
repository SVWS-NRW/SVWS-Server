package de.svws_nrw.core.data.gost;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse repräsentiert eine Schiene in einer Blockung der gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description="Informationen zu einer Schiene der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "bezeichnung", "wochenstunden" })
@TranspilerDTO
public class GostBlockungSchiene {
	
	/** Die ID der Schiene */
	public long id = -1;
	
	/** Die Nummer der Schiene bei der Blockung (zur Sortierung) */
	public int nummer = 1;

    /** Bezeichnung der Schiene (z.B. LK Schiene 1) */
	public @NotNull String bezeichnung = "Neue Schiene";

	/** Die Anzahl der Wochenstunden, welche der Schiene zugeordnet sind */
	public int wochenstunden = 3;

}
