package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der Anlagen beim Berufskolleg.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Anlagen beim Berufskolleg.")
@TranspilerDTO
public class BerufskollegAnlageKatalogEintrag {


	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id;

	/** Das Kürzel der Anlage */
	@Schema(required = true, description = "das Kürzel der Anlage", example="A")
	public @NotNull String kuerzel = "";
	
	/** Die Bezeichnung der Anlage. */
	@Schema(required = true, description = "die Bezeichnung der Anlage", example="Fachklassen duales System und Ausbildungsvorbereitung")
	public @NotNull String bezeichnung = "";

	/** Gibt an, in welchem Schuljahr die Anlage einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr die Anlage einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Anlage gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr die Anlage gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Anlage-Eintrag mit Standardwerten
	 */
	public BerufskollegAnlageKatalogEintrag() {
	}


	/**
	 * Erstellt einen Anlage-Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param kuerzel            das Kürzel 
	 * @param bezeichnung        die Bezeichnung
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public BerufskollegAnlageKatalogEintrag(final long id, final @NotNull String kuerzel, final @NotNull String bezeichnung, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnung = bezeichnung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}	
	
}
