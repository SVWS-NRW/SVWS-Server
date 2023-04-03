package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der Verkehrssprachen.  
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Verkehrssprachen.")
@TranspilerDTO
public class VerkehrsspracheKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "34")
	public long id;

	/** Der dreistellige ISO 639-2 code */
	@Schema(description = "der dreistellige ISO 639-2 code", example = "deu")
	public @NotNull String kuerzel = "";
	
	/** Die ISO-Bezeichnung der Sprache. */
	@Schema(description = "die ISO-Bezeichnung der Sprache", example = "Deutsch")
	public @NotNull String bezeichnung = "";

	/** Der zweistellige ISO 639-1 code (wird aktuell für die Statistik verwendet - null entspricht dem Statistik-Code xn) */
	@Schema(description = "der zweistellige ISO 639-1 code (wird aktuell für die Statistik verwendet - null entspricht dem Statistik-Code xn)", example = "de")
	public String iso2 = null;
	
	/** Gibt an, in welchem Schuljahr die Verkehrssprache ergänzt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr die Verkehrssprache ergänzt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Verkehrssprache verwendet wird. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr die Verkehrssprache verwendet wird. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Katalog-Eintrag mit Standardwerten
	 */
	public VerkehrsspracheKatalogEintrag() {
	}


	/**
	 * Erstellt einen Katalog-Eintrag mit den angegebenen Werten
	 * 
	 * @param id                    die ID
	 * @param kuerzel               der dreistellige ISO 639-2 code 
	 * @param bezeichnung           die ISO-Bezeichnung der Sprache
	 * @param iso2                  der zweistellige ISO 639-1 code, sofern vorhanden
	 * @param gueltigVon            das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis            das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	
	public VerkehrsspracheKatalogEintrag(final long id, final @NotNull String kuerzel, final @NotNull String bezeichnung, 
			final String iso2, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnung = bezeichnung;
		this.iso2 = iso2;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}	
	
}
