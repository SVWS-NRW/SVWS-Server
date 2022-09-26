package de.nrw.schule.svws.core.data.adressbuch;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse repräsentiert die Telefonnummern mit der Art der Telefonnummer
 */
@XmlRootElement
@Schema(description = "gibt eine Telefonnummer zusammen mit der Art der Telefonnummer an")
@TranspilerDTO
public class Telefonnummer {
	/**
	 * die Art der Telefonnummer
	 */
	@Schema(required = true, description = "die Art der Telefonnummer")
	public @NotNull String type = "";

	/**
	 * die Telefon-, Fax- oder Pagernummer
	 */
	@Schema(required = true, description = "die Telefon-, Fax- oder Pagernummer")
	public @NotNull String number = "";
}
