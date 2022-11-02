package de.nrw.schule.svws.core.data.fach;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Core-DTO für den Katalog der Sprachreferenzniveaus.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Sprachreferenzniveaus.")
@TranspilerDTO
public class SprachreferenzniveauKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id = -1;

	/** Die Bezeichnung des Sprachreferenzniveaus */
	@Schema(required = true, description = "die Bezeichnung des Sprachreferenzniveaus", example="A1")
	public @NotNull String bezeichnung = "";
	
	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public SprachreferenzniveauKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id            die ID
	 * @param bezeichnung   die Bezeichnung 
	 * @param gueltigVon    das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und 
	 *                      "schon immer gültig war"
	 * @param gueltigBis    das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */	
	public SprachreferenzniveauKatalogEintrag(long id, @NotNull String bezeichnung, 
			Integer gueltigVon, Integer gueltigBis) {
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
