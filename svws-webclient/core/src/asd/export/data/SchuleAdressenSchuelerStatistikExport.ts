import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuleAdressenSchuelerStatistikExport extends JavaObject {

	/**
	 * Schüler Insgesamt Zusammen .
	 */
	public insgesamtZusammen: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.SchuleAdressenSchuelerStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.SchuleAdressenSchuelerStatistikExport'].includes(name);
	}

	public static readonly class = new Class<SchuleAdressenSchuelerStatistikExport>('de.svws_nrw.asd.export.data.SchuleAdressenSchuelerStatistikExport');

	public static transpilerFromJSON(json: string): SchuleAdressenSchuelerStatistikExport {
		const obj = JSON.parse(json) as Partial<SchuleAdressenSchuelerStatistikExport>;
		const result = new SchuleAdressenSchuelerStatistikExport();
		if (obj.insgesamtZusammen === undefined)
			throw new Error('invalid json format, missing attribute insgesamtZusammen');
		result.insgesamtZusammen = obj.insgesamtZusammen;
		return result;
	}

	public static transpilerToJSON(obj: SchuleAdressenSchuelerStatistikExport): string {
		let result = '{';
		result += '"insgesamtZusammen" : ' + obj.insgesamtZusammen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuleAdressenSchuelerStatistikExport>): string {
		let result = '{';
		if (obj.insgesamtZusammen !== undefined) {
			result += '"insgesamtZusammen" : ' + obj.insgesamtZusammen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_SchuleAdressenSchuelerStatistikExport(obj: unknown): SchuleAdressenSchuelerStatistikExport {
	return obj as SchuleAdressenSchuelerStatistikExport;
}
