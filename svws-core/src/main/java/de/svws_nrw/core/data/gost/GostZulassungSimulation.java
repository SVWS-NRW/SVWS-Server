package de.svws_nrw.core.data.gost;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Der Stand einer Simulation der Abiturzulassung und des Abiturergebnisses eines Schülers.
 * Abgelegt wird nur, was die Laufbahnplanung nicht liefert.
 */
@XmlRootElement
@Schema(description = "Der Stand einer Simulation der Abiturzulassung und des Abiturergebnisses eines Schülers.")
@TranspilerDTO
public class GostZulassungSimulation {

	/** Die eingegebenen Notenpunkte der Fächer in den Halbjahren der Qualifikationsphase */
	@ArraySchema(schema = @Schema(implementation = GostZulassungSimulationNote.class,
			description = "die eingegebenen Notenpunkte der Fächer in den Halbjahren der Qualifikationsphase"))
	public final @NotNull List<GostZulassungSimulationNote> noten = new ArrayList<>();

	/** Die eingegebenen Prüfungsergebnisse der Abiturfächer */
	@ArraySchema(schema = @Schema(implementation = GostZulassungSimulationAbiturfach.class,
			description = "die eingegebenen Prüfungsergebnisse der Abiturfächer"))
	public final @NotNull List<GostZulassungSimulationAbiturfach> abiturfaecher = new ArrayList<>();

	/** Die Art der besonderen Lernleistung (K - keine, P - in einem Projektkurs, E - extern) */
	@Schema(description = "die Art der besonderen Lernleistung (K - keine, P - in einem Projektkurs, E - extern)", example = "K")
	public @NotNull String besondereLernleistung = "K";

	/** Die Notenpunkte der besonderen Lernleistung oder null, wenn nichts eingegeben wurde */
	@Schema(description = "die Notenpunkte der besonderen Lernleistung oder null, wenn nichts eingegeben wurde", example = "11")
	public Integer besondereLernleistungNotenpunkte = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostZulassungSimulation() {
		// leer
	}

}
