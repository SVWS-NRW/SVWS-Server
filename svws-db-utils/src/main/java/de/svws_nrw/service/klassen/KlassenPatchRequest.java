package de.svws_nrw.service.klassen;

import java.util.List;

import org.openapitools.jackson.nullable.JsonNullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Enthält die Attribute, die beim Ändern einer Klasse über den {@link KlassenService} angegeben werden können.
 * <p>
 * Nicht änderbar sind die ID, der Schuljahresabschnitt, die Kürzel der Vorgänger- und Folgeklasse sowie die Prüfungsordnung.
 * Diese Attribute sind im Request nicht vorhanden. Werden sie im JSON dennoch übergeben, so werden sie ignoriert.
 * <p>
 * Alle Felder sind als {@link JsonNullable} deklariert, um zwischen "nicht gesetzt" und "explizit null" zu unterscheiden.
 * Nur gesetzte Felder werden auf die Klasse angewendet. Die Feldnamen stimmen mit dem Core-DTO {@code KlassenDaten} überein.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public final class KlassenPatchRequest {

	/** Das Kürzel der Klasse. Es muss im Schuljahresabschnitt eindeutig sein. */
	@Schema(description = "das Kürzel der Klasse", example = "5a")
	public JsonNullable<@NotBlank @Size(max = 15) String> kuerzel = JsonNullable.undefined();

	/** Die ID des Jahrgangs der Klasse. Der Wert null steht für eine jahrgangsübergreifende Klasse. */
	@Schema(description = "die ID des Jahrgangs der Klasse (null für jahrgangsübergreifende Klassen)", example = "4")
	public JsonNullable<Long> idJahrgang = JsonNullable.undefined();

	/** Die Parallelität der Klasse, ein Großbuchstabe von A bis Z. */
	@Schema(description = "die Parallelität der Klasse (A bis Z)", example = "A")
	public JsonNullable<@Pattern(regexp = "[A-Z]") String> parallelitaet = JsonNullable.undefined();

	/** Die Sortierreihenfolge der Klasse. */
	@Schema(description = "die Sortierreihenfolge der Klasse", example = "1")
	public JsonNullable<@NotNull @Min(0) Integer> sortierung = JsonNullable.undefined();

	/** Das Adressmerkmal des Teilstandortes der Klasse. */
	@Schema(description = "das Adressmerkmal des Teilstandortes der Klasse", example = "A")
	public JsonNullable<@NotBlank @Size(max = 1) String> teilstandort = JsonNullable.undefined();

	/** Die Beschreibung der Klasse. */
	@Schema(description = "die Beschreibung der Klasse", example = "Klasse mit bilingualem Zweig")
	public JsonNullable<@Size(max = 150) String> beschreibung = JsonNullable.undefined();

	/** Die ID der Vorgängerklasse aus dem vorigen Schuljahresabschnitt. */
	@Schema(description = "die ID der Vorgängerklasse aus dem vorigen Schuljahresabschnitt", example = "10")
	public JsonNullable<Long> idVorgaengerklasse = JsonNullable.undefined();

	/** Die ID der Folgeklasse aus dem nachfolgenden Schuljahresabschnitt. */
	@Schema(description = "die ID der Folgeklasse aus dem nachfolgenden Schuljahresabschnitt", example = "12")
	public JsonNullable<Long> idFolgeklasse = JsonNullable.undefined();

	/** Die ID der Organisationsform bei einer allgemeinbildenden Schulform. */
	@Schema(description = "die ID der allgemeinbildenden Organisationsform", example = "1")
	public JsonNullable<Long> idAllgemeinbildendOrganisationsform = JsonNullable.undefined();

	/** Die ID der Organisationsform bei einer berufsbildenden Schulform. */
	@Schema(description = "die ID der berufsbildenden Organisationsform", example = "1")
	public JsonNullable<Long> idBerufsbildendOrganisationsform = JsonNullable.undefined();

	/** Die ID der Organisationsform bei einer Schulform der Weiterbildung. */
	@Schema(description = "die ID der Organisationsform am Weiterbildungskolleg", example = "1")
	public JsonNullable<Long> idWeiterbildungOrganisationsform = JsonNullable.undefined();

	/** Die ID der Schulgliederung. Bei null oder -1 wird die Standard-Schulgliederung der Schulform verwendet. */
	@Schema(description = "die ID der Schulgliederung (null oder -1 für die Standard-Schulgliederung)", example = "5")
	public JsonNullable<Long> idSchulgliederung = JsonNullable.undefined();

	/** Die ID der Klassenart. */
	@Schema(description = "die ID der Klassenart", example = "3")
	public JsonNullable<Long> idKlassenart = JsonNullable.undefined();

	/** Die ID der Fachklasse aus dem Fachklassen-Katalog. */
	@Schema(description = "die ID der Fachklasse aus dem Fachklassen-Katalog", example = "100")
	public JsonNullable<Long> idFachklasse = JsonNullable.undefined();

	/** Gibt an, ob die Noteneingabe für die Klasse gesperrt ist. */
	@Schema(description = "gibt an, ob die Noteneingabe gesperrt ist", example = "false")
	public JsonNullable<@NotNull Boolean> noteneingabeGesperrt = JsonNullable.undefined();

	/** Gibt an, ob für die Klasse Ankreuzkompetenzen verwendet werden. */
	@Schema(description = "gibt an, ob Ankreuzkompetenzen verwendet werden", example = "false")
	public JsonNullable<@NotNull Boolean> verwendungAnkreuzkompetenzen = JsonNullable.undefined();

	/** Gibt an, ob die Klasse im Sommersemester beginnt. */
	@Schema(description = "gibt an, ob die Klasse im Sommersemester beginnt", example = "false")
	public JsonNullable<@NotNull Boolean> beginnSommersemester = JsonNullable.undefined();

	/** Gibt an, ob die Klasse an einer Justizvollzugsanstalt (JVA) unterrichtet wird. */
	@Schema(description = "gibt an, ob die Klasse an einer JVA unterrichtet wird", example = "false")
	public JsonNullable<@NotNull Boolean> istJva = JsonNullable.undefined();

	/** Gibt an, ob in der Klasse reformpädagogisch unterrichtet wird. */
	@Schema(description = "gibt an, ob in der Klasse reformpädagogisch unterrichtet wird", example = "false")
	public JsonNullable<@NotNull Boolean> istReformpaedagogik = JsonNullable.undefined();

	/** Die IDs der Lehrkräfte, die die Klasse leiten. Die Reihenfolge der Liste ist die Reihenfolge der Klassenleitungen. */
	@Schema(description = "die IDs der Lehrkräfte, die die Klasse leiten (in der Reihenfolge der Klassenleitungen)", example = "[1, 2]")
	public JsonNullable<@NotNull List<@NotNull Long>> klassenLeitungen = JsonNullable.undefined();

}
