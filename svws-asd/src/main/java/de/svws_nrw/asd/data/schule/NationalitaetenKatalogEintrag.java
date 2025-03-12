package de.svws_nrw.asd.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog der in der amtlichenn Schulstatistik berücksichtigten Nationalitäten.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Nationalitäten.")
@TranspilerDTO
public class NationalitaetenKatalogEintrag extends CoreTypeData {

	/** Das 3-stellige Kürzel des ISO-Standards 3166-1 */
	@Schema(description = "das 3-stellige Kürzel des ISO-Standards 3166-1", example = "DEU")
	public @NotNull String iso3 = "";

	/** Das 2-stellige Kürzel des ISO-Standards 3166-1 */
	@Schema(description = "das 2-stellige Kürzel des ISO-Standards 3166-1", example = "de")
	public @NotNull String iso2 = "";

	/** Die 3-stellige Nummer des ISO-Standards 3166-1 */
	@Schema(description = "die 3-stellige Nummer des ISO-Standards 3166-1", example = "276")
	public String isoNumerisch = null;

	/** Die 3-stellige Nummer, welche vom statistischen Bundesamt verwendet wird (destatis.de) */
	@Schema(description = "die 3-stellige Nummer, welche vom statistischen Bundesamt verwendet wird (destatis.de)", example = "000")
	public @NotNull String codeDEStatis = "";

	/** Die Bezeichnung für eine Suche */
	@Schema(description = "die Bezeichnung für eine Suche", example = "Deutschland")
	public @NotNull String bezeichnungSuche = "";

	/** Die kurze Bezeichnung */
	@Schema(description = "die kurze Bezeichnung", example = "Deutschland")
	public @NotNull String bezeichnung = "";

	/** Die Bezeichnung für eine Suche */
	@Schema(description = "die lange Bezeichnung", example = "die Bundesrepublik Deutschland")
	public @NotNull String bezeichnungLang = "";

	/** Die Bezeichnung der Staatsangehörigkeit */
	@Schema(description = "die Bezeichnung der Staatsangehörigkeit", example = "deutsch")
	public @NotNull String staatsangehoerigkeit = "";


	/**
	 * Erstellt einen Katalog-Eintrag mit Standardwerten
	 */
	public NationalitaetenKatalogEintrag() {
		// leer
	}

}
