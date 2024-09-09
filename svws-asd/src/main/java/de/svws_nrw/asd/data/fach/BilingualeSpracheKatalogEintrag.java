package de.svws_nrw.asd.data.fach;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Dieser DTO beinhaltet die Daten für den Katalog der möglichen Einträge bei den
 * bilingualen Sprachen
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Bilingualen Sprachfächer.")
@TranspilerDTO
public class BilingualeSpracheKatalogEintrag extends CoreTypeData {

	/** Die Kürzel der Schulformen, wo die Sprache als bilinguale Fremdsprache zulässig ist. */
	@Schema(description = "die Kürzel der Schulformen, wo die Sprache als bilinguale Fremdsprache zulässig ist.")
	public @NotNull List<String> schulformen = new ArrayList<>();

}
