package de.svws_nrw.service.uv.stundentafeln;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Die Klasse beschreibt das Patch-DTO für ein UV-Stundentafel-Fach.
 */
@Schema(description = "Patch-Daten für ein UV-Stundentafel-Fach.")
public class UvStundentafelFachPatchRequest {

	/** Die ID des UV-Stundentafel-Fachs. */
	@Schema(description = "die ID des UV-Stundentafel-Fachs", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Der Abschnitt. */
	@Schema(description = "Der Abschnitt.")
	public JsonNullable<@NotNull(message = "Der Abschnitt darf nicht null sein.") Integer> abschnitt = JsonNullable.undefined();

	/** Die ID des Faches. */
	@Schema(description = "Die ID des Faches.")
	public JsonNullable<@NotNull(message = "Die ID des Fachs darf nicht null sein.") Long> idFach = JsonNullable.undefined();

	/** Die Anzahl der Wochenstunden. */
	@Schema(description = "Die Anzahl der Wochenstunden.")
	public JsonNullable<@NotNull(message = "Die Wochenstunden dürfen nicht null sein.") Double> wochenstunden = JsonNullable.undefined();

	/** Anzahl der davon enthaltenen Ergänzungsstunden. */
	@Schema(description = "Anzahl der davon enthaltenen Ergänzungsstunden.")
	public JsonNullable<@NotNull(message = "Die Ergänzungsstunden dürfen nicht null sein.") Double> davonErgaenzungsstunden = JsonNullable.undefined();

}
