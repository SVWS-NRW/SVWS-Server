package de.svws_nrw.asd.data.schueler;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Stammdaten eines Schülers mit der angegebenen ID.
 */
@XmlRootElement
@Schema(description = "Die Stammdaten eines Schüler-Eintrags.")
@TranspilerDTO
public class SchuelerStammdaten {

	// **** Basisdaten

	/** Die ID des Schülerdatensatzes. */
	@Schema(description = "die ID", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die ID des Schuljahresabschnitts, welchem der Schüler aktuell zugeordnet ist. */
	@Schema(description = "die ID des Schuljahresabschnitts, welchem der Schüler aktuell zugeordnet ist", example = "42")
	public Long idSchuljahresabschnitt;

	/** Das Foto (in Base64 kodiert) des Schülerdatensatzes. */
	@Schema(description = "ggf. das Foto des Schülers (jpg, Base64-kodiert)", example = "ein Bild")
	public String foto;

	/** Der Nachname des Schülerdatensatzes. */
	@Schema(description = "der Nachname", example = "Mustermann")
	public @NotNull String nachname = "";

	/** Der Vorname des Schülerdatensatzes. */
	@Schema(description = "der Vorname", example = "Max")
	public @NotNull String vorname = "";

	/** Alle Vornamen, sofern es mehrere gibt, des Schülerdatensatzes. */
	@Schema(description = "alle Vornamen, sofern es mehrere gibt, sonst erfolgt der Zugriff nur auf Vorname", example = "Max Moritz")
	public @NotNull String alleVornamen = "";

	/** Die ID des Geschlechtes */
	@Schema(description = "die ID des Geschlechtes", example = "3")
	public int geschlecht;

	/** Das Geburtsdatum des Schülerdatensatzes. */
	@Schema(description = "das Geburtsdatum", example = "1911-11-11")
	public String geburtsdatum;

	/** Der Geburtsort des Schülerdatensatzes. */
	@Schema(description = "der Geburtsort", example = "Berlin")
	public String geburtsort;

	/** Der Geburtsname des Schülerdatensatzes. */
	@Schema(description = "ggf. der Geburtsname", example = "Muster")
	public String geburtsname;


	// **** Wohnort und Kontaktdaten

	/** Ggf. der Straßenname im Wohnort des Schülers. */
	@Schema(description = "Ggf. der Straßenname im Wohnort des Schülers.", example = "Musterweg")
	public String strassenname;

	/** Ggf. die Hausnummer zur Straße im Wohnort des Schülers. */
	@Schema(description = "Ggf. die Hausnummer zur Straße im Wohnort des Schülers.", example = "4711")
	public String hausnummer;

	/** Ggf. der Hausnummerzusatz zur Straße im Wohnort des Schülers. */
	@Schema(description = "Ggf. der Hausnummerzusatz zur Straße im Wohnort des Schülers.", example = "a-d")
	public String hausnummerZusatz;

	/** Die ID des Wohnortes des Schülerdatensatzes. */
	@Schema(description = "ggf. die ID des Wohnortes", example = "4711")
	public Long wohnortID;

	/** Die ID des Ortsteils des Schülerdatensatzes. */
	@Schema(description = "ggf. die ID des Ortsteils im Wohnort", example = "Muster")
	public Long ortsteilID;

	/** Die Telefonnummer des Schülerdatensatzes. */
	@Schema(description = "ggf. die Telefonnummer", example = "00007-4711")
	public String telefon;

	/** Die Mobilnummer des Schülerdatensatzes. */
	@Schema(description = "ggf. die Mobilnummer", example = "0007-47114711")
	public String telefonMobil;

	/** Die private Email-Adresse des Schülerdatensatzes. */
	@Schema(description = "ggf. die private Email-Adresse", example = "max.mustermann@home")
	public String emailPrivat;

	/** Die schulische Email-Adresse des Schülerdatensatzes. */
	@Schema(description = "ggf. die schulische Email-Adresse", example = "max.mustermann@schule")
	public String emailSchule;


	// **** Daten zur Staatsangehörigkeit und zur Religion

	/** Die ID der Staatsangehörigkeit des Schülerdatensatzes. */
	@Schema(description = "die ID der Staatsangehörigkeit", example = "123456")
	public Long idStaatsangehoerigkeit;

	/** Die ID einer zweiten Staatsangehörigkeit des Schülerdatensatzes. */
	@Schema(description = "ggf. die ID einer zweiten Staatsangehörigkeit", example = "123456")
	public Long idStaatsangehoerigkeit2;

	/** Die ID der Religion des Schülerdatensatzes. */
	@Schema(description = "ggf. die ID der Religion", example = "4711")
	public Long religionID;

	/** Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll. */
	@Schema(description = "gibt an, ob die Konfession des Schülers auf dem Zeugnis erscheinen soll oder nicht.", example = "true")
	public boolean druckeKonfessionAufZeugnisse;

	/** Das Datum der Religionsabmeldung des Schülerdatensatzes. */
	@Schema(description = "das Datum der Religionsabmeldung", example = "1911-11-11")
	public String religionabmeldung;

	/** Das Datum der Religionsanmeldung des Schülerdatensatzes. */
	@Schema(description = "das Datum der Religionsanmeldung", example = "1912-12-12")
	public String religionanmeldung;


	// **** Daten zum Migrationshintergrund

	/** Gibt an, ob ein Migrationshintergrund bei dem Schülerdatensatz vorhanden ist. */
	@Schema(description = "gibt an, ob ein Migrationshintergrund vorhanden ist", example = "true")
	public boolean hatMigrationshintergrund;

	/** Das Zuzugsjahr des Schülerdatensatzes. */
	@Schema(description = "ggf. das Zuzugsjahr", example = "2013")
	public Integer zuzugsjahr;

	/** Die Id des Geburtslandes des Schülerdatensatzes. */
	@Schema(description = "ggf. die Id des Geburtslandes", example = "12345")
	public Long idGeburtsland;

	/** Die ID zur Verkehrssprache der Familie des Schülerdatensatzes. */
	@Schema(description = "ggf. die ID der in der Familie hauptsächlich gesprochen Sprache", example = "123456")
	public Long idVerkehrspracheFamilie;

	/** Die Id des Geburtslandes des Vaters des Schülerdatensatzes. */
	@Schema(description = "ggf. die ID des Geburtslandes des Vaters", example = "12345")
	public Long idGeburtslandVater;

	/** Die Id des Geburtslandes der Mutter des Schülerdatensatzes. */
	@Schema(description = "ggf. die ID des Geburtslandes der Mutter", example = "12345")
	public Long idGeburtslandMutter;


	// **** Statusdaten

	/** Die ID des Status des Schülerdatensatzes. */
	@Schema(description = "die ID des aktuellen Schülerstatus", example = "2")
	public int status;

	/** Gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht. */
	@Schema(description = "gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht", example = "true")
	public boolean istDuplikat;

	/** Das Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist. */
	@Schema(description = "die Schulnummer eines externen Schülers oder null", example = "null")
	public String externeSchulNr;

	/** Die Nummer der Schülerausweises, sofern ein Schülerausweis verwendet wird, ansonsten null */
	@Schema(description = "die Nummer der Schülerausweises, sofern ein Schülerausweis verwendet wird, ansonsten null", example = "null")
	public String idSchuelerausweis;

	/** Die ID der Art des Fahrschülers des Schülerdatensatzes. */
	@Schema(description = "ggf. die ID der Art des Fahrschülers", example = "3")
	public Long fahrschuelerArtID;

	/** Die ID der Haltestelle, ab der der Schüler das Transportmittel nimmt, des Schülerdatensatzes. */
	@Schema(description = "ggf. die ID der Haltestelle, ab der der Schüler das Transportmittel nimmt", example = "3")
	public Long haltestelleID;

	/** Das Anmeldedatum des Schülerdatensatzes. */
	@Schema(description = "das Anmeldedatum", example = "1911-11-11")
	public String anmeldedatum;

	/** Das Aufnahmedatum des Schülerdatensatzes. */
	@Schema(description = "das Aufnahmedatum", example = "1911-11-11")
	public String aufnahmedatum;

	/** Gibt an, ob der Schüler volljährig ist oder nicht. */
	@Schema(description = "gibt an, ob der Schüler volljährig ist oder nicht", example = "true")
	public boolean istVolljaehrig;

	/** Gibt an, ob der Schüler die Schulpflicht erfüllt hat oder nicht. */
	@Schema(description = "gibt an, ob der Schüler die Schulpflicht erfüllt hat oder nicht", example = "true")
	public boolean istSchulpflichtErfuellt;

	/** Gibt an, ob der Schüler die Berufsschulpflicht erfüllt hat oder nicht. */
	@Schema(description = "gibt an, ob der Schüler die Berufsschulpflicht erfüllt hat oder nicht", example = "true")
	public boolean istBerufsschulpflichtErfuellt;

	/** Gibt an, ob der Schüler einen Nachweis über die Masernimpfpflicht erbracht hat. */
	@Schema(description = "gibt an, ob der Schüler einen Nachweis über die Masernimpfpflicht erbracht hat", example = "true")
	public boolean hatMasernimpfnachweis;

	/** Gibt an, ob über den Schüler eine Auskunft an dritte erteilt werden darf oder dies unter allen Umständen vermieden werden sollte.*/
	@Schema(description = "gibt an, ob über den Schüler eine Auskunft an dritte erteilt werden darf oder dies unter allen Umständen vermieden werden sollte.",
			example = "true")
	public boolean keineAuskunftAnDritte;

	/** Gibt an, ob der Schüler BAFÖG erhält oder nicht. */
	@Schema(description = "gibt an, ob der Schüler BAFÖG erhält oder nicht", example = "true")
	public boolean erhaeltSchuelerBAFOEG;

	/** Gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht. */
	@Schema(description = "gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht", example = "true")
	public boolean erhaeltMeisterBAFOEG;


	/** Der Beginn des Bildungsgangs eines Schülers. */
	@Schema(description = "der Beginn des Bildungsgangs eines Schülers", example = "1911-11-11")
	public String beginnBildungsgang;

	/** Dauer des Bildungsgangs am BK eines Schülers. */
	@Schema(description = "die Dauer des Bildungsgangs am BK eines Schülers", example = "null")
	public Integer dauerBildungsgang;

	/** Der Beruf des Schülers (nur bei Schulform BK/SB/WB). */
	@Schema(description = "Der Beruf des Schülers (nur bei Schulform BK/SB/WB)", example = "Tischler")
	public String beruf;


}
