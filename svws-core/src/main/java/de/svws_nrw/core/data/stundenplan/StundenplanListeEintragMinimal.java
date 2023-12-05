package de.svws_nrw.core.data.stundenplan;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Minimalinformationen zum Erzeugen eines neuen Stundenplans für eine Liste von Stundenplänen.
 */
@XmlRootElement
@Schema(description = "die Minimalinformationen für einen neuen Eintrag in der Liste von Stundenplänen.")
@TranspilerDTO
public class StundenplanListeEintragMinimal {

	/** Die ID des Schuljahresabschnitts des Stundenplans. */
	@Schema(description = "die ID des Schuljahresabschnitts des Stundenplans", example = "1")
	public long idSchuljahresabschnitt = -1;

	/** Die textuelle Beschreibung des Stundenplans. */
	@Schema(description = "die textuelle Beschreibung des Stundenplans", example = "Stundenplan zum Schuljahresanfang")
	public @NotNull String bezeichnung = "";

	/** Das Datum, ab dem der Stundenpland gültig ist. */
	@Schema(description = "das Datum, ab dem der Stundenpland gültig ist", example = "1899-1-1")
	public @NotNull String gueltigAb = "";

}
