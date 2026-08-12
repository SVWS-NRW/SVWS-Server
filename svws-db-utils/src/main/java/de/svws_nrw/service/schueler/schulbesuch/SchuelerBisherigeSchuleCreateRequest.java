package de.svws_nrw.service.schueler.schulbesuch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SchuelerBisherigeSchuleCreateRequest {

	/** Die ID des Schülers. */
	@Schema(description = "Die ID des Schülers.", example = "178947")
	@NotNull
	public Long idSchueler;

	/** Die ID der Schule. */
	@Schema(description = "Die ID der Schule", example = "178947")
	@NotNull
	public Long idSchule;

	/** Der Schlüssel des Bildungsganges/Schulgliederung an der Schule. */
	@Schema(description = "Der Schlüssel des Bildungsganges/Schulgliederung an der Schule", example = "***")
	@Size(max = 5)
	public String schluesselSchulgliederung;

	/** Die ID des Grundes für die Entlassung von der Schule. */
	@Schema(description = "die ID des Grundes für die Entlassung von der Schule", example = "2")
	public Long idEntlassgrund;

	/** Die ID des Abschlusses, welcher an der Schule erworben wurde. */
	@Schema(description = "die ID des Abschlusses, welcher an der Schule erworben wurde", example = "OA")
	public String idAbschlussart;

	/** Die ID der Organisationsform der Schule (z.B. für Halbtagsunterricht). */
	@Schema(description = "die ID der Organisationsform der Schule (z.B. für Halbtagsunterricht)", example = "1")
	public String idOrganisationsform;

	/** Das Datum, ab dem die Schule besucht wurde. */
	@Schema(description = "das Datum, ab dem die Schule besucht wurde", example = "1907-12-01")
	@ValidDateFormat
	public String datumVon;

	/** Das Datum, bis wann die Schule besucht wurde. */
	@Schema(description = "das Datum, bis wann die Schule besucht wurde", example = "1908-12-01")
	@ValidDateFormat
	public String datumBis;

	/** Der Jahrgang, ab dem die Schule besucht wurde. */
	@Schema(description = "der Jahrgang, ab dem die Schule besucht wurde", example = "07")
	@Size(max = 2)
	public String jahrgangVon;

	/** Der Jahrgang, bis zu dem die Schule besucht wurde. */
	@Schema(description = "der Jahrgang, bis zu dem die Schule besucht wurde", example = "07")
	@Size(max = 2)
	public String jahrgangBis;

}
