package de.svws_nrw.core.data;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse ist ein Core-DTO für die Farbdefintion nach dem RGB-Schema.
 */
@XmlRootElement
@Schema(description="Eine RGB-Farbdefinition.")
@TranspilerDTO
public class RGBFarbe {
	
    /** Der Rot-Anteil der Farbe (0-255) */
    @Schema(required = true, description = "der Rot-Anteil der Farbe (0-255)", example="220")
    public int red = 220;

    /** Der Grün-Anteil der Farbe (0-255) */
    @Schema(required = true, description = "der Grün-Anteil der Farbe (0-255)", example="220")
    public int green = 220;
    
    /** Der Blau-Anteil der Farbe (0-255) */
    @Schema(required = true, description = "der Blau-Anteil der Farbe (0-255)", example="220")
    public int blue = 220;


    /**
     * Erstellt einen RGB-Farbwert mit dem Standardwerten (220,220,220)
     */
    public RGBFarbe() {
    }
    

	/** 
	 * Erstellt einen RGB-Farbwert aus dem übergebenen 24-Bit-Farbwert. 
	 * 
	 * @param color24Bit   RGB-Farbwerte
	 */ 
	public RGBFarbe(final int color24Bit) {
		red = color24Bit & 0xFF;
		green = (color24Bit >> 8) & 0xFF;
		blue = color24Bit >> 16;
	}


	/**
	 * Erstellt einen RGB-Farbwert mit den angegebenen RGB-Werten.
	 * 
	 * @param red     der Rot-Anteil (0-255)
	 * @param green   der Grün-Anteil (0-255)
	 * @param blue    der Blau-Anteil (0-255)
	 */
	public RGBFarbe(final int red, final int green, final int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

}
