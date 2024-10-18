import { Service } from '../../core/Service';
import { Class } from '../../java/lang/Class';
import { StundenplanblockungManager } from '../../core/utils/stundenplanblockung/StundenplanblockungManager';

export class StundenplanblockungAlgorithmus extends Service<StundenplanblockungManager, StundenplanblockungManager> {


	public constructor() {
		super();
	}

	public handle(pInput : StundenplanblockungManager) : StundenplanblockungManager {
		return pInput;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.stundenplanblockung.StundenplanblockungAlgorithmus';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.stundenplanblockung.StundenplanblockungAlgorithmus', 'de.svws_nrw.core.Service'].includes(name);
	}

	public static class = new Class<StundenplanblockungAlgorithmus>('de.svws_nrw.core.stundenplanblockung.StundenplanblockungAlgorithmus');

}

export function cast_de_svws_nrw_core_stundenplanblockung_StundenplanblockungAlgorithmus(obj : unknown) : StundenplanblockungAlgorithmus {
	return obj as StundenplanblockungAlgorithmus;
}
