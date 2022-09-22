package de.nrw.schule.svws.core.data.stundenplan;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert den Eintrag für einen Stundenplan in einer Liste von Stundenplänen.  
 */
@XmlRootElement
@Schema(description="der Eintrag in der Liste von Stundenplänen.")
@TranspilerDTO
public class StundenplanListeEintrag {

	/** Die ID des Stundenplans. */
	@Schema(required = true, description = "die ID des Stundenplans", example="815")
	public long id = -1;

	/** Die textuelle Beschreibung des Stundenplans. */
	@Schema(required = false, description = "die textuelle Beschreibung des Stundenplans", example="Stundenplan zum Schuljahresanfang")
	public @NotNull String bezeichnung = "";
	
	/** Die ID des Schuljahresabschnitts des Stundenplans. */
	@Schema(required = true, description = "die ID des Schuljahresabschnitts des Stundenplans", example="7")
	public long idSchuljahresabschnitt = -1;
	
	/** Das Schuljahr, in welchem der Stundenplan gültig ist. */
	@Schema(required = true, description = "das Schuljahr, in welchem der Stundenplan gültig ist", example="2027")
	public int schuljahr = -1;

	/** Der Abschnitt, in welchem der Stundenplan gültig ist (z.B. 2. Halbjahr oder 3. Quartal). */
	@Schema(required = true, description = "der Abschnitt, in welchem der Stundenplan gültig ist (z.B. 2. Halbjahr oder 3. Quartal)", example="2")
	public int abschnitt = -1;
	
	/** Das Datum, ab dem der Stundenpland gültig ist. */
	@Schema(required = false, description = "das Datum, ab dem der Stundenpland gültig ist", example="1.1.1899")
	public @NotNull String gueltigAb = "";

	/** Das Datum, bis wann der Stundenplan gültig ist. */
	@Schema(required = false, description = "das Datum, bis wann der Stundenplan gültig ist", example="31.7.3218")
	public @NotNull String gueltigBis = "";

}
