package de.svws_nrw.transpiler.test;

import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse spezifiziert den Core-DTO für einen Katalog-Eintrag im
 * Katalog der Schulstufen.
 */
@XmlRootElement
@Schema(description = "ein Eintrag im Katalog der Schulstufen.")
@TranspilerDTO
public class SchulstufeKatalogEintrag extends CoreTypeData {

	/** Die Kürzel der Schulformen die bei der Schulstufe vorkommen. */
	@Schema(description = "die Kürzel der Schulformen die bei der Schulstufe vorkommen")
	public @NotNull List<@NotNull String> schulformen = new ArrayList<>();

}
