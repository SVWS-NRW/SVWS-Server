package de.svws_nrw.asd.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Die Klasse liefert die allgemeinen textuellen Bemerkungen zu dem Lernabschnitt eines Schülers zurück.
 * Siehe auch {@link SchuelerLernabschnittsdaten}.
 */
@XmlRootElement
@Schema(description = "Die allgemeinen textuellen Bemerkungen zu dem Lernabschnitt eines Schülers.")
@TranspilerDTO
public class SchuelerLernabschnittBemerkungen {

	/** Der Text für allgemeine Zeugnisbemerkungen. */
	@Schema(description = "der Text für allgemeine Zeugnisbemerkungen", example = "")
	public String zeugnisAllgemein = "";

	/** Der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten. */
	@Schema(description = "der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten", example = "")
	public String zeugnisASV = "";

	/** Der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen. */
	@Schema(description = "der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen", example = "")
	public String zeugnisLELS = "";

	/** Der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement. */
	@Schema(description = "der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement", example = "")
	public String zeugnisAUE = "";

	/** Der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I. */
	@Schema(description = "der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I", example = "")
	public String uebergangESF = "";

	/** Eine Bemerkung zum Förderschwerpunkt. */
	@Schema(description = "eine Bemerkung zum Förderschwerpunkt", example = "")
	public String foerderschwerpunkt = "";

	/** Eine Bemerkung zur Versetzungsentscheidung. */
	@Schema(description = "eine Bemerkung zur Versetzungsentscheidung", example = "")
	public String versetzungsentscheidung = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuelerLernabschnittBemerkungen() {
		// leer
	}

}
