import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class LehrerLehraemterStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Ein Lehramt eines Lehrers.
	 */
	public lehramt: string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.LehrerLehraemterStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.LehrerLehraemterStatistikExport'].includes(name);
	}

	public static readonly class = new Class<LehrerLehraemterStatistikExport>('de.svws_nrw.asd.export.data.LehrerLehraemterStatistikExport');

	public static transpilerFromJSON(json: string): LehrerLehraemterStatistikExport {
		const obj = JSON.parse(json) as Partial<LehrerLehraemterStatistikExport>;
		const result = new LehrerLehraemterStatistikExport();
		if (obj.lehramt === undefined)
			throw new Error('invalid json format, missing attribute lehramt');
		result.lehramt = obj.lehramt;
		return result;
	}

	public static transpilerToJSON(obj: LehrerLehraemterStatistikExport): string {
		let result = '{';
		result += '"lehramt" : ' + JSON.stringify(obj.lehramt) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<LehrerLehraemterStatistikExport>): string {
		let result = '{';
		if (obj.lehramt !== undefined) {
			result += '"lehramt" : ' + JSON.stringify(obj.lehramt) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_LehrerLehraemterStatistikExport(obj: unknown): LehrerLehraemterStatistikExport {
	return obj as LehrerLehraemterStatistikExport;
}
