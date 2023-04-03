package de.svws_nrw.core.data.schule;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert einen Eintrag im Katalog der Abgangsarten.
 */
@XmlRootElement
@Schema(description = "ein Eintrag im Katalog der Abgangsarten.")
@TranspilerDTO
public class AbgangsartKatalogEintrag {

	/** Das eindeutige Kürzel des Katalog-Eintrags. */
	@Schema(description = "das eindeutige Kürzel des Katalog-Eintrags", example = "0A")
	public @NotNull String kuerzel = "";

	/** Die Historie des Katalog-Eintrags. */
	@Schema(description = "die Historie des Katalog-Eintrags")
	public @NotNull List<@NotNull AbgangsartKatalogDaten> historie = new Vector<>();

}
