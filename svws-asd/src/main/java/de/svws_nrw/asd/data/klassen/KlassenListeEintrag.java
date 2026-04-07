package de.svws_nrw.asd.data.klassen;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die minimalen Daten einer Klasse für die Anzeige in eine Liste.
 */
@XmlRootElement
@Schema(description = "Die minimalen Daten einer Klasse für die Anzeige in eine Liste.")
@TranspilerDTO
public class KlassenListeEintrag {

	/** Die ID der Klasse. */
	@Schema(description = "die ID der Klasse", example = "4709")
	public long id;

	/** Die ID des Schuljahresabschnittes. */
	@Schema(description = "die ID des Schuljahresabschnittes", example = "14")
	public long idSchuljahresabschnitt;

	/** Die ID des Jahrgangs. */
	@Schema(description = "die ID des Jahrgangs", example = "42")
	public Long idJahrgang;

	/** Das Kürzel der Klasse. */
	@Schema(description = "das Kürzel der Klasse", example = "06b", nullable = true)
	public String kuerzel;

	/** Eine zusätzliche Beschreibung zu der Klasse */
	@Schema(description = "Eine zusätzliche Beschreibung zu der Klasse", example = "09b - bilinguale Klasse")
	public @NotNull String beschreibung = "";

	/** Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z). */
	@Schema(description = "das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z)", example = "B", nullable = true)
	public String parallelitaet;

}
