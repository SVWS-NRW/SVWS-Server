package de.svws_nrw.asd.data.schule;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog der Förderschwerpunkte.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Förderschwerpunkte.")
@TranspilerDTO
public class FoerderschwerpunktKatalogEintrag extends CoreTypeData {

	/** Die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt. */
	@Schema(description = "die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt")
	public @NotNull List<String> schulformen = new ArrayList<>();

}
