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

	/**
	 * Leerer Standardkonstruktor.
	 */
	public RGBFarbe() {
		// leer
	}

	/**
	 * Erstellt eine neue Farbe mit den übergebenenen Komponenten
	 *
	 * @param red     die Rot-Komponente (0-225)
	 * @param green   die Grün-Komponente (0-225)
	 * @param blue    die Blau-Komponente (0-225)
	 */
	public RGBFarbe(final int red, final int green, final int blue) {
		super();
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	/**
	 * Gibt die Farbe als komma-separierten String zurück.
	 *
	 * @return der String
	 */
	@Override
	public String toString() {
		return red + "," + green + "," + blue;
	}

}
