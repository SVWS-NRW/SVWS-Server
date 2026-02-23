package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Abiturprüfungsergebnisse (X93).
 *
 */
@XmlRootElement
@Schema(description = "Die Abiturprüfungsergebnisse (X93)")
@TranspilerDTO
public class AbiturpruefungsergebnisseStatistikExport {

	/** Satzschlüssel: Die laufende Nummer des Schülers. */
	@Schema(description = "satzschlüssel: Die laufende Nummer des Schülers", example = "00123")
	public String laufendeNummer = "";

	/** Der Jahrgang (leer). */
	@Schema(description = "der Jahrgang (leer)", example = "leer")
	public String jahrgang = "";

	/** Das Bildungsgangkennzeichen. */
	@Schema(description = "das Bildungsgangkennzeichen", example = "A")
	public String bildungsgangkennzeichen = "";

	/** Die Schulgliederung. */
	@Schema(description = "die Schulgliederung", example = "G9")
	public String schulgliederung = "";

	/** Die Fachklasse (leer). */
	@Schema(description = "die Fachklasse (leer)", example = "leer")
	public String fachklasse = "";

	/** Die Abgangsart (leer). */
	@Schema(description = "die Abgangsart (leer)", example = "leer")
	public String abgangsart = "";

	/** Das Geburtsjahr des Abiturienten. */
	@Schema(description = "das Geburtsjahr des Abiturienten", example = "2007")
	public String geburtsjahr = "";

	/** Das Geschlecht des Abiturienten. */
	@Schema(description = "das Geschlecht des Abiturienten", example = "3")
	public String geschlecht = "";

	/** Das erste Abiturfach. */
	@Schema(description = "das erste Abiturfach", example = "M")
	public String abiturfach1 = "";

	/** Das erste Abiturfach Aufgabenfeld. */
	@Schema(description = "das erste Abiturfach Aufgabenfeld", example = "1")
	public String abiturfach1Aufgabenfeld = "";

	/** Das erste Abiturfach Fachtyp. */
	@Schema(description = "das erste Abiturfach Fachtyp", example = "1")
	public String abiturfach1Fachtyp = "";

	/** Das zweite Abiturfach. */
	@Schema(description = "das zweite Abiturfach", example = "D")
	public String abiturfach2 = "";

	/** Das zweite Abiturfach Aufgabenfeld. */
	@Schema(description = "das zweite Abiturfach Aufgabenfeld", example = "2")
	public String abiturfach2Aufgabenfeld = "";

	/** Das zweite Abiturfach Fachtyp. */
	@Schema(description = "das zweite Abiturfach Fachtyp", example = "3")
	public String abiturfach2Fachtyp = "";

	/** Das dritte Abiturfach. */
	@Schema(description = "das dritte Abiturfach", example = "GE")
	public String abiturfach3 = "";

	/** Das dritte Abiturfach Aufgabenfeld. */
	@Schema(description = "das dritte Abiturfach Aufgabenfeld", example = "3")
	public String abiturfach3Aufgabenfeld = "";

	/** Das dritte Abiturfach Fachtyp. */
	@Schema(description = "das dritte Abiturfach Fachtyp", example = "4")
	public String abiturfach3Fachtyp = "";

	/** Das vierte Abiturfach. */
	@Schema(description = "das vierte Abiturfach", example = "NL")
	public String abiturfach4 = "";

	/** Das vierte Abiturfach Aufgabenfeld. */
	@Schema(description = "das vierte Abiturfach Aufgabenfeld", example = "2")
	public String abiturfach4Aufgabenfeld = "";

	/** Das vierte Abiturfach Fachtyp. */
	@Schema(description = "das vierte Abiturfach Fachtyp", example = "3")
	public String abiturfach4Fachtyp = "";

	/** Die Abiturnote. */
	@Schema(description = "die Abiturnote", example = "3.8")
	public String abiturnote = "";

	/** Die Nationalität. */
	@Schema(description = "die Nationalität", example = "141")
	public String nationalitaet = "";

	/** Der Abiturstatus (bestanden/nicht bestanden). */
	@Schema(description = "der Abiturstatus (bestanden/nicht bestanden)", example = "1")
	public String abiturstatus = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public AbiturpruefungsergebnisseStatistikExport() {
	}

}
