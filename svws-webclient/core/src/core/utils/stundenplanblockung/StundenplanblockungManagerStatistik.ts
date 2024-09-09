import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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

	public static class = new Class<StundenplanblockungManagerStatistik>('de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerStatistik');

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerStatistik(obj : unknown) : StundenplanblockungManagerStatistik {
	return obj as StundenplanblockungManagerStatistik;
}
