package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird für den Export der Anzahl vom Schulbesuch zurückgestellter Kinder verwendet (Satzart S43)
 *
 */
@XmlRootElement
@Schema(description = "Diese Klasse wird für den Export der Anzahl vom Schulbesuch zurückgestellter Kinder verwendet (Satzart S43)")
@TranspilerDTO
public class VomSchulbesuchZurueckgestelltStatistikExport {

	/** Die Anzahl der Zurückgestellten Kinder insgesamt. */
	@Schema(description = "die Anzahl der Zurückgestellten Kinder insgesamt", example = "0")
	public long zurueckgestelltInsgesamtZusammen = 0;

	/** Die Anzahl der Zurückgestellten Kinder insgesamt Weiblich. */
	@Schema(description = "die Anzahl der Zurückgestellten Kinder insgesamt Weiblich", example = "0")
	public long zurueckgestelltInsgesamtWeiblich = 0;

	/** Die Anzahl der Zurückgestellten ausländischen Kinder zusammen. */
	@Schema(description = "die Anzahl der Zurückgestellten ausländischen Kinder zusammen", example = "0")
	public long zurueckgestelltAuslaenderZusammen = 0;

	/** Die Anzahl der Zurückgestellten ausländischen Kinder Weiblich. */
	@Schema(description = "die Anzahl der Zurückgestellten ausländischen Kinder Weiblich", example = "0")
	public long zurueckgestelltAuslaenderWeiblich = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public VomSchulbesuchZurueckgestelltStatistikExport() {
	}

}
