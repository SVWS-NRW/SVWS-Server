package de.nrw.schule.svws.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der (Schul-)Typen von Bildungsgängen beim Berufskolleg oder Weiterbildungskolleg.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Berufsschultypen von Bildungsgängen beim Berufskolleg oder Weiterbildungskolleg.")
@TranspilerDTO
public class BildungsgangTypKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id;

	/** Das Kürzel des (Schul-)Typs von Bildungsgängen */
	@Schema(required = true, description = "das Kürzel des (Schul-)Typs von Bildungsgängen", example="FO")
	public @NotNull String kuerzel = "";
	
	/** Die Bezeichnung des (Schul-)Typs von Bildungsgängen. */
	@Schema(required = true, description = "die Bezeichnung des des (Schul-)Typs von Bildungsgängen", example="Fachoberschule")
	public @NotNull String bezeichnung = "";

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public BildungsgangTypKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param kuerzel            das Kürzel 
	 * @param bezeichnung        die Bezeichnung
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public BildungsgangTypKatalogEintrag(final long id, final @NotNull String kuerzel, final @NotNull String bezeichnung, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnung = bezeichnung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}	
	
}
