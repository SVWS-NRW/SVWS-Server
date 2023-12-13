import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanblockungManagerStatistik extends JavaObject {


	/**
	 * Erzeugt ein neues Objekt.
	 */
	public constructor() {
		super();
		// empty block
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerStatistik';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerStatistik'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerStatistik(obj : unknown) : StundenplanblockungManagerStatistik {
	return obj as StundenplanblockungManagerStatistik;
}
