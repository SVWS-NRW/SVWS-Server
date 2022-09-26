package de.nrw.schule.svws.core.data.adressbuch;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;

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
	@Schema(required = true, description = "die ID des Kontakts", example = "123")
	public @NotNull String id = "";

	/** ID des Adressbuchs */
	@Schema(required = true, description = "die ID des Adressbuches, zu dem der Kontakt gehört", example = "global")
	public @NotNull String adressbuchId = "";

	/**
	 * URI der VCard des Kontakts
	 */
	@Schema(required = true, description = "uri des Kontakts", example = "https://example.com/kontakt.vcf")
	public @NotNull String uri = "";

	/**
	 * Versionskennzeichen des Kontaks
	 */
	@Schema(required = true, description = "Versionskennzeichen des Kontakts", example = "78")
	public @NotNull String version = "";

}
