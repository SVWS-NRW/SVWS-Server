package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Adress-Daten, die zu einer Schule gehören (B02)
 */
@XmlRootElement
@Schema(description = "Die Adress-Daten, die zu einer Schule gehören (B02)")
@TranspilerDTO
public class SchuleAdressenStatistikExport {

	/** Die ID der Adressen. */
	@Schema(description = "die ID der Adressen", example = "4709")
	public long id = -1;

	/** Satzschlüssel: Das Adresskennzeichen einer Adresse. */
	@Schema(description = "satzschlüssel: Das Adresskennzeichen einer Adresse", example = "A")
	public String adresskennzeichen = "";

	/** Der Amtliche Gemeindeschlüssel: Länderkuerzel. */
	@Schema(description = "der Amtliche Gemeindeschlüssel: Länderkuerzel", example = "05")
	public String regionalschluesselLaenderkuerzel = "";

	/** Der Amtliche Gemeindeschlüssel: Gemeindekennzahl. */
	@Schema(description = "der Amtliche Gemeindeschlüssel: Gemeindekennzahl.", example = "123456")
	public String regionalschluesselGemeindekennzahl = "";

	/** Der Strassenname einer Adresse. */
	@Schema(description = "der Strassenname einer Adresse", example = "Musterstrasse")
	public String strassenname = "";

	/** Die Hausnummer und Hausnummerzusatz einer Adresse. */
	@Schema(description = "die Hausnummer und Hausnummerzusatz einer Adresse", example = "123a")
	public String hausnummer = "";

	/** Die Postleitzahl einer Adresse. */
	@Schema(description = "die Postleitzahl einer Adresse", example = "42651")
	public String plz = "";

	/** Der Ort einer Adresse. */
	@Schema(description = "der Ort einer Adresse", example = "Solingen")
	public String ort = "";

	/** Ist die Adresse der Hauptsitz der Schule. */
	@Schema(description = "ist die Adresse der Hauptsitz der Schule", example = "false")
	public boolean istHauptsitz = true;

	/** Das Kennzeichen des Standorts. */
	@Schema(description = "das Kennzeichen des Standorts", example = "01")
	public String standortkennzeichen = "";

	/** Ist die Adresse aktiv. */
	@Schema(description = "ist die Adresse aktiv", example = "true")
	public boolean istAktiv = true;

	/** Die Art der Adresse. */
	@Schema(description = "die Art der Adresse", example = "1")
	public long idArt = 1;

	/** Die Qualität der Verortung. */
	@Schema(description = "die Qualität der Verortung", example = "1")
	public String verortungQualitaet = "";

	/** Koordinaten Rechtswert. */
	@Schema(description = "koordinaten Rechtswert", example = "312345")
	public String koordinateRechtswert = "";

	/** Koordinaten Hochwert. */
	@Schema(description = "koordinaten Hochwert", example = "5123456")
	public String koordinateHochwert = "";

	/** Adressvorgabedatensatz. */
	@Schema(description = "adressvorgabedatensatz", example = "true")
	public boolean istAdressvorgabedatensatz = false;

	/** Datumstempel Vorgabedaten. */
	@Schema(description = "datumstempel Vorgabedaten", example = "2026")
	public String datumStempelVorgabedaten = "";

	/** Die Schülersummen zur Adresse (K88). */
	@Schema(description = "die Schülersummen zur Adresse (K88)")
	public @NotNull SchuleAdressenSchuelerStatistikExport schuleAdressenSchuelerStatistikExport = new SchuleAdressenSchuelerStatistikExport();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuleAdressenStatistikExport() {
		// leer
	}

}
