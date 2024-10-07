package de.svws_nrw.core.data.gost;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse stellt einen Core-Type für einen Jahrgang der gymnasialen Oberstufe
 * zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
@XmlRootElement
@Schema(description = "ein Abiturjahrgang in der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostJahrgang {

	/** Das Jahr, in welchem der Jahrgang Abitur machen wird. -1 bei der Vorlage für neue Abiturjahrgänge. */
	@Schema(description = "das Jahr, in welchem der Jahrgang Abitur machen wird. -1 bei der Vorlage für neue Abiturjahrgänge.", example = "2042")
	public int abiturjahr = -1;

	/** Die aktuelle Jahrgangstufe, welche dem Abiturjahrgang zugeordnet ist. */
	@Schema(description = "die aktuelle Jahrgangstufe, welche dem Abiturjahrgang zugeordnet ist", example = "Q1")
	public String jahrgang = null;

	/** Das aktuelle Halbjahr, in dem sich der Jahrgang laut Schuljahrsabschnitt der Schule befindet. */
	@Schema(description = "das aktuelle Halbjahr, in dem sich der Jahrgang laut Schuljahrsabschnitt der Schule befindet", example = "Q1")
	public int halbjahr;

	/** Die textuelle Bezeichnung für den Abiturjahrgang */
	@Schema(description = "die textuelle Bezeichnung für den Abiturjahrgang", example = "Q1")
	public String bezeichnung = "Allgemein / Vorlage";

	/** Gibt an, ob das Abitur für diesen Jahrgang bereits abgeschlossen ist, d.h. die Schule sich bereits in einem späteren Schuljahr befindet. */
	@Schema(description = "gibt an, ob das Abitur für diesen Jahrgang bereits abgeschlossen ist, d.h. die Schule sich bereits in einem späteren Schuljahr befindet",
			example = "false")
	public boolean istAbgeschlossen = false;

	/** Gibt an, ob für die jeweiligen Halbjahre der Oberstufe bereits eine Blockung in den Leistungsdaten persistiert wurde (0=EF.1, 1=EF.2, ...) */
	@ArraySchema(schema = @Schema(implementation = Boolean.class,
			description = "gibt an, ob für die jeweiligen Halbjahre der Oberstufe bereits eine Blockung in den Leistungsdaten persistiert wurde (0=EF.1, 1=EF.2, ...)"))
	public @NotNull boolean[] istBlockungFestgelegt = new boolean[6];

	/** Gibt an, ob für die jeweiligen Halbjahre der Oberstufe bereits (Quartals-)Noten in den Leistungsdaten vorhanden sind (0=EF.1, 1=EF.2, ...) */
	@ArraySchema(schema = @Schema(implementation = Boolean.class,
			description = "gibt an, ob für die jeweiligen Halbjahre der Oberstufe bereits (Quartals-)Noten in den Leistungsdaten vorhanden sind (0=EF.1, 1=EF.2, ...)"))
	public @NotNull boolean[] existierenNotenInLeistungsdaten = new boolean[6];

}
