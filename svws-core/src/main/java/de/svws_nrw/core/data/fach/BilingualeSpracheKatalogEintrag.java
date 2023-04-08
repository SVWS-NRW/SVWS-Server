package de.svws_nrw.core.data.fach;

import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.schule.Schulform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert den Katalog der Bilingualen Sprachfächer und die Information für welche Schulformen
 * diese zulässig sind.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Bilingualen Sprachfächer.")
@TranspilerDTO
public class BilingualeSpracheKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id = -1;

	/** Das einstellige Kürzel der Fremdsprache */
	@Schema(description = "das einstellige Kürzel der Fremdsprache", example = "S")
	public @NotNull String kuerzel = "";

	/** Die Kürzel der Schulformen, wo die Sprache als bilinguale Fremdsprache zulässig ist. */
	@Schema(description = "die Kürzel der Schulformen, wo die Sprache als bilinguale Fremdsprache zulässig ist.")
	public @NotNull List<@NotNull String> schulformen = new ArrayList<>();

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public BilingualeSpracheKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 *
	 * @param id                    die ID
	 * @param fach                  das Fremdsprachenfach
	 * @param schulformen           die Schulformen, wo die Sprache als bilinguale Fremdsprache zulässig ist
	 * @param gueltigVon            das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und
	 *                              "schon immer gültig war"
	 * @param gueltigBis            das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public BilingualeSpracheKatalogEintrag(final long id, final @NotNull ZulaessigesFach fach,
			final @NotNull List<@NotNull Schulform> schulformen, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = fach.daten.kuerzel;
		for (final @NotNull Schulform schulform : schulformen)
			this.schulformen.add(schulform.daten.kuerzel);
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
