package de.svws_nrw.core.data.lehrer;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gütligen Statistikwerte für den Katalog der LehrerEinsatzstati.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog des Lehrer-Einsatzstatus.")
@TranspilerDTO
public class LehrerKatalogEinsatzstatusEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id;

	/** Das Kürzel für den Einsatzstatus. */
	@Schema(description = "das Kürzel für den Einsatzstatus", example = "A")
	public @NotNull String kuerzel = "";

	/** Der Klartext des Einsatzstatus.*/
	@Schema(description = "der Einsatzstatus", example = "Stammschule, ganz oder teilweise auch an anderen Schulen tätig")
	public @NotNull String text = "";

	/** Gibt an, in welchem Schuljahr der Einsatzstatus einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr der Einsatzstatus einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Beschäftigungsart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr der Einsatzstatus gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Einsatzstatus-Eintrag mit Standardwerten
	 */
	public LehrerKatalogEinsatzstatusEintrag() {
	}


	/**
	 * Erstellt einen Einsatzstatus-Eintrag mit den angegebenen Werten
	 *
	 * @param id           die ID
	 * @param kuerzel      das Kürzel
	 * @param text         die textuelle Beschreibung
	 * @param gueltigVon   das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis   das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public LehrerKatalogEinsatzstatusEintrag(final long id, final @NotNull String kuerzel, final @NotNull String text, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.text = text;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
