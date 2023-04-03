package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der Nationalitäten.
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Nationalitäten.")
@TranspilerDTO
public class NationalitaetenKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example="276000")
	public long id;

	/** Das 3-stellige Kürzel des ISO-Standards 3166-1 */
	@Schema(description = "das 3-stellige Kürzel des ISO-Standards 3166-1", example="DEU")
	public @NotNull String iso3 = "";

	/** Das 2-stellige Kürzel des ISO-Standards 3166-1 */
	@Schema(description = "das 2-stellige Kürzel des ISO-Standards 3166-1", example="de")
	public @NotNull String iso2 = "";
	
	/** Die 3-stellige Nummer des ISO-Standards 3166-1 */
	@Schema(description = "die 3-stellige Nummer des ISO-Standards 3166-1", example="276")
	public String isoNumerisch = null;
	
	/** Die 3-stellige Nummer, welche vom statistischen Bundesamt verwendet wird (destatis.de) */
	@Schema(description = "die 3-stellige Nummer, welche vom statistischen Bundesamt verwendet wird (destatis.de)", example="000")
	public @NotNull String codeDEStatis = "";
	
	/** Die Bezeichnung für eine Suche */
	@Schema(description = "die Bezeichnung für eine Suche", example="Deutschland")
	public @NotNull String bezeichnungSuche = "";

	/** Die kurze Bezeichnung */
	@Schema(description = "die kurze Bezeichnung", example="Deutschland")
	public @NotNull String bezeichnung = "";

	/** Die Bezeichnung für eine Suche */
	@Schema(description = "die lange Bezeichnung", example="die Bundesrepublik Deutschland")
	public @NotNull String bezeichnungLang = "";

	/** Die Bezeichnung der Staatsangehörigkeit */
	@Schema(description = "die Bezeichnung der Staatsangehörigkeit", example="deutsch")
	public @NotNull String staatsangehoerigkeit = "";
	
	/** Gibt an, in welchem Schuljahr die Nationalität ergänzt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr die Nationalität ergänzt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Nationalität verwendet wird. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr die Nationalität verwendet wird. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Katalog-Eintrag mit Standardwerten
	 */
	public NationalitaetenKatalogEintrag() {
	}


	/**
	 * Erstellt einen Katalog-Eintrag mit den angegebenen Werten
	 *
	 * @param id                     die ID des Katalog-Eintrags
	 * @param iso3                   das 3-stellige Kürzel des ISO-Standards 3166-1
	 * @param iso2                   das 2-stellige Kürzel des ISO-Standards 3166-1
	 * @param isoNumerisch           die 3-stellige Nummer des ISO-Standards 3166-1
	 * @param codeDEStatis           die 3-stellige Nummer, welche vom statistischen Bundesamt verwendet wird (destatis.de)
	 * @param bezeichnungSuche       die Bezeichnung für eine Suche
	 * @param bezeichnung            die kurze Bezeichnung
	 * @param bezeichnungLang        die Bezeichnung für eine Suche
	 * @param staatsangehoerigkeit   die Bezeichnung der Staatsangehörigkeit
	 * @param gueltigVon             das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis             das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public NationalitaetenKatalogEintrag(final long id, final @NotNull String iso3, final @NotNull String iso2, final String isoNumerisch, 
			final @NotNull String codeDEStatis, final @NotNull String bezeichnungSuche, final @NotNull String bezeichnung, 
			final @NotNull String bezeichnungLang, final @NotNull String staatsangehoerigkeit, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.iso3 = iso3;
		this.iso2 = iso2;
		this.isoNumerisch = isoNumerisch;
		this.codeDEStatis = codeDEStatis;
		this.bezeichnungSuche = bezeichnungSuche;
		this.bezeichnung = bezeichnung;
		this.bezeichnungLang = bezeichnungLang;
		this.staatsangehoerigkeit = staatsangehoerigkeit;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}	

	
	
	
}
