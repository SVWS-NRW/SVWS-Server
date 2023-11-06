package de.svws_nrw.core.data.bk;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert einen Historieneintrag eines Bildungsplans aus dem Katalog der BK-Lehrpläne
 */
@XmlRootElement
@Schema(description = "ein Historiendatum eines Eintrags in dem Katalog der BK-Lehrpläne.")
@TranspilerDTO
public class BKBildungsplan {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "1010004001")
	public long id = -1;

	/** Der zugehörige Bildungsgang. */
	@Schema(description = "Der zugehörige Bildungsgang des Bildungsplans", example = "{10,10004}")
	public @NotNull BKFachklassenSchluessel fachklasse = new BKFachklassenSchluessel();

	/** Die Dauer des Bildungsgangs in Halbjahren. */
	@Schema(description = "die Dauer des Bildungsgangs in Halbjahren", example = "7")
	public int dauer = -1;

	/** Die zugehörigen Bündelfächer */
	@Schema(description = "Die zugehörigen Bündelfächer des Bildungsplans", example = "{1000,2000}")
	public @NotNull List<@NotNull BKFBFach> fbFaecher = new ArrayList<>();

	/** Die zugehörige Liste der Lernfelder. */
	public @NotNull List<@NotNull BKLernfeld> lernfelder = new ArrayList<>();

	/** Gibt an, in welchem Schuljahr der Historien-Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr der Historien-Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem (Einschulungs-)Schuljahr der Historien-Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem (Einschulungs-)Schuljahr der Historien-Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigBis = null;

}
