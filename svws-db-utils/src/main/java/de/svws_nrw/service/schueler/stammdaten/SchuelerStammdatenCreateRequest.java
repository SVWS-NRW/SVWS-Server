package de.svws_nrw.service.schueler.stammdaten;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Enthält die Attribute, die beim Anlegen eines Schülers über den {@link SchuelerStammdatenService} angegeben werden
 * können. Die Feldnamen stimmen mit den Stammdaten-Feldern des Core-DTOs {@code SchuelerNeu} überein.
 * Optionale Felder, die nicht gesetzt sind, werden mit ihren Standardwerten angelegt.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchuelerStammdatenCreateRequest {

	/** Der Nachname des Schülers. */
	@Schema(description = "Der Nachname des Schülers.", example = "Mustermann")
	@NotBlank
	@Size(max = 120)
	@NoLeadingOrTrailingWhitespaces
	public String nachname;

	/** Der Vorname des Schülers. */
	@Schema(description = "Der Vorname des Schülers.", example = "Max")
	@NotBlank
	@Size(max = 80)
	@NoLeadingOrTrailingWhitespaces
	public String vorname;

	/** Alle Vornamen des Schülers, sofern es mehrere gibt. */
	@Schema(description = "Alle Vornamen des Schülers.", example = "Max Moritz")
	@Size(max = 255)
	@NoLeadingOrTrailingWhitespaces
	public String alleVornamen;

	/** Die ID des Geschlechts des Schülers. */
	@Schema(description = "Das Geschlecht des Schülers", example = "3")
	@NotNull
	public Integer geschlecht;

	/** Das Geburtsdatum des Schülers im Format yyyy-MM-dd. */
	@Schema(description = "Das Geburtsdatum des Schülers.", example = "2005-10-09")
	@NotBlank
	@ValidDateFormat
	public String geburtsdatum;

	/** Die ID des Status des Schülers. */
	@Schema(description = "Der Status des Schülers.", example = "2")
	@NotNull
	public Integer status;

	/** Das Anmeldedatum des Schülers im Format yyyy-MM-dd. */
	@Schema(description = "Das Anmeldedatum des Schülers.", example = "2005-10-09")
	@Pattern(regexp = "\\S+", message = "darf nicht leer sein")
	@ValidDateFormat
	public String anmeldedatum;

	/** Das Aufnahmedatum des Schülers im Format yyyy-MM-dd. */
	@Schema(description = "Das Aufnahmedatum des Schülers.", example = "2005-10-09")
	@Pattern(regexp = "\\S+", message = "darf nicht leer sein")
	@ValidDateFormat
	public String aufnahmedatum;

	/** Der Beginn des Bildungsgangs des Schülers im Format yyyy-MM-dd. */
	@Schema(description = "Der Beginn des Bildungsgangs des Schülers.", example = "2005-10-09")
	@Pattern(regexp = "\\S+", message = "darf nicht leer sein")
	@ValidDateFormat
	public String beginnBildungsgang;

	/** Die Dauer des Bildungsgangs des Schülers (nur bei BK/SB). */
	@Schema(description = "Dauer des Bildungsgangs des Schülers. (nur bei BK/SB)", example = "0")
	public Integer dauerBildungsgang;

	/** Die ID der Religion des Schülers. */
	@Schema(description = "Die ID der Religion des Schülers.", example = "11")
	@PositiveOrZero
	public Long idReligion;

	/** Die ID des Schuljahresabschnitts, in dem der Schüler angelegt wird. */
	@Schema(description = "Die ID des Schuljahresabschnitts, zu dem die Lernabschnittdaten angelegt werden.", example = "1")
	@NotNull
	public Long idSchuljahresabschnitt;

}
