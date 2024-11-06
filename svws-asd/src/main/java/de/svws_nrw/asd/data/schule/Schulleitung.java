package de.svws_nrw.asd.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieses Core-DTO beinhaltet die Information zu einer Leitungsfunktion
 * der Schule, welche von einem Lehrer übernommen wird.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer Leitungsfunktion der Schule, welche von einem Lehrer übernommen wird.")
@TranspilerDTO
public class Schulleitung {

	/** Die ID des Eintrags für die Schulleitungsfunktion */
	@Schema(description = "die ID des Eintrags für die Schulleitungsfunktion", example = "4711")
	public long id = -1;

	/** Die ID der Leitungsfunktion des Lehrers. */
	@Schema(description = "die ID der Leitungsfunktion des Lehrers", example = "1")
	public long idLeitungsfunktion;

	/** Die Bezeichnung der Leitungsfunktion (max. 255 Zeichen) */
	@Schema(description = "die Bezeichnung der Leitungsfunktion (max. 255 Zeichen)", example = "Schulleiter")
	public @NotNull String bezeichnung = "";

	/** Die ID des Lehrers */
	@Schema(description = "die ID des Lehrers", example = "42")
	public long idLehrer = -1;

	/** Das Datum, mit welchem die Leitunsfunktion übernommen wurde */
	@Schema(description = "das Datum, mit welchem die Leitunsfunktion übernommen wurde", example = "2019-01-06")
	public String beginn = null;

	/** Das Datum, bis zu welchem die Leitunsfunktion übernommen wurde */
	@Schema(description = "das Datum, bis zu welchem die Leitunsfunktion übernommen wurde", example = "2020-02-18")
	public String ende = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Schulleitung() {
		// leer
	}

}
