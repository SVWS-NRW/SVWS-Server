package de.svws_nrw.service.uv.klassen;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Klasse.
 */
@Schema(description = "die Informationen zum Erstellen einer UV-Klasse.")
public class UvKlasseCreateRequest {

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts.")
	@NotNull(message = "Die ID des Planungsabschnitts muss gesetzt werden.")
	public Long idPlanungsabschnitt;

	/** Die ID des Schuljahresabschnitts. */
	@Schema(description = "die ID des Schuljahresabschnitts.")
	@NotNull(message = "Die ID des Schuljahresabschnitts muss gesetzt werden.")
	public Long idSchuljahresabschnitt;

	/** Die Bezeichnung. */
	@Schema(description = "die Bezeichnung.")
	@Size(max = 150, message = "Die Bezeichnung darf maximal 150 Zeichen lang sein.")
	public String bezeichnung;

	/** Das Kürzel. */
	@Schema(description = "das Kürzel.")
	@NotNull(message = "Das Kürzel muss gesetzt werden.")
	@Size(max = 15, message = "Das Kürzel darf maximal 15 Zeichen lang sein.")
	public String kuerzel;

	/** Die Parallelität. */
	@Schema(description = "die Parallelität.")
	@NotNull(message = "Die Parallelität muss gesetzt werden.")
	@Size(max = 2, message = "Die Parallelität darf maximal 2 Zeichen lang sein.")
	public String parallelitaet;

	/** Die ID der Stundentafel. */
	@Schema(description = "die ID der Stundentafel.")
	public Long idStundentafel;

	/** Die ID der Schülergruppe. */
	@Schema(description = "die ID der Schülergruppe.")
	@NotNull(message = "Die ID der Schülergruppe muss gesetzt werden.")
	public Long idSchuelergruppe;

	/** Das Organisationsform-Kürzel. */
	@Schema(description = "das Organisationsform-Kürzel.")
	@Size(max = 1, message = "Das Organisationsform-Kürzel darf maximal 1 Zeichen lang sein.")
	public String orgFormKrz;

	/** Die ID der Fachklasse. */
	@Schema(description = "die ID der Fachklasse.")
	public Long idFachklasse;

	/** Die ASD-Schulformnummer. */
	@Schema(description = "die ASD-Schulformnummer.")
	@Size(max = 3, message = "Die ASD-Schulformnummer darf maximal 3 Zeichen lang sein.")
	public String asdSchulformNr;

}
