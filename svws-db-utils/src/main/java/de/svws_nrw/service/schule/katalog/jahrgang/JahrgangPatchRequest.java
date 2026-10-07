package de.svws_nrw.service.schule.katalog.jahrgang;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public class JahrgangPatchRequest {

	/** Das schulinterne Kürzel des Jahrgangs */
	@Schema(description = "Das schulinterne Kürzel des Jahrgangs", example = "05")
	public JsonNullable<@NotBlank @Size(max = 20) @NoLeadingOrTrailingWhitespaces String> kuerzel = JsonNullable.undefined();

	/** Die schulinterne Bezeichnung des Jahrgangs */
	@Schema(description = "Die schulinterne Bezeichnung des Jahrgangs", example = "5. Jahrgang")
	public JsonNullable<@NotBlank @Size(max = 100) @NoLeadingOrTrailingWhitespaces String> bezeichnung = JsonNullable.undefined();

	/** Die schulinterne Kurzbezeichnung des Jahrgangs */
	@Schema(description = "Die schulinterne Kurzbezeichnung des Jahrgangs", example = "05")
	public JsonNullable<@Size(max = 2) String> kurzbezeichnung = JsonNullable.undefined();

	/** Die ID des ASD-Jahrgangs (CoreType) */
	@Schema(description = "Die ID des ASD-Jahrgangs (CoreType)", example = "5000001")
	public JsonNullable<@NotNull Long> idJahrgang = JsonNullable.undefined();

	/** Die Sortierreihenfolge des Jahrgangs */
	@Schema(description = "Die Sortierreihenfolge des Jahrgangs", example = "1")
	public JsonNullable<@PositiveOrZero Integer> sortierung = JsonNullable.undefined();

	/** Die ID der Schulgliederung (CoreType) */
	@Schema(description = "Die ID der Schulgliederung (CoreType)", example = "1001000")
	public JsonNullable<Long> idSchulgliederung = JsonNullable.undefined();

	/** Die ID des Folgejahrgangs */
	@Schema(description = "Die ID des Folgejahrgangs", example = "4712")
	public JsonNullable<Long> idFolgejahrgang = JsonNullable.undefined();

	/** Die ID der Bildungsstufe (CoreType) */
	@Schema(description = "Die ID der Bildungsstufe (CoreType)", example = "1")
	public JsonNullable<Long> idBildungsstufe = JsonNullable.undefined();

	/** Die Anzahl der Restabschnitte bis zum Abschluss */
	@Schema(description = "Die Anzahl der Restabschnitte bis zum Abschluss", example = "12")
	public JsonNullable<@Min(0) @Max(41) Integer> anzahlRestabschnitte = JsonNullable.undefined();

	/** Gibt an, ob der Jahrgang in der Anwendung sichtbar ist */
	@Schema(description = "Gibt an, ob der Jahrgang in der Anwendung sichtbar ist", example = "true")
	public JsonNullable<@NotNull Boolean> istSichtbar = JsonNullable.undefined();

	/** Die ID des Schuljahresabschnitts, ab dem der Jahrgang gültig ist (null = ab dem ersten Abschnitt) */
	@Schema(description = "Die ID des Schuljahresabschnitts, ab dem der Jahrgang gültig ist (null = ab dem ersten Abschnitt)", example = "null")
	public JsonNullable<Long> gueltigVon = JsonNullable.undefined();

	/** Die ID des Schuljahresabschnitts, bis zu dem der Jahrgang gültig ist (null = Ende offen) */
	@Schema(description = "Die ID des Schuljahresabschnitts, bis zu dem der Jahrgang gültig ist (null = Ende offen)", example = "null")
	public JsonNullable<Long> gueltigBis = JsonNullable.undefined();

}
