package de.svws_nrw.service.schueler.schulbesuch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SchuelerBisherigeSchulePatchRequest {

	/** Die ID der Schule. */
	@Schema(description = "Die ID der Schule", example = "178947")
	@NotNull
	public JsonNullable<Long> idSchule = JsonNullable.undefined();

	/** Der Schlüssel des Bildungsganges/Schulgliederung an der Schule. */
	@Schema(description = "Der Schlüssel des Bildungsganges/Schulgliederung an der Schule", example = "***")
	@Size(max = 5)
	public JsonNullable<String> schluesselSchulgliederung = JsonNullable.undefined();

	/** Die ID des Grundes für die Entlassung von der Schule. */
	@Schema(description = "die ID des Grundes für die Entlassung von der Schule", example = "2")
	public JsonNullable<Long> idEntlassgrund = JsonNullable.undefined();

	/** Die ID des Abschlusses, welcher an der Schule erworben wurde. */
	@Schema(description = "die ID des Abschlusses, welcher an der Schule erworben wurde", example = "OA")
	public JsonNullable<String> idAbschlussart = JsonNullable.undefined();

	/** Die ID der Organisationsform der Schule (z.B. für Halbtagsunterricht). */
	@Schema(description = "die ID der Organisationsform der Schule (z.B. für Halbtagsunterricht)", example = "1")
	public JsonNullable<String> idOrganisationsform = JsonNullable.undefined();

	/** Das Datum, ab dem die Schule besucht wurde. */
	@Schema(description = "das Datum, ab dem die Schule besucht wurde", example = "1907-12-01")
	@ValidDateFormat
	public JsonNullable<String> datumVon = JsonNullable.undefined();

	/** Das Datum, bis wann die Schule besucht wurde. */
	@Schema(description = "das Datum, bis wann die Schule besucht wurde", example = "1908-12-01")
	@ValidDateFormat
	public JsonNullable<String> datumBis = JsonNullable.undefined();

	/** Der Jahrgang, ab dem die Schule besucht wurde. */
	@Schema(description = "der Jahrgang, ab dem die Schule besucht wurde", example = "07")
	@Size(max = 2)
	public JsonNullable<String> jahrgangVon = JsonNullable.undefined();

	/** Der Jahrgang, bis zu dem die Schule besucht wurde. */
	@Schema(description = "der Jahrgang, bis zu dem die Schule besucht wurde", example = "07")
	@Size(max = 2)
	public JsonNullable<String> jahrgangBis = JsonNullable.undefined();

}
