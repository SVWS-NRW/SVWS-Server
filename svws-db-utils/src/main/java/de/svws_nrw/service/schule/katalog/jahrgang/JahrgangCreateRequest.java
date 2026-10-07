package de.svws_nrw.service.schule.katalog.jahrgang;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class JahrgangCreateRequest {

	/** Das schulinterne Kürzel des Jahrgangs */
	@Schema(description = "Das schulinterne Kürzel des Jahrgangs", example = "05")
	@NotBlank
	@Size(max = 20)
	@NoLeadingOrTrailingWhitespaces
	public String kuerzel;

	/** Die schulinterne Bezeichnung des Jahrgangs */
	@Schema(description = "Die schulinterne Bezeichnung des Jahrgangs", example = "5. Jahrgang")
	@NotBlank
	@Size(max = 100)
	@NoLeadingOrTrailingWhitespaces
	public String bezeichnung;

	/** Die schulinterne Kurzbezeichnung des Jahrgangs */
	@Schema(description = "Die schulinterne Kurzbezeichnung des Jahrgangs", example = "05")
	@Size(max = 2)
	public String kurzbezeichnung;

	/** Die ID des ASD-Jahrgangs (CoreType) */
	@Schema(description = "Die ID des ASD-Jahrgangs (CoreType)", example = "5000001")
	@NotNull
	public Long idJahrgang;

	/** Die Sortierreihenfolge des Jahrgangs */
	@Schema(description = "Die Sortierreihenfolge des Jahrgangs", example = "1")
	@PositiveOrZero
	public Integer sortierung;

	/** Die ID der Schulgliederung (CoreType) */
	@Schema(description = "Die ID der Schulgliederung (CoreType)", example = "1001000")
	public Long idSchulgliederung;

	/** Die ID des Folgejahrgangs */
	@Schema(description = "Die ID des Folgejahrgangs", example = "4712")
	public Long idFolgejahrgang;

	/** Die ID der Bildungsstufe (CoreType) */
	@Schema(description = "Die ID der Bildungsstufe (CoreType)", example = "1")
	public Long idBildungsstufe;

	/** Die Anzahl der Restabschnitte bis zum Abschluss */
	@Schema(description = "Die Anzahl der Restabschnitte bis zum Abschluss", example = "12")
	@Min(0)
	@Max(41)
	public Integer anzahlRestabschnitte;

	/** Gibt an, ob der Jahrgang in der Anwendung sichtbar ist */
	@Schema(description = "Gibt an, ob der Jahrgang in der Anwendung sichtbar ist", example = "true")
	public boolean istSichtbar;

	/** Die ID des Schuljahresabschnitts, ab dem der Jahrgang gültig ist (null = ab dem ersten Abschnitt) */
	@Schema(description = "Die ID des Schuljahresabschnitts, ab dem der Jahrgang gültig ist (null = ab dem ersten Abschnitt)", example = "null")
	public Long gueltigVon;

	/** Die ID des Schuljahresabschnitts, bis zu dem der Jahrgang gültig ist (null = Ende offen) */
	@Schema(description = "Die ID des Schuljahresabschnitts, bis zu dem der Jahrgang gültig ist (null = Ende offen)", example = "null")
	public Long gueltigBis;

}
