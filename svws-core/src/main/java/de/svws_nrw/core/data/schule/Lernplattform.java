package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt den schulspezifischen Katalog der Einwilligungsarten.
 */
@XmlRootElement
@Schema(description = "Ein Eintrag in dem Katalog der schulspezifischen Lernplattformen.")
@TranspilerDTO
public class Lernplattform {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Die Bezeichnung der Lernplattform. */
	@Schema(description = "Die Bezeichnung der Lernplattform", example = "IServ")
	public @NotNull String bezeichnung = "";

	/** Suffix für den Benutzernamen bei den Lehrern. */
	@Schema(description = "Suffix für den Benutzernamen bei den Lehrern", example = "L_")
	public String benutzernameSuffixLehrer = "";

	/** Suffix für den Benutzernamen bei den Erziehern. */
	@Schema(description = "Suffix für den Benutzernamen bei den Erziehern", example = "E_")
	public String benutzernameSuffixErzieher = "";

	/** Suffix für den Benutzernamen bei den Schülern. */
	@Schema(description = "Suffix für den Benutzernamen bei den Schülern", example = "S_")
	public String benutzernameSuffixSchueler = "";

	/** Json-Objekt mit den Konfigurationseinstellungen der Accounterstellung für die Lernplattform. */
	@Schema(description = "Json-Objekt mit den Konfigurationseinstellungen der Accounterstellung für die Lernplattform")
	public String konfiguration = "";

	/** Gibt an, für welche Personengruppe die Lernplattform relevant ist. */
	@Schema(description = "Gibt an wie viele Einwilligungen dem entsprechenden Lernplattform-Eintrag zugeordnet sind", example = "3")
	public int anzahlEinwilligungen;
}
