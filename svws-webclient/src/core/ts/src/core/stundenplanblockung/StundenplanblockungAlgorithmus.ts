import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { Service, cast_de_nrw_schule_svws_core_Service } from '../Service';
import { StundenplanblockungManager, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManager } from '../utils/stundenplanblockung/StundenplanblockungManager';

export class StundenplanblockungAlgorithmus extends Service<StundenplanblockungManager, StundenplanblockungManager> {


	public constructor() {
		super();
	}

	public handle(pInput : StundenplanblockungManager) : StundenplanblockungManager {
		return pInput;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.stundenplanblockung.StundenplanblockungAlgorithmus', 'de.nrw.schule.svws.core.Service'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_stundenplanblockung_StundenplanblockungAlgorithmus(obj : unknown) : StundenplanblockungAlgorithmus {
	return obj as StundenplanblockungAlgorithmus;
}
