package de.svws_nrw.service.schule.logoverwaltung;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LogoCreateRequest {

	/** Die Kennung des Logos {@link ReportingBildDefinition}. */
	@Schema(description = "die Kennung des Logos", example = "DIN5008_BRIEFKOPF")
	@NotBlank
	@Size(max = 100)
	public String kennung;

	/** Das Logo als Bild im Base64-Format. */
	@Schema(description = "das Logo als Bild im Base64-Format")
	@NotBlank
	public String logoBase64;

	/** Datum, wann das Logo hinzugefügt wurde. */
	@Schema(description = "Datum, wann das Logo hinzugefügt wurde", example = "2026-04-10")
	public String hinzugefuegtAm = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
}
