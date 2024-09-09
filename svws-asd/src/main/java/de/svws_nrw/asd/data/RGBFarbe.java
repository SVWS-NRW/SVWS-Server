package de.svws_nrw.asd.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse ist ein Core-DTO für die Farbdefinition nach dem RGB-Schema.
 */
@XmlRootElement
@Schema(description = "Eine RGB-Farbdefinition.")
@TranspilerDTO
public class RGBFarbe {

	/** Der Rot-Anteil der Farbe (0-255) */
	@Schema(description = "der Rot-Anteil der Farbe (0-255)", example = "220")
	public int red = 220;

	/** Der Grün-Anteil der Farbe (0-255) */
	@Schema(description = "der Grün-Anteil der Farbe (0-255)", example = "220")
	public int green = 220;

	/** Der Blau-Anteil der Farbe (0-255) */
	@Schema(description = "der Blau-Anteil der Farbe (0-255)", example = "220")
	public int blue = 220;

}
