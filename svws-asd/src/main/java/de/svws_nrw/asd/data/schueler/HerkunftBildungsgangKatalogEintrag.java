package de.svws_nrw.asd.data.schueler;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.asd.data.schule.SchulformSchulgliederung;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog der möglichen Herkünfte
 * aus Bildungsgängen an Berufskollegs beim Wechsel zu einem Berufskolleg.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Herkünfte aus Bildungsgängen an Berufskollegs.")
@TranspilerDTO
public class HerkunftBildungsgangKatalogEintrag extends CoreTypeData {

	/** Die Kürzel der Schulformen, bei welchen der Bildungsgang als Herkunft vorkommen kann (BK und SB). */
	@Schema(description = "die Kürzel der Schulformen, bei welchen der Bildungsgang als Herkunft vorkommen kann (BK und SB)")
	public @NotNull List<@NotNull SchulformSchulgliederung> zulaessig = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public HerkunftBildungsgangKatalogEintrag() {
		// leer
	}

}
