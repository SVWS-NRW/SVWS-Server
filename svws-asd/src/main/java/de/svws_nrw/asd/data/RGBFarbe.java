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
	 * Erstellt eine neue Farbe mit den übergebenen Komponenten
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
	 * Erstellt eine neue Farbe mit den übergebenen Komponenten
	 *
	 * @param decimal     Die Farbe in Dezimal
	 */
	public RGBFarbe(final int decimal) {
		super();
		this.red = (decimal >> 16) & 0xff;
		this.green = (decimal >> 8) & 0xff;
		this.blue = decimal & 0xff;
	}

	/**
	 * Gibt die Farbe als Dezimalwert zurück.
	 *
	 * @return		Farbwert in Dezimal
	 */
	public Integer asDecimal() {
		return (this.red << 16) | (this.green << 8) | this.blue;
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
