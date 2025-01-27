package de.svws_nrw.asd.data.kaoa;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Objekte dieser Klasse enthalten die im Rahmen von KAoA
 * gültigen Zusatzmerkmalen.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der KAoA-Zusatzmerkmale.")
@TranspilerDTO
public class KAOAZusatzmerkmalKatalogEintrag extends CoreTypeData {

	/** Das Merkmal, welcher das Zusatzmerkmal zugeordnet ist. */
	@Schema(description = "das Merkmal, welcher das Zusatzmerkmal zugeordnet ist", example = "SBO 2.1")
	public @NotNull String merkmal = "";

	/** Die Optionsart des Zusatzmerkmals. */
	@Schema(description = "die Optionsart des Zusatzmerkmals", example = "Anschlussoption")
	public String optionsart = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KAOAZusatzmerkmalKatalogEintrag() {
		// leer
	}
}
