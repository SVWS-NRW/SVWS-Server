package de.svws_nrw.service.schule.teilstandort;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.svws_nrw.validation.constraints.NoWhitespaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request-Klasse zum Anlegen eines Teilstandortes.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeilstandortCreateRequest {

	/** Das Adressmerkmal des Teilstandortes (A...Z). */
	@Schema(description = "Das Adressmerkmal des Teilstandortes (A...Z)", example = "A")
	@NotBlank
	@Size(min = 1, max = 1)
	@NoWhitespaces
	public String adrMerkmal;

	/** Die Postleitzahl des Teilstandortes. */
	@Schema(description = "Die Postleitzahl des Teilstandortes", example = "40213")
	@Size(max = 10)
	public String plz;

	/** Der Ort des Teilstandortes. */
	@Schema(description = "Der Ort des Teilstandortes", example = "Düsseldorf")
	@Size(max = 50)
	public String ort;

	/** Der Straßenname des Teilstandortes. */
	@Schema(description = "Der Straßenname des Teilstandortes", example = "Muster Str.")
	@Size(max = 55)
	public String strassenname;

	/** Die Hausnummer des Teilstandortes. */
	@Schema(description = "Die Hausnummer des Teilstandortes", example = "4")
	@Size(max = 10)
	public String hausNr;

	/** Der Hausnummernzusatz des Teilstandortes. */
	@Schema(description = "Der Hausnummernzusatz des Teilstandortes", example = "a-c")
	@Size(max = 30)
	public String hausNrZusatz;

	/** Eine Bemerkung zum Teilstandort. */
	@Schema(description = "Eine Bemerkung zum Teilstandort", example = "Hauptgebäude")
	@Size(max = 50)
	public String bemerkung;

	/** Das Kürzel des Teilstandortes. */
	@Schema(description = "Das Kürzel des Teilstandortes", example = "Hauptst.")
	@Size(max = 30)
	public String kuerzel;

}
