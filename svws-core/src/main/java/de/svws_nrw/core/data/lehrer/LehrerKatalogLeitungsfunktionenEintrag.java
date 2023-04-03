package de.svws_nrw.core.data.lehrer;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse spezifiziert den Core-DTO für einen Katalog-Eintrag im
 * Katalog der Leitungsfunktionen von Lehrern.
 */
@XmlRootElement
@Schema(description = "ein Eintrag im Katalog der Leitungsfunktionen von Lehrern.")
@TranspilerDTO
public class LehrerKatalogLeitungsfunktionenEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id = -1;

	/** Das eindeutige Kürzel des Katalog-Eintrags. */
	@Schema(description = "das eindeutige Kürzel des Katalog-Eintrags", example = "SL")
	public @NotNull String kuerzel = "";

	/** Die Bezeichnung des Katalog-Eintrags. */
	@Schema(description = "die Bezeichnung des Katalog-Eintrags", example = "Schulleiter")
	public @NotNull String bezeichnung = "";

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Leitungsfunktion-Eintrag mit Standardwerten
	 */
	public LehrerKatalogLeitungsfunktionenEintrag() {
	}


	/**
	 * Erstellt einen Leitungsfunktion-Eintrag mit den angegebenen Werten
	 *
	 * @param id              die ID
	 * @param kuerzel         das Kürzel
	 * @param bezeichnung     die Bezeichnung
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public LehrerKatalogLeitungsfunktionenEintrag(final long id, final @NotNull String kuerzel, final @NotNull String bezeichnung,
			final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnung = bezeichnung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
