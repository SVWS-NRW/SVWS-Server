package de.svws_nrw.core.data.fach;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Core-DTO für den Katalog der Sprachprüfungsniveaus.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Sprachprüfungsniveaus.")
@TranspilerDTO
public class SprachpruefungsniveauKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public int id = -1;

	/** Das Kürzel des Sprachprüfungsniveaus */
	@Schema(required = true, description = "das Kürzel des Sprachprüfungsniveaus", example="NIVEAU_HA9")
	public @NotNull String kuerzel = "";
	
    /** Die Beschreibung des Sprachprüfungsniveaus */
    @Schema(required = true, description = "die Beschreibung des Sprachprüfungsniveaus", example="Hauptschulabschluss nach Klasse 9")
    public @NotNull String beschreibung = "";

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public SprachpruefungsniveauKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id             die ID
	 * @param kuerzel        das Kürzel des Sprachprüfungsniveaus 
	 * @param beschreibung   die Beschreibung des Sprachprüfungsniveaus 
	 * @param gueltigVon     das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und 
	 *                       "schon immer gültig war"
	 * @param gueltigBis     das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */	
	public SprachpruefungsniveauKatalogEintrag(final int id, final @NotNull String kuerzel, 
			final @NotNull String beschreibung, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
        this.beschreibung = beschreibung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
