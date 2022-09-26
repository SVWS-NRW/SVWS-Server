package de.nrw.schule.svws.core.data.schule;

import java.util.List;
import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert eine Kombination aus einer Schulform, ggf. einer Schulgliederung
 * und mehreren Jahrgängen.  
 */
@XmlRootElement
@Schema(description="eine Kombination aus einer Schulform, ggf. einer Schulgliederung und mehreren Jahrgängen.")
@TranspilerDTO
public class SchulformGliederungJahrgaenge {

	/** Das Kürzel der Schulform */
	@Schema(required = true, description = "das Kürzel der Schulform", example="BK")
	public @NotNull String schulform = "";
	
	/** Das Kürzel der Schulgliederung bzw. des Bildungsganges. Null, falls alle Gliederungen der Schulform gemeint sind. */
	@Schema(required = true, description = "das Kürzel der Schulgliederung bzw. des Bildungsganges", example="A01")
	public String gliederung = null;
	
	/** Die Liste der Jahrgänge. */
	@Schema(required = true, description = "die Liste der Jahrgänge")
	public @NotNull List<@NotNull String> jahrgaenge = new Vector<>();	

}
