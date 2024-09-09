package de.svws_nrw.asd.data.schule;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse stellt die Daten für den Katalog der Organisationsformen
 * bei allgemeinbildenden Schulen, bei berufsbildenden Schule und beim
 * Weiterbildungskolleg.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Anlagen beim Berufskolleg.")
@TranspilerDTO
public class OrganisationsformKatalogEintrag extends CoreTypeData {

	/** Die Kürzel der Schulformen, bei welchen die Organisationsform vorkommt. */
	@Schema(description = "die Kürzel der Schulformen, bei welchen die Organisationsform vorkommt")
	public @NotNull List<String> schulformen = new ArrayList<>();

}
