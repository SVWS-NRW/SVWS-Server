package de.nrw.schule.svws.core.data.lehrer;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gütligen Statistikwerte für den Katalog der Mehrleistungsarten für Lehrer.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Mehrleistungsarten für Lehrer.")
@TranspilerDTO
public class LehrerKatalogMehrleistungsartEintrag {

	/** Die ID der Mehrleistungsart. */
	@Schema(required = true, description = "die ID der Mehrleistungsart", example="4711")
	public long id;

	/** Das Kürzel für die Mehrleistungsart, welches für die amtliche Schulstatistik verwendet wird. */
	@Schema(required = true, description = "das Kürzel für die Mehrleistungsart", example="110")
	public @NotNull String kuerzel = "";
	
	/** Der Klartext der Mehrleistungsart. */
	@Schema(required = true, description = "die textuelle Beschreibung der Mehrleistungsart", example="Mehrarbeit (angeordnet und regelmäßig)")
	public @NotNull String text = "";

	/** Gibt an, in welchem Schuljahr die Mehrleistungsart einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr die Mehrleistungsart einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Mehrleistungsart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr die Mehrleistungsart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;
	

	/**
	 * Erstellt einen Mehrleistungsart-Eintrag mit Standardwerten
	 */
	public LehrerKatalogMehrleistungsartEintrag() {
	}


	/**
	 * Erstellt einen Mehrleistungsart-Eintrag mit den angegebenen Werten
	 * 
	 * @param id           die ID
	 * @param kuerzel      das Kürzel 
	 * @param text         die textuelle Beschreibung
	 * @param gueltigVon   das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis   das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public LehrerKatalogMehrleistungsartEintrag(long id, @NotNull String kuerzel, @NotNull String text, Integer gueltigVon, Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.text = text;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
