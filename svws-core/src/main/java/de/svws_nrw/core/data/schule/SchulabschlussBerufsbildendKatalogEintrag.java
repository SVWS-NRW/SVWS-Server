package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog der Arten von berufsbildenden Schulabschlüsse.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Arten von berufsbildenden Schulabschlüsse.")
@TranspilerDTO
public class SchulabschlussBerufsbildendKatalogEintrag {


	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example="4711")
	public long id;

	/** Das Kürzel der Abschlussart */
	@Schema(description = "das Kürzel der Abschlussart", example="MSA")
	public @NotNull String kuerzel = "";
	
	/** Die Bezeichnung der Abschlussart. */
	@Schema(description = "die Bezeichnung der Abschlussart", example="Mittlerer Schulabschluss")
	public @NotNull String bezeichnung = "";

	/** Das Kürzel der Abschlussart für die amtliche Schulstatistik */
	@Schema(description = "das Kürzel der Abschlussart für die amtliche Schulstatistik", example="D")
	public @NotNull String kuerzelStatistik = "";
	
	/** Gibt an, in welchem Schuljahr die Abschlussart einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr die Abschlussart einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Abschlussart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr die Abschlussart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Abschlussart-Eintrag mit Standardwerten
	 */
	public SchulabschlussBerufsbildendKatalogEintrag() {
	}


	/**
	 * Erstellt einen Abschlussart-Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param kuerzel            das Kürzel 
	 * @param bezeichnung        die Bezeichnung
	 * @param kuerzelStatistik   das Kürzel der Abschlussart für die amtliche Schulstatistik
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public SchulabschlussBerufsbildendKatalogEintrag(final long id, final @NotNull String kuerzel, final @NotNull String bezeichnung, 
			final @NotNull String kuerzelStatistik, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnung = bezeichnung;
		this.kuerzelStatistik = kuerzelStatistik;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}	
	
}
