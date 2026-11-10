package de.svws_nrw.service.kurse;

import java.util.List;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Die Daten zum Ändern eines Kurses. Nur gesetzte Felder werden geändert. Die ID und der Schuljahresabschnitt
 * sind nicht änderbar. Ein Wert null ist nur bei der Lehrer-ID, der Schulnummer, der Zeugnisbezeichnung und den
 * Wochenstunden des Lehrers zulässig.
 */
@Schema(description = "Die Patch-Daten für einen Kurs.")
public class KursPatchRequest {

	/** Das Kürzel des Kurses */
	@Schema(description = "das Kürzel des Kurses", example = "D-GK1")
	public JsonNullable<@NotNull @NotEmpty @Size(max = 20) String> kuerzel = JsonNullable.undefined();

	/** Die allgemeine Kursart */
	@Schema(description = "die allgemeine Kursart", example = "GK")
	public JsonNullable<@NotNull @Size(max = 10) String> kursartAllg = JsonNullable.undefined();

	/** Die ID des Faches */
	@Schema(description = "die ID des Faches", example = "42")
	public JsonNullable<@NotNull Long> idFach = JsonNullable.undefined();

	/** Die ID des Lehrers */
	@Schema(description = "die ID des Lehrers", example = "17")
	public JsonNullable<Long> idLehrer = JsonNullable.undefined();

	/** Die Sortierung des Kurses */
	@Schema(description = "die Sortierung des Kurses", example = "32000")
	public JsonNullable<@NotNull @PositiveOrZero Integer> sortierung = JsonNullable.undefined();

	/** Gibt an, ob der Kurs sichtbar ist */
	@Schema(description = "gibt an, ob der Kurs sichtbar ist", example = "true")
	public JsonNullable<@NotNull Boolean> istSichtbar = JsonNullable.undefined();

	/** Die Wochenstunden des Kurses */
	@Schema(description = "die Wochenstunden des Kurses", example = "3")
	public JsonNullable<@NotNull @Min(0) @Max(40) Integer> wochenstunden = JsonNullable.undefined();

	/** Die Wochenstunden des Lehrers, null wird zu 0.0 */
	@Schema(description = "die Wochenstunden des Lehrers, null wird zu 0.0", example = "3.0")
	public JsonNullable<Double> wochenstundenLehrer = JsonNullable.undefined();

	/** Gibt an, ob es sich um Epochalunterricht handelt */
	@Schema(description = "gibt an, ob es sich um Epochalunterricht handelt", example = "false")
	public JsonNullable<@NotNull Boolean> istEpochalunterricht = JsonNullable.undefined();

	/** Die Bezeichnung des Kurses auf dem Zeugnis */
	@Schema(description = "die Bezeichnung des Kurses auf dem Zeugnis", example = "Deutsch")
	public JsonNullable<@Size(max = 130) String> bezeichnungZeugnis = JsonNullable.undefined();

	/** Die ID der Fortschreibungsart */
	@Schema(description = "die ID der Fortschreibungsart", example = "0")
	public JsonNullable<@NotNull @Min(0) @Max(3) Integer> idKursFortschreibungsart = JsonNullable.undefined();

	/** Die Schulnummer der Schule, an der der Kurs stattfindet */
	@Schema(description = "die Schulnummer der Schule, an der der Kurs stattfindet", example = "123456")
	public JsonNullable<@Min(100000) @Max(999999) Integer> schulnummer = JsonNullable.undefined();

	/** Die Nummern der Schienen */
	@Schema(description = "die Nummern der Schienen")
	public JsonNullable<@NotNull List<@NotNull @PositiveOrZero Integer>> schienen = JsonNullable.undefined();

	/** Die IDs der Jahrgänge */
	@Schema(description = "die IDs der Jahrgänge")
	public JsonNullable<@NotNull List<@NotNull Long>> idJahrgaenge = JsonNullable.undefined();

}
