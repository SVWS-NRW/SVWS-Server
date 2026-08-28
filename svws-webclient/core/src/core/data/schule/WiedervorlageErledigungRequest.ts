import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class WiedervorlageErledigungRequest extends JavaObject {

	/**
	 * Gibt an, ob der Wiedervorlage-Eintrag als erledigt markiert (true) oder die Markierung wieder entfernt werden soll (false).
	 */
	public erledigt: boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.WiedervorlageErledigungRequest';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.WiedervorlageErledigungRequest'].includes(name);
	}

	public static readonly class = new Class<WiedervorlageErledigungRequest>('de.svws_nrw.core.data.schule.WiedervorlageErledigungRequest');

	public static transpilerFromJSON(json: string): WiedervorlageErledigungRequest {
		const obj = JSON.parse(json) as Partial<WiedervorlageErledigungRequest>;
		const result = new WiedervorlageErledigungRequest();
		if (obj.erledigt === undefined)
			throw new Error('invalid json format, missing attribute erledigt');
		result.erledigt = obj.erledigt;
		return result;
	}

	public static transpilerToJSON(obj: WiedervorlageErledigungRequest): string {
		let result = '{';
		result += '"erledigt" : ' + obj.erledigt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<WiedervorlageErledigungRequest>): string {
		let result = '{';
		if (obj.erledigt !== undefined) {
			result += '"erledigt" : ' + obj.erledigt.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_WiedervorlageErledigungRequest(obj: unknown): WiedervorlageErledigungRequest {
	return obj as WiedervorlageErledigungRequest;
}
