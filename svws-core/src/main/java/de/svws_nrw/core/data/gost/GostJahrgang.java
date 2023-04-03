package de.svws_nrw.core.data.gost;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse stellt einen Core-Types für einem Jahrgang der gymnasialen Oberstufe
 * zur Verfügung. 
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
@XmlRootElement
@Schema(description="ein Abiturjahrgang in der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostJahrgang {
	
	/** Das Jahr, in welchem der Jahrgang Abitur machen wird. -1 bei der Vorlage für neue Abiturjahrgänge. */
	@Schema(description = "das Jahr, in welchem der Jahrgang Abitur machen wird. -1 bei der Vorlage für neue Abiturjahrgänge.", example="2042")
	public int abiturjahr = -1;
	
	/** Die aktuelle Jahrgangstufe, welche dem Abiturjahrgang zugeordnet ist. */
	@Schema(description = "die aktuelle Jahrgangstufe, welche dem Abiturjahrgang zugeordnet ist", example="Q1")
	public String jahrgang = null;
	
	/** Die textuelle Bezeichnung für den Abiturjahrgang */
	@Schema(description = "die textuelle Bezeichnung für den Abiturjahrgang", example="Q1")
	public String bezeichnung = "Allgemein / Vorlage";
	
	/** Gibt an, ob das Abitur für diesen Jahrgang berets abgeschlossen ist, d.h. die Schule sich bereits in einem späteren Schuljahr befindet. */
	@Schema(description = "gibt an, ob das Abitur für diesen Jahrgang bereits abgeschlossen ist, d.h. die Schule sich bereits in einem späteren Schuljahr befindet", example="false")
	public boolean istAbgeschlossen = false;

}
