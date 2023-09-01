package de.svws_nrw.core.data.gost;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dies ist ein Core-Type für die Information zu allen Fachwahlen eines Abiturjahrgangs
 * in der gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description = "Informationen zu den Fachwahlen eines Abiturjahrgangs in der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostJahrgangFachwahlen {

	/** Die Fachwahlen der einzelnen Halbjahre der gymnasialen Oberstufe  */
	public @NotNull GostJahrgangFachwahlenHalbjahr[] halbjahr = new GostJahrgangFachwahlenHalbjahr[GostHalbjahr.maxHalbjahre];

	/** Die Fachwahlen für den Abiturbereich */
	public @NotNull GostJahrgangFachwahlenHalbjahr abitur = new GostJahrgangFachwahlenHalbjahr();

}
