package de.nrw.schule.svws.core.data.schueler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Core-DTO beinhaltet die Sprachbelegungsinformationen eines Schülers. 
 */
@TranspilerDTO
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sprachbelegung {

	/** Das einstellige Sprachkürzel des belegten Faches */
	public @NotNull String sprache = "";

	/** Gibt an, an welcher Stelle in der Sprachenfolge die Sprache begonnen wurde */
	public Integer reihenfolge;
	
	/** Der Jahrgang, in dem die Sprache zum ersten mal belegt wurde */
	public String belegungVonJahrgang;
	
	/** Der Abschnitt des Jahrganges, in welchem die Sprache zum ersten mal belegt wurde */
	public Integer belegungVonAbschnitt;
	
	/** Der Jahrgang, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde */
	public String belegungBisJahrgang;
	
	/** Der Abschnitt des Jahrgangs, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde */
	public Integer belegungBisAbschnitt;
	
	/** Das Referenzniveau, welches bisher erreicht wurde */
	public String referenzniveau;

}
