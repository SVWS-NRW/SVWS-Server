package de.nrw.schule.svws.core.data.stundenplanblockung;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für einen Raum bei
 * {@link StundenplanInputSimple}. */
@XmlRootElement(name = "StundenplanInputSimpleRaum")
@Schema(name = "StundenplanInputSimpleRaum", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für einen Raum bei {@link StundenplanInputSimple}.")
@TranspilerDTO
public class StundenplanInputSimpleRaum {

	/** Die ID des Raumes. */
	public long id;

	/** Das Kürzel des Raumes. Beispielsweise 'SpH1' oder 'BK05'. */
	public @NotNull String kuerzel = "";

}
