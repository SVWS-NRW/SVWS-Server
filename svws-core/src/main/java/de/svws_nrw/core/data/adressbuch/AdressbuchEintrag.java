package de.svws_nrw.core.data.adressbuch;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.xml.bind.annotation.XmlRootElement;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse repräsentiert Einträge in einem Adressbuch.
 *
 */
@XmlRootElement
@Schema(description = "Ein Kontakt in einem Adressbuch.")
@TranspilerDTO
public class AdressbuchEintrag {
	/** ID des AdressbuchEintrags */
	@Schema(description = "die ID des Kontakts", example = "123")
	public @NotNull String id = "";

	/** ID des Adressbuchs */
	@Schema(description = "die ID des Adressbuches, zu dem der Kontakt gehört", example = "global")
	public @NotNull String adressbuchId = "";

	/**
	 * URI der VCard des Kontakts
	 */
	@Schema(description = "uri des Kontakts", example = "https://example.com/kontakt.vcf")
	public @NotNull String uri = "";

	/**
	 * Versionskennzeichen des Kontaks
	 */
	@Schema(description = "Versionskennzeichen des Kontakts", example = "78")
	public @NotNull String version = "";

}
