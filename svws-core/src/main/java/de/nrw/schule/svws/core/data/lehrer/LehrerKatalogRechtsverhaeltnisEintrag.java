package de.nrw.schule.svws.core.data.lehrer;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gütligen Statistikwerte für den Katalog der LehrerRechtsverhältnisse.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog des Lehrer-Rechtsverhältnis.")
@TranspilerDTO
public class LehrerKatalogRechtsverhaeltnisEintrag {

	/** Die ID des Katalog-Eintrags.*/
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id;

	/** Das Kürzel für das Rechtsverhältnis. */
	@Schema(required = true, description = "das Kürzel für das Rechtsverhältnis", example="L")
	public @NotNull String kuerzel = "";
	
	/** Der Klartext des Rechtsverhältnisses. */
	@Schema(required = true, description = "das Rechtsverhältnis", example="Beamter auf Lebenszeit")
	public @NotNull String text = "";

	/** Gibt an, in welchem Schuljahr das Rechtsverhältnis einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr das Rechtsverhältnis einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr das Rechtsverhältnis gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr das Rechtsverhältnis gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;
	
	
	/**
	 * Erstellt einen Rechtsverhältnis-Eintrag mit Standardwerten
	 */
	public LehrerKatalogRechtsverhaeltnisEintrag() {
	}


	/**
	 * Erstellt einen Rechtsverhältnis-Eintrag mit den angegebenen Werten
	 * 
	 * @param id           die ID
	 * @param kuerzel      das Kürzel 
	 * @param text         die textuelle Beschreibung
	 * @param gueltigVon   das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis   das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public LehrerKatalogRechtsverhaeltnisEintrag(long id, @NotNull String kuerzel, @NotNull String text, Integer gueltigVon, Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.text = text;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}	
	
}
