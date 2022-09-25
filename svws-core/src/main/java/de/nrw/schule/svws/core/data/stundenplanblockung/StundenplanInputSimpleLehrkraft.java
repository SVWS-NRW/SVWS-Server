package de.nrw.schule.svws.core.data.stundenplanblockung;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Lehrkraft bei
 * {@link StundenplanInputSimple}. */
@XmlRootElement(name = "StundenplanInputSimpleLehrkraft")
@Schema(name = "StundenplanInputSimpleLehrkraft", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Lehrkraft bei {@link StundenplanInputSimple}.")
@TranspilerDTO
public class StundenplanInputSimpleLehrkraft {

	/** Die ID der Lehrkraft. */
	public long id;

	/** Das Kürzel der Lehrkraft. Beispielsweise 'BAR'. */
	public @NotNull String kuerzel = "";

}
