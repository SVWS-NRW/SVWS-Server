package de.svws_nrw.service.lehrer.stammdaten;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Enthält die Attribute, die beim Anlegen der Stammdaten eines Lehrers über den {@link LehrerStammdatenService}
 * angegeben werden können. Die Feldnamen stimmen mit denen des Core-DTOs {@code LehrerStammdaten} überein.
 * Die reinen Lesefelder {@code id}, {@code leitungsfunktionen} und {@code kuerzelStatistik} sowie {@code foto} werden
 * ignoriert. Alle anderen unbekannten Attribute führen zu einem Fehler (HTTP 400).
 */
@JsonIgnoreProperties({ "id", "leitungsfunktionen", "kuerzelStatistik", "foto" })
public class LehrerStammdatenCreateRequest {

	/** Das Kürzel des Lehrers. */
	@Schema(description = "Das Kürzel des Lehrers.", example = "MUS")
	@NotBlank
	@Size(max = 10)
	public String kuerzel;

	/** Die Bezeichnung des Personal-Typs des Lehrers. */
	@Schema(description = "Das Kürzel des Personal-Typs des Lehrers.", example = "LEHRKRAFT")
	@NotBlank
	public String personalTyp;

	/** Der Nachname des Lehrers. */
	@Schema(description = "Der Nachname des Lehrers.", example = "Mustermann")
	@NotBlank
	@Size(max = 120)
	public String nachname;

	/** Der Vorname des Lehrers. */
	@Schema(description = "Der Vorname des Lehrers.", example = "Max")
	@NotBlank
	@Size(max = 80)
	public String vorname;

	/** Die ID des Geschlechts des Lehrers. */
	@Schema(description = "Die ID des Geschlechts des Lehrers.", example = "3")
	@NotNull
	public Integer geschlecht;

	/** Ggf. die Anrede des Lehrers. */
	@Schema(description = "Ggf. die Anrede des Lehrers.", example = "Herr")
	@Size(max = 10)
	public String anrede;

	/** Ggf. ein akademischer Grad des Lehrers. */
	@Schema(description = "Ggf. ein akademischer Grad des Lehrers.", example = "Dr.")
	@Size(max = 20)
	public String titel;

	/** Ggf. die Amtsbezeichnung des Lehrers. */
	@Schema(description = "Ggf. die Amtsbezeichnung des Lehrers.", example = "OStD")
	@Size(max = 15)
	public String amtsbezeichnung;

	/** Das Geburtsdatum des Lehrers im Format yyyy-MM-dd. */
	@Schema(description = "Das Geburtsdatum des Lehrers.", example = "1911-11-11")
	@ValidDateFormat
	public String geburtsdatum;

	/** Ggf. die ID der Staatsangehörigkeit des Lehrers. */
	@Schema(description = "Ggf. die ID der Staatsangehörigkeit des Lehrers.", example = "68090065")
	public Long idStaatsangehoerigkeit;

	/** Ggf. der Straßenname im Wohnort des Lehrers. */
	@Schema(description = "Ggf. der Straßenname im Wohnort des Lehrers.", example = "Musterweg")
	@Size(max = 55)
	public String strassenname;

	/** Ggf. die Hausnummer zur Straße im Wohnort des Lehrers. */
	@Schema(description = "Ggf. die Hausnummer zur Straße im Wohnort des Lehrers.", example = "4711")
	@Size(max = 10)
	public String hausnummer;

	/** Ggf. der Hausnummerzusatz zur Straße im Wohnort des Lehrers. */
	@Schema(description = "Ggf. der Hausnummerzusatz zur Straße im Wohnort des Lehrers.", example = "a-d")
	@Size(max = 30)
	public String hausnummerZusatz;

	/** Ggf. die ID des Wohnortes des Lehrers. */
	@Schema(description = "Ggf. die ID des Wohnortes des Lehrers.", example = "4711")
	public Long wohnortID;

	/** Ggf. die ID des Ortsteils im Wohnort des Lehrers. */
	@Schema(description = "Ggf. die ID des Ortsteils im Wohnort des Lehrers.", example = "4711")
	public Long ortsteilID;

	/** Ggf. die Telefonnummer des Lehrers. */
	@Schema(description = "Ggf. die Telefonnummer des Lehrers.", example = "00007-4711")
	@Size(max = 20)
	public String telefon;

	/** Ggf. die Mobilnummer des Lehrers. */
	@Schema(description = "Ggf. die Mobilnummer des Lehrers.", example = "0007-47114711")
	@Size(max = 20)
	public String telefonMobil;

	/** Ggf. die dienstliche E-Mail-Adresse des Lehrers. */
	@Schema(description = "Ggf. die dienstliche E-Mail-Adresse des Lehrers.", example = "max.mustermann@dienst")
	@Size(max = 100)
	public String emailDienstlich;

	/** Ggf. die private E-Mail-Adresse des Lehrers. */
	@Schema(description = "Ggf. die private E-Mail-Adresse des Lehrers.", example = "max.mustermann@home")
	@Size(max = 100)
	public String emailPrivat;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll. */
	@Schema(description = "Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.", example = "true")
	@NotNull
	public Boolean istSichtbar = true;

	/** Gibt an, ob der Eintrag für die Schulstatistik relevant ist. */
	@Schema(description = "Gibt an, ob der Eintrag für die Schulstatistik relevant ist oder nicht.", example = "true")
	@NotNull
	public Boolean istRelevantFuerStatistik = true;

}
