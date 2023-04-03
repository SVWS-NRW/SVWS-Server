package de.svws_nrw.core.data.schule;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog der möglichen Herkünfte 
 * aus Bildungsgängen an Berufskollegs beim Wechsel zu einem Berufskolleg.  
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Herkünfte aus Bildungsgängen an Berufskollegs.")
@TranspilerDTO
public class HerkunftBildungsgangKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id = -1;

	/** Das Kürzel des Bildungsganges, welches im Rahmen der amtlichen Schulstatistik verwendet wird */
	@Schema(description = "das Kürzel des Bildungsganges, welches im Rahmen der amtlichen Schulstatistik verwendet wird", example = "UN")
	public @NotNull String kuerzel = "";

	/** Die Kürzel der Schulformen, bei welchen der Bildungsgang als Herkunft vorkommen kann (BK und SB). */
	@Schema(description = "die Kürzel der Schulformen, bei welchen der Bildungsgang als Herkunft vorkommen kann (BK und SB)")
	public @NotNull List<@NotNull String> schulformen = new Vector<>();
	
	/** Die textuelle Beschreibung der sonstigen Herkunft. */
	@Schema(description = "die textuelle Beschreibung des Bildungsganges", example = "Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 3j. TZ)")
	public @NotNull String beschreibung = "";

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public HerkunftBildungsgangKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id              die ID
	 * @param gliederung      die Schulgliederung 
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public HerkunftBildungsgangKatalogEintrag(final long id, final @NotNull Schulgliederung gliederung,
			final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = gliederung.daten.kuerzel;
		this.schulformen.add(Schulform.BK.daten.kuerzel);
		this.schulformen.add(Schulform.SB.daten.kuerzel);
		this.beschreibung = gliederung.daten.beschreibung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
