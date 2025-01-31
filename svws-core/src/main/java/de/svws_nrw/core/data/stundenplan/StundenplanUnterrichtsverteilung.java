package de.svws_nrw.core.data.stundenplan;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu der Unterrichtsverteilung eines Stundenplans.
 */
@XmlRootElement
@Schema(description = "Die Daten zu der Unterrichtsverteilung des Stundenplans.")
@TranspilerDTO
public class StundenplanUnterrichtsverteilung {

	/** Die ID des Stundenplans. */
	@Schema(description = "die ID des Stundenplans", example = "815")
	public long id = -1;

	/** Die Liste der Lehrer, die für den Stundenplan zur Verfügung stehen. */
	@Schema(description = "die Liste der Lehrer, die für den Stundenplan zur Verfügung stehen")
	public @NotNull List<StundenplanLehrer> lehrer = new ArrayList<>();

	/** Die Liste der Schüler, die für den Stundenplan zur Verfügung stehen. */
	@Schema(description = "die Liste der Schüler, die für den Stundenplan zur Verfügung stehen")
	public @NotNull List<StundenplanSchueler> schueler = new ArrayList<>();

	/** Die Liste der Fächer, die für den Stundenplan zur Verfügung stehen. */
	@Schema(description = "die Liste der Fächer, die für den Stundenplan zur Verfügung stehen")
	public @NotNull List<StundenplanFach> faecher = new ArrayList<>();

	/** Die Liste der Klassen, die für den Stundenplan zur Verfügung stehen. */
	@Schema(description = "die Liste der Klassen, die für den Stundenplan zur Verfügung stehen")
	public @NotNull List<StundenplanKlasse> klassen = new ArrayList<>();

	/** Die Liste der Kurse, die für den Stundenplan zur Verfügung stehen. */
	@Schema(description = "die Liste der Kurse, die für den Stundenplan zur Verfügung stehen")
	public @NotNull List<StundenplanKurs> kurse = new ArrayList<>();

	/** Die Liste der Klassenunterrichte, die für den Stundenplan zur Verfügung stehen. */
	@Schema(description = "die Liste der Klassenunterrichte, die für den Stundenplan zur Verfügung stehen")
	public @NotNull List<StundenplanKlassenunterricht> klassenunterricht = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public StundenplanUnterrichtsverteilung() {
		// leer
	}

}
