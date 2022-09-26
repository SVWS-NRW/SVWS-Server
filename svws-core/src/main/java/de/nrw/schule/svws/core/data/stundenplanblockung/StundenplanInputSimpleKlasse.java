package de.nrw.schule.svws.core.data.stundenplanblockung;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Klasse bei
 * {@link StundenplanInputSimple}. */
@XmlRootElement(name = "StundenplanInputSimpleKlasse")
@Schema(name = "StundenplanInputSimpleKlasse", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Klasse bei {@link StundenplanInputSimple}.")
@TranspilerDTO
public class StundenplanInputSimpleKlasse {

	/** Die ID der Klasse. */
	public long id;

	/** Das Kürzel der Klasse. Beispielsweise '7b' oder 'Q1'. */
	public @NotNull String kuerzel = "";

}
