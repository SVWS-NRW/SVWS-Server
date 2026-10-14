package de.svws_nrw.service.lehrer.personaldaten;

import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * LehrerPersonaldatenPatchRequest
 */
public class LehrerPersonaldatenPatchRequest {

	/** Der vordere Teil der NRW-weit eindeutigen Ident-Nummer - setzt sich normalerweise aus Geburtsdatum und Geschlecht (3/4) zusammen, kann in Einzelfällen aber von diesem Schema abweichen. */
	@Schema(description = "Der vordere Teil der NRW-weit eindeutigen Ident-Nummer - setzt sich normalerweise aus Geburtsdatum und Geschlecht (3/4) zusammen, kann in Einzelfällen aber von diesem Schema abweichen.", example = "1708593")
	public JsonNullable<@Size(max = 10) String> identNrTeil1 = JsonNullable.undefined();

	/** Der hintere Teil der Ident-Nummer – wird üblicherweise NRW-weit fortlaufend vergeben. */
	@Schema(description = "Der hintere Teil der Ident-Nummer – wird üblicherweise NRW-weit fortlaufend vergeben.")
	public JsonNullable<@Size(max = 5) String> identNrTeil2SerNr = JsonNullable.undefined();

	/** Die Personalaktennummer des Lehrers für den Export zu GPC. */
	@Schema(description = "Die Personalaktennummer des Lehrers für den Export zu GPC.")
	public JsonNullable<@Size(max = 20) String> personalaktennummer = JsonNullable.undefined();

	/** Die Personalnummer des LBV. */
	@Schema(description = "Die Personalnummer des LBV.")
	public JsonNullable<@Size(max = 15) String> lbvPersonalnummer = JsonNullable.undefined();

	/** Der zur Personalnummer gehörige Vergütungsschlüssel. */
	@Schema(description = "Der zur Personalnummer gehörige Vergütungsschlüssel.", example = "2")
	public JsonNullable<@Size(max = 1) String> lbvVerguetungsschluessel = JsonNullable.undefined();

	/** Das Datum, wann der Lehrer an die Schule gekommen ist. */
	@Schema(description = "Das Datum, wann der Lehrer an die Schule gekommen ist.", example = "1911-11-11")
	public JsonNullable<@NotBlank @ValidDateFormat String> zugangsdatum = JsonNullable.undefined();

	/** Der Grund für den Zugang des Lehrers - siehe Statistik-Katalog. */
	@Schema(description = "Der Grund für den Zugang des Lehrers - siehe Statistik-Katalog.", example = "WIEDER")
	public JsonNullable<@NotBlank @Size(max = 10) String> zugangsgrund = JsonNullable.undefined();

	/** Das Datum, wann der Lehrer an die Schule verlassen hat. */
	@Schema(description = "Das Datum, wann der Lehrer an die Schule verlassen hat.", example = "1920-11-12")
	public JsonNullable<@NotBlank @ValidDateFormat String> abgangsdatum = JsonNullable.undefined();

	/** Der Grund für den Abgang des Lehrers - siehe Statistik-Katalog. */
	@Schema(description = "Der Grund für den Abgang des Lehrers - siehe Statistik-Katalog.", example = "RUHEST")
	public JsonNullable<@NotBlank @Size(max = 10) String> abgangsgrund = JsonNullable.undefined();

}
