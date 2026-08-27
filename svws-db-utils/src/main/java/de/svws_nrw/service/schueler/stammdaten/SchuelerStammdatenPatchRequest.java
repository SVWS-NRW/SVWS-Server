package de.svws_nrw.service.schueler.stammdaten;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public class SchuelerStammdatenPatchRequest {

	// **** Persönliche Daten

	/** Das Foto (in Base64 kodiert) des Schülerdatensatzes. */
	@Schema(description = "das Foto des Schülers (jpg, Base64-kodiert)", example = "ein Bild")
	public JsonNullable<String> foto = JsonNullable.undefined();

	/** Der Nachname des Schülerdatensatzes. */
	@Schema(description = "der Nachname", example = "Mustermann")
	public JsonNullable<@NotBlank @Size(max = 120) @NoLeadingOrTrailingWhitespaces String> nachname = JsonNullable.undefined();

	/** Der Vorname des Schülerdatensatzes. */
	@Schema(description = "der Vorname", example = "Max")
	public JsonNullable<@NotBlank @Size(max = 80) @NoLeadingOrTrailingWhitespaces String> vorname = JsonNullable.undefined();

	/** Alle Vornamen, sofern es mehrere gibt, des Schülerdatensatzes. */
	@Schema(description = "alle Vornamen, sofern es mehrere gibt, sonst erfolgt der Zugriff nur auf Vorname", example = "Max Moritz")
	public JsonNullable<@Size(max = 255) @NoLeadingOrTrailingWhitespaces String> alleVornamen = JsonNullable.undefined();

	/** Die ID des Geschlechtes */
	@Schema(description = "die ID des Geschlechtes", example = "3")
	public JsonNullable<@NotNull Integer> geschlecht = JsonNullable.undefined();

	/** Das Geburtsdatum des Schülerdatensatzes. */
	@Schema(description = "das Geburtsdatum", example = "1911-11-11")
	public JsonNullable<@NotBlank @ValidDateFormat String> geburtsdatum = JsonNullable.undefined();

	/** Der Geburtsort des Schülerdatensatzes. */
	@Schema(description = "der Geburtsort", example = "Berlin")
	public JsonNullable<@Size(max = 100) @NoLeadingOrTrailingWhitespaces String> geburtsort = JsonNullable.undefined();

	/** Der Geburtsname des Schülerdatensatzes. */
	@Schema(description = "der Geburtsname", example = "Muster")
	public JsonNullable<@Size(max = 120) @NoLeadingOrTrailingWhitespaces String> geburtsname = JsonNullable.undefined();


	// **** Wohnort und Kontaktdaten

	/** der Straßenname im Wohnort des Schülers. */
	@Schema(description = "der Straßenname im Wohnort des Schülers.", example = "Musterweg")
	public JsonNullable<@Size(max = 55) @NoLeadingOrTrailingWhitespaces String> strassenname = JsonNullable.undefined();

	/** die Hausnummer zur Straße im Wohnort des Schülers. */
	@Schema(description = "die Hausnummer zur Straße im Wohnort des Schülers.", example = "4711")
	public JsonNullable<@Size(max = 10) @NoLeadingOrTrailingWhitespaces String> hausnummer = JsonNullable.undefined();

	/** der Hausnummerzusatz zur Straße im Wohnort des Schülers. */
	@Schema(description = "der Hausnummerzusatz zur Straße im Wohnort des Schülers.", example = "a-d")
	public JsonNullable<@Size(max = 30) @NoLeadingOrTrailingWhitespaces String> hausnummerZusatz = JsonNullable.undefined();

	/** Die ID des Wohnortes des Schülerdatensatzes. */
	@Schema(description = "die ID des Wohnortes", example = "4711")
	public JsonNullable<Long> wohnortID = JsonNullable.undefined();

	/** Die ID des Ortsteils des Schülerdatensatzes. */
	@Schema(description = "die ID des Ortsteils im Wohnort", example = "Muster")
	public JsonNullable<Long> ortsteilID = JsonNullable.undefined();

	/** Die Telefonnummer des Schülerdatensatzes. */
	@Schema(description = "die Telefonnummer", example = "00007-4711")
	public JsonNullable<@Size(max = 20) @NoLeadingOrTrailingWhitespaces String> telefon = JsonNullable.undefined();

	/** Die Mobilnummer des Schülerdatensatzes. */
	@Schema(description = "die Mobilnummer", example = "0007-47114711")
	public JsonNullable<@Size(max = 20) @NoLeadingOrTrailingWhitespaces String> telefonMobil = JsonNullable.undefined();

	/** Die private Email-Adresse des Schülerdatensatzes. */
	@Schema(description = "die private Email-Adresse", example = "max.mustermann@home")
	public JsonNullable<@Size(max = 100) @NoLeadingOrTrailingWhitespaces String> emailPrivat = JsonNullable.undefined();

	/** Die schulische Email-Adresse des Schülerdatensatzes. */
	@Schema(description = "die schulische Email-Adresse", example = "max.mustermann@schule")
	public JsonNullable<@Size(max = 100) @NoLeadingOrTrailingWhitespaces String> emailSchule = JsonNullable.undefined();


	// **** Daten zur Staatsangehörigkeit und zur Religion

	/** Die ID der Staatsangehörigkeit des Schülerdatensatzes. */
	@Schema(description = "die ID der Staatsangehörigkeit", example = "123456")
	public JsonNullable<Long> idStaatsangehoerigkeit = JsonNullable.undefined();

	/** Die ID einer zweiten Staatsangehörigkeit des Schülerdatensatzes. */
	@Schema(description = "die ID einer zweiten Staatsangehörigkeit", example = "123456")
	public JsonNullable<Long> idStaatsangehoerigkeit2 = JsonNullable.undefined();

	/** Die ID der Religion des Schülerdatensatzes. */
	@Schema(description = "die ID der Religion", example = "4711")
	public JsonNullable<Long> religionID = JsonNullable.undefined();

	/** Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll. */
	@Schema(description = "gibt an, ob die Konfession des Schülers auf dem Zeugnis erscheinen soll oder nicht.", example = "true")
	public JsonNullable<@NotNull Boolean> druckeKonfessionAufZeugnisse = JsonNullable.undefined();

	/** Das Datum der Religionsabmeldung des Schülerdatensatzes. */
	@Schema(description = "das Datum der Religionsabmeldung", example = "1911-11-11")
	public JsonNullable<@ValidDateFormat String> religionabmeldung = JsonNullable.undefined();

	/** Das Datum der Religionsanmeldung des Schülerdatensatzes. */
	@Schema(description = "das Datum der Religionsanmeldung", example = "1912-12-12")
	public JsonNullable<@ValidDateFormat String> religionanmeldung = JsonNullable.undefined();


	// **** Daten zum Migrationshintergrund

	/** Gibt an, ob ein Migrationshintergrund bei dem Schülerdatensatz vorhanden ist. */
	@Schema(description = "gibt an, ob ein Migrationshintergrund vorhanden ist", example = "true")
	public JsonNullable<@NotNull Boolean> hatMigrationshintergrund = JsonNullable.undefined();

	/** Das Zuzugsjahr des Schülerdatensatzes. */
	@Schema(description = "das Zuzugsjahr", example = "2013")
	public JsonNullable<Integer> zuzugsjahr = JsonNullable.undefined();

	/** Die Id des Geburtslandes des Schülerdatensatzes. */
	@Schema(description = "die Id des Geburtslandes", example = "12345")
	public JsonNullable<Long> idGeburtsland = JsonNullable.undefined();

	/** Die ID zur Verkehrssprache der Familie des Schülerdatensatzes. */
	@Schema(description = "die ID der in der Familie hauptsächlich gesprochen Sprache", example = "123456")
	public JsonNullable<Long> idVerkehrspracheFamilie = JsonNullable.undefined();

	/** Die Id des Geburtslandes des Vaters des Schülerdatensatzes. */
	@Schema(description = "die ID des Geburtslandes des Vaters", example = "12345")
	public JsonNullable<Long> idGeburtslandVater = JsonNullable.undefined();

	/** Die Id des Geburtslandes der Mutter des Schülerdatensatzes. */
	@Schema(description = "die ID des Geburtslandes der Mutter", example = "12345")
	public JsonNullable<Long> idGeburtslandMutter = JsonNullable.undefined();


	// **** Statusdaten

	/** Die ID des Status des Schülerdatensatzes. */
	@Schema(description = "die ID des aktuellen Schülerstatus", example = "2")
	public JsonNullable<@NotNull Integer> status = JsonNullable.undefined();

	/** Gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht. */
	@Schema(description = "gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht", example = "true")
	public JsonNullable<@NotNull Boolean> istDuplikat = JsonNullable.undefined();

	/** Das Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist. */
	@Schema(description = "die Schulnummer eines externen Schülers oder null", example = "null")
	public JsonNullable<@Size(min = 6, max = 6) String> externeSchulNr = JsonNullable.undefined();

	/** Die Nummer der Schülerausweises, sofern ein Schülerausweis verwendet wird, ansonsten null */
	@Schema(description = "die Nummer der Schülerausweises, sofern ein Schülerausweis verwendet wird, ansonsten null", example = "null")
	public JsonNullable<@Size(max = 30) @NoLeadingOrTrailingWhitespaces String> idSchuelerausweis = JsonNullable.undefined();

	/** Die ID der Art des Fahrschülers des Schülerdatensatzes. */
	@Schema(description = "die ID der Art des Fahrschülers", example = "3")
	public JsonNullable<Long> fahrschuelerArtID = JsonNullable.undefined();

	/** Die ID der Haltestelle, ab der der Schüler das Transportmittel nimmt, des Schülerdatensatzes. */
	@Schema(description = "die ID der Haltestelle, ab der der Schüler das Transportmittel nimmt", example = "3")
	public JsonNullable<Long> haltestelleID = JsonNullable.undefined();

	/** Das Anmeldedatum des Schülerdatensatzes. */
	@Schema(description = "das Anmeldedatum", example = "1911-11-11")
	public JsonNullable<@ValidDateFormat String> anmeldedatum = JsonNullable.undefined();

	/** Das Aufnahmedatum des Schülerdatensatzes. */
	@Schema(description = "das Aufnahmedatum", example = "1911-11-11")
	public JsonNullable<@ValidDateFormat String> aufnahmedatum = JsonNullable.undefined();

	/** Gibt an, ob der Schüler volljährig ist oder nicht. */
	@Schema(description = "gibt an, ob der Schüler volljährig ist oder nicht", example = "true")
	public JsonNullable<@NotNull Boolean> istVolljaehrig = JsonNullable.undefined();

	/** Gibt an, ob der Schüler die Schulpflicht erfüllt hat oder nicht. */
	@Schema(description = "gibt an, ob der Schüler die Schulpflicht erfüllt hat oder nicht", example = "true")
	public JsonNullable<@NotNull Boolean> istSchulpflichtErfuellt = JsonNullable.undefined();

	/** Gibt an, ob der Schüler die Berufsschulpflicht erfüllt hat oder nicht. */
	@Schema(description = "gibt an, ob der Schüler die Berufsschulpflicht erfüllt hat oder nicht", example = "true")
	public JsonNullable<@NotNull Boolean> istBerufsschulpflichtErfuellt = JsonNullable.undefined();

	/** Gibt an, ob der Schüler einen Nachweis über die Masernimpfpflicht erbracht hat. */
	@Schema(description = "gibt an, ob der Schüler einen Nachweis über die Masernimpfpflicht erbracht hat", example = "true")
	public JsonNullable<@NotNull Boolean> hatMasernimpfnachweis = JsonNullable.undefined();

	/** Gibt an, ob über den Schüler eine Auskunft an dritte erteilt werden darf oder dies unter allen Umständen vermieden werden sollte.*/
	@Schema(description = "gibt an, ob über den Schüler eine Auskunft an dritte erteilt werden darf oder dies unter allen Umständen vermieden werden sollte.",
			example = "true")
	public JsonNullable<@NotNull Boolean> keineAuskunftAnDritte = JsonNullable.undefined();

	/** Gibt an, ob der Schüler BAFÖG erhält oder nicht. */
	@Schema(description = "gibt an, ob der Schüler BAFÖG erhält oder nicht", example = "true")
	public JsonNullable<@NotNull Boolean> erhaeltSchuelerBAFOEG = JsonNullable.undefined();

	/** Gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht. */
	@Schema(description = "gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht", example = "true")
	public JsonNullable<@NotNull Boolean> erhaeltMeisterBAFOEG = JsonNullable.undefined();

	/** Der Beginn des Bildungsgangs eines Schülers. */
	@Schema(description = "der Beginn des Bildungsgangs eines Schülers", example = "1911-11-11")
	public JsonNullable<@ValidDateFormat String> beginnBildungsgang = JsonNullable.undefined();

	/** Dauer des Bildungsgangs am BK eines Schülers. */
	@Schema(description = "die Dauer des Bildungsgangs am BK eines Schülers", example = "null")
	public JsonNullable<Integer> dauerBildungsgang = JsonNullable.undefined();

	/** Der Beruf des Schülers (nur bei Schulform BK/SB/WB). */
	@Schema(description = "Der Beruf des Schülers (nur bei Schulform BK/SB/WB)", example = "Tischler")
	public JsonNullable<@Size(max = 100) @NoLeadingOrTrailingWhitespaces String> beruf = JsonNullable.undefined();

}
