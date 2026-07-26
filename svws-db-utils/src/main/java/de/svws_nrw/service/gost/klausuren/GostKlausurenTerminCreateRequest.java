package de.svws_nrw.service.gost.klausuren;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen eines GOSt-Klausurtermins.
 */
@Schema(description = "die Informationen zum Erstellen eines GOSt-Klausurtermins.")
public class GostKlausurenTerminCreateRequest {

	/** Die ID des Schuljahresabschnitts. */
	@Schema(description = "die ID des Schuljahresabschnitts")
	@NotNull(message = "Der Schuljahresabschnitt muss gesetzt werden.")
	public Long idSchuljahresabschnitt;

	/** Der Abiturjahrgang. */
	@Schema(description = "der Abiturjahrgang")
	@NotNull(message = "Der Abiturjahrgang muss gesetzt werden.")
	public Integer abiturjahrgang;

	/** Das GOSt-Halbjahr. */
	@Schema(description = "das GOSt-Halbjahr")
	@NotNull(message = "Das GOSt-Halbjahr muss gesetzt werden.")
	public Integer halbjahr;

	/** Das Quartal. */
	@Schema(description = "das Quartal")
	@NotNull(message = "Das Quartal muss gesetzt werden.")
	public Integer quartal;

	/** Gibt an, ob es sich um einen Haupttermin handelt. */
	@Schema(description = "gibt an, ob es sich um einen Haupttermin handelt")
	public Boolean istHaupttermin;

	/** Gibt an, ob Nachschreiber zugelassen sind. */
	@Schema(description = "gibt an, ob Nachschreiber zugelassen sind")
	public Boolean nachschreiberZugelassen;

	/** Das Datum. */
	@Schema(description = "das Datum")
	public JsonNullable<String> datum = JsonNullable.undefined();

	/** Die Startzeit in Minuten. */
	@Schema(description = "die Startzeit in Minuten")
	public JsonNullable<Integer> startzeit = JsonNullable.undefined();

	/** Die Bezeichnung. */
	@Schema(description = "die Bezeichnung")
	public JsonNullable<String> bezeichnung = JsonNullable.undefined();

	/** Die Bemerkung. */
	@Schema(description = "die Bemerkung")
	public JsonNullable<String> bemerkung = JsonNullable.undefined();

}
