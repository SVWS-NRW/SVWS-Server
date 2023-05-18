import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanPausenaufsicht } from '../../../core/data/stundenplan/StundenplanPausenaufsicht';
import { StundenplanUnterrichtsverteilung } from '../../../core/data/stundenplan/StundenplanUnterrichtsverteilung';
import { StundenplanUnterricht } from '../../../core/data/stundenplan/StundenplanUnterricht';
import { Stundenplan } from '../../../core/data/stundenplan/Stundenplan';

export class StundenplanManager extends JavaObject {


	/**
	 * Der {@link StundenplanManager} benötigt vier data-Objekte und baut damit eine Datenstruktur für schnelle Zugriffe auf.
	 *
	 * @param daten                 liefert die Grunddaten des Stundenplanes.
	 * @param unterricht            liefert die Informationen zu einem Unterricht im Stundenplan.
	 * @param pausenaufsicht        liefert die Informationen zu den Pausenaufsichten im Stundenplan.
	 * @param unterrichtsverteilung liefert die Informationen zu der Unterrichtsverteilung eines Stundenplans.
	 */
	public constructor(daten : Stundenplan, unterricht : StundenplanUnterricht, pausenaufsicht : StundenplanPausenaufsicht, unterrichtsverteilung : StundenplanUnterrichtsverteilung) {
		super();
		// empty block
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplan.StundenplanManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplan_StundenplanManager(obj : unknown) : StundenplanManager {
	return obj as StundenplanManager;
}
