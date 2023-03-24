package de.nrw.schule.svws.core.data.lehrer;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog der Anerkennungsgründe für 
 * eine Lehrbefähigung.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Anerkennungsgründe für Lehrbefähigungen.")
@TranspilerDTO
public class LehrerKatalogLehrbefaehigungAnerkennungEintrag {

	/** Die ID des Katalog-Eintrags.*/
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id;

	/** Das Kürzel für den Anerkennungsgrund einer Lehrbefähigung. */
	@Schema(required = true, description = "das Kürzel für den Anerkennungsgrund einer Lehrbefähigung", example="2")
	public @NotNull String kuerzel = "";
	
	/** Der Klartext des Anerkennungsgrundes einer Lehrbefähigung. */
	@Schema(required = true, description = "der Anerkennungsgrund einer Lehrbefähigung", example="Unterrichtserlaubnis (z. B. Zertifikatskurs)")
	public @NotNull String text = "";

	/** Gibt an, in welchem Schuljahr der Anerkennungsgrund einer Lehrbefähigung einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr der Anerkennungsgrund einer Lehrbefähigung einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Anerkennungsgrund einer Lehrbefähigung gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr der Anerkennungsgrund einer Lehrbefähigung gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;
	
	
	/**
	 * Erstellt einen Anerkennungsgrund-Eintrag mit Standardwerten
	 */
	public LehrerKatalogLehrbefaehigungAnerkennungEintrag() {
	}


	/**
	 * Erstellt einen Anerkennungsgrund-Eintrag mit den angegebenen Werten
	 * 
	 * @param id           die ID
	 * @param kuerzel      das Kürzel 
	 * @param text         die textuelle Beschreibung
	 * @param gueltigVon   das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis   das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public LehrerKatalogLehrbefaehigungAnerkennungEintrag(final long id, final @NotNull String kuerzel, final @NotNull String text, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.text = text;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}	
	
}
