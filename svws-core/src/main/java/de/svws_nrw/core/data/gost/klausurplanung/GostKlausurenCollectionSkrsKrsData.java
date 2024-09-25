package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Sammlung von Objekten, die durch eine Raumzuweisung oder Termin-/Zeitverschiebung geändert werden.
 */
@XmlRootElement
@Schema(description = "die Sammlung von Objekten, die durch eine Raumzuweisung oder Termin-/Zeitverschiebung geändert werden.")
@TranspilerDTO
public class GostKlausurenCollectionSkrsKrsData {

	/** Die enthaltenen Raumdaten werden durch die Veränderung neu erzeugt. */
	@Schema(implementation = GostKlausurenCollectionRaumData.class,
			description = "Die enthaltenen Raumdaten werden durch die Veränderung neu erzeugt.")
	public @NotNull GostKlausurenCollectionRaumData raumdata = new GostKlausurenCollectionRaumData();

	/** Ein Array mit den Klausurraumstunden, die durch die Veränderung gelöscht wurden. */
	@ArraySchema(schema = @Schema(implementation = GostKlausurraumstunde.class, description = "Ein Array mit den Klausurraumstunden, die durch die Veränderung gelöscht wurden."))
	public @NotNull List<GostKlausurraumstunde> raumstundenGeloescht = new ArrayList<>();

	/** Ein Array mit den IDs der Schülerklausurtermine, bei denen Daten geändert wurden. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den IDs der Schülerklausurtermine, bei denen Daten geändert wurden."))
	public @NotNull List<Long> idsSchuelerklausurtermine = new ArrayList<>();

	/** Die gepatchte Kursklausur. */
	@Schema(implementation = GostKursklausur.class,
			description = "Die gepatchte Kursklausur.")
	public GostKursklausur kursKlausurPatched = null;

	/**
	 * Default-Konstruktor
	 */
	public GostKlausurenCollectionSkrsKrsData() {
		super();
	}
}
