package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Daten zu den Internatsplätzen der Schule (X97).
 */
@XmlRootElement
@Schema(description = "die Daten zu den Internatsplätzen der Schule (X97).")
@TranspilerDTO
public class InternatsplaetzeStatistikExport {

	/** Die Art des Internatsbetriebs. */
	@Schema(description = "die Art des Internatsbetriebs.", example = "1")
	public String internatsart = "";

	/** Die Internatsplätze für Jungen. */
	@Schema(description = "die Internatsplätze für Jungen.", example = "52")
	public int internatsplaetzeJungen = 0;

	/** Die belegten Internatsplätze für Jungen. */
	@Schema(description = "die belegten Internatsplätze für Jungen.", example = "49")
	public int internatsplaetzeJungenBelegt = 0;

	/** Die Internatsplätze für Mädchen. */
	@Schema(description = "die Internatsplätze für Mädchen.", example = "55")
	public int internatsplaetzeMaedchen = 0;

	/** Die belegten Internatsplätze für Mädchen. */
	@Schema(description = "die belegten Internatsplätze für Mädchen.", example = "43")
	public int internatsplaetzeMaedchenBelegt = 0;

	/** Die Internatsplätze geschlechtsneutral. */
	@Schema(description = "die Internatsplätze geschlechtsneutral.", example = "10")
	public int internatsplaetzeGeschlechtsneutral = 0;

	/** Die belegten Internatsplätze geschlechtsneutral. */
	@Schema(description = "die belegten Internatsplätze geschlechtsneutral.", example = "7")
	public int internatsplaetzeGeschlechtsneutralBelegt = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public InternatsplaetzeStatistikExport() {
	}

}
