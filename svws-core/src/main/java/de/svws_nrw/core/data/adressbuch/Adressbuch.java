package de.svws_nrw.core.data.adressbuch;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle
 * verwendet. Sie beschreibt ein Adressbuch.
 */
@XmlRootElement
@Schema(description = "Ein Adressbuch.")
@TranspilerDTO
public class Adressbuch {

	/** ID des Adressbuchs */
	@Schema(description = "die ID des Adressbuchs", example = "global")
	public @NotNull String id = "";

	/** Anzeigename des Adressbuchs */
	@Schema(description = "Name des Adressbuchs", example = "Globales Adressbuch")
	public String displayname;

	/** Beschreibung des Adressbuchs */
	@Schema(description = "Beschreibung des Adressbuchs", example = "Globales Adressbuch für öffentliche Adressen.")
	public String beschreibung;

	/** Versionskennzeichen des Adressbuchs */
	@Schema(description = "Versionskennzeichen des Adressbuchs", example = "98")
	public int synctoken;

	/**
	 * der Typ des Adressbuchs
	 */
	@Schema(description = "der Typ des Adressbuchs", example = "GENERIERT")
	public @NotNull String adressbuchTyp = "";

	/**
	 * eine Liste der Einträge des Adressbuchs
	 */
	@ArraySchema(schema = @Schema(description = "eine Liste der Einträge des Adressbuchs", example = "..."))
	public @NotNull List<@NotNull AdressbuchEintrag> adressbuchEintraege = new Vector<>();

}
