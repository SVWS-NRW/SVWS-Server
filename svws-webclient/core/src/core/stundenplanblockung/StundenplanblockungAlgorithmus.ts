import { Service } from '../../core/Service';
import { Class } from '../../java/lang/Class';

export class StundenplanblockungAlgorithmus extends Service {


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
		// empty block
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.stundenplanblockung.StundenplanblockungAlgorithmus';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.stundenplanblockung.StundenplanblockungAlgorithmus', 'de.svws_nrw.core.Service'].includes(name);
	}

	public static readonly class = new Class<StundenplanblockungAlgorithmus>('de.svws_nrw.core.stundenplanblockung.StundenplanblockungAlgorithmus');

}

export function cast_de_svws_nrw_core_stundenplanblockung_StundenplanblockungAlgorithmus(obj: unknown): StundenplanblockungAlgorithmus {
	return obj as StundenplanblockungAlgorithmus;
}
