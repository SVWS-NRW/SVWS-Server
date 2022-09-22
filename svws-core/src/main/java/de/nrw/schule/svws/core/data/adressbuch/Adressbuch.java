package de.nrw.schule.svws.core.data.adressbuch;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Vector;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle
 * verwendet. Sie beschreibt ein Adressbuch.
 */
@XmlRootElement
@Schema(description = "Ein Adressbuch.")
@TranspilerDTO
public class Adressbuch {

	/** ID des Adressbuchs */
	@Schema(required = true, description = "die ID des Adressbuchs", example = "global")
	public @NotNull String id = "";

	/** Anzeigename des Adressbuchs */
	@Schema(required = false, description = "Name des Adressbuchs", example = "Globales Adressbuch")
	public String displayname;

	/** Beschreibung des Adressbuchs */
	@Schema(required = false, description = "Beschreibung des Adressbuchs", example = "Globales Adressbuch für öffentliche Adressen.")
	public String beschreibung;

	/** Versionskennzeichen des Adressbuchs */
	@Schema(required = true, description = "Versionskennzeichen des Adressbuchs", example = "98")
	public int synctoken;

	/**
	 * der Typ des Adressbuchs
	 */
	@Schema(required = true, description = "der Typ des Adressbuchs", example = "GENERIERT")
	public @NotNull String adressbuchTyp = "";

	/**
	 * eine Liste der Einträge des Adressbuchs
	 */
	@ArraySchema(schema = @Schema(required = true, description = "eine Liste der Einträge des Adressbuchs", example = "..."))
	public @NotNull List<@NotNull AdressbuchEintrag> adressbuchEintraege = new Vector<>();

}
