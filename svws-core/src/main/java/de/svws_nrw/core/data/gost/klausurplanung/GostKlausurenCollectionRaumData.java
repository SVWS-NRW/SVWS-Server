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
 * Sie liefert eine Sammlung von Raumdaten zu einer Klausurplanung.
 */
@XmlRootElement
@Schema(description = "die Sammlung von Raumdaten zu einer Klausurplanung.")
@TranspilerDTO
public class GostKlausurenCollectionRaumData {

	/** Ein Array mit den Klausurräumen. */
	@ArraySchema(schema = @Schema(implementation = GostKlausurraum.class, description = "Ein Array mit den Klausurräumen."))
	public @NotNull List<GostKlausurraum> raeume = new ArrayList<>();

	/** Ein Array mit den Klausurraumstunden. */
	@ArraySchema(schema = @Schema(implementation = GostKlausurraumstunde.class, description = "Ein Array mit den Klausurraumstunden."))
	public @NotNull List<GostKlausurraumstunde> raumstunden = new ArrayList<>();

	/** Ein Array mit den Schülerklausurtermin-Raumstunden. */
	@ArraySchema(schema = @Schema(implementation = GostSchuelerklausurterminraumstunde.class, description = "Ein Array mit den Schülerklausurtermin-Raumstunden."))
	public @NotNull List<GostSchuelerklausurterminraumstunde> sktRaumstunden = new ArrayList<>();

	/** Ein Array mit den IDs der Klausurtermine, zu denen Raumdaten enthalten sind. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den IDs der Klausurtermine, zu denen Raumdaten enthalten sind."))
	public @NotNull List<Long> idsKlausurtermine = new ArrayList<>();

	/**
	 * Default-Konstruktor
	 */
	public GostKlausurenCollectionRaumData() {
		super();
	}

}
