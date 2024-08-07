package de.svws_nrw.schulen.v1.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt ein Merkmal einer Organisationseinheit der Schuldatei.
 */
@XmlRootElement
@Schema(description = "ein Merkmal einer Organisationseinheit der Schuldatei.")
@TranspilerDTO
public class SchuldateiOrganisationseinheitMerkmal extends SchuldateiEintrag {

	/** Die ID des Merkmal-Eintrags. */
	@Schema(description = "die ID des Merkmal-Eintrags", example = "4711")
	public Integer id;

	/** Die Schulnummer. */
	@Schema(description = "die Schulnummer", example = "100001")
	public @NotNull Integer schulnummer = 0;

	/** Die Nummer der Liegenschaft der Organisationseinheit */
	@Schema(description = "Die Nummer der Liegenschaft der Organisationseinheit", example = "1")
	public @NotNull Integer liegenschaft = 0;

	/** Das Merkmal */
	@Schema(description = "das Merkmal", example = "32")
	public @NotNull String merkmal = "";

	/** Die Merkmalsgruppe (hat zur Zeit 25.07.24 keine Bedeutung)*/
	@Schema(description = "die Merkmalsgruppe", example = "2")
	public @NotNull String merkmalgruppe = "";

	/** Das Attribut*/
	@Schema(description = "Das Attribut", example = "55")
	public String attribut;

	/** Die Attributsgruppe (hat zur Zeit 25.07.24 keine Bedeutung)*/
	@Schema(description = "Die Attributsgruppe", example = "-1")
	public String attributgruppe;

	/** Der Wert*/
	@Schema(description = "Der Wert", example = "G01")
	public String wert;


	/**
	 * Erstellt ein neues Merkmal für eine Organisationseinheit der Schuldatei
	 */
	public SchuldateiOrganisationseinheitMerkmal() {
		// Die Initialisierung mit Defaults erfolgt direkt über die Attribute
	}

}
