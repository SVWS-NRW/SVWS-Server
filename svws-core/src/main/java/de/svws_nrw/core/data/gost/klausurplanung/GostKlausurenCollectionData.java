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
 * Sie liefert eine Sammlung aller zu einer Klausurplanung gehörigen Daten.
 */
@XmlRootElement
@Schema(description = "die Sammlung aller zu einer Klausurplanung gehörigen Daten.")
@TranspilerDTO
public class GostKlausurenCollectionData {

	/** Ein Array mit den Klausurvorgaben. */
	@ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class, description = "Ein Array mit den Klausurvorgaben."))
	public @NotNull List<GostKlausurvorgabe> vorgaben = new ArrayList<>();

	/** Ein Array mit den Kursklausuren. */
	@ArraySchema(schema = @Schema(implementation = GostKursklausur.class, description = "Ein Array mit den Kursklausuren."))
	public @NotNull List<GostKursklausur> kursklausuren = new ArrayList<>();

	/** Ein Array mit den Schülerklausuren. */
	@ArraySchema(schema = @Schema(implementation = GostSchuelerklausur.class, description = "Ein Array mit den Schülerklausuren."))
	public @NotNull List<GostSchuelerklausur> schuelerklausuren = new ArrayList<>();

	/** Ein Array mit den Schülerklausurterminen. */
	@ArraySchema(schema = @Schema(implementation = GostSchuelerklausurTermin.class, description = "Ein Array mit den Schülerklausurterminen."))
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurtermine = new ArrayList<>();

	/** Ein Array mit den Klausurterminen. */
	@ArraySchema(schema = @Schema(implementation = GostKlausurtermin.class, description = "Ein Array mit den GostKlausurtermin."))
	public @NotNull List<GostKlausurtermin> termine = new ArrayList<>();

	@Schema(description = "Beschreibung zur Collection", example = "...")
	public String description;

	/**
	 * Default-Konstruktor
	 */
	public GostKlausurenCollectionData() {
		super();
	}

}
