package de.svws_nrw.asd.data.jahrgang;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieser DTO beinhaltet die Daten für den Katalog der möglichen Einträge bei den
 * Jahrgängen
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Jahrgänge")
@TranspilerDTO
public class JahrgaengeKatalogEintrag extends CoreTypeData {

	/** Die Kürzel der Schulformen, bei welchen der Jahrgang vorkommt. */
	@Schema(description = "die Kürzel der Schulformen, bei welchen der Jahrgang vorkommt")
	public @NotNull List<String> schulformen = new ArrayList<>();

}
