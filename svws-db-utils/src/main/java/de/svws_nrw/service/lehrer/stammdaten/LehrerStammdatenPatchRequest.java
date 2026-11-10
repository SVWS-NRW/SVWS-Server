package de.svws_nrw.service.lehrer.stammdaten;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Enthält die Attribute der Lehrer-Stammdaten, die über den {@link LehrerStammdatenService} geändert werden können.
 * Alle Felder sind als {@link JsonNullable} deklariert, um zwischen "nicht gesetzt" (undefined) und "explizit null"
 * zu unterscheiden. Die reinen Lesefelder {@code id}, {@code leitungsfunktionen} und {@code kuerzelStatistik} werden
 * ignoriert. Alle anderen unbekannten Attribute führen zu einem Fehler (HTTP 400).
 */
@JsonIgnoreProperties({ "id", "leitungsfunktionen", "kuerzelStatistik" })
public class LehrerStammdatenPatchRequest {

	/** Das Kürzel des Lehrers. */
	@Schema(description = "Das Kürzel des Lehrers.", example = "MUS")
	public JsonNullable<@NotBlank @Size(max = 10) String> kuerzel = JsonNullable.undefined();

	/** Das Kürzel des Personal-Typs des Lehrers. */
	@Schema(description = "Das Kürzel des Personal-Typs des Lehrers.", example = "LEHRKRAFT")
	public JsonNullable<@NotBlank String> personalTyp = JsonNullable.undefined();

	/** Der Nachname des Lehrers. */
	@Schema(description = "Der Nachname des Lehrers.", example = "Mustermann")
	public JsonNullable<@NotBlank @Size(max = 120) String> nachname = JsonNullable.undefined();

	/** Der Vorname des Lehrers. */
	@Schema(description = "Der Vorname des Lehrers.", example = "Max")
	public JsonNullable<@NotBlank @Size(max = 80) String> vorname = JsonNullable.undefined();

	/** Die ID des Geschlechts des Lehrers. */
	@Schema(description = "Die ID des Geschlechts des Lehrers.", example = "3")
	public JsonNullable<@NotNull Integer> geschlecht = JsonNullable.undefined();

	/** Ggf. die Anrede des Lehrers. */
	@Schema(description = "Ggf. die Anrede des Lehrers.", example = "Herr")
	public JsonNullable<@Size(max = 10) String> anrede = JsonNullable.undefined();

	/** Ggf. ein akademischer Grad des Lehrers. */
	@Schema(description = "Ggf. ein akademischer Grad des Lehrers.", example = "Dr.")
	public JsonNullable<@Size(max = 20) String> titel = JsonNullable.undefined();

	/** Ggf. die Amtsbezeichnung des Lehrers. */
	@Schema(description = "Ggf. die Amtsbezeichnung des Lehrers.", example = "OStD")
	public JsonNullable<@Size(max = 15) String> amtsbezeichnung = JsonNullable.undefined();

	/** Das Geburtsdatum des Lehrers im Format yyyy-MM-dd. */
	@Schema(description = "Das Geburtsdatum des Lehrers.", example = "1911-11-11")
	public JsonNullable<@ValidDateFormat String> geburtsdatum = JsonNullable.undefined();

	/** Ggf. die ID der Staatsangehörigkeit des Lehrers. */
	@Schema(description = "Ggf. die ID der Staatsangehörigkeit des Lehrers.", example = "68090065")
	public JsonNullable<Long> idStaatsangehoerigkeit = JsonNullable.undefined();

	/** Ggf. der Straßenname im Wohnort des Lehrers. */
	@Schema(description = "Ggf. der Straßenname im Wohnort des Lehrers.", example = "Musterweg")
	public JsonNullable<@Size(max = 55) String> strassenname = JsonNullable.undefined();

	/** Ggf. die Hausnummer zur Straße im Wohnort des Lehrers. */
	@Schema(description = "Ggf. die Hausnummer zur Straße im Wohnort des Lehrers.", example = "4711")
	public JsonNullable<@Size(max = 10) String> hausnummer = JsonNullable.undefined();

	/** Ggf. der Hausnummerzusatz zur Straße im Wohnort des Lehrers. */
	@Schema(description = "Ggf. der Hausnummerzusatz zur Straße im Wohnort des Lehrers.", example = "a-d")
	public JsonNullable<@Size(max = 30) String> hausnummerZusatz = JsonNullable.undefined();

	/** Ggf. die ID des Wohnortes des Lehrers. */
	@Schema(description = "Ggf. die ID des Wohnortes des Lehrers.", example = "4711")
	public JsonNullable<Long> wohnortID = JsonNullable.undefined();

	/** Ggf. die ID des Ortsteils im Wohnort des Lehrers. */
	@Schema(description = "Ggf. die ID des Ortsteils im Wohnort des Lehrers.", example = "4711")
	public JsonNullable<Long> ortsteilID = JsonNullable.undefined();

	/** Ggf. die Telefonnummer des Lehrers. */
	@Schema(description = "Ggf. die Telefonnummer des Lehrers.", example = "00007-4711")
	public JsonNullable<@Size(max = 20) String> telefon = JsonNullable.undefined();

	/** Ggf. die Mobilnummer des Lehrers. */
	@Schema(description = "Ggf. die Mobilnummer des Lehrers.", example = "0007-47114711")
	public JsonNullable<@Size(max = 20) String> telefonMobil = JsonNullable.undefined();

	/** Ggf. die dienstliche E-Mail-Adresse des Lehrers. */
	@Schema(description = "Ggf. die dienstliche E-Mail-Adresse des Lehrers.", example = "max.mustermann@dienst")
	public JsonNullable<@Size(max = 100) String> emailDienstlich = JsonNullable.undefined();

	/** Ggf. die private E-Mail-Adresse des Lehrers. */
	@Schema(description = "Ggf. die private E-Mail-Adresse des Lehrers.", example = "max.mustermann@home")
	public JsonNullable<@Size(max = 100) String> emailPrivat = JsonNullable.undefined();

	/** Das Foto des Lehrers (jpg, Base64-kodiert). Bei {@code null} wird das Foto gelöscht. */
	@Schema(description = "Ggf. das Foto des Lehrers (jpg, Base64-kodiert).", example = "ein Bild")
	public JsonNullable<String> foto = JsonNullable.undefined();

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll. */
	@Schema(description = "Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.", example = "true")
	public JsonNullable<@NotNull Boolean> istSichtbar = JsonNullable.undefined();

	/** Gibt an, ob der Eintrag für die Schulstatistik relevant ist. */
	@Schema(description = "Gibt an, ob der Eintrag für die Schulstatistik relevant ist oder nicht.", example = "true")
	public JsonNullable<@NotNull Boolean> istRelevantFuerStatistik = JsonNullable.undefined();

}
