package de.svws_nrw.core.data.stundenplanblockung;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für ein Fach bei
 * {@link StundenplanInputSimple}. */
@XmlRootElement(name = "StundenplanInputSimpleFach")
@Schema(name = "StundenplanInputSimpleFach", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für ein Fach bei {@link StundenplanInputSimple}.")
@TranspilerDTO
public class StundenplanInputSimpleFach {

	/** Die ID des Faches. */
	public long id;

	/** Das Kürzel des Faches. Beispielsweise 'D' oder 'E5'. */
	public @NotNull String kuerzel = "";

}
