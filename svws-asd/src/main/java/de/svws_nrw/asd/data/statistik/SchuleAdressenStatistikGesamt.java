package de.svws_nrw.asd.data.statistik;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Adressen einer Schule.
 */
@XmlRootElement
@Schema(description = "Die Daten einer Schule.")
@TranspilerDTO
public class SchuleAdressenStatistikGesamt {

	/** Die ID der Adressen. */
	@Schema(description = "die ID der Adressen", example = "4709")
	public long id;

	/** Das Adresskennzeichen einer Adresse. */
	@Schema(description = "Das Adresskennzeichen einer Adresse", example = "A")
	public String adresskennzeichen;

	/** Der Strassenname einer Adresse. */
	@Schema(description = "Der Strassenname einer Adresse", example = "Musterstrasse")
	public String strassenname;

	/** Die Hausnummer einer Adresse. */
	@Schema(description = "Die Hausnummer einer Adresse", example = "123")
	public String hausnummer;

	/** Der Hausnummernzusatz einer Adresse. */
	@Schema(description = "Der Hausnummernzusatz einer Adresse", example = "a")
	public String hausnummerZusatz;

	/** Die Postleitzahl einer Adresse. */
	@Schema(description = "Die Postleitzahl einer Adresse", example = "42651")
	public String plz;

	/** Der Ort einer Adresse. */
	@Schema(description = "Der Ort einer Adresse", example = "Solingen")
	public String ort;

	/** Ist die Adresse der Hauptsitz der Schule. */
	@Schema(description = "Ist die Adresse der Hauptsitz der Schule", example = "false")
	public boolean istHauptsitz;

	/** Das Kennzeichen des Standorts. */
	@Schema(description = "Das Kennzeichen des Standorts", example = "01")
	public String standortkennzeichen;

	/** Ist die Adresse aktiv. */
	@Schema(description = "Ist die Adresse aktiv", example = "true")
	public boolean istAktiv;

	/** Die Art der Adresse. */
	@Schema(description = "Die Art der Adresse", example = "1")
	public long idArt;

}
