package de.svws_nrw.core.data.abschluss;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse enthält die Beschreibung aller Fächer mit Zusatzinformationen, 
 * welches zur Abschlussberechnung an der Gesamtschule verwendet werden.
 * Sie ist Bestandteil der JSON-API für die Abschlussberechnung.
 */
@XmlRootElement
@Schema(description="Die Fachinformationen für eine Abschlussberechnung")
@TranspilerDTO
public class GEAbschlussFaecher {

	/** Gibt das Schuljahr an, in welchem die Abschlussberechnung durchgeführt werden soll. */
	@Schema(required = true, description = "Das Schuljahr, in welchem die Abschlussberechnung durchgeführt werden soll", example="2020")	
	public int schuljahr;

	/** Gibt den Abschnitt in des Schuljahres an, in welchem die Abschlussberechnung durchgeführt werden soll. */
	@Schema(required = true, description = "Der Lernabschnitt des Schuljahres, in welchem die Abschlussberechnung durchgeführt werden soll", example="2")	
	public int abschnitt;

	/** Gibt den Jahrgang an, für den die Abschlussberechnung durchgeführt werden soll. */
	@Schema(required = true, description = "Der Jahrgang für den die Abschlussberechnung erstellt werden soll, im allgemeinen 10 - alles andere wird als voriger Jahrgang gewertet", example="10")	
	public String jahrgang;

	/** Eine Liste der einzelnen Fächer, die für die Abschlussberechnung genutzt werden sollen. */
	@Schema(required = true, description = "Die Fachinformationen")	
    public @NotNull List<@NotNull GEAbschlussFach> faecher = new Vector<>();

}
