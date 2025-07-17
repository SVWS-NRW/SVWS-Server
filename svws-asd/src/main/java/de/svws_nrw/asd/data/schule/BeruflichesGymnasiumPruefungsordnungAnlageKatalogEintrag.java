package de.svws_nrw.asd.data.schule;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der Anlagen des Beruflichen Gymnasiums aus der APO-BK.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Anlagen des Beruflichen Gymnasiums.")
@TranspilerDTO
public class BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag extends CoreTypeData {

	/** Der angestrebte allgemeinbildende Abschluss des Bildungsgangs */
	@Schema(description = "Der angestrebte allgemeinbildende Abschluss des Bildungsgangs", example = "ABITUR")
	public @NotNull String abschlussAllgemeinbildend = "";

	/** Die Schulgliederung */
	@Schema(description = "die Schulgliederung", example = "D01")
	public @NotNull String gliederung = "";
	/** Der Fachklassenschlüssel */

	@Schema(description = "der Fachklassenschlüssel", example = "13000")
	public @NotNull String fachklassenschluessel = "";

	/** Die Varianten der Stundentafeln eines Bildungsgangs */
	@Schema(description = "die Varianten der Stundentafeln eines Bildungsgangs", example = "")
	public @NotNull List<BeruflichesGymnasiumStundentafel> stundentafeln = new ArrayList<>();

	/** Die Fussnoten zu den Stundentafeln */
	@Schema(description = "Die Fussnoten zu den Stundentafeln", example = "3) Die in § 4 Absatz 6 genannten Schülerinnen und Schüler haben im Beruflichen Gymnasium mindestens 102 Gesamtwochenstunden Pflichtunterricht.")
	public @NotNull List<String> fussnoten = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag() {
		// leer
	}

}
