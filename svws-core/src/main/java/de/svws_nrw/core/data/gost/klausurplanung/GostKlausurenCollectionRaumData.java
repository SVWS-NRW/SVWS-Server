package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert eine Sammlung von Gost-Klausurvorgaben, Kursklausuren und Schülerklausuren.
 */
@XmlRootElement
@Schema(description = "die Sammlung von Gost-Klausurvorgaben, Kursklausuren und Schülerklausuren")
@TranspilerDTO
public class GostKlausurenCollectionRaumData {

	/** Die ID der Schülerklausur. */
	@Schema(description = "die ID der Schülerklausur", example = "")
	public @NotNull List<GostKlausurraum> raeume = new ArrayList<>();

	/** Die ID der Schülerklausur. */
	@Schema(description = "die ID der Schülerklausur", example = "")
	public @NotNull List<GostKlausurraumstunde> raumstunden = new ArrayList<>();

	/** Die ID der Klausurraumstunde. */
	@Schema(description = "die ID der Klausurraumstunde", example = "")
	public @NotNull List<GostSchuelerklausurterminraumstunde> sktRaumstunden = new ArrayList<>();

	/** Die ID der Klausurraumstunde. */
	@Schema(description = "die ID der Klausurraumstunde", example = "")
	public @NotNull List<Long> idsKlausurtermine = new ArrayList<>();

	/**
	 * Sammelt alle Daten, die für die Klausurplanung der gesamten Oberstufe nötig sind.
	 *
	 * @param addData dd
	 */
	public void addAll(final @NotNull GostKlausurenCollectionRaumData addData) {
		raeume.addAll(addData.raeume);
		raumstunden.addAll(addData.raumstunden);
		sktRaumstunden.addAll(addData.sktRaumstunden);
		idsKlausurtermine.addAll(addData.idsKlausurtermine);
	}

}
