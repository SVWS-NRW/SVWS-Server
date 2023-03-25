package de.svws_nrw.core.data.lehrer;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gütligen Statistikwerte für den Katalog der LehrerZugangsgründe.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Lehrer-Zugangsgründe.")
@TranspilerDTO
public class LehrerKatalogZugangsgrundEintrag {

	/** Die ID des Katalog-Eintrags.*/
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id;

	/** Das Kürzel für den Zugangsgrund. */
	@Schema(required = true, description = "das Kürzel für den Zugangsgrund", example="WIEDER")
	public @NotNull String kuerzel = "";
	
	/** Kartext des Zugangsgrunds. */
	@Schema(required = true, description = "der Zugangsgrund", example="Wiedereintritt in den Schuldienst")
	public @NotNull String text = "";

	/** Der Schlüssel für die ASD-Schulstatistik. */
	@Schema(required = true, description = "der Schlüssel für die ASD-Schulstatistik", example="05")
	public @NotNull String schluessel = "";

	/** Gibt an, in welchem Schuljahr der Zugangsgrund einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr der Zugangsgrund einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Zugangsgrund gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr der Zugangsgrund gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;

	
	
	/**
	 * Erstellt einen Zugangsgrund-Eintrag mit Standardwerten
	 */
	public LehrerKatalogZugangsgrundEintrag() {
	}


	/**
	 * Erstellt einen Zugangsgrund-Eintrag mit den angegebenen Werten
	 * 
	 * @param id           die ID
	 * @param kuerzel      das Kürzel 
	 * @param text         die textuelle Beschreibung
	 * @param schluessel   der Schlüssel für die ASD-Schulstatistik
	 * @param gueltigVon   das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis   das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public LehrerKatalogZugangsgrundEintrag(final long id, final @NotNull String kuerzel, final @NotNull String text,
			                                final @NotNull String schluessel, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.text = text;
		this.schluessel = schluessel;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

	
}
