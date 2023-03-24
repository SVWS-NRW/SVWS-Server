package de.nrw.schule.svws.core.data.lehrer;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gütligen Statistikwerte für den Katalog der LehrerAnrechnungsgründe.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Lehrer-Anrechnungsgründe.")
@TranspilerDTO
public class LehrerKatalogAnrechnungsgrundEintrag {

	/** Die ID des Anrechnungsgrundes. */
	@Schema(required = true, description = "die ID des Anrechnungsgrundes", example="4711")
	public long id;

	/** Das Kürzel für den Anrechnungsgrund, welches für die amtliche Schulstatistik verwendet wird. */
	@Schema(required = true, description = "das Kürzel für den Anrechnungsgrund", example="755")
	public @NotNull String kuerzel = "";
	
	/** Der Klartext des Anrechnungsgrundes. */
	@Schema(required = true, description = "die textuelle Beschreibung des Anrechnungsgrundes", example="Schulleitungspauschale")
	public @NotNull String text = "";

	/** Gibt an, in welchem Schuljahr der Anrechnungsgrund einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr der Anrechnungsgrund einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Anrechnungsgrund gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr der Anrechnungsgrund gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;
	

	/**
	 * Erstellt einen Anrechnungsgrund-Eintrag mit Standardwerten
	 */
	public LehrerKatalogAnrechnungsgrundEintrag() {
	}


	/**
	 * Erstellt einen Anrechnungsgrund-Eintrag mit den angegebenen Werten
	 * 
	 * @param id           die ID
	 * @param kuerzel      das Kürzel 
	 * @param text         die textuelle Beschreibung
	 * @param gueltigVon   das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis   das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public LehrerKatalogAnrechnungsgrundEintrag(final long id, final @NotNull String kuerzel, final @NotNull String text, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.text = text;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
