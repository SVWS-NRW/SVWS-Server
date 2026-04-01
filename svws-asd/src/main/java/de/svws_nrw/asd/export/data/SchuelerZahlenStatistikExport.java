package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Summendaten der Schüler (K84)
 *
 */
@XmlRootElement
@Schema(description = "Die Summendaten der Schüler (K84)")
@TranspilerDTO
public class SchuelerZahlenStatistikExport {

	/** Die Schüler Insgesamt Zusammen. */
	@Schema(description = "die Schüler Insgesamt Zusammen.", example = "1031")
	public int insgesamtZusammen = 0;

	/** Die Schüler Insgesamt Weiblich. */
	@Schema(description = "die Schüler Insgesamt Weiblich.", example = "541")
	public int insgesamtWeiblich = 0;

	/** Die ausländischen Schüler Zusammen. */
	@Schema(description = "die ausländischen Schüler Zusammen.", example = "189")
	public int auslaenderZusammen = 0;

	/** Die ausländischen Schüler Weiblich. */
	@Schema(description = "die ausländischen Schüler Weiblich.", example = "114")
	public int auslaenderWeiblich = 0;

	/** Die Schüler mit Schwerstbehinderung Zusammen. */
	@Schema(description = "die Schüler mit Schwerstbehinderung Zusammen.", example = "14")
	public int schwerstbehinderteZusammen = 0;

	/** Die Schüler mit Schwerstbehinderung Zusammen Weiblich. */
	@Schema(description = "die Schüler mit Schwerstbehinderung Zusammen Weiblich.", example = "7")
	public int schwerstbehinderteWeiblich = 0;

	/** Die Vollbeleger Zusammen. */
	@Schema(description = "die Vollbeleger Zusammen.", example = "410")
	public int vollbelegerZusammen = 0;

	/** Die Vollbeleger Weiblich. */
	@Schema(description = "die Vollbeleger Zusammen Weiblich.", example = "208")
	public int vollbelegerWeiblich = 0;

	/** Die Teilbeleger Zusammen. */
	@Schema(description = "die Teilbeleger Zusammen.", example = "11")
	public int teilbelegerZusammen = 0;

	/** Die Teilbeleger Weiblich. */
	@Schema(description = "die Teilbeleger Zusammen Weiblich.", example = "7")
	public int teilbelegerWeiblich = 0;

	/** Die ausländischen Schüler von der Berufsschule Teilzeit Zusammen. */
	@Schema(description = "die ausländischen Schüler von der Berufsschule Teilzeit Zusammen.", example = "34")
	public int auslaenderBsTeilzeitZusammen = 0;

	/** Die ausländischen Schüler von der Berufsschule Teilzeit Weiblich. */
	@Schema(description = "die ausländischen Schüler von der Berufsschule Teilzeit Weiblich.", example = "17")
	public int auslaenderBsTeilzeitWeiblich = 0;

	/** Die beurlaubten Studenten Zusammen. */
	@Schema(description = "die beurlaubten Studenten Zusammen.", example = "13")
	public int studentenBeurlaubtZusammen = 0;

	/** Die beurlaubten Studenten Weiblich. */
	@Schema(description = "die beurlaubten Studenten Weiblich.", example = "8")
	public int studentenBeurlaubtWeiblich = 0;

	/** Die Schüler mit Förderschwerpunkt Zusammen. */
	@Schema(description = "die Schüler mit Förderschwerpunkt Zusammen.", example = "44")
	public int foerderschwerpunktZusammen = 0;

	/** Die Schüler mit Förderschwerpunkt Weiblich. */
	@Schema(description = "die Schüler mit Förderschwerpunkt Weiblich.", example = "23")
	public int foerderschwerpunktWeiblich = 0;

	/** Die ausländischen Schüler von der Berufsschule Vollzeit Zusammen. */
	@Schema(description = "die ausländischen Schüler von der Berufsschule Vollzeit Zusammen.", example = "15")
	public int auslaenderBsVollzeitZusammen = 0;

	/** Die ausländischen Schüler von der Berufsschule Vollzeit Weiblich. */
	@Schema(description = "die ausländischen Schüler von der Berufsschule Vollzeit Weiblich.", example = "9")
	public int auslaenderBsVollzeitWeiblich = 0;

	/** Die zur Zeit angemeldeten Schüler A12 Zusammen. */
	@Schema(description = "die zur Zeit angemeldeten Schüler A12 Zusammen.", example = "61")
	public int zurZeitAngemeldetA12Zusammen = 0;

	/** Die zur Zeit angemeldeten Schüler A12 Weiblich. */
	@Schema(description = "die zur Zeit angemeldeten Schüler A12 Weiblich.", example = "32")
	public int zurZeitAngemeldetA12Weiblich = 0;

	/** Die weiteren zu erwartenden Schüler A12 Zusammen. */
	@Schema(description = "die weiteren zu erwartenden Schüler A12 Zusammen.", example = "21")
	public int weitereErwarteteSchuelerA12Zusammen = 0;

	/** Die weiteren zu erwartenden Schüler A12 Weiblich. */
	@Schema(description = "die weiteren zu erwartenden Schüler A12 Weiblich.", example = "11")
	public int weitereErwarteteSchuelerA12Weiblich = 0;

	/** Die zur Zeit angemeldeten Schüler A13 Zusammen. */
	@Schema(description = "die zur Zeit angemeldeten Schüler A13 Zusammen.", example = "43")
	public int zurZeitAngemeldetA13Zusammen = 0;

	/** Die zur Zeit angemeldeten Schüler A13 Weiblich. */
	@Schema(description = "die zur Zeit angemeldeten Schüler A13 Weiblich.", example = "24")
	public int zurZeitAngemeldetA13Weiblich = 0;

	/** Die zu erwartenden Schüler A13 Zusammen. */
	@Schema(description = "die zu erwartenden Schüler A13 Zusammen.", example = "19")
	public int zuErwartendeSchuelerA13Zusammen = 0;

	/** Die zu erwartenden Schüler A13 Weiblich. */
	@Schema(description = "die zu erwartenden Schüler A13 Weiblich.", example = "11")
	public int zuErwartendeSchuelerA13Weiblich = 0;

	/** Die weiteren zu erwartenden Schüler A13 Zusammen. */
	@Schema(description = "die weiteren zu erwartenden Schüler A13 Zusammen.", example = "9")
	public int weitereErwarteteSchuelerA13Zusammen = 0;

	/** Die weiteren zu erwartenden Schüler A13 Weiblich. */
	@Schema(description = "die zur Zeit angemeldeten Schüler A13 Weiblich.", example = "5")
	public int weitereErwarteteSchuelerA13Weiblich = 0;

	/** Die Schüler mit dem Geschlecht 'Divers'. */
	@Schema(description = "Die Schüler mit dem Geschlecht 'Divers'.", example = "4")
	public int schuelerDivers = 0;

	/** Die Schüler mit der Geschlechtsangabe 'ohne Angabe'. */
	@Schema(description = "Die Schüler mit der Geschlechtsangabe 'ohne Angabe'.", example = "3")
	public int schuelerOhneAngabe = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuelerZahlenStatistikExport() {
		// leer
	}

}
