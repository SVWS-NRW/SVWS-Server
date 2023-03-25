package de.svws_nrw.core.data.stundenplanblockung;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.stundenplanblockung.StundenplanblockungAlgorithmus;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** 
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für einen Raum bei {@link StundenplanblockungInput}. <br>
 * Für jeden Raum wird vom {@link StundenplanblockungAlgorithmus} ein Stundenplan berechnet. 
 * 
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "StundenplanblockungRaum")
@Schema(name = "StundenplanblockungRaum", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für einen Raum bei {@link StundenplanblockungInput}.")
@TranspilerDTO
public class StundenplanblockungRaum {

	/** Die Datenbank-ID des Raumes. */
	public long id;

	/** Das Kürzel des Raumes. Beispielsweise 'E21'. */
	public @NotNull String kuerzel = "";

}
