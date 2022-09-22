package de.nrw.schule.svws.core.data.schule;

import java.util.List;
import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert einen Eintrag im Katalog der Abgangsarten.  
 */
@XmlRootElement
@Schema(description="ein Eintrag im Katalog der Abgangsarten.")
@TranspilerDTO
public class AbgangsartKatalogEintrag {

	/** Das eindeutige Kürzel des Katalog-Eintrags. */
	@Schema(required = true, description = "das eindeutige Kürzel des Katalog-Eintrags", example="0A")
	public @NotNull String kuerzel = "";

	/** Die Historie des Katalog-Eintrags. */
	@Schema(required = true, description = "die Historie des Katalog-Eintrags")
	public @NotNull List<@NotNull AbgangsartKatalogDaten> historie = new Vector<>();

}
