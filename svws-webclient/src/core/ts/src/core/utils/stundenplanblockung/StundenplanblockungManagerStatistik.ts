import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanblockungManagerStatistik extends JavaObject {


	/**
	 * Erzeugt ein neues Objekt.
	 */
	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerStatistik'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerStatistik(obj : unknown) : StundenplanblockungManagerStatistik {
	return obj as StundenplanblockungManagerStatistik;
}
