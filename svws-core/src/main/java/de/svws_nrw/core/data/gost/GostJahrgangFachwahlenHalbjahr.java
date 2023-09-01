package de.svws_nrw.core.data.gost;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dies ist ein Core-Type für die Information zu allen Fachwahlen eines Abiturjahrgangs
 * in der gymnasialen Oberstufe in Bezug auf ein Halbjahr der gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description = "Informationen zu den Fachwahlen eines Abiturjahrgangs in der gymnasialen Oberstufe bezüglich eines Halbjahres.")
@TranspilerDTO
public class GostJahrgangFachwahlenHalbjahr {

	/** Die Fachwahlen für den Abiturbereich */
	public @NotNull List<@NotNull GostFachwahl> fachwahlen = new ArrayList<>();

}
