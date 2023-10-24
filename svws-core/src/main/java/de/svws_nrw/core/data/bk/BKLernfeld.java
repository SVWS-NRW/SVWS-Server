package de.svws_nrw.core.data.bk;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert ein Lernfeld, das Teil der Klasse BKLehrplan ist.
 * Sie spezifiziert ein Lernfeld laut Lehrplan mit der Nummer des Lernfelds,
 * die komplette Bezeichnung, das zugehörige Bündelfach (in seltenen Fällen
 * mehr als eins), das Ausbildungsjahr sowie den Zeitrichtwert.
 */
@XmlRootElement
@Schema(description = "ein Lernfeld mit den Grunddaten aus dem Lehrplan")
@TranspilerDTO
public class BKLernfeld {

	/** Die Nummer des Lernfelds. */
	@Schema(description = "die Nummer des Lernfelds", example = "1")
	public @NotNull int nummer = -1;

	/** Die Bezeichnung des Lernfelds */
	@Schema(description = "die Bezeichnung des Lernfelds", example = "Bauteile mit Maschinen fertigen")
	public @NotNull String bezeichnung = "";

	/** Die Bündelfächer, denen das Lernfeld zugeordnet ist.*/
	@Schema(description = "die zugeordneten Buendelfaecher (meist nur eins).")
	public @NotNull List<@NotNull String> buendelfaecher = new ArrayList<>();

	/** Das Ausbildungsjahr, in dem das Lernfeld unterrichtet wird.*/
	@Schema(description = "das Ausbildungsjahr, in dem das Lernfeld unterrichtet wird.")
	public @NotNull int ausbildungsjahr = -1;

	/** Der Zeitrichtwert, der den zeitlichen Umfang des Lernfelds in 45Min. Einheiten angibt.*/
	@Schema(description = "Zeitrichtwert in 45-Min. Einheiten.")
	public @NotNull int zeitrichtwert = -1;

}
