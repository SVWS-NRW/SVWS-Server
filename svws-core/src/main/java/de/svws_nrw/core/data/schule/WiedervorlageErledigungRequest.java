package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert den Request-Body zum Setzen des Erledigungsstatus eines Wiedervorlage-Eintrags.
 */
@XmlRootElement
@Schema(description = "der Request-Body zum Setzen des Erledigungsstatus eines Wiedervorlage-Eintrags.")
@TranspilerDTO
public class WiedervorlageErledigungRequest {

	/** Gibt an, ob der Wiedervorlage-Eintrag als erledigt markiert (true) oder die Markierung wieder entfernt werden soll (false). */
	@Schema(description = "gibt an, ob der Wiedervorlage-Eintrag als erledigt markiert (true) oder die Markierung wieder entfernt werden soll (false)",
			example = "true")
	public boolean erledigt;

}
