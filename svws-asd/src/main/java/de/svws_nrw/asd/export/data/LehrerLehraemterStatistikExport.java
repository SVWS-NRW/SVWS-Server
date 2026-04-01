package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Lehramtsdaten der Lehrer (L62).
 */
@XmlRootElement
@Schema(description = "Die Lehramts-Daten der Lehrer (L62)")
@TranspilerDTO
public class LehrerLehraemterStatistikExport {

	/** Satzschlüssel: Ein Lehramt eines Lehrers. */
	@Schema(description = "satzschlüssel: ein Lehramt eines Lehrers", example = "")
	public @NotNull String lehramt = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerLehraemterStatistikExport() {
		// leer
	}

}
