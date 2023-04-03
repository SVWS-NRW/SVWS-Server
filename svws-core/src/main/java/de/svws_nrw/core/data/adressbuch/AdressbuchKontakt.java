package de.svws_nrw.core.data.adressbuch;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle
 * verwendet. Sie beschreibt einen Kontakt in einem Adressbuch.
 */
@XmlRootElement
@Schema(description = "Ein Kontakt in einem Adressbuch.")
@TranspilerDTO
public class AdressbuchKontakt extends AdressbuchEintrag {

	/** Der Nachname des Kontakts. */
	@Schema(description = "der Nachname", example = "Mustermann")
	public @NotNull String nachname = "";

	/** Ggf. Zusatz zum Nachnamen des Schülerdatensatzes. */
	@Schema(description = "ggf. ein Zusatz zum Nachnamen", example = "von")
	public @NotNull String zusatzNachname = "";

	/** Der Vorname des Schülerdatensatzes. */
	@Schema(description = "der Vorname", example = "Max")
	public @NotNull String vorname = "";

	/** Der Straßenname des Kontakts. */
	@Schema(description = "der Straßenname des Kontakts.", example = "Musterweg")
	public String strassenname;

	/** Die Hausnummer des Kontakts. */
	@Schema(description = "Ggf. die Hausnummer des Kontakts.", example = "4711")
	public String hausnummer;

	/** Ggf. der Hausnummerzusatz des Kontakts. */
	@Schema(description = "Ggf. der Hausnummerzusatz des Kontakts.", example = "a-d")
	public String hausnummerZusatz;

	/** Die Postleitzahl des Kontakts. */
	@Schema(description = "die Postleitzahl des Kontakts", example = "42287")
	public String plz;

	/** Der Ort des Kontakts. */
	@Schema(description = "der Ort des Kontakts", example = "Düsseldorf")
	public String ort;

	/** Die Telefonnummern des Kontakts. */
	@ArraySchema(schema = @Schema(description = "Die Telefonnummern des Kontakts. ", example = "..."))
	public @NotNull List<@NotNull Telefonnummer> telefonnummern = new Vector<>();

	/** Die Mailadresse des Kontakts. */
	@Schema(description = "die Mailadresse des Kontakts", example = "info@schule.de")
	public String email;

	/** Die Webadresse des Kontakts */
	@Schema(description = "die Adresse der Homepage des Kontakts", example = "www.schule.de")
	public String webAdresse = "";

	/** Die Kategorien dieses Kontakts */
	@ArraySchema(schema = @Schema(description = "Die Kategorien dieses Kontakts", example = "..."))
	public List<String> kategorien = new Vector<>();

	/**
	 * Die Organisation dieses Kontakts
	 */
	@Schema(description = "die Organisation des Kontakts", example = "Sophie-Scholl-Gymnasium")
	public String organisation;

	/**
	 * Die Rolle innerhalb der Organisation dieses Kontakts
	 */
	@Schema(description = "Die Rolle innerhalb der Organisation des Kontakts", example = "Schüler")
	public String rolle;

	/** Die ID des Adressbuchkontakts des Kinds */
	@Schema(description = "Die ID des Adressbuchkontakts des Kinds", example = "Schueler_1234")
	public String idKind;

	/** Die ID des Adressbuchkontakts der Eltern */
	@Schema(description = "Die ID des Adressbuchkontakts der Eltern", example = "Erzieher_1234")
	public String idEltern;
}
