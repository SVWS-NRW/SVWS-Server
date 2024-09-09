package de.svws_nrw.asd.data.kaoa;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Objekte dieser Klasse enthalten die im Rahmen von KAoA
 * gültigen Einträge für die SBO Ebene 4.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Einträge für die SBO Ebene 4.")
@TranspilerDTO
public class KAOAEbene4KatalogEintrag extends CoreTypeData {

	/** Das Zusatzmerkmal, welcher der Eintrag zugeordnet ist. */
	@Schema(description = "das Zusatzmerkmal, welcher der Eintrag zugeordnet ist", example = "SBO 6.5.1")
	public @NotNull String zusatzmerkmal = "";

}
