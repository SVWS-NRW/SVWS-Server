package de.svws_nrw.core.data.schule;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.schule.BerufskollegBildungsgangTyp;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.WeiterbildungskollegBildungsgangTyp;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog der möglichen Herkünfte 
 * aus Typen von Bildungsgängen an Berufskollegs oder Weiterbildungskollegs
 * beim Wechsel zu einem Weiterbildungskolleg.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Herkünfte aus Typen von Bildungsgängen an Berufskollegs oder Weiterbildungskollegs.")
@TranspilerDTO
public class HerkunftBildungsgangTypKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id = -1;

	/** Das Kürzel des Bildungsgangestyps, welches im Rahmen der amtlichen Schulstatistik verwendet wird */
	@Schema(required = true, description = "das Kürzel des Bildungsgangestyps, welches im Rahmen der amtlichen Schulstatistik verwendet wird", example="AG")
	public @NotNull String kuerzel = "";

	/** Die Kürzel der Schulformen, bei welchen der Bildungsgangstyp als Herkunft vorkommen kann (WB oder BK und SB). */
	@Schema(required = true, description = "die Kürzel der Schulformen, bei welchen der Bildungsgang als Herkunft vorkommen kann (WB oder BK und SB)")
	public @NotNull List<@NotNull String> schulformen = new Vector<>();
	
	/** Die textuelle Beschreibung des Bildungsgangtyps. */
	@Schema(required = true, description = "die textuelle Beschreibung des Bildungsgangtyps", example="Weiterbildungskolleg: Abendgymnasium")
	public @NotNull String beschreibung = "";

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public HerkunftBildungsgangTypKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id            die ID
	 * @param wbkTyp        der Bildungsgangtyp am Weiterbildungskolleg oder null 
	 * @param bkTyp         der Bildungsgangtyp am BErufskolleg oder null 
	 * @param gueltigVon    das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis    das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public HerkunftBildungsgangTypKatalogEintrag(final long id, final WeiterbildungskollegBildungsgangTyp wbkTyp,
			final BerufskollegBildungsgangTyp bkTyp, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		if ((wbkTyp != null) && (bkTyp != null))
			throw new IllegalArgumentException("Fehler im Core-Type: wbkTyp und bkTyp dürfen nicht gleichzeitig gesetzt sein.");
		if (wbkTyp != null) {
			this.kuerzel = wbkTyp.daten.kuerzel;
			this.schulformen.add(Schulform.WB.daten.kuerzel);
			this.beschreibung = wbkTyp.daten.bezeichnung;
		} else if (bkTyp != null) {
			this.kuerzel = bkTyp.daten.kuerzel;
			this.schulformen.add(Schulform.BK.daten.kuerzel);
			this.schulformen.add(Schulform.SB.daten.kuerzel);
			this.beschreibung = bkTyp.daten.bezeichnung;
		} else
			throw new NullPointerException("Fehler im Core-Type. Entweder wbkTyp oder bkTyp muss gesetzt sein.");
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
