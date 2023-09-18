import { Service } from '../../core/Service';
import { StundenplanblockungManager } from '../../core/utils/stundenplanblockung/StundenplanblockungManager';

export class StundenplanblockungAlgorithmus extends Service<StundenplanblockungManager, StundenplanblockungManager> {


	public constructor() {
		super();
	}

	public handle(pInput : StundenplanblockungManager) : StundenplanblockungManager {
		return pInput;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.stundenplanblockung.StundenplanblockungAlgorithmus', 'de.svws_nrw.core.Service'].includes(name);
	}

}

export function cast_de_svws_nrw_core_stundenplanblockung_StundenplanblockungAlgorithmus(obj : unknown) : StundenplanblockungAlgorithmus {
	return obj as StundenplanblockungAlgorithmus;
}
