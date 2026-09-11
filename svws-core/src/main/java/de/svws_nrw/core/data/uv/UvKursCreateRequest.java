package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Die Klasse beschreibt das DTO für das Erstellen eines UV-Kurses.
 */
@TranspilerDTO
@Schema(description = "die Informationen zum Erstellen eines UV-Kurses.")
public class UvKursCreateRequest {

	/** Die negative temporäre ID beim Sammelimport. */
	public @NotNull Long id = 0L;

	/** Die ID des Planungsabschnitts, in dem der Kurs gilt. */
	@Schema(description = "die ID des Planungsabschnitts, in dem der Kurs gilt.")
	@NotNull(message = "Die ID des Planungsabschnitts muss gesetzt werden.")
	public Long idPlanungsabschnitt = 0L;

	/** Die ID des Schuljahresabschnitts. */
	@Schema(description = "die ID des Schuljahresabschnitts.")
	@NotNull(message = "Die ID des Schuljahresabschnitts muss gesetzt werden.")
	public Long idSchuljahresabschnitt = 0L;

	/** Die ID des Faches. */
	@Schema(description = "die ID des Faches.")
	@NotNull(message = "Die ID des Faches muss gesetzt werden.")
	public Long idFach = 0L;

	/** Das Kürzel der Kursart. */
	@Schema(description = "das Kürzel der Kursart.")
	@NotNull(message = "Die Kursart muss gesetzt werden.")
	@NotBlank(message = "Die Kursart darf nicht leer sein.")
	@Size(max = 10, message = "Die Kursart darf maximal 10 Zeichen lang sein.")
	public String kursart = "";

	/** Die Kursnummer. */
	@Schema(description = "die Kursnummer.")
	@NotNull(message = "Die Kursnummer muss gesetzt werden.")
	public Integer kursnummer = 0;

	/** Die ID der Schülergruppe. */
	@Schema(description = "die ID der Schülergruppe.")
	@NotNull(message = "Die ID der Schülergruppe muss gesetzt werden.")
	public Long idSchuelergruppe = 0L;

}
