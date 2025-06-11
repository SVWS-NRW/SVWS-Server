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

	/** Ein Array mit den Schülerklausurterminraumstunden, die durch die Veränderung gelöscht wurden. */
	@ArraySchema(schema = @Schema(implementation = GostSchuelerklausurterminraumstunde.class, description = "Ein Array mit den Schülerklausurterminraumstunden, die durch die Veränderung gelöscht wurden."))
	public @NotNull List<GostSchuelerklausurterminraumstunde> schuelerklausurterminraumstundenGeloescht = new ArrayList<>();

	/**
	 * Default-Konstruktor
	 */
	public GostKlausurenCollectionSkrsKrsData() {
		super();
	}

	/**
	 * Fügt die Daten der übergebenen Instanz zu den aktuellen Daten hinzu.
	 * @param data die zu hinzuzufügenden Daten
	 */
	public void addAll(final @NotNull GostKlausurenCollectionSkrsKrsData data) {
		raumdata.addAll(data.raumdata);
		raumstundenGeloescht.addAll(data.raumstundenGeloescht);
		schuelerklausurterminraumstundenGeloescht.addAll(data.schuelerklausurterminraumstundenGeloescht);
	}
}
