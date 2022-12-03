package de.nrw.schule.svws.core.data.stundenplanblockung;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** 
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für ein Stundenelement bei {@link StundenplanblockungInput}. <br>
 * Ein Stundenelement definiert eine Einzelstunde/Doppelstunde/etc. für {@link StundenplanblockungKopplung} oder {@link StundenplanblockungLerngruppe}.
 *  
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "StundenplanblockungStundenelement")
@Schema(name = "StundenplanblockungStundenelement", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für ein Stundenelement bei {@link StundenplanblockungInput}.")
@TranspilerDTO
public class StundenplanblockungStundenelement {

	/** Die Datenbank-ID des Stundenelementes. */
	public long id;

	/** Die Anzahl an Stunden. */
	public int stunden = -1;
	
	/** Der Typ. (1 = jede Woche, 2 = gedoppelte Stundenzahl) */
	public @NotNull int typ = -1;

}
