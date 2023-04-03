package de.svws_nrw.core.data.schule;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert einen Eintrag in der Historie eines Katalog-Eintrags der Abgangsarten.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in der Historie eines Katalog-Eintrags der Abgangsarten.")
@TranspilerDTO
public class AbgangsartKatalogDaten {

	/** Die ID des Eintrags. */
	@Schema(description = "die ID des Eintrags", example = "4711")
	public long id = -1;

	/** Die Beschreibung der Abgangsart. */
	@Schema(description = "die Beschreibung der Abgangsart", example = "Ohne Abschluss")
	public @NotNull String beschreibung = "";

	/** Die Kombinationen von Schulformen, -gliederungen und Jahrgängen, bei der die Abgangsart zulässig ist. */
	@Schema(description = "die Kombinationen von Schulformen, -gliederungen und Jahrgängen, bei der die Abgangsart zulässig ist")
	public @NotNull List<@NotNull SchulformGliederungJahrgaenge> zulaessig = new Vector<>();

	/** Gibt an, in welchem Schuljahr der Historien-Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr der Historien-Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Historien-Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr der Historien-Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigBis = null;

}
