package de.svws_nrw.core.data.schule;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog der Schulformen.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Schulformen.")
@TranspilerDTO
public class NotenKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example="4711")
	public int id = -1;
	/** Eine ID, die der Sortierung der Noteneinträge in einer Anwendung vorgibt */
	@Schema(description = "eine ID, die der Sortierung der Noteneinträge in einer Anwendung vorgibt", example="3")
	public int sortierung = -1;

	/** Die Notenpunkte, die dieser Note zugeordnet sind */
	@Schema(description = "die Notenpunkte, die dieser Note zugeordnet sind", example="13")
	public Integer notenpunkte = null;

	/** Die Kurzschreibweise der Note als Zahl ggf. mit Tendenz (+/-) */
	@Schema(description = "die Kurzschreibweise der Note als Zahl ggf. mit Tendenz (+/-)", example="1-")
	public @NotNull String kuerzel = "";

	/** Die Note in ausführlicher Textform ggf. mit Tendenz (plus/minus) */
	@Schema(description = "die Note in ausführlicher Textform ggf. mit Tendenz (plus/minus)", example="sehr gut (minus)")
	public @NotNull String text = "";

	/** Die Note in ausführlicher Textform, wie sie auf einem Zeugnis dargestellt wird. */
	@Schema(description = "die Note in ausführlicher Textform, wie sie auf einem Zeugnis dargestellt wird", example="sehr gut (minus)")
	public @NotNull String textZeugnis = "";

	/** Gibt an, in welchem Schuljahr die Note einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem die Note einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Note gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem die Note gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Noten-Eintrag mit Standardwerten
	 */
	public NotenKatalogEintrag() {
	}


	/**
	 * Erstellt einen Noten-Eintrag mit den angegebenen Werten
	 * 
	 * @param id              die ID
	 * @param sortierung      die Nummer für die Sortierung der Noten-Einträge
	 * @param notenpunkte     die Notenpunkte 
	 * @param kuerzel         das Kürzel 
	 * @param text            der Text für die Notenbeschreibung 
	 * @param textZeugnis     das Text, welcher auf dem Zeugnis erscheint
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public NotenKatalogEintrag(final int id, final int sortierung, final Integer notenpunkte, final @NotNull String kuerzel, 
			final @NotNull String text, final @NotNull String textZeugnis, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.sortierung = sortierung;
		this.notenpunkte = notenpunkte;
		this.kuerzel = kuerzel;
		this.text = text;
		this.textZeugnis = textZeugnis;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
