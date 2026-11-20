package de.svws_nrw.service.schueler.kaoa;

import org.openapitools.jackson.nullable.JsonNullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Patch-Request-DTO für KAoA-Daten eines Schülers. Alle Felder sind als {@link JsonNullable} deklariert, um zwischen
 * "nicht gesetzt" (undefined) und "explizit gesetzt" unterscheiden zu können. Nur gesetzte Felder werden angewendet.
 * {@code null} ist nur bei den optionalen Feldern Anschlussoption, Berufsfeld, Ebene 4 und Bemerkung erlaubt. Die ID ist nicht patchbar.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SchuelerKAoAPatchRequest {

	/** Der Schuljahresabschnitt, zu dem die KAoA-Daten gehören. */
	@Schema(description = "Der Schuljahresabschnitt, zu dem die KAoA-Daten gehören.", example = "42")
	public JsonNullable<@NotNull Long> idSchuljahresabschnitt = JsonNullable.undefined();

	/** Die ID des Jahrgangs des Schülers. */
	@Schema(description = "Die ID des Jahrgangs des Schülers, zu dem die KAoA-Daten gehören.", example = "42")
	public JsonNullable<@NotNull Long> idJahrgang = JsonNullable.undefined();

	/** Die ID der Kategorie. */
	@Schema(description = "Die Kategorie der KAoA-Daten.", example = "14")
	public JsonNullable<@NotNull Long> idKategorie = JsonNullable.undefined();

	/** Die ID des Merkmals. */
	@Schema(description = "Das Merkmal der KAoA-Daten.", example = "69")
	public JsonNullable<@NotNull Long> idMerkmal = JsonNullable.undefined();

	/** Die ID des Zusatzmerkmals. */
	@Schema(description = "Das Zusatzmerkmal der KAoA-Daten.", example = "117")
	public JsonNullable<@NotNull Long> idZusatzmerkmal = JsonNullable.undefined();

	/** Die ID der Anschlussoption. */
	@Schema(description = "Die Anschlussoption der KAoA-Daten.", example = "22")
	public JsonNullable<Long> idAnschlussoption = JsonNullable.undefined();

	/** Die ID des Berufsfelds. */
	@Schema(description = "Das Berufsfeld der KAoA-Daten.", example = "12")
	public JsonNullable<Long> idBerufsfeld = JsonNullable.undefined();

	/** Die ID der Ebene 4. */
	@Schema(description = "Die Ebene 4 der KAoA-Daten.", example = "4")
	public JsonNullable<Long> idEbene4 = JsonNullable.undefined();

	/** Die Bemerkung zu den KAoA-Daten. */
	@Schema(description = "Die Bemerkung zu den KAoA-Daten.", example = "text")
	public JsonNullable<@Size(max = SchuelerKAoAValidator.MAX_LAENGE_BEMERKUNG,
			message = "Bemerkung darf nicht mehr als {max} Zeichen beinhalten.") String> bemerkung = JsonNullable.undefined();

}
