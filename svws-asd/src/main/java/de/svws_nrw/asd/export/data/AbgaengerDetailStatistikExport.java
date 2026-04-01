package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Details der abgehenden Schüler (V54).
 */
@XmlRootElement
@Schema(description = "Die Abschlüsse der abgehenden Schüler (V54)")
@TranspilerDTO
public class AbgaengerDetailStatistikExport {

	/** Satzschlüssel: Die Abgangsart bzw. die Abschlüsse. */
	@Schema(description = "satzschlüssel: die Abgangsart bzw. die Abschlüsse", example = "F")
	public String abgangsart = "";

	/** Satzschlüssel: Das Geburtsjahr der abgehenden Schüler. */
	@Schema(description = "satzschlüssel: das Geburtsjahr der abgehenden Schüler", example = "2002")
	public String geburtsjahr = "";

	/** Satzschlüssel: Die Staatsangehörigkeit der abgehenden Schüler. */
	@Schema(description = "satzschlüssel: die Staatsangehörigkeit der abgehenden Schüler", example = "134")
	public String staatsangehoerigkeit = "";

	/** Die abgehenden Schüler zu dieser Abgangsart Zusammen. */
	@Schema(description = "die abgehenden Schüler zu dieser Abgangsart Zusammen", example = "8")
	public long abschluesseInsgesamtZusammen = 0;

	/** Die abgehenden Schüler zu dieser Abgangsart Zusammen Weiblich. */
	@Schema(description = "die abgehenden Schüler zu dieser Abgangsart Zusammen Weiblich", example = "4")
	public long abschluesseInsgesamtWeiblich = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public AbgaengerDetailStatistikExport() {
		// leer
	}

}
