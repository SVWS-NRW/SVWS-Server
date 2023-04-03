package de.svws_nrw.core.data.lehrer;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog der Lehrbefähigungen.  
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Lehrbefähigungen.")
@TranspilerDTO
public class LehrerKatalogLehrbefaehigungEintrag {

	/** Die ID des Katalog-Eintrags.*/
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id;

	/** Das Kürzel für die Lehrbefähigung. */
	@Schema(description = "das Kürzel für die Lehrbefähigung", example = "AL")
	public @NotNull String kuerzel = "";
	
	/** Der Klartext die Lehrbefähigung. */
	@Schema(description = "die Lehrbefähigung", example = "Arbeitslehre")
	public @NotNull String text = "";

	/** Gibt an, in welchem Schuljahr die Lehrbefähigung einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr die Lehrbefähigung einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Lehrbefähigung gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr die Lehrbefähigung gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "2025")
	public Integer gueltigBis = null;
	
	
	/**
	 * Erstellt einen Lehrbefähigungs-Eintrag mit Standardwerten
	 */
	public LehrerKatalogLehrbefaehigungEintrag() {
	}


	/**
	 * Erstellt einen Lehrbefähigungs-Eintrag mit den angegebenen Werten
	 * 
	 * @param id           die ID
	 * @param kuerzel      das Kürzel 
	 * @param text         die textuelle Beschreibung
	 * @param gueltigVon   das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis   das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public LehrerKatalogLehrbefaehigungEintrag(final long id, final @NotNull String kuerzel, final @NotNull String text, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.text = text;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}	
	
}
