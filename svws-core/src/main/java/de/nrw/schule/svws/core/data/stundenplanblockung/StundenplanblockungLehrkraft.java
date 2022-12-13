package de.nrw.schule.svws.core.data.stundenplanblockung;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.nrw.schule.svws.core.stundenplanblockung.StundenplanblockungAlgorithmus;
import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** 
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Lehrkraft bei {@link StundenplanblockungInput}. <br>
 * Für jede Lehrkraft wird vom {@link StundenplanblockungAlgorithmus} ein Stundenplan berechnet. 
 * 
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "StundenplanblockungLehrkraft")
@Schema(name = "StundenplanblockungLehrkraft", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Lehrkraft bei {@link StundenplanblockungInput}.")
@TranspilerDTO
public class StundenplanblockungLehrkraft {

	/** Die Datenbank-ID der Lehrkraft. */
	public long id;

	/** Das Kürzel der Lehrkraft. Beispielsweise 'BAR'. */
	public @NotNull String kuerzel = "";

}
