package de.svws_nrw.core.data.stundenplanblockung;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** 
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für ein Fach bei {@link StundenplanblockungInput}.
 * 
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "StundenplanblockungFach")
@Schema(name = "StundenplanblockungFach", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für ein Fach bei {@link StundenplanblockungInput}.")
@TranspilerDTO
public class StundenplanblockungFach {

	/** Die Datenbank-ID des Faches. */
	public long id;

	/** Das Kürzel des Faches. Beispielsweise 'KU'. */
	public @NotNull String kuerzel = "";
	
	/** Die Nummer, welche die Sortierung der Fächer angibt. */
	public int sortierung = 32000;

}
