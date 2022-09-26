package de.nrw.schule.svws.core.data.lehrer;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog der Lehrämter.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Lehrämter.")
@TranspilerDTO
public class LehrerKatalogLehramtEintrag {

	/** Die ID des Katalog-Eintrags.*/
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id;

	/** Das Kürzel für das Lehramt. */
	@Schema(required = true, description = "das Kürzel für das Lehramt", example="15")
	public @NotNull String kuerzel = "";
	
	/** Der Klartext das Lehramt. */
	@Schema(required = true, description = "das Lehramt", example="Grund-, Haupt- u. Realschule u. entspr. Jahrg.stufen d. Gesamtschule -Schwerp.- Grundschule")
	public @NotNull String text = "";

	/** Gibt an, in welchem Schuljahr das Lehramt einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr das Lehramt einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr das Lehramt gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr das Lehramt gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;
	
	
	/**
	 * Erstellt einen Lehramt-Eintrag mit Standardwerten
	 */
	public LehrerKatalogLehramtEintrag() {
	}


	/**
	 * Erstellt einen Lehramt-Eintrag mit den angegebenen Werten
	 * 
	 * @param id           die ID
	 * @param kuerzel      das Kürzel 
	 * @param text         die textuelle Beschreibung
	 * @param gueltigVon   das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis   das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public LehrerKatalogLehramtEintrag(long id, @NotNull String kuerzel, @NotNull String text, Integer gueltigVon, Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.text = text;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}	
	
}
