package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der Berufsebenen beim Berufskolleg.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Berufsebenen beim Berufskolleg.")
@TranspilerDTO
public class BerufskollegBerufsebeneKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id;

	/** Die Berufsebene. */
	@Schema(description = "die Berufsebene", example = "1")
	public int ebene;

	/** Das Kürzel der Berufsebene */
	@Schema(description = "das Kürzel der Berufsebene", example = "IF")
	public @NotNull String kuerzel = "";

	/** Die Bezeichnung der Berufsebene. */
	@Schema(description = "die Bezeichnung der Berufsebene", example = "Informatik")
	public @NotNull String bezeichnung = "";

	/** Gibt an, in welchem Schuljahr die Berufsebene einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr die Berufsebene einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Berufsebene gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr die Berufsebene gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Berufsebene-Eintrag mit Standardwerten
	 */
	public BerufskollegBerufsebeneKatalogEintrag() {
	}


	/**
	 * Erstellt einen Berufsebene-Eintrag mit den angegebenen Werten
	 *
	 * @param id                 die ID
	 * @param ebene              die Berufsebene
	 * @param kuerzel            das Kürzel
	 * @param bezeichnung        die Bezeichnung
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public BerufskollegBerufsebeneKatalogEintrag(final long id, final int ebene, final @NotNull String kuerzel, final @NotNull String bezeichnung,
			final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.ebene = ebene;
		this.kuerzel = kuerzel;
		this.bezeichnung = bezeichnung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
