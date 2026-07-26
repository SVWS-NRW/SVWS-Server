package de.svws_nrw.core.data.gost.klausuren;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert eine Sammlung aller zu einer Klausurplanung gehörigen Daten.
 */
@XmlRootElement
@Schema(description = "die Sammlung aller zu einer Klausurplanung gehörigen Daten.")
@TranspilerDTO
public class GostKlausurenAlleKlausurdaten {

	/** Ein Array mit Paaren der enthaltenen Abiturjahrgänge / GostHalbjahre.*/
	@ArraySchema(schema = @Schema(implementation = GostKlausurenHalbjahresdaten.class, description = "Ein Array mit Paaren der enthaltenen Abiturjahrgänge / GostHalbjahre."))
	public @NotNull List<GostKlausurenHalbjahresdaten> halbjahresdaten = new ArrayList<>();

	/** Ein Array mit den Daten der Lehrer. */
	@ArraySchema(schema = @Schema(implementation = LehrerListeEintrag.class, description = "Ein Array mit den Daten der Lehrer."))
	public @NotNull List<LehrerListeEintrag> lehrer = new ArrayList<>();

	/**
	 * Default-Konstruktor
	 */
	public GostKlausurenAlleKlausurdaten() {
		super();
	}

}
