package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog der in der amtlichenn Schulstatistik
 * berücksichtigten Religionen / Konfessionen.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der in der amtlichen Schulstatistik berücksichtigten Religionen / Konfessionen.")
@TranspilerDTO
public class ReligionKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id = -1;

	/** Das Kürzel der Religion, welches im Rahmen der amtlichen Schulstatistik verwendet wird */
	@Schema(description = "das Kürzel der Religion bzw. Konfession, welches im Rahmen der amtlichen Schulstatistik verwendet wird", example = "OH")
	public @NotNull String kuerzel = "";

	/** Eine kurze Bezeichnung der Religion. */
	@Schema(description = "eine kurze Bezeichnung der Religion bzw. Konfession", example = "ohne Bekenntnis")
	public @NotNull String bezeichnung = "";

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public ReligionKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 *
	 * @param id              die ID
	 * @param kuerzel         das Kürzel der Religion
	 * @param bezeichnung     eine kurze Bezeichnung der Religion
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public ReligionKatalogEintrag(final long id, final @NotNull String kuerzel, final @NotNull String bezeichnung,
			final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnung = bezeichnung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
